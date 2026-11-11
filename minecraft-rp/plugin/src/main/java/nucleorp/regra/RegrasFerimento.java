package nucleorp.regra;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

/** Duração e penalidade de cada grau de ferimento (conflito.md, "Ferimentos"). */
public record RegrasFerimento(Map<Grau, Efeito> efeitos) {

    /** @param atributos atributos afetados; {@code null} quer dizer todos */
    public record Efeito(int dias, int penalidade, Set<String> atributos) {}

    /** Penalidade (zero ou negativa) do ferimento em um teste do atributo. */
    public int penalidade(Grau grau, String atributo) {
        Efeito e = efeitos.get(grau);
        if (e == null) return 0;
        return e.atributos() == null || e.atributos().contains(atributo) ? e.penalidade() : 0;
    }

    public Duration duracao(Grau grau) {
        Efeito e = efeitos.get(grau);
        return e == null ? Duration.ZERO : Duration.ofDays(e.dias());
    }

    /** Resultado de aplicar um ferimento novo sobre o que o personagem já tem. */
    public record Aplicado(Grau grau, Instant fim, boolean mudou) {}

    /**
     * Não acumula: vale o pior. Grau igual renova o prazo se o novo durar mais;
     * grau menor que o atual é ignorado.
     */
    public Aplicado aplicar(Grau atual, Instant fimAtual, Grau novo, Instant agora) {
        boolean temAtual = atual != null && atual != Grau.NENHUM && fimAtual != null && fimAtual.isAfter(agora);
        Instant fimNovo = agora.plus(duracao(novo));
        if (novo == Grau.NENHUM) return new Aplicado(temAtual ? atual : Grau.NENHUM, temAtual ? fimAtual : null, false);
        if (!temAtual || novo.piorQue(atual)) return new Aplicado(novo, fimNovo, true);
        if (novo == atual && fimNovo.isAfter(fimAtual)) return new Aplicado(novo, fimNovo, true);
        return new Aplicado(atual, fimAtual, false);
    }

    /**
     * Fim do ferimento depois de um tratamento que o reduziu: o prazo restante
     * é mantido, mas não passa da duração normal do grau novo.
     */
    public Instant fimAposTratar(Grau novo, Instant fimAtual, Instant agora) {
        Instant teto = agora.plus(duracao(novo));
        return fimAtual.isBefore(teto) ? fimAtual : teto;
    }
}
