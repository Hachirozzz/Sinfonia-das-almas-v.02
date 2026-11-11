package nucleorp.regra;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * O que o jogador digitou depois do atributo em /testar e /disputar:
 * {@code [dificuldade] [±n] [oficio] [traco] [fraqueza]}, em qualquer ordem.
 */
public record ArgumentosTeste(String dificuldade, int circunstancia, boolean oficio, boolean traco, boolean fraqueza) {

    public static final ArgumentosTeste VAZIO = new ArgumentosTeste(null, 0, false, false, false);

    public static ArgumentosTeste ler(List<String> palavras, Catalogo catalogo, RegrasTeste regras, boolean aceitaDificuldade) {
        String dificuldade = null;
        Integer circunstancia = null;
        boolean oficio = false, traco = false, fraqueza = false;
        for (String bruta : palavras) {
            String p = Texto.semAcento(bruta).toLowerCase(Locale.ROOT);
            if (p.isEmpty()) continue;
            if (p.matches("[+-]\\d{1,3}")) {
                if (circunstancia != null) throw new ErroRegra("teste.erro.circunstancia-repetida");
                int v = Integer.parseInt(p);
                if (Math.abs(v) > regras.circunstanciaMax())
                    throw new ErroRegra("teste.erro.circunstancia-limite", "max", regras.circunstanciaMax());
                circunstancia = v;
            } else if (p.equals("oficio")) oficio = true;
            else if (p.equals("traco")) traco = true;
            else if (p.equals("fraqueza")) fraqueza = true;
            else {
                String id = aceitaDificuldade ? catalogo.dificuldadePorPrefixo(p) : null;
                if (id == null || dificuldade != null) throw new ErroRegra("teste.erro.palavra", "palavra", bruta);
                dificuldade = id;
            }
        }
        return new ArgumentosTeste(dificuldade, circunstancia == null ? 0 : circunstancia, oficio, traco, fraqueza);
    }

    /** Modificadores declarados pelo jogador (o de ferimento é automático e entra à parte). */
    public List<Modificador> modificadores(RegrasTeste regras) {
        List<Modificador> lista = new ArrayList<>();
        if (circunstancia != 0) lista.add(new Modificador(Modificador.CIRCUNSTANCIA, circunstancia));
        if (oficio) lista.add(new Modificador(Modificador.OFICIO, regras.oficio()));
        if (traco) lista.add(new Modificador(Modificador.TRACO, regras.traco()));
        if (fraqueza) lista.add(new Modificador(Modificador.FRAQUEZA, regras.fraqueza()));
        return lista;
    }
}
