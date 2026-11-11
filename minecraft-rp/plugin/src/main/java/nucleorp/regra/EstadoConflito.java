package nucleorp.regra;

import java.time.Duration;
import java.time.Instant;

/**
 * Fôlego e derrota de uma conta (conflito.md). Fica só em memória. O relógio
 * entra por parâmetro para a regra ser testável.
 */
public final class EstadoConflito {

    public record Regras(int folegoMax, Duration folegoVolta, Duration foraDeCombate) {}

    private final Regras regras;
    private int perdido;
    private Instant ultimaTroca;
    private Instant derrotadoAte;

    public EstadoConflito(Regras regras) { this.regras = regras; }

    /** O Fôlego volta ao máximo passado o prazo desde a última troca. */
    public int folego(Instant agora) {
        if (perdido > 0 && ultimaTroca != null && !agora.isBefore(ultimaTroca.plus(regras.folegoVolta()))) perdido = 0;
        return Math.max(0, regras.folegoMax() - perdido);
    }

    public int folegoMax() { return regras.folegoMax(); }

    public boolean perdeuFolego(Instant agora) { return folego(agora) < regras.folegoMax(); }

    /** Marca uma troca de golpes sem perda (empate ou vitória): só reinicia o prazo de recuperação. */
    public void trocou(Instant agora) { folego(agora); ultimaTroca = agora; }

    /** Perde 1 de Fôlego. Devolve true se chegou a zero, isto é, foi derrotado. */
    public boolean perder(Instant agora) {
        folego(agora);
        perdido = Math.min(regras.folegoMax(), perdido + 1);
        ultimaTroca = agora;
        if (perdido >= regras.folegoMax()) { derrotar(agora); return true; }
        return false;
    }

    /** Derrota direta (rendição, duelo, saída no meio do conflito). */
    public void derrotar(Instant agora) {
        derrotadoAte = agora.plus(regras.foraDeCombate());
        perdido = 0;
        ultimaTroca = null;
    }

    /** Derrotado fica fora de combate: não ataca, não duela e não pode ser atacado. */
    public boolean derrotado(Instant agora) { return derrotadoAte != null && agora.isBefore(derrotadoAte); }

    public Duration foraDeCombateRestante(Instant agora) {
        return derrotado(agora) ? Duration.between(agora, derrotadoAte) : Duration.ZERO;
    }

    /** Usado pela staff ao remover as consequências de uma queda de conexão. */
    public void limpar() { perdido = 0; ultimaTroca = null; derrotadoAte = null; }
}
