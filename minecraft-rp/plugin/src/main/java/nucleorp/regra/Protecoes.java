package nucleorp.regra;

import java.time.Duration;
import java.time.Instant;

/** Contas pequenas de proteção e de vagas, separadas para terem teste. */
public final class Protecoes {
    private Protecoes() {}

    /**
     * Novato não pode ser alvo de ataque, a menos que tenha atacado aquele
     * jogador dentro do prazo de revide.
     *
     * @param ultimoAtaqueDoAlvoNoAtacante quando o alvo atacou quem agora o ataca; null se nunca
     */
    public static boolean novatoProtegido(Instant primeiraEntrada, Instant agora, Duration prazoNovato,
                                          Instant ultimoAtaqueDoAlvoNoAtacante, Duration prazoRevide) {
        if (!novato(primeiraEntrada, agora, prazoNovato)) return false;
        return ultimoAtaqueDoAlvoNoAtacante == null || !agora.isBefore(ultimoAtaqueDoAlvoNoAtacante.plus(prazoRevide));
    }

    public static boolean novato(Instant primeiraEntrada, Instant agora, Duration prazoNovato) {
        return agora.isBefore(primeiraEntrada.plus(prazoNovato));
    }

    /**
     * Vagas livres para criar personagem. Mortos e aposentados não ocupam vaga,
     * mas cada encerramento recente segura uma vaga até o fim da espera.
     */
    public static int vagasLivres(int vagas, int vivos, int encerradosDentroDaEspera) {
        return Math.max(0, vagas - vivos - encerradosDentroDaEspera);
    }
}
