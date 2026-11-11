package nucleorp.dados;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import nucleorp.dados.Modelo.Conta;
import nucleorp.dados.Modelo.Ferimento;
import nucleorp.dados.Modelo.NovoPersonagem;
import nucleorp.dados.Modelo.Nota;
import nucleorp.dados.Modelo.Personagem;
import nucleorp.dados.Modelo.PersonagemDeConta;
import nucleorp.dados.Modelo.RegistroDesfecho;
import nucleorp.dados.Modelo.Revisao;
import nucleorp.dados.Modelo.Rolagem;
import nucleorp.dados.Modelo.Situacao;
import nucleorp.regra.Consentimento;
import nucleorp.regra.ErroRegra;
import nucleorp.regra.Grau;
import nucleorp.regra.RegrasFicha;
import nucleorp.regra.Texto;

/**
 * Acesso ao banco SQLite. Tudo aqui é síncrono e NÃO pode ser chamado da
 * thread principal do servidor: quem chama é o {@link Banco}, em thread própria.
 * Cada método público é uma transação: ou grava tudo, ou não grava nada.
 */
public final class Repositorio implements AutoCloseable {

    /**
     * Migrações do formato, em ordem. Para mudar o banco, acrescente um item no
     * fim; nunca edite um item já publicado. A posição + 1 é a versão.
     */
    private static final String[][] MIGRACOES = {
        { // versão 1: etapas 1 e 2
            """
            CREATE TABLE conta (
              uuid TEXT PRIMARY KEY, nick TEXT NOT NULL, nick_chave TEXT NOT NULL,
              primeira_entrada INTEGER NOT NULL, ultima_entrada INTEGER NOT NULL,
              vagas INTEGER, personagem_ativo INTEGER)""",
            "CREATE INDEX conta_nick ON conta(nick_chave)",
            "CREATE TABLE raca_liberada (conta TEXT NOT NULL, raca TEXT NOT NULL, PRIMARY KEY (conta, raca))",
            """
            CREATE TABLE personagem (
              id INTEGER PRIMARY KEY AUTOINCREMENT, conta TEXT NOT NULL, vaga INTEGER NOT NULL,
              nome TEXT NOT NULL, nome_chave TEXT NOT NULL UNIQUE, idade INTEGER NOT NULL, raca TEXT NOT NULL,
              tratamento TEXT NOT NULL DEFAULT '', aparencia TEXT NOT NULL DEFAULT '', oficio TEXT,
              oficio_trocado_em INTEGER, situacao TEXT NOT NULL, revisao TEXT NOT NULL, recado TEXT,
              criado_em INTEGER NOT NULL, encerrado_em INTEGER)""",
            "CREATE INDEX personagem_conta ON personagem(conta)",
            "CREATE TABLE personagem_atributo (personagem INTEGER NOT NULL, atributo TEXT NOT NULL, valor INTEGER NOT NULL, PRIMARY KEY (personagem, atributo))",
            """
            CREATE TABLE ferimento (
              id INTEGER PRIMARY KEY AUTOINCREMENT, personagem INTEGER NOT NULL, grau TEXT NOT NULL,
              origem TEXT NOT NULL, inicio INTEGER NOT NULL, fim INTEGER NOT NULL,
              encerrado INTEGER NOT NULL DEFAULT 0, tratado_dia TEXT, tratado_vezes INTEGER NOT NULL DEFAULT 0)""",
            "CREATE INDEX ferimento_personagem ON ferimento(personagem)",
            """
            CREATE TABLE consentimento (
              id INTEGER PRIMARY KEY AUTOINCREMENT, de TEXT NOT NULL, personagem INTEGER NOT NULL,
              para TEXT NOT NULL, grau TEXT NOT NULL, inicio INTEGER NOT NULL, validade INTEGER NOT NULL,
              fim TEXT, fim_em INTEGER)""",
            "CREATE INDEX consentimento_de ON consentimento(de)",
            """
            CREATE TABLE desfecho (
              id INTEGER PRIMARY KEY AUTOINCREMENT, personagem INTEGER NOT NULL, conta TEXT NOT NULL,
              grau TEXT NOT NULL, aplicado_por TEXT NOT NULL, consentimento INTEGER, motivo TEXT,
              quando INTEGER NOT NULL)""",
            """
            CREATE TABLE rolagem (
              id INTEGER PRIMARY KEY AUTOINCREMENT, quando INTEGER NOT NULL, conta TEXT, personagem INTEGER,
              nome TEXT NOT NULL, tipo TEXT NOT NULL, atributo TEXT, dificuldade INTEGER, dado TEXT NOT NULL,
              modificadores TEXT NOT NULL DEFAULT '', total INTEGER NOT NULL, resultado TEXT NOT NULL DEFAULT '',
              adversario TEXT)""",
            "CREATE INDEX rolagem_conta ON rolagem(conta, quando)",
            """
            CREATE TABLE conflito (
              id INTEGER PRIMARY KEY AUTOINCREMENT, quando INTEGER NOT NULL, tipo TEXT NOT NULL,
              conta TEXT NOT NULL, outra TEXT, detalhe TEXT)""",
            "CREATE INDEX conflito_conta ON conflito(conta, quando)",
            "CREATE TABLE nota (id INTEGER PRIMARY KEY AUTOINCREMENT, personagem INTEGER NOT NULL, autor TEXT NOT NULL, quando INTEGER NOT NULL, texto TEXT NOT NULL)",
        },
    };

    private final Connection c;

    private Repositorio(Connection c) { this.c = c; }

    /** Abre (ou cria) o arquivo do banco e aplica as migrações que faltam. */
    public static Repositorio abrir(Path arquivo) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
        } catch (Exception e) {
            throw new SQLException("não foi possível preparar o SQLite: " + e.getMessage(), e);
        }
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + arquivo.toAbsolutePath());
        Repositorio r = new Repositorio(c);
        try (Statement s = c.createStatement()) {
            // WAL + synchronous=FULL: uma transação confirmada sobrevive a queda do servidor ou de energia.
            s.execute("PRAGMA journal_mode=WAL");
            s.execute("PRAGMA synchronous=FULL");
            s.execute("PRAGMA foreign_keys=OFF");
            s.execute("PRAGMA busy_timeout=5000");
        }
        r.migrar(arquivo);
        return r;
    }

    /** Banco só em memória, para os testes. */
    public static Repositorio emMemoria() throws SQLException {
        Repositorio r = new Repositorio(DriverManager.getConnection("jdbc:sqlite::memory:"));
        r.migrar(null);
        return r;
    }

    public int versao() throws SQLException {
        try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery("PRAGMA user_version")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void migrar(Path arquivo) throws SQLException {
        int atual = versao();
        if (atual > MIGRACOES.length)
            throw new SQLException("o banco está na versão " + atual + ", mais nova que este plugin (" + MIGRACOES.length
                + "). Atualize o plugin; não abra este banco com uma versão antiga.");
        if (atual > 0 && atual < MIGRACOES.length && arquivo != null) {
            // Cópia do banco antes de mudar o formato, para poder voltar atrás.
            try (Statement s = c.createStatement()) { s.execute("PRAGMA wal_checkpoint(TRUNCATE)"); }
            try {
                Files.copy(arquivo, arquivo.resolveSibling(arquivo.getFileName() + ".antes-da-v" + MIGRACOES.length),
                    StandardCopyOption.REPLACE_EXISTING);
            } catch (java.io.IOException e) {
                throw new SQLException("não foi possível copiar o banco antes da migração: " + e.getMessage(), e);
            }
        }
        for (int v = atual; v < MIGRACOES.length; v++) {
            final int versao = v;
            tx(() -> {
                try (Statement s = c.createStatement()) {
                    for (String sql : MIGRACOES[versao]) s.execute(sql);
                    s.execute("PRAGMA user_version=" + (versao + 1));
                }
                return null;
            });
        }
    }

    @Override public void close() throws SQLException { c.close(); }

    // ------------------------------------------------------------------ contas

    /** Registra a entrada de um jogador autenticado: cria a conta se não existe e atualiza o nick. */
    public Conta contaAoEntrar(UUID id, String nick, Instant agora) throws SQLException {
        return tx(() -> {
            exec("INSERT INTO conta (uuid, nick, nick_chave, primeira_entrada, ultima_entrada) VALUES (?,?,?,?,?) "
                + "ON CONFLICT(uuid) DO UPDATE SET nick=excluded.nick, nick_chave=excluded.nick_chave, ultima_entrada=excluded.ultima_entrada",
                id.toString(), nick, nick.toLowerCase(java.util.Locale.ROOT), agora.toEpochMilli(), agora.toEpochMilli());
            return conta(id).orElseThrow();
        });
    }

    public Optional<Conta> conta(UUID id) throws SQLException {
        try (PreparedStatement p = prep("SELECT * FROM conta WHERE uuid=?", id.toString()); ResultSet rs = p.executeQuery()) {
            return rs.next() ? Optional.of(lerConta(rs)) : Optional.empty();
        }
    }

    /** Busca pelo último nick visto. Se o nick já foi de mais de uma conta, vale a que entrou por último. */
    public Optional<Conta> contaPorNick(String nick) throws SQLException {
        try (PreparedStatement p = prep("SELECT * FROM conta WHERE nick_chave=? ORDER BY ultima_entrada DESC LIMIT 1",
            nick.toLowerCase(java.util.Locale.ROOT)); ResultSet rs = p.executeQuery()) {
            return rs.next() ? Optional.of(lerConta(rs)) : Optional.empty();
        }
    }

    public Conta contaPorNickOuErro(String nick) throws SQLException {
        return contaPorNick(nick).orElseThrow(() -> new ErroRegra("erro.conta-desconhecida", "nick", nick));
    }

    private Conta lerConta(ResultSet rs) throws SQLException {
        String uuid = rs.getString("uuid");
        Set<String> racas = new HashSet<>();
        try (PreparedStatement p = prep("SELECT raca FROM raca_liberada WHERE conta=?", uuid); ResultSet r2 = p.executeQuery()) {
            while (r2.next()) racas.add(r2.getString(1));
        }
        return new Conta(UUID.fromString(uuid), rs.getString("nick"), Instant.ofEpochMilli(rs.getLong("primeira_entrada")),
            Instant.ofEpochMilli(rs.getLong("ultima_entrada")), (Integer) rs.getObject("vagas"), Set.copyOf(racas),
            rs.getObject("personagem_ativo") == null ? null : rs.getLong("personagem_ativo"));
    }

    public void definirVagas(UUID conta, int vagas) throws SQLException {
        tx(() -> { exec("UPDATE conta SET vagas=? WHERE uuid=?", vagas, conta.toString()); return null; });
    }

    public void liberarRaca(UUID conta, String raca) throws SQLException {
        tx(() -> { exec("INSERT OR IGNORE INTO raca_liberada (conta, raca) VALUES (?,?)", conta.toString(), raca); return null; });
    }

    // ------------------------------------------------------------- personagens

    public Optional<Personagem> personagem(long id) throws SQLException {
        try (PreparedStatement p = prep("SELECT * FROM personagem WHERE id=?", id); ResultSet rs = p.executeQuery()) {
            return rs.next() ? Optional.of(lerPersonagem(rs)) : Optional.empty();
        }
    }

    public Optional<Personagem> personagemAtivo(UUID conta) throws SQLException {
        try (PreparedStatement p = prep("SELECT p.* FROM personagem p JOIN conta c ON c.personagem_ativo=p.id WHERE c.uuid=? AND p.situacao='ativo'",
            conta.toString()); ResultSet rs = p.executeQuery()) {
            return rs.next() ? Optional.of(lerPersonagem(rs)) : Optional.empty();
        }
    }

    public Personagem personagemAtivoOuErro(Conta conta) throws SQLException {
        return personagemAtivo(conta.id()).orElseThrow(() -> new ErroRegra("erro.alvo-sem-personagem", "nick", conta.nick()));
    }

    public List<Personagem> personagensDaConta(UUID conta) throws SQLException {
        List<Personagem> lista = new ArrayList<>();
        try (PreparedStatement p = prep("SELECT * FROM personagem WHERE conta=? ORDER BY criado_em", conta.toString()); ResultSet rs = p.executeQuery()) {
            while (rs.next()) lista.add(lerPersonagem(rs));
        }
        return lista;
    }

    /** Personagem da conta pelo nome (sem diferenciar maiúsculas) ou pelo número interno. */
    public Optional<Personagem> personagemDaConta(UUID conta, String nomeOuId) throws SQLException {
        String chave = RegrasFicha.chaveDoNome(nomeOuId);
        for (Personagem p : personagensDaConta(conta))
            if (RegrasFicha.chaveDoNome(p.nome()).equals(chave) || String.valueOf(p.id()).equals(nomeOuId.strip())) return Optional.of(p);
        return Optional.empty();
    }

    private Personagem lerPersonagem(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        Map<String, Integer> atributos = new LinkedHashMap<>();
        try (PreparedStatement p = prep("SELECT atributo, valor FROM personagem_atributo WHERE personagem=?", id); ResultSet r2 = p.executeQuery()) {
            while (r2.next()) atributos.put(r2.getString(1), r2.getInt(2));
        }
        return new Personagem(id, UUID.fromString(rs.getString("conta")), rs.getInt("vaga"), rs.getString("nome"),
            rs.getInt("idade"), rs.getString("raca"), rs.getString("tratamento"), rs.getString("aparencia"),
            rs.getString("oficio"), instante(rs, "oficio_trocado_em"), Map.copyOf(atributos),
            Situacao.deId(rs.getString("situacao")), Revisao.deId(rs.getString("revisao")), rs.getString("recado"),
            instante(rs, "criado_em"), instante(rs, "encerrado_em"));
    }

    public boolean nomeEmUso(String nome) throws SQLException {
        try (PreparedStatement p = prep("SELECT 1 FROM personagem WHERE nome_chave=?", RegrasFicha.chaveDoNome(nome)); ResultSet rs = p.executeQuery()) {
            return rs.next();
        }
    }

    /** Quantos personagens vivos a conta tem e quantos foram encerrados depois de {@code desde}. */
    public int[] ocupacao(UUID conta, Instant desde) throws SQLException {
        int vivos = 0, recentes = 0;
        for (Personagem p : personagensDaConta(conta)) {
            if (p.situacao() == Situacao.ATIVO) vivos++;
            else if (p.encerradoEm() != null && p.encerradoEm().isAfter(desde)) recentes++;
        }
        return new int[] { vivos, recentes };
    }

    /**
     * Cria o personagem e o torna o ativo da conta, na mesma transação. O nome
     * é único no servidor, inclusive contra mortos e aposentados.
     */
    public Personagem criarPersonagem(NovoPersonagem n, Instant agora) throws SQLException {
        return tx(() -> {
            if (nomeEmUso(n.nome())) throw new ErroRegra("ficha.erro.nome-em-uso", "nome", n.nome());
            long id;
            try (PreparedStatement p = c.prepareStatement(
                "INSERT INTO personagem (conta, vaga, nome, nome_chave, idade, raca, tratamento, aparencia, oficio, situacao, revisao, criado_em) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
                args(p, n.conta().toString(), n.vaga(), n.nome(), RegrasFicha.chaveDoNome(n.nome()), n.idade(), n.raca(),
                    n.tratamento(), n.aparencia(), n.oficio(), Situacao.ATIVO.id(), Revisao.NOVA.id(), agora.toEpochMilli());
                p.executeUpdate();
                try (ResultSet k = p.getGeneratedKeys()) { k.next(); id = k.getLong(1); }
            }
            for (Map.Entry<String, Integer> e : n.atributos().entrySet())
                exec("INSERT INTO personagem_atributo (personagem, atributo, valor) VALUES (?,?,?)", id, e.getKey(), e.getValue());
            exec("UPDATE conta SET personagem_ativo=? WHERE uuid=?", id, n.conta().toString());
            return personagem(id).orElseThrow();
        });
    }

    /** Campos que o jogador edita. Editar uma ficha devolvida a coloca de novo na fila de revisão. */
    public void editarFicha(long id, int idade, String tratamento, String aparencia, String oficio, Instant oficioTrocadoEm) throws SQLException {
        tx(() -> {
            exec("UPDATE personagem SET idade=?, tratamento=?, aparencia=?, oficio=?, oficio_trocado_em=?, "
                + "revisao=CASE WHEN revisao='devolvida' THEN 'nova' ELSE revisao END, "
                + "recado=CASE WHEN revisao='devolvida' THEN NULL ELSE recado END WHERE id=?",
                idade, tratamento, aparencia, oficio, oficioTrocadoEm == null ? null : oficioTrocadoEm.toEpochMilli(), id);
            return null;
        });
    }

    public void renomear(long id, String nome) throws SQLException {
        tx(() -> {
            String chave = RegrasFicha.chaveDoNome(nome);
            try (PreparedStatement p = prep("SELECT 1 FROM personagem WHERE nome_chave=? AND id<>?", chave, id); ResultSet rs = p.executeQuery()) {
                if (rs.next()) throw new ErroRegra("ficha.erro.nome-em-uso", "nome", nome);
            }
            exec("UPDATE personagem SET nome=?, nome_chave=? WHERE id=?", nome, chave, id);
            return null;
        });
    }

    public void definirRaca(long id, String raca) throws SQLException {
        tx(() -> { exec("UPDATE personagem SET raca=? WHERE id=?", raca, id); return null; });
    }

    public void definirAtributo(long id, String atributo, int valor) throws SQLException {
        tx(() -> {
            exec("INSERT INTO personagem_atributo (personagem, atributo, valor) VALUES (?,?,?) "
                + "ON CONFLICT(personagem, atributo) DO UPDATE SET valor=excluded.valor", id, atributo, valor);
            return null;
        });
    }

    public void definirRevisao(long id, Revisao revisao, String recado) throws SQLException {
        tx(() -> { exec("UPDATE personagem SET revisao=?, recado=? WHERE id=?", revisao.id(), recado, id); return null; });
    }

    /** Aposenta ou mata: muda a situação e tira o personagem do posto de ativo, na mesma transação. */
    public void encerrar(long id, Situacao situacao, Instant agora) throws SQLException {
        tx(() -> {
            exec("UPDATE personagem SET situacao=?, encerrado_em=? WHERE id=?", situacao.id(), agora.toEpochMilli(), id);
            exec("UPDATE conta SET personagem_ativo=NULL WHERE personagem_ativo=?", id);
            return null;
        });
    }

    /** Traz um aposentado de volta. Quem chama confere se há vaga. */
    public void reativar(long id, UUID conta) throws SQLException {
        tx(() -> {
            exec("UPDATE personagem SET situacao='ativo', encerrado_em=NULL WHERE id=?", id);
            exec("UPDATE conta SET personagem_ativo=? WHERE uuid=?", id, conta.toString());
            return null;
        });
    }

    public List<PersonagemDeConta> filaDeRevisao(Revisao revisao, int limite) throws SQLException {
        List<PersonagemDeConta> lista = new ArrayList<>();
        try (PreparedStatement p = prep("SELECT p.*, c.nick AS nick_conta FROM personagem p JOIN conta c ON c.uuid=p.conta "
            + "WHERE p.situacao='ativo' AND p.revisao=? ORDER BY p.criado_em LIMIT ?", revisao.id(), limite); ResultSet rs = p.executeQuery()) {
            while (rs.next()) lista.add(new PersonagemDeConta(lerPersonagem(rs), rs.getString("nick_conta")));
        }
        return lista;
    }

    // -------------------------------------------------------------- ferimentos

    public Optional<Ferimento> ferimentoAtivo(long personagem, Instant agora) throws SQLException {
        try (PreparedStatement p = prep("SELECT * FROM ferimento WHERE personagem=? AND encerrado=0 AND fim>? ORDER BY id DESC LIMIT 1",
            personagem, agora.toEpochMilli()); ResultSet rs = p.executeQuery()) {
            if (!rs.next()) return Optional.empty();
            return Optional.of(new Ferimento(rs.getLong("id"), personagem, Grau.deId(rs.getString("grau")), rs.getString("origem"),
                instante(rs, "inicio"), instante(rs, "fim"), rs.getString("tratado_dia"), rs.getInt("tratado_vezes")));
        }
    }

    /** Troca o ferimento ativo do personagem por este (o anterior fica no histórico, encerrado). */
    public Ferimento gravarFerimento(long personagem, Grau grau, String origem, Instant agora, Instant fim) throws SQLException {
        return tx(() -> {
            exec("UPDATE ferimento SET encerrado=1 WHERE personagem=? AND encerrado=0", personagem);
            if (grau == Grau.NENHUM) return null;
            exec("INSERT INTO ferimento (personagem, grau, origem, inicio, fim) VALUES (?,?,?,?,?)",
                personagem, grau.id(), origem, agora.toEpochMilli(), fim.toEpochMilli());
            return ferimentoAtivo(personagem, agora).orElse(null);
        });
    }

    /** Registra uma tentativa de tratamento e, se deu certo, o grau e o prazo novos. */
    public Ferimento registrarTratamento(Ferimento f, String dia, int vezes, Grau novoGrau, Instant novoFim, Instant agora) throws SQLException {
        return tx(() -> {
            if (novoGrau == Grau.NENHUM)
                exec("UPDATE ferimento SET tratado_dia=?, tratado_vezes=?, encerrado=1 WHERE id=?", dia, vezes, f.id());
            else
                exec("UPDATE ferimento SET tratado_dia=?, tratado_vezes=?, grau=?, fim=? WHERE id=?", dia, vezes, novoGrau.id(), novoFim.toEpochMilli(), f.id());
            return ferimentoAtivo(f.personagem(), agora).orElse(null);
        });
    }

    // ---------------------------------------------------------- consentimentos

    public Consentimento consentir(UUID de, long personagem, UUID para, Consentimento.GrauC grau, Instant agora, Instant validade) throws SQLException {
        return tx(() -> {
            // Um consentimento novo do mesmo grau para a mesma conta substitui o anterior.
            exec("UPDATE consentimento SET fim='cancelado', fim_em=? WHERE de=? AND para=? AND personagem=? AND grau=? AND fim IS NULL",
                agora.toEpochMilli(), de.toString(), para.toString(), personagem, grau.id());
            exec("INSERT INTO consentimento (de, personagem, para, grau, inicio, validade) VALUES (?,?,?,?,?,?)",
                de.toString(), personagem, para.toString(), grau.id(), agora.toEpochMilli(), validade.toEpochMilli());
            List<Consentimento> l = consentimentos("de=? ORDER BY id DESC LIMIT 1", de.toString());
            return l.get(0);
        });
    }

    /** Marca como expirados os vencidos e devolve os que ainda valem, dados por esta conta. */
    public List<Consentimento> consentimentosValidos(UUID de, Instant agora) throws SQLException {
        return tx(() -> {
            exec("UPDATE consentimento SET fim='expirado', fim_em=validade WHERE fim IS NULL AND validade<=?", agora.toEpochMilli());
            return consentimentos("de=? AND fim IS NULL ORDER BY id", de.toString());
        });
    }

    public int cancelarConsentimentos(UUID de, Instant agora) throws SQLException {
        return tx(() -> {
            exec("UPDATE consentimento SET fim='expirado', fim_em=validade WHERE fim IS NULL AND validade<=?", agora.toEpochMilli());
            return exec("UPDATE consentimento SET fim='cancelado', fim_em=? WHERE de=? AND fim IS NULL", agora.toEpochMilli(), de.toString());
        });
    }

    public List<Consentimento> historicoDeConsentimentos(UUID conta, int limite) throws SQLException {
        return consentimentos("(de=? OR para=?) ORDER BY id DESC LIMIT " + limite, conta.toString(), conta.toString());
    }

    private List<Consentimento> consentimentos(String onde, Object... a) throws SQLException {
        List<Consentimento> lista = new ArrayList<>();
        try (PreparedStatement p = prep("SELECT * FROM consentimento WHERE " + onde, a); ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                String fim = rs.getString("fim");
                lista.add(new Consentimento(rs.getLong("id"), UUID.fromString(rs.getString("de")), rs.getLong("personagem"),
                    UUID.fromString(rs.getString("para")), Consentimento.GrauC.deId(rs.getString("grau")), instante(rs, "inicio"),
                    instante(rs, "validade"), fim == null ? null : Consentimento.Fim.valueOf(fim.toUpperCase(java.util.Locale.ROOT)),
                    instante(rs, "fim_em")));
            }
        }
        return lista;
    }

    /**
     * Aplica um desfecho com consentimento, em uma só transação: confere de novo
     * no banco se o consentimento vale, gasta-o, registra e muda o personagem.
     * Devolve o ferimento novo (grave) ou null (morte).
     */
    public Ferimento aplicarDesfechoConsentido(UUID alvo, long personagem, UUID quemAplica, Consentimento.GrauC grau,
                                               Instant agora, Instant fimFerimento) throws SQLException {
        return tx(() -> {
            Consentimento usado = null;
            for (Consentimento k : consentimentos("de=? AND fim IS NULL", alvo.toString()))
                if (k.autoriza(grau, personagem, quemAplica, agora)) usado = k;
            if (usado == null) throw new ErroRegra("desfecho.erro.sem-consentimento");
            exec("UPDATE consentimento SET fim='usado', fim_em=? WHERE id=?", agora.toEpochMilli(), usado.id());
            return desfecho(alvo, personagem, grau, quemAplica.toString(), usado.id(), null, agora, fimFerimento);
        });
    }

    /** Desfecho aplicado pela staff nos casos de exceção. O motivo é obrigatório. */
    public Ferimento aplicarDesfechoDaStaff(UUID alvo, long personagem, Consentimento.GrauC grau, String autor, String motivo,
                                            Instant agora, Instant fimFerimento) throws SQLException {
        if (motivo == null || motivo.isBlank()) throw new ErroRegra("desfecho.erro.motivo");
        return tx(() -> desfecho(alvo, personagem, grau, autor, null, motivo, agora, fimFerimento));
    }

    private Ferimento desfecho(UUID alvo, long personagem, Consentimento.GrauC grau, String autor, Long consentimento,
                               String motivo, Instant agora, Instant fimFerimento) throws SQLException {
        exec("INSERT INTO desfecho (personagem, conta, grau, aplicado_por, consentimento, motivo, quando) VALUES (?,?,?,?,?,?,?)",
            personagem, alvo.toString(), grau.id(), autor, consentimento, motivo, agora.toEpochMilli());
        if (grau == Consentimento.GrauC.MORTE) {
            exec("UPDATE personagem SET situacao='morto', encerrado_em=? WHERE id=?", agora.toEpochMilli(), personagem);
            exec("UPDATE conta SET personagem_ativo=NULL WHERE personagem_ativo=?", personagem);
            exec("UPDATE ferimento SET encerrado=1 WHERE personagem=? AND encerrado=0", personagem);
            return null;
        }
        if (grau != Consentimento.GrauC.GRAVE) throw new ErroRegra("desfecho.erro.grau");
        exec("UPDATE ferimento SET encerrado=1 WHERE personagem=? AND encerrado=0", personagem);
        exec("INSERT INTO ferimento (personagem, grau, origem, inicio, fim) VALUES (?,?,?,?,?)",
            personagem, Grau.GRAVE.id(), consentimento == null ? "staff" : "desfecho", agora.toEpochMilli(), fimFerimento.toEpochMilli());
        return ferimentoAtivo(personagem, agora).orElse(null);
    }

    public List<RegistroDesfecho> desfechosDaConta(UUID conta, int limite) throws SQLException {
        List<RegistroDesfecho> lista = new ArrayList<>();
        try (PreparedStatement p = prep("SELECT d.*, p.nome AS nome_p FROM desfecho d LEFT JOIN personagem p ON p.id=d.personagem "
            + "WHERE d.conta=? OR d.aplicado_por=? ORDER BY d.id DESC LIMIT ?", conta.toString(), conta.toString(), limite); ResultSet rs = p.executeQuery()) {
            while (rs.next())
                lista.add(new RegistroDesfecho(rs.getLong("personagem"), rs.getString("nome_p"), rs.getString("grau"),
                    rs.getString("aplicado_por"), (Long) (rs.getObject("consentimento") == null ? null : rs.getLong("consentimento")),
                    rs.getString("motivo"), instante(rs, "quando")));
        }
        return lista;
    }

    // ------------------------------------------------- rolagens, conflito, notas

    public void registrarRolagem(Rolagem r) throws SQLException {
        tx(() -> {
            exec("INSERT INTO rolagem (quando, conta, personagem, nome, tipo, atributo, dificuldade, dado, modificadores, total, resultado, adversario) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                r.quando().toEpochMilli(), r.conta() == null ? null : r.conta().toString(), r.personagem(), r.nome(), r.tipo(),
                r.atributo(), r.dificuldade(), r.dado(), r.modificadores(), r.total(), r.resultado(), r.adversario());
            return null;
        });
    }

    public List<Rolagem> rolagens(UUID conta, int quantidade) throws SQLException {
        List<Rolagem> lista = new ArrayList<>();
        try (PreparedStatement p = prep("SELECT * FROM rolagem WHERE conta=? ORDER BY quando DESC, id DESC LIMIT ?", conta.toString(), quantidade); ResultSet rs = p.executeQuery()) {
            while (rs.next())
                lista.add(new Rolagem(instante(rs, "quando"), conta, rs.getObject("personagem") == null ? null : rs.getLong("personagem"),
                    rs.getString("nome"), rs.getString("tipo"), rs.getString("atributo"),
                    rs.getObject("dificuldade") == null ? null : rs.getInt("dificuldade"), rs.getString("dado"),
                    rs.getString("modificadores"), rs.getInt("total"), rs.getString("resultado"), rs.getString("adversario")));
        }
        return lista;
    }

    public int apagarRolagensAntigas(Instant antesDe) throws SQLException {
        return tx(() -> exec("DELETE FROM rolagem WHERE quando<?", antesDe.toEpochMilli()));
    }

    /** Registro para contestação: ataque, derrota, fuga, saída no meio do conflito. */
    public void registrarConflito(String tipo, UUID conta, UUID outra, String detalhe, Instant agora) throws SQLException {
        tx(() -> {
            exec("INSERT INTO conflito (quando, tipo, conta, outra, detalhe) VALUES (?,?,?,?,?)",
                agora.toEpochMilli(), tipo, conta.toString(), outra == null ? null : outra.toString(), detalhe);
            return null;
        });
    }

    public void anotar(long personagem, String autor, String texto, Instant agora) throws SQLException {
        tx(() -> { exec("INSERT INTO nota (personagem, autor, quando, texto) VALUES (?,?,?,?)", personagem, autor, agora.toEpochMilli(), texto); return null; });
    }

    public List<Nota> notas(long personagem) throws SQLException {
        List<Nota> lista = new ArrayList<>();
        try (PreparedStatement p = prep("SELECT * FROM nota WHERE personagem=? ORDER BY id", personagem); ResultSet rs = p.executeQuery()) {
            while (rs.next()) lista.add(new Nota(personagem, rs.getString("autor"), instante(rs, "quando"), rs.getString("texto")));
        }
        return lista;
    }

    // ------------------------------------------------------- migrar e apagar

    /**
     * Move tudo de uma conta para outra (troca de nick em modo offline). A conta
     * de destino precisa existir, isto é, o nick novo já entrou ao menos uma
     * vez (o plugin nunca calcula UUID), e não pode ter personagens.
     */
    public void migrarConta(UUID antiga, UUID nova) throws SQLException {
        tx(() -> {
            if (antiga.equals(nova)) throw new ErroRegra("conta.erro.mesma");
            Conta a = conta(antiga).orElseThrow(), n = conta(nova).orElseThrow();
            if (!personagensDaConta(nova).isEmpty()) throw new ErroRegra("conta.erro.destino-ocupado", "nick", n.nick());
            String de = antiga.toString(), para = nova.toString();
            exec("UPDATE personagem SET conta=? WHERE conta=?", para, de);
            exec("UPDATE OR IGNORE raca_liberada SET conta=? WHERE conta=?", para, de);
            exec("DELETE FROM raca_liberada WHERE conta=?", de);
            exec("UPDATE consentimento SET de=? WHERE de=?", para, de);
            exec("UPDATE consentimento SET para=? WHERE para=?", para, de);
            exec("UPDATE desfecho SET conta=? WHERE conta=?", para, de);
            exec("UPDATE desfecho SET aplicado_por=? WHERE aplicado_por=?", para, de);
            exec("UPDATE rolagem SET conta=? WHERE conta=?", para, de);
            exec("UPDATE conflito SET conta=? WHERE conta=?", para, de);
            exec("UPDATE conflito SET outra=? WHERE outra=?", para, de);
            // A data de primeira entrada mais antiga é a que vale (proteção de novato não recomeça).
            exec("UPDATE conta SET personagem_ativo=?, vagas=?, primeira_entrada=? WHERE uuid=?", a.personagemAtivo(), a.vagas(),
                Math.min(a.primeiraEntrada().toEpochMilli(), n.primeiraEntrada().toEpochMilli()), para);
            exec("DELETE FROM conta WHERE uuid=?", de);
            return null;
        });
    }

    /** Apaga a conta e tudo ligado a ela (pedido de privacidade). Não há como desfazer. */
    public void apagarConta(UUID conta) throws SQLException {
        tx(() -> {
            String u = conta.toString();
            for (Personagem p : personagensDaConta(conta)) {
                exec("DELETE FROM personagem_atributo WHERE personagem=?", p.id());
                exec("DELETE FROM ferimento WHERE personagem=?", p.id());
                exec("DELETE FROM nota WHERE personagem=?", p.id());
                exec("DELETE FROM consentimento WHERE personagem=?", p.id());
                exec("DELETE FROM desfecho WHERE personagem=?", p.id());
                exec("UPDATE rolagem SET adversario=NULL WHERE adversario=?", p.nome());
            }
            exec("DELETE FROM personagem WHERE conta=?", u);
            exec("DELETE FROM raca_liberada WHERE conta=?", u);
            exec("DELETE FROM consentimento WHERE de=? OR para=?", u, u);
            exec("DELETE FROM desfecho WHERE conta=? OR aplicado_por=?", u, u);
            exec("DELETE FROM rolagem WHERE conta=?", u);
            exec("DELETE FROM conflito WHERE conta=? OR outra=?", u, u);
            exec("DELETE FROM conta WHERE uuid=?", u);
            return null;
        });
    }

    // ---------------------------------------------------------------- suporte

    @FunctionalInterface private interface Operacao<T> { T rodar() throws SQLException; }

    /** Roda em transação. Transações aninhadas entram na de fora. */
    private <T> T tx(Operacao<T> op) throws SQLException {
        if (!c.getAutoCommit()) return op.rodar();
        c.setAutoCommit(false);
        try {
            T r = op.rodar();
            c.commit();
            return r;
        } catch (SQLException | RuntimeException e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(true);
        }
    }

    private PreparedStatement prep(String sql, Object... a) throws SQLException {
        PreparedStatement p = c.prepareStatement(sql);
        args(p, a);
        return p;
    }

    private static void args(PreparedStatement p, Object... a) throws SQLException {
        for (int i = 0; i < a.length; i++) p.setObject(i + 1, a[i]);
    }

    private int exec(String sql, Object... a) throws SQLException {
        try (PreparedStatement p = prep(sql, a)) { return p.executeUpdate(); }
    }

    private static Instant instante(ResultSet rs, String coluna) throws SQLException {
        long v = rs.getLong(coluna);
        return rs.wasNull() ? null : Instant.ofEpochMilli(v);
    }
}
