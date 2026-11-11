package nucleorp.dados;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import nucleorp.regra.Grau;

/** Registros guardados no banco. O UUID da conta é sempre o que o servidor entregou: valor opaco. */
public final class Modelo {
    private Modelo() {}

    /** @param vagas null enquanto a staff não ajustou: vale o padrão da configuração */
    public record Conta(UUID id, String nick, Instant primeiraEntrada, Instant ultimaEntrada, Integer vagas,
                        Set<String> racasLiberadas, Long personagemAtivo) {}

    public enum Situacao {
        EM_CRIACAO, ATIVO, APOSENTADO, MORTO;
        public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
        public static Situacao deId(String s) { return valueOf(s.toUpperCase(java.util.Locale.ROOT)); }
    }

    public enum Revisao {
        NOVA, REVISADA, DEVOLVIDA;
        public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
        public static Revisao deId(String s) {
            for (Revisao r : values()) if (r.id().equalsIgnoreCase(s)) return r;
            return null;
        }
    }

    public record Personagem(long id, UUID conta, int vaga, String nome, int idade, String raca, String tratamento,
                             String aparencia, String oficio, Instant oficioTrocadoEm, Map<String, Integer> atributos,
                             Situacao situacao, Revisao revisao, String recado, Instant criadoEm, Instant encerradoEm) {
        public int atributo(String id) { return atributos.getOrDefault(id, 0); }
    }

    /** Dados de um personagem novo, já validados pelas regras. */
    public record NovoPersonagem(UUID conta, int vaga, String nome, int idade, String raca, String tratamento,
                                 String aparencia, String oficio, Map<String, Integer> atributos) {}

    public record Ferimento(long id, long personagem, Grau grau, String origem, Instant inicio, Instant fim,
                            String tratadoDia, int tratadoVezes) {}

    public record Rolagem(Instant quando, UUID conta, Long personagem, String nome, String tipo, String atributo,
                          Integer dificuldade, String dado, String modificadores, int total, String resultado,
                          String adversario) {}

    public record Nota(long personagem, String autor, Instant quando, String texto) {}

    public record RegistroDesfecho(long personagem, String nomePersonagem, String grau, String aplicadoPor,
                                   Long consentimento, String motivo, Instant quando) {}

    /** Personagem com o nick da conta, para listas da staff. */
    public record PersonagemDeConta(Personagem personagem, String nick) {}
}
