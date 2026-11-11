package nucleorp.regra;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RegrasFichaTest {
    private final RegrasFicha f = Apoio.FICHA;
    private final Catalogo cat = Apoio.catalogo();

    @Test void nomeValido() {
        assertEquals("Aldara Venn", f.nome("  Aldara   Venn "));
        assertEquals("D'Ávila-Souza", f.nome("D'Ávila-Souza"));
        assertEquals(RegrasFicha.chaveDoNome("ALDARA venn"), RegrasFicha.chaveDoNome("Aldara Venn"));
        assertEquals(RegrasFicha.chaveDoNome("Áldara"), RegrasFicha.chaveDoNome("aldara"));
    }

    @Test void nomeHostilERecusado() {
        for (String ruim : new String[] {"Al", "A".repeat(31), "Aldara2", "<red>Aldara", "-Aldara", "Aldara-",
            "Аldara" /* A cirílico */, "Aldara_Venn", "   "}) {
            assertThrows(ErroRegra.class, () -> f.nome(ruim), ruim);
        }
    }

    @Test void caracteresInvisiveisSaemDoTexto() {
        assertEquals("Aldara", f.nome("Al\u200bdara"));
        assertEquals("capa ccinza", f.aparencia("capa\u0000 \u00a7ccinza\n"), "controle e símbolo de cor saem");
        assertEquals("Aldarac", f.nome("Aldara\u00a7c"), "o símbolo de cor some, a letra fica");
        assertEquals("Al dara", f.nome("Al\ndara"), "quebra de linha vira espaço");
    }

    @Test void idadeDentroDaFaixaDaRaca() {
        Catalogo.DefRaca r = cat.racas().get("raca_a");
        assertEquals(34, f.idade(" 34 ", r));
        assertThrows(ErroRegra.class, () -> f.idade("15", r));
        assertThrows(ErroRegra.class, () -> f.idade("81", r));
        assertThrows(ErroRegra.class, () -> f.idade("trinta", r));
        assertThrows(ErroRegra.class, () -> f.idade("99999999999999", r));
    }

    @Test void tratamentoEAparencia() {
        assertEquals("ela/dela", f.tratamento("ela/dela"));
        assertEquals("", f.tratamento("nenhum"));
        assertThrows(ErroRegra.class, () -> f.tratamento("abcdefghijklm"));
        assertThrows(ErroRegra.class, () -> f.tratamento("<b>ela"));
        assertThrows(ErroRegra.class, () -> f.aparencia("x".repeat(201)));
        assertThrows(ErroRegra.class, () -> f.aparencia("  "));
        assertEquals(200, f.aparencia("x".repeat(200)).length());
    }

    @Test void distribuicaoDePontos() {
        Map<String, Integer> p = new HashMap<>();
        f.ajustarPonto(p, cat.atributos().get("tino"), 1);
        f.ajustarPonto(p, cat.atributos().get("tino"), 1);
        f.ajustarPonto(p, cat.atributos().get("tino"), 1);
        assertThrows(ErroRegra.class, () -> f.ajustarPonto(p, cat.atributos().get("tino"), 1), "máximo 3 em cada");
        assertThrows(ErroRegra.class, () -> f.ajustarPonto(p, cat.atributos().get("voz"), -1));
        assertThrows(ErroRegra.class, () -> f.ajustarPonto(p, cat.atributos().get("dom"), 1), "atributo só por concessão");
        f.ajustarPonto(p, cat.atributos().get("passo"), 1);
        assertEquals(1, f.pontosRestantes(p));
        assertThrows(ErroRegra.class, () -> f.atributosFinais(p, cat, cat.racas().get("raca_a"), null), "sobrou ponto");
        f.ajustarPonto(p, cat.atributos().get("voz"), 1);
        assertThrows(ErroRegra.class, () -> f.ajustarPonto(p, cat.atributos().get("braco"), 1), "acabaram os pontos");
        Map<String, Integer> fim = f.atributosFinais(p, cat, cat.racas().get("raca_a"), null);
        assertEquals(4, fim.get("tino"), "3 + 1 da aptidão da raça");
        assertEquals(0, fim.get("braco"));
        assertEquals(0, fim.get("dom"));
        assertEquals(6, fim.values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test void distribuicaoForjadaERecusada() {
        Catalogo.DefRaca a = cat.racas().get("raca_a");
        assertThrows(ErroRegra.class, () -> f.atributosFinais(Map.of("tino", 5), cat, a, null));
        assertThrows(ErroRegra.class, () -> f.atributosFinais(Map.of("tino", 3, "voz", 3, "braco", -1), cat, a, null));
        assertThrows(ErroRegra.class, () -> f.atributosFinais(Map.of("tino", 3, "dom", 2), cat, a, null));
        assertThrows(ErroRegra.class, () -> f.atributosFinais(Map.of("tino", 3, "voz", 2, "inventado", 0), cat, a, null));
    }

    @Test void aptidaoLivrePedeEscolha() {
        Catalogo.DefRaca b = cat.racas().get("raca_b");
        Map<String, Integer> p = Map.of("braco", 3, "voz", 2);
        assertThrows(ErroRegra.class, () -> f.atributosFinais(p, cat, b, null));
        assertThrows(ErroRegra.class, () -> f.atributosFinais(p, cat, b, "dom"));
        assertEquals(4, f.atributosFinais(p, cat, b, "braco").get("braco"));
        assertEquals(1, f.atributosFinais(p, cat, b, "passo").get("passo"));
    }
}
