package nucleorp.regra;

import java.util.LinkedHashMap;
import java.util.Map;

/** Validação dos campos da ficha (ficha.md, seção 2). */
public record RegrasFicha(int nomeMin, int nomeMax, int tratamentoMax, int aparenciaMax, int pontosNaCriacao,
                          int maximoNaCriacao, int teto, int aptidaoBonus) {

    /** Nome: letras latinas, espaço, apóstrofo e hífen; começa e termina com letra. */
    public String nome(String bruto) {
        String n = Texto.linha(bruto).replace('’', '\'');
        int tam = Texto.tamanho(n);
        if (tam < nomeMin || tam > nomeMax) throw new ErroRegra("ficha.erro.nome-tamanho", "min", nomeMin, "max", nomeMax);
        boolean ok = n.codePoints().allMatch(c -> letraLatina(c) || c == ' ' || c == '\'' || c == '-');
        if (!ok || !letraLatina(n.codePointAt(0)) || !letraLatina(n.codePointBefore(n.length())))
            throw new ErroRegra("ficha.erro.nome-caracteres");
        return n;
    }

    /** Chave de unicidade do nome: não diferencia maiúsculas nem acentos. */
    public static String chaveDoNome(String nome) { return Texto.chave(nome); }

    public int idade(String bruto, Catalogo.DefRaca raca) {
        int idade;
        try { idade = Integer.parseInt(bruto.strip()); }
        catch (NumberFormatException e) { throw new ErroRegra("ficha.erro.idade-numero"); }
        if (idade < raca.idadeMin() || idade > raca.idadeMax())
            throw new ErroRegra("ficha.erro.idade-faixa", "min", raca.idadeMin(), "max", raca.idadeMax(), "raca", raca.nome());
        return idade;
    }

    /** Tratamento é opcional: texto vazio ou "nenhum" limpa o campo. */
    public String tratamento(String bruto) {
        String t = Texto.linha(bruto);
        if (t.isEmpty() || Texto.chave(t).equals("nenhum")) return "";
        boolean ok = t.codePoints().allMatch(c -> letraLatina(c) || c == ' ' || c == '/' || c == '-');
        if (!ok || Texto.tamanho(t) > tratamentoMax) throw new ErroRegra("ficha.erro.tratamento", "max", tratamentoMax);
        return t;
    }

    public String aparencia(String bruto) {
        String a = Texto.linha(bruto);
        if (a.isEmpty() || Texto.tamanho(a) > aparenciaMax) throw new ErroRegra("ficha.erro.aparencia", "max", aparenciaMax);
        return a;
    }

    /** Pontos que ainda faltam distribuir. */
    public int pontosRestantes(Map<String, Integer> pontos) {
        return pontosNaCriacao - pontos.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** Muda um ponto na distribuição da criação, respeitando os limites. */
    public void ajustarPonto(Map<String, Integer> pontos, Catalogo.DefAtributo atributo, int delta) {
        if (atributo.soPorConcessao()) throw new ErroRegra("ficha.erro.atributo-concedido", "atributo", atributo.nome());
        int novo = pontos.getOrDefault(atributo.id(), 0) + delta;
        if (novo < 0 || novo > maximoNaCriacao) throw new ErroRegra("ficha.erro.ponto-limite", "max", maximoNaCriacao);
        if (delta > 0 && pontosRestantes(pontos) < delta) throw new ErroRegra("ficha.erro.sem-pontos");
        pontos.put(atributo.id(), novo);
    }

    /**
     * Atributos finais da ficha: confere a distribuição inteira (ela vem do
     * cliente, pode ter sido forjada) e soma a aptidão da raça.
     *
     * @param aptidaoEscolhida atributo escolhido quando a raça tem aptidão livre; ignorado nas demais
     */
    public Map<String, Integer> atributosFinais(Map<String, Integer> pontos, Catalogo catalogo, Catalogo.DefRaca raca,
                                                String aptidaoEscolhida) {
        Map<String, Integer> finais = new LinkedHashMap<>();
        int soma = 0;
        for (Catalogo.DefAtributo a : catalogo.atributos().values()) {
            int v = pontos.getOrDefault(a.id(), 0);
            if (v < 0 || v > maximoNaCriacao || (a.soPorConcessao() && v != 0))
                throw new ErroRegra("ficha.erro.ponto-limite", "max", maximoNaCriacao);
            soma += v;
            finais.put(a.id(), v);
        }
        for (String id : pontos.keySet())
            if (!catalogo.atributos().containsKey(id)) throw new ErroRegra("erro.atributo-desconhecido", "atributo", id, "lista", "");
        if (soma != pontosNaCriacao) throw new ErroRegra("ficha.erro.pontos-sobrando", "faltam", pontosNaCriacao - soma);
        String aptidao = raca.aptidaoLivre() ? aptidaoEscolhida : raca.aptidao();
        if (raca.aptidaoLivre() && aptidao == null) throw new ErroRegra("ficha.erro.aptidao-falta");
        if (aptidao != null && !aptidao.isEmpty()) {
            Catalogo.DefAtributo a = catalogo.atributos().get(aptidao);
            if (a == null || a.soPorConcessao()) throw new ErroRegra("ficha.erro.aptidao-invalida");
            finais.put(aptidao, Math.min(teto, finais.get(aptidao) + aptidaoBonus));
        }
        return finais;
    }

    private static boolean letraLatina(int c) {
        return Character.isLetter(c) && Character.UnicodeScript.of(c) == Character.UnicodeScript.LATIN;
    }
}
