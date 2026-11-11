package nucleorp.regra;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConflitoTest {
    private static final Instant T0 = Instant.parse("2026-11-11T12:00:00Z");
    private final EstadoConflito.Regras regras = new EstadoConflito.Regras(3, Duration.ofMinutes(10), Duration.ofMinutes(30));

    @Test void perderFolegoAteADerrota() {
        EstadoConflito e = new EstadoConflito(regras);
        assertEquals(3, e.folego(T0));
        assertFalse(e.perdeuFolego(T0));
        assertFalse(e.perder(T0));
        assertEquals(2, e.folego(T0));
        assertTrue(e.perdeuFolego(T0));
        assertFalse(e.perder(T0.plusSeconds(30)));
        assertTrue(e.perder(T0.plusSeconds(60)), "o terceiro Fôlego perdido derrota");
        assertTrue(e.derrotado(T0.plusSeconds(61)));
        assertEquals(Duration.ofMinutes(30), e.foraDeCombateRestante(T0.plusSeconds(60)));
        assertFalse(e.derrotado(T0.plusSeconds(60).plus(Duration.ofMinutes(30))));
    }

    @Test void folegoVoltaDezMinutosDepoisDaUltimaTroca() {
        EstadoConflito e = new EstadoConflito(regras);
        e.perder(T0);
        assertEquals(2, e.folego(T0.plus(Duration.ofMinutes(9))));
        // Uma troca sem perda reinicia o prazo.
        e.trocou(T0.plus(Duration.ofMinutes(9)));
        assertEquals(2, e.folego(T0.plus(Duration.ofMinutes(18))));
        assertEquals(3, e.folego(T0.plus(Duration.ofMinutes(19))));
        assertFalse(e.perdeuFolego(T0.plus(Duration.ofMinutes(19))));
    }

    @Test void rendicaoDerrotaSemPassarPeloFolego() {
        EstadoConflito e = new EstadoConflito(regras);
        e.perder(T0);
        e.derrotar(T0);
        assertTrue(e.derrotado(T0));
        assertEquals(3, e.folego(T0), "derrotado recomeça com o Fôlego cheio quando voltar");
        e.limpar();
        assertFalse(e.derrotado(T0));
    }

    @Test void consentimentoValeNoPrazoParaOMesmoGrauPersonagemEConta() {
        UUID dona = UUID.randomUUID(), outro = UUID.randomUUID(), terceiro = UUID.randomUUID();
        Consentimento c = new Consentimento(1, dona, 10, outro, Consentimento.GrauC.MORTE, T0, T0.plus(Duration.ofMinutes(30)), null, null);
        assertTrue(c.autoriza(Consentimento.GrauC.MORTE, 10, outro, T0.plusSeconds(60)));
        assertFalse(c.autoriza(Consentimento.GrauC.GRAVE, 10, outro, T0.plusSeconds(60)), "morte não autoriza grave");
        assertFalse(c.autoriza(Consentimento.GrauC.MORTE, 11, outro, T0.plusSeconds(60)), "outro personagem");
        assertFalse(c.autoriza(Consentimento.GrauC.MORTE, 10, terceiro, T0.plusSeconds(60)), "outra conta");
        assertFalse(c.autoriza(Consentimento.GrauC.MORTE, 10, outro, T0.plus(Duration.ofMinutes(30))), "expirado");
        assertFalse(c.autoriza(Consentimento.GrauC.MORTE, 10, outro, T0.minusSeconds(1)), "antes do início");
    }

    @Test void consentimentoUsadoOuCanceladoNaoVale() {
        UUID dona = UUID.randomUUID(), outro = UUID.randomUUID();
        for (Consentimento.Fim fim : Consentimento.Fim.values()) {
            Consentimento c = new Consentimento(1, dona, 10, outro, Consentimento.GrauC.GRAVE, T0, T0.plus(Duration.ofMinutes(30)), fim, T0);
            assertFalse(c.valido(T0.plusSeconds(1)), fim.toString());
        }
    }

    @Test void ferimentoNaoAcumulaValeOPior() {
        RegrasFerimento f = Apoio.ferimentos();
        RegrasFerimento.Aplicado a = f.aplicar(Grau.NENHUM, null, Grau.LEVE, T0);
        assertEquals(Grau.LEVE, a.grau());
        assertEquals(T0.plus(Duration.ofDays(2)), a.fim());
        // Leve sobre grave: ignorado.
        Instant fimGrave = T0.plus(Duration.ofDays(7));
        RegrasFerimento.Aplicado b = f.aplicar(Grau.GRAVE, fimGrave, Grau.LEVE, T0.plusSeconds(60));
        assertEquals(Grau.GRAVE, b.grau());
        assertFalse(b.mudou());
        // Grave sobre leve: substitui.
        assertEquals(Grau.GRAVE, f.aplicar(Grau.LEVE, a.fim(), Grau.GRAVE, T0).grau());
        // Leve sobre leve: renova o prazo.
        RegrasFerimento.Aplicado c = f.aplicar(Grau.LEVE, a.fim(), Grau.LEVE, T0.plus(Duration.ofDays(1)));
        assertTrue(c.mudou());
        assertEquals(T0.plus(Duration.ofDays(3)), c.fim());
        // Ferimento vencido não conta.
        assertEquals(Grau.LEVE, f.aplicar(Grau.GRAVE, T0.minusSeconds(1), Grau.LEVE, T0).grau());
    }

    @Test void penalidadeDoFerimentoETratamento() {
        RegrasFerimento f = Apoio.ferimentos();
        assertEquals(-1, f.penalidade(Grau.LEVE, "braco"));
        assertEquals(0, f.penalidade(Grau.LEVE, "tino"));
        assertEquals(-2, f.penalidade(Grau.GRAVE, "voz"));
        assertEquals(0, f.penalidade(Grau.NENHUM, "braco"));
        assertEquals(Grau.LEVE, Grau.GRAVE.aposTratar());
        assertEquals(Grau.NENHUM, Grau.LEVE.aposTratar());
        // Grave com 6 dias restantes vira leve com no máximo 2 dias.
        assertEquals(T0.plus(Duration.ofDays(2)), f.fimAposTratar(Grau.LEVE, T0.plus(Duration.ofDays(6)), T0));
        assertEquals(T0.plus(Duration.ofDays(1)), f.fimAposTratar(Grau.LEVE, T0.plus(Duration.ofDays(1)), T0));
    }

    @Test void novatoProtegidoSalvoRevide() {
        Duration sete = Duration.ofDays(7), trinta = Duration.ofMinutes(30);
        Instant entrou = T0.minus(Duration.ofDays(2));
        assertTrue(Protecoes.novatoProtegido(entrou, T0, sete, null, trinta));
        assertFalse(Protecoes.novatoProtegido(entrou, T0, sete, T0.minus(Duration.ofMinutes(10)), trinta), "atacou há 10 minutos");
        assertTrue(Protecoes.novatoProtegido(entrou, T0, sete, T0.minus(Duration.ofMinutes(31)), trinta), "revide vencido");
        assertFalse(Protecoes.novatoProtegido(T0.minus(Duration.ofDays(8)), T0, sete, null, trinta), "veterano");
    }

    @Test void vagasLivres() {
        assertEquals(1, Protecoes.vagasLivres(1, 0, 0));
        assertEquals(0, Protecoes.vagasLivres(1, 1, 0));
        assertEquals(0, Protecoes.vagasLivres(1, 0, 1), "encerramento recente segura a vaga");
        assertEquals(1, Protecoes.vagasLivres(2, 0, 1));
        assertEquals(0, Protecoes.vagasLivres(2, 1, 2));
    }

    @Test void zonaContemOsCantosEmQualquerOrdem() {
        Zona z = new Zona("praca", "world", 10, 70, -5, -10, 60, 5, true, false);
        assertTrue(z.contem("world", -10, 60, -5));
        assertTrue(z.contem("world", 10, 70, 5));
        assertTrue(z.contem("world", 0, 65, 0));
        assertFalse(z.contem("world", 11, 65, 0));
        assertFalse(z.contem("world", 0, 71, 0));
        assertFalse(z.contem("world_nether", 0, 65, 0));
    }
}
