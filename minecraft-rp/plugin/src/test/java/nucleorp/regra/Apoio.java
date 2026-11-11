package nucleorp.regra;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Catálogo e regras de exemplo para os testes, com os valores iniciais da especificação. */
final class Apoio {
    private Apoio() {}

    static final RegrasTeste TESTE = new RegrasTeste(12, 3, 2, 1, 2, -2);
    static final RegrasFicha FICHA = new RegrasFicha(3, 30, 12, 200, 5, 3, 4, 1);

    static Catalogo catalogo() {
        Map<String, Catalogo.DefAtributo> at = new LinkedHashMap<>();
        at.put("braco", new Catalogo.DefAtributo("braco", "Braço", true, true, false, null, "Aparar"));
        at.put("passo", new Catalogo.DefAtributo("passo", "Passo", true, true, false, null, "Esquivar"));
        at.put("tino", new Catalogo.DefAtributo("tino", "Tino", false, false, false, null, null));
        at.put("voz", new Catalogo.DefAtributo("voz", "Voz", false, false, false, null, null));
        at.put("dom", new Catalogo.DefAtributo("dom", "Dom", false, false, true, Grau.LEVE, null));
        Map<String, Integer> dif = new LinkedHashMap<>();
        dif.put("facil", 4); dif.put("normal", 7); dif.put("dificil", 10); dif.put("extrema", 13);
        Map<String, Catalogo.DefRaca> racas = new LinkedHashMap<>();
        racas.put("raca_a", new Catalogo.DefRaca("raca_a", "Raça A", 16, 80, "tino", "T", "t", "F", "f", 1.0, false));
        racas.put("raca_b", new Catalogo.DefRaca("raca_b", "Raça B", 20, 300, "escolha", "T", "t", "F", "f", 1.0, true));
        Map<String, Catalogo.DefOficio> of = new LinkedHashMap<>();
        of.put("escriba", new Catalogo.DefOficio("escriba", "Escriba", ""));
        return new Catalogo(at, dif, racas, of);
    }

    static RegrasFerimento ferimentos() {
        return new RegrasFerimento(Map.of(
            Grau.LEVE, new RegrasFerimento.Efeito(2, -1, Set.of("braco", "passo")),
            Grau.GRAVE, new RegrasFerimento.Efeito(7, -2, null)));
    }
}
