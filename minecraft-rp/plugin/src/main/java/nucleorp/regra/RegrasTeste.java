package nucleorp.regra;

import java.util.List;

/** A conta do teste (testes.md): dado + atributo + modificadores, contra a dificuldade. */
public record RegrasTeste(int lados, int somaLimite, int circunstanciaMax, int oficio, int traco, int fraqueza) {

    public enum Vencedor { PRIMEIRO, SEGUNDO, EMPATE }

    public RegrasTeste {
        if (lados < 2) throw new IllegalArgumentException("o dado precisa de pelo menos 2 lados");
        if (somaLimite < 0 || circunstanciaMax < 0) throw new IllegalArgumentException("limites não podem ser negativos");
    }

    /**
     * @param dado        valor já rolado, de 1 a {@link #lados}
     * @param dificuldade número a igualar ou passar; {@code null} para teste sem dificuldade
     */
    public ResultadoTeste resolver(int dado, int atributo, List<Modificador> modificadores, Integer dificuldade) {
        if (dado < 1 || dado > lados) throw new IllegalArgumentException("dado fora da faixa: " + dado);
        int bruta = modificadores.stream().mapToInt(Modificador::valor).sum();
        int aplicada = Math.max(-somaLimite, Math.min(somaLimite, bruta));
        int total = dado + atributo + aplicada;
        Desfecho desfecho;
        if (dado == lados) desfecho = Desfecho.TRIUNFO;          // sucesso sempre
        else if (dado == 1) desfecho = Desfecho.TROPECO;         // falha sempre
        else if (dificuldade == null) desfecho = Desfecho.SEM_DIFICULDADE;
        else desfecho = total >= dificuldade ? Desfecho.SUCESSO : Desfecho.FALHA;
        return new ResultadoTeste(dado, atributo, List.copyOf(modificadores), bruta, aplicada, total, dificuldade, desfecho);
    }

    /** Disputa: maior total vence; empate não muda nada. */
    public Vencedor disputar(ResultadoTeste primeiro, ResultadoTeste segundo) {
        if (primeiro.total() > segundo.total()) return Vencedor.PRIMEIRO;
        if (segundo.total() > primeiro.total()) return Vencedor.SEGUNDO;
        return Vencedor.EMPATE;
    }
}
