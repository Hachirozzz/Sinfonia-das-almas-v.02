package nucleorp.regra;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class RegrasTesteTest {
    private final RegrasTeste r = Apoio.TESTE;
    private final Catalogo cat = Apoio.catalogo();

    @Test void resultadoIgualaOuPassaADificuldade() {
        // Exemplo de testes.md: 6 +3 +1 ofício = 10 contra Difícil 10.
        ResultadoTeste t = r.resolver(6, 3, List.of(new Modificador(Modificador.OFICIO, 1)), 10);
        assertEquals(10, t.total());
        assertEquals(Desfecho.SUCESSO, t.desfecho());
        assertEquals(Desfecho.FALHA, r.resolver(5, 3, List.of(new Modificador(Modificador.OFICIO, 1)), 10).desfecho());
    }

    @Test void semDificuldadeMostraSoOTotal() {
        ResultadoTeste t = r.resolver(7, 2, List.of(), null);
        assertEquals(9, t.total());
        assertEquals(Desfecho.SEM_DIFICULDADE, t.desfecho());
    }

    @Test void triunfoESucessoSempre() {
        // 12 +0 -3 = 9 contra 13: falharia na conta, mas 12 no dado é Triunfo.
        ResultadoTeste t = r.resolver(12, 0, List.of(new Modificador(Modificador.FRAQUEZA, -2), new Modificador(Modificador.CIRCUNSTANCIA, -2)), 13);
        assertEquals(Desfecho.TRIUNFO, t.desfecho());
        assertTrue(t.desfecho().sucesso());
    }

    @Test void tropecoEFalhaSempre() {
        // 1 +4 +3 = 8 contra 4: passaria na conta, mas 1 no dado é Tropeço.
        ResultadoTeste t = r.resolver(1, 4, List.of(new Modificador(Modificador.TRACO, 2), new Modificador(Modificador.OFICIO, 1)), 4);
        assertEquals(8, t.total());
        assertEquals(Desfecho.TROPECO, t.desfecho());
        assertTrue(t.desfecho().falha());
    }

    @Test void somaDosModificadoresTemLimite() {
        // +2 circunstância +1 ofício +2 traço = +5, limitado a +3.
        ResultadoTeste t = r.resolver(5, 1, List.of(new Modificador(Modificador.CIRCUNSTANCIA, 2),
            new Modificador(Modificador.OFICIO, 1), new Modificador(Modificador.TRACO, 2)), 7);
        assertEquals(5, t.somaBruta());
        assertEquals(3, t.somaAplicada());
        assertTrue(t.limitado());
        assertEquals(9, t.total());
        // -2 fraqueza -2 circunstância -2 ferimento grave = -6, limitado a -3.
        ResultadoTeste n = r.resolver(8, 2, List.of(new Modificador(Modificador.FRAQUEZA, -2),
            new Modificador(Modificador.CIRCUNSTANCIA, -2), new Modificador(Modificador.FERIMENTO, -2)), 7);
        assertEquals(-3, n.somaAplicada());
        assertEquals(7, n.total());
        assertEquals(Desfecho.SUCESSO, n.desfecho());
    }

    @Test void disputaMaiorTotalVenceEEmpateNaoMudaNada() {
        ResultadoTeste a = r.resolver(9, 1, List.of(), null), b = r.resolver(5, 2, List.of(), null);
        assertEquals(RegrasTeste.Vencedor.PRIMEIRO, r.disputar(a, b));
        assertEquals(RegrasTeste.Vencedor.SEGUNDO, r.disputar(b, a));
        ResultadoTeste c = r.resolver(8, 2, List.of(), null);
        assertEquals(RegrasTeste.Vencedor.EMPATE, r.disputar(a, c));
    }

    @Test void dadoForaDaFaixaERecusado() {
        assertThrows(IllegalArgumentException.class, () -> r.resolver(0, 0, List.of(), 7));
        assertThrows(IllegalArgumentException.class, () -> r.resolver(13, 0, List.of(), 7));
    }

    @Test void argumentosEmQualquerOrdem() {
        ArgumentosTeste a = ArgumentosTeste.ler(List.of("oficio", "dif", "+1", "TRAÇO"), cat, r, true);
        assertEquals("dificil", a.dificuldade());
        assertEquals(1, a.circunstancia());
        assertTrue(a.oficio() && a.traco() && !a.fraqueza());
        assertEquals(3, a.modificadores(r).size());
        assertEquals(-2, ArgumentosTeste.ler(List.of("fraqueza"), cat, r, true).modificadores(r).get(0).valor());
    }

    @Test void argumentosHostisSaoRecusados() {
        assertEquals("teste.erro.circunstancia-limite",
            assertThrows(ErroRegra.class, () -> ArgumentosTeste.ler(List.of("+3"), cat, r, true)).chave());
        assertThrows(ErroRegra.class, () -> ArgumentosTeste.ler(List.of("+1", "-1"), cat, r, true));
        assertThrows(ErroRegra.class, () -> ArgumentosTeste.ler(List.of("<red>oi"), cat, r, true));
        assertThrows(ErroRegra.class, () -> ArgumentosTeste.ler(List.of("normal", "facil"), cat, r, true));
        assertThrows(ErroRegra.class, () -> ArgumentosTeste.ler(List.of("+99999999999"), cat, r, true));
        // Em /disputar não existe dificuldade.
        assertThrows(ErroRegra.class, () -> ArgumentosTeste.ler(List.of("normal"), cat, r, false));
    }

    @Test void atributoAceitaPrimeirasLetrasENome() {
        assertEquals("braco", cat.atributoPorPrefixo("b").id());
        assertEquals("braco", cat.atributoPorPrefixo("Braço").id());
        assertEquals("tino", cat.atributoPorPrefixo("ti").id());
        assertNull(cat.atributoPorPrefixo("x"));
        assertNull(cat.atributoPorPrefixo(""));
        assertThrows(ErroRegra.class, () -> cat.atributoOuErro("zzz"));
    }

    @Test void dadoSolto() {
        assertEquals(new DadoSolto(1, 12), DadoSolto.ler(null, 12, 10, 100));
        assertEquals(new DadoSolto(2, 6), DadoSolto.ler("2d6", 12, 10, 100));
        assertEquals(new DadoSolto(1, 20), DadoSolto.ler("d20", 12, 10, 100));
        assertThrows(ErroRegra.class, () -> DadoSolto.ler("11d6", 12, 10, 100));
        assertThrows(ErroRegra.class, () -> DadoSolto.ler("1d101", 12, 10, 100));
        assertThrows(ErroRegra.class, () -> DadoSolto.ler("0d6", 12, 10, 100));
        assertThrows(ErroRegra.class, () -> DadoSolto.ler("1d1", 12, 10, 100));
        assertThrows(ErroRegra.class, () -> DadoSolto.ler("abc", 12, 10, 100));
        int[] v = new DadoSolto(3, 6).rolar(l -> 4);
        assertArrayEquals(new int[] {4, 4, 4}, v);
    }
}
