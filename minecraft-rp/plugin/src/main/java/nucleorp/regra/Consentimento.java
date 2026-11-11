package nucleorp.regra;

import java.time.Instant;
import java.util.UUID;

/**
 * Consentimento do dono de um personagem (conflito.md). Vale para um
 * personagem, diante de uma conta, por um prazo, e é gasto ao ser usado.
 */
public record Consentimento(long id, UUID de, long personagem, UUID para, GrauC grau, Instant inicio,
                            Instant validade, Fim fim, Instant fimEm) {

    public enum GrauC {
        GRAVE, PERMANENTE, MORTE;
        public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
        public static GrauC deId(String s) {
            for (GrauC g : values()) if (g.id().equalsIgnoreCase(s)) return g;
            return null;
        }
    }

    public enum Fim {
        CANCELADO, EXPIRADO, USADO;
        public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
    }

    public boolean valido(Instant agora) {
        return fim == null && !agora.isBefore(inicio) && agora.isBefore(validade);
    }

    /**
     * Autoriza o desfecho só se for o mesmo grau, o mesmo personagem e a mesma
     * conta que recebeu o consentimento. Consentir com a morte não autoriza
     * ferimento grave, nem o contrário.
     */
    public boolean autoriza(GrauC desfecho, long personagemAlvo, UUID quemAplica, Instant agora) {
        return valido(agora) && grau == desfecho && personagem == personagemAlvo && para.equals(quemAplica);
    }
}
