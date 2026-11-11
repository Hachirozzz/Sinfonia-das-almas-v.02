package nucleorp.regra;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Atributos, dificuldades, raças e ofícios do mundo. Tudo vem de arquivo de
 * configuração: nenhum nome de atributo, raça ou ofício existe no código.
 */
public record Catalogo(Map<String, DefAtributo> atributos, Map<String, Integer> dificuldades,
                       Map<String, DefRaca> racas, Map<String, DefOficio> oficios) {

    /** Valor de aptidão que deixa o jogador escolher o atributo na criação. */
    public static final String APTIDAO_ESCOLHA = "escolha";

    public record DefAtributo(String id, String nome, boolean ataque, boolean defesa, boolean soPorConcessao,
                              Grau falhaCausaFerimento, String verboDefesa) {}

    public record DefRaca(String id, String nome, int idadeMin, int idadeMax, String aptidao, String tracoNome,
                          String tracoTexto, String fraquezaNome, String fraquezaTexto, double altura, boolean restrita) {
        public boolean aptidaoLivre() { return APTIDAO_ESCOLHA.equals(aptidao); }
    }

    public record DefOficio(String id, String nome, String descricao) {}

    public Catalogo {
        atributos = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(atributos));
        dificuldades = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(dificuldades));
        racas = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(racas));
        oficios = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(oficios));
    }

    /** Aceita o identificador, o nome ou as primeiras letras de um deles, desde que só um atributo combine. */
    public DefAtributo atributoPorPrefixo(String digitado) {
        String p = Texto.chave(digitado);
        if (p.isEmpty()) return null;
        DefAtributo exato = atributos.get(p);
        if (exato != null) return exato;
        List<DefAtributo> achados = new ArrayList<>();
        for (DefAtributo a : atributos.values())
            if (a.id().startsWith(p) || Texto.chave(a.nome()).startsWith(p)) achados.add(a);
        return achados.size() == 1 ? achados.get(0) : null;
    }

    public DefAtributo atributoOuErro(String digitado) {
        DefAtributo a = atributoPorPrefixo(digitado);
        if (a == null) throw new ErroRegra("erro.atributo-desconhecido", "atributo", digitado, "lista", String.join(", ", atributos.keySet()));
        return a;
    }

    /** Devolve o identificador da dificuldade, ou null. */
    public String dificuldadePorPrefixo(String digitado) {
        String p = Texto.chave(digitado);
        if (p.isEmpty()) return null;
        if (dificuldades.containsKey(p)) return p;
        String achado = null;
        for (String id : dificuldades.keySet())
            if (id.startsWith(p)) { if (achado != null) return null; achado = id; }
        return achado;
    }

    public String dificuldadeOuErro(String digitado) {
        String id = dificuldadePorPrefixo(digitado);
        if (id == null) throw new ErroRegra("erro.dificuldade-desconhecida", "dificuldade", digitado, "lista", String.join(", ", dificuldades.keySet()));
        return id;
    }

    public DefRaca racaOuErro(String digitado) {
        DefRaca r = porIdOuNome(racas, digitado, DefRaca::nome);
        if (r == null) throw new ErroRegra("erro.raca-desconhecida", "raca", digitado, "lista", String.join(", ", racas.keySet()));
        return r;
    }

    /** Devolve null para "nenhum". */
    public DefOficio oficioOuErro(String digitado) {
        if (Texto.chave(digitado).equals("nenhum")) return null;
        DefOficio o = porIdOuNome(oficios, digitado, DefOficio::nome);
        if (o == null) throw new ErroRegra("erro.oficio-desconhecido", "oficio", digitado, "lista", "nenhum, " + String.join(", ", oficios.keySet()));
        return o;
    }

    private static <T> T porIdOuNome(Map<String, T> mapa, String digitado, java.util.function.Function<T, String> nome) {
        String p = Texto.chave(digitado);
        T exato = mapa.get(p);
        if (exato != null) return exato;
        for (T t : mapa.values()) if (Texto.chave(nome.apply(t)).equals(p)) return t;
        return null;
    }
}
