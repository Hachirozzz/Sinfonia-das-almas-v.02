package nucleorp.dados;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import nucleorp.dados.Modelo.Conta;
import nucleorp.dados.Modelo.Ferimento;
import nucleorp.dados.Modelo.NovoPersonagem;
import nucleorp.dados.Modelo.Personagem;
import nucleorp.dados.Modelo.Revisao;
import nucleorp.dados.Modelo.Rolagem;
import nucleorp.dados.Modelo.Situacao;
import nucleorp.regra.Consentimento;
import nucleorp.regra.ErroRegra;
import nucleorp.regra.Grau;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RepositorioTest {
    private static final Instant T0 = Instant.parse("2026-11-11T12:00:00Z");
    private Repositorio r;
    private final UUID joana = UUID.randomUUID(), bruno = UUID.randomUUID();

    @BeforeEach void abrir() throws Exception {
        r = Repositorio.emMemoria();
        r.contaAoEntrar(joana, "joana_m", T0);
        r.contaAoEntrar(bruno, "bruno_k", T0);
    }

    @AfterEach void fechar() throws Exception { r.close(); }

    private Personagem criar(UUID conta, String nome) throws Exception {
        return r.criarPersonagem(new NovoPersonagem(conta, 1, nome, 34, "raca_a", "ela", "Magra, capa cinza.", "escriba",
            Map.of("braco", 0, "passo", 1, "tino", 4, "voz", 1)), T0);
    }

    @Test void contaGuardaNickEPrimeiraEntrada() throws Exception {
        Conta c = r.contaAoEntrar(joana, "Joana_M", T0.plusSeconds(3600));
        assertEquals("Joana_M", c.nick());
        assertEquals(T0, c.primeiraEntrada(), "a primeira entrada não muda");
        assertEquals(joana, r.contaPorNick("JOANA_m").orElseThrow().id());
        assertTrue(r.contaPorNick("ninguem").isEmpty());
        assertNull(c.vagas());
        r.definirVagas(joana, 2);
        r.liberarRaca(joana, "raca_b");
        r.liberarRaca(joana, "raca_b");
        c = r.conta(joana).orElseThrow();
        assertEquals(2, c.vagas());
        assertTrue(c.racasLiberadas().contains("raca_b"));
    }

    @Test void criarPersonagemAtivaENomeEUnicoAteParaMortos() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        assertEquals(p.id(), r.conta(joana).orElseThrow().personagemAtivo());
        assertEquals(4, r.personagemAtivo(joana).orElseThrow().atributo("tino"));
        assertEquals(Revisao.NOVA, p.revisao());
        assertEquals("ficha.erro.nome-em-uso", assertThrows(ErroRegra.class, () -> criar(bruno, "ALDARA venn")).chave());
        assertTrue(r.personagensDaConta(bruno).isEmpty(), "a criação recusada não deixa resto");
        r.encerrar(p.id(), Situacao.MORTO, T0.plusSeconds(10));
        assertTrue(r.personagemAtivo(joana).isEmpty());
        assertNull(r.conta(joana).orElseThrow().personagemAtivo());
        assertThrows(ErroRegra.class, () -> criar(bruno, "Aldara Venn"), "nome de morto fica reservado");
        assertArrayEquals(new int[] {0, 1}, r.ocupacao(joana, T0));
        assertArrayEquals(new int[] {0, 0}, r.ocupacao(joana, T0.plusSeconds(11)));
    }

    @Test void textoHostilEGuardadoComoTexto() throws Exception {
        String nome = "Robert'); DROP TABLE personagem;--";
        Personagem p = criar(joana, nome);
        assertEquals(nome, r.personagem(p.id()).orElseThrow().nome());
        assertTrue(r.nomeEmUso(nome));
    }

    @Test void editarFichaDevolvidaVoltaParaAFila() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        r.definirRevisao(p.id(), Revisao.DEVOLVIDA, "aparência descreve poder");
        assertEquals(1, r.filaDeRevisao(Revisao.DEVOLVIDA, 10).size());
        assertEquals("joana_m", r.filaDeRevisao(Revisao.DEVOLVIDA, 10).get(0).nick());
        r.editarFicha(p.id(), 35, "", "Outra aparência.", null, T0);
        Personagem q = r.personagem(p.id()).orElseThrow();
        assertEquals(Revisao.NOVA, q.revisao());
        assertNull(q.recado());
        assertEquals(35, q.idade());
        assertNull(q.oficio());
        r.definirRevisao(p.id(), Revisao.REVISADA, null);
        r.editarFicha(p.id(), 36, "", "Mais uma.", null, T0);
        assertEquals(Revisao.REVISADA, r.personagem(p.id()).orElseThrow().revisao());
    }

    @Test void renomearRespeitaNomeUnico() throws Exception {
        Personagem a = criar(joana, "Aldara Venn"), b = criar(bruno, "Bruno Calado");
        assertThrows(ErroRegra.class, () -> r.renomear(b.id(), "aldara venn"));
        r.renomear(a.id(), "ALDARA VENN");
        r.renomear(a.id(), "Aldara Vennar");
        assertFalse(r.nomeEmUso("Aldara Venn"));
        r.definirAtributo(a.id(), "dom", 2);
        r.definirAtributo(a.id(), "dom", 1);
        assertEquals(1, r.personagem(a.id()).orElseThrow().atributo("dom"));
    }

    @Test void ferimentoAtivoVenceETratamento() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        assertTrue(r.ferimentoAtivo(p.id(), T0).isEmpty());
        Ferimento f = r.gravarFerimento(p.id(), Grau.GRAVE, "desfecho", T0, T0.plus(Duration.ofDays(7)));
        assertEquals(Grau.GRAVE, f.grau());
        f = r.registrarTratamento(f, "2026-11-11", 1, Grau.GRAVE, f.fim(), T0);
        assertEquals(1, f.tratadoVezes());
        f = r.registrarTratamento(f, "2026-11-12", 1, Grau.LEVE, T0.plus(Duration.ofDays(2)), T0);
        assertEquals(Grau.LEVE, f.grau());
        assertTrue(r.ferimentoAtivo(p.id(), T0.plus(Duration.ofDays(3))).isEmpty(), "venceu");
        assertNull(r.registrarTratamento(f, "2026-11-13", 1, Grau.NENHUM, null, T0));
        assertTrue(r.ferimentoAtivo(p.id(), T0).isEmpty());
        r.gravarFerimento(p.id(), Grau.LEVE, "derrota", T0, T0.plus(Duration.ofDays(2)));
        assertNull(r.gravarFerimento(p.id(), Grau.NENHUM, "staff", T0, T0));
    }

    @Test void consentimentoEGastoUmaVezSo() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        Instant validade = T0.plus(Duration.ofMinutes(30));
        r.consentir(joana, p.id(), bruno, Consentimento.GrauC.MORTE, T0, validade);
        assertEquals(1, r.consentimentosValidos(joana, T0.plusSeconds(1)).size());
        // Grau diferente não serve.
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoConsentido(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0.plusSeconds(5), validade));
        // Outra conta não serve.
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoConsentido(joana, p.id(), UUID.randomUUID(), Consentimento.GrauC.MORTE, T0.plusSeconds(5), validade));
        assertEquals(Situacao.ATIVO, r.personagem(p.id()).orElseThrow().situacao(), "recusa não muda nada");
        assertNull(r.aplicarDesfechoConsentido(joana, p.id(), bruno, Consentimento.GrauC.MORTE, T0.plusSeconds(10), validade));
        assertEquals(Situacao.MORTO, r.personagem(p.id()).orElseThrow().situacao());
        assertTrue(r.personagemAtivo(joana).isEmpty());
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoConsentido(joana, p.id(), bruno, Consentimento.GrauC.MORTE, T0.plusSeconds(11), validade),
            "o consentimento já foi usado");
        assertEquals(Consentimento.Fim.USADO, r.historicoDeConsentimentos(joana, 10).get(0).fim());
        assertEquals(1, r.desfechosDaConta(joana, 10).size());
        assertEquals("Aldara Venn", r.desfechosDaConta(bruno, 10).get(0).nomePersonagem());
    }

    @Test void consentimentoExpiradoOuCanceladoNaoAplica() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        Instant validade = T0.plus(Duration.ofMinutes(30));
        r.consentir(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0, validade);
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoConsentido(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, validade, validade));
        assertTrue(r.consentimentosValidos(joana, validade).isEmpty());
        assertEquals(Consentimento.Fim.EXPIRADO, r.historicoDeConsentimentos(joana, 10).get(0).fim());
        r.consentir(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0, validade);
        assertEquals(1, r.cancelarConsentimentos(joana, T0.plusSeconds(1)));
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoConsentido(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0.plusSeconds(2), validade));
        // Consentimento válido de grave aplica ferimento grave.
        r.consentir(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0, validade);
        Ferimento f = r.aplicarDesfechoConsentido(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0.plusSeconds(3), T0.plus(Duration.ofDays(7)));
        assertEquals(Grau.GRAVE, f.grau());
        assertEquals("desfecho", f.origem());
    }

    @Test void desfechoDaStaffExigeMotivo() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoDaStaff(joana, p.id(), Consentimento.GrauC.MORTE, "console", "  ", T0, T0));
        assertThrows(ErroRegra.class, () -> r.aplicarDesfechoDaStaff(joana, p.id(), Consentimento.GrauC.PERMANENTE, "console", "chamado 12", T0, T0));
        assertEquals(Situacao.ATIVO, r.personagem(p.id()).orElseThrow().situacao());
        r.aplicarDesfechoDaStaff(joana, p.id(), Consentimento.GrauC.MORTE, "console", "chamado 12", T0, T0);
        assertEquals(Situacao.MORTO, r.personagem(p.id()).orElseThrow().situacao());
        assertEquals("chamado 12", r.desfechosDaConta(joana, 5).get(0).motivo());
    }

    @Test void rolagensEmOrdemELimpeza() throws Exception {
        for (int i = 0; i < 5; i++)
            r.registrarRolagem(new Rolagem(T0.plusSeconds(i), joana, null, "Aldara Venn", "teste", "tino", 7, String.valueOf(i + 1), "+1 oficio", i + 5, "sucesso", null));
        assertEquals(3, r.rolagens(joana, 3).size());
        assertEquals("5", r.rolagens(joana, 3).get(0).dado(), "a mais recente primeiro");
        assertEquals(2, r.apagarRolagensAntigas(T0.plusSeconds(2)));
        assertEquals(3, r.rolagens(joana, 10).size());
    }

    @Test void migrarContaLevaTudo() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        r.liberarRaca(joana, "raca_b");
        r.definirVagas(joana, 2);
        r.registrarRolagem(new Rolagem(T0, joana, p.id(), "Aldara Venn", "dado", null, null, "4", "", 4, "", null));
        r.consentir(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0, T0.plusSeconds(60));
        UUID nova = UUID.randomUUID();
        r.contaAoEntrar(nova, "joana_nova", T0.plus(Duration.ofDays(30)));
        r.migrarConta(joana, nova);
        assertTrue(r.conta(joana).isEmpty());
        Conta c = r.conta(nova).orElseThrow();
        assertEquals(p.id(), c.personagemAtivo());
        assertEquals(T0, c.primeiraEntrada());
        assertEquals(2, c.vagas());
        assertTrue(c.racasLiberadas().contains("raca_b"));
        assertEquals("Aldara Venn", r.personagemAtivo(nova).orElseThrow().nome());
        assertEquals(1, r.rolagens(nova, 10).size());
        assertEquals(nova, r.historicoDeConsentimentos(bruno, 10).get(0).de());
    }

    @Test void migrarParaContaComPersonagemERecusado() throws Exception {
        criar(joana, "Aldara Venn");
        criar(bruno, "Bruno Calado");
        assertEquals("conta.erro.destino-ocupado", assertThrows(ErroRegra.class, () -> r.migrarConta(joana, bruno)).chave());
        assertThrows(ErroRegra.class, () -> r.migrarConta(joana, joana));
        assertEquals("Aldara Venn", r.personagemAtivo(joana).orElseThrow().nome());
    }

    @Test void apagarContaNaoDeixaRastro() throws Exception {
        Personagem p = criar(joana, "Aldara Venn");
        criar(bruno, "Bruno Calado");
        r.gravarFerimento(p.id(), Grau.LEVE, "derrota", T0, T0.plusSeconds(60));
        r.anotar(p.id(), "staff", "observação", T0);
        r.consentir(joana, p.id(), bruno, Consentimento.GrauC.GRAVE, T0, T0.plusSeconds(60));
        r.registrarRolagem(new Rolagem(T0, joana, p.id(), "Aldara Venn", "dado", null, null, "4", "", 4, "", null));
        r.registrarRolagem(new Rolagem(T0, bruno, null, "Bruno Calado", "disputa", "braco", null, "4", "", 4, "perdeu", "Aldara Venn"));
        r.registrarConflito("ataque", bruno, joana, null, T0);
        r.apagarConta(joana);
        assertTrue(r.conta(joana).isEmpty());
        assertTrue(r.personagem(p.id()).isEmpty());
        assertTrue(r.notas(p.id()).isEmpty());
        assertTrue(r.rolagens(joana, 10).isEmpty());
        assertTrue(r.historicoDeConsentimentos(bruno, 10).isEmpty());
        assertNull(r.rolagens(bruno, 10).get(0).adversario());
        assertFalse(r.nomeEmUso("Aldara Venn"), "o nome fica livre de novo");
        assertEquals("Bruno Calado", r.personagemAtivo(bruno).orElseThrow().nome());
    }

    @Test void arquivoSobreviveAReabrirSemFechar(@TempDir Path pasta) throws Exception {
        Path arquivo = pasta.resolve("nucleo.db");
        UUID id = UUID.randomUUID();
        Repositorio a = Repositorio.abrir(arquivo);
        a.contaAoEntrar(id, "fulano", T0);
        a.criarPersonagem(new NovoPersonagem(id, 1, "Fulano de Tal", 30, "raca_a", "", "x", null, Map.of("tino", 3)), T0);
        assertEquals(1, a.versao());
        // Simula queda do servidor: abre de novo sem fechar a conexão anterior.
        try (Repositorio b = Repositorio.abrir(arquivo)) {
            assertEquals("Fulano de Tal", b.personagemAtivo(id).orElseThrow().nome());
            assertEquals(1, b.versao());
        }
        a.close();
        assertTrue(Files.exists(arquivo));
    }
}
