# Especificação do plugin "núcleo de RP"

Versão 0.1, para o `mc-dev` programar e o `mc-plugins` integrar. As regras que
este plugin executa estão em `ficha.md`, `testes.md`, `conflito.md` e
`progressao-e-economia.md`; em caso de divergência, valem aqueles arquivos e
este deve ser corrigido.

## 1. Escopo

O `DECISOES.md` define o núcleo como "personagens, nome de RP, testes com
dados". Esta especificação inclui também o **estado de conflito** (Fôlego,
ferimentos, consentimento, duelo), porque ele depende da ficha e dos testes e
não existe em plugin pronto. Isso alarga um pouco a decisão registrada; está
listado no fim para o dono confirmar.

### O que o núcleo faz

1. Contas e personagens: ficha, vagas, criação, troca, aposentadoria, morte.
2. Nome de RP: define o nome de exibição do jogador e expõe placeholders.
3. Testes: `/testar`, `/disputar`, `/dado`, comandos de narrador, histórico.
4. Conflito: `/atacar`, Fôlego, ferimentos, `/tratar`, `/duelo`, bloqueio de
   dano entre jogadores, consentimento e desfecho, zonas de paz.
5. Progressão: dias de jogo, marcos e a diária em moeda.

### O que o núcleo não faz (vem de plugin pronto)

| Função | De onde vem |
| --- | --- |
| Canais, alcance, formato e atalhos de chat; `/me`; narração; modo espião | O plugin de chat único (ver `chat.md`) |
| Login, senha, sessão, login automático de conta original | O plugin de login |
| Permissões e grupos (jogador, narrador, staff) | LuckPerms |
| Saldo, pagar, ver saldo | Economia do EssentialsX, por uma API de economia (Vault ou sucessor) |
| Nome sobre a cabeça e lista do TAB | TAB, lendo os placeholders do núcleo |
| Aplicar skin | SkinsRestorer (o núcleo só guarda a skin do personagem e pede a troca) |
| Proteção de áreas e construção | WorldGuard |
| Registro de blocos e reversão | CoreProtect |
| Punições | LibertyBans |
| Sentar e deitar | Plugin pronto do kit |
| Placeholders | PlaceholderAPI (o núcleo registra uma expansão) |

O `mc-plugins` confirma versão e link de cada um. O núcleo deve **ligar e
funcionar** mesmo sem os opcionais (economia, SkinsRestorer, PlaceholderAPI),
só desligando a função correspondente e avisando no console.

## 2. Identidade em modo offline

O servidor roda em modo offline. Regras para o código:

1. A chave da conta é o UUID que o servidor entrega para o jogador conectado
   (`getUniqueId()`), tratado como **valor opaco**. Nunca consultar a API da
   Mojang, nunca converter nick em UUID por conta própria, nunca supor que o
   UUID é o da conta original.
2. Guardar o último nick visto, só para exibir e para busca pela staff.
3. **Nada do núcleo vale antes do login.** Até o plugin de login confirmar a
   autenticação, o núcleo não aplica nome de RP, não aceita comando, não conta
   tempo de jogo e não troca personagem. A integração é por um adaptador
   pequeno para o plugin de login escolhido (evento de "autenticou"); o
   `mc-plugins` informa qual é. Deve existir a opção `login.integracao:
   nenhuma` para teste local sem plugin de login.
4. Em modo offline, nicks que diferem só por maiúsculas podem gerar UUIDs
   diferentes. Barrar isso é tarefa do plugin de login; o `mc-plugins` confere
   a configuração.
5. Quem troca de nick vira outra conta. A staff move os dados com
   `/nucleorp conta migrar`.
6. Comandos recebem **nick** como argumento (com completar automático de quem
   está online). Onde fizer sentido, sem argumento vale o jogador que está na
   mira, dentro do alcance configurado.

## 3. Comandos

Nomes sem acento. Mensagens em português, todas em arquivo de mensagens
editável (formato MiniMessage).

### Jogador (permissão `nucleorp.jogador`, padrão para todos)

| Comando | Atalho | O que faz |
| --- | --- | --- |
| `/personagem criar` | `/pers criar` | Abre a criação guiada no chat |
| `/personagem confirmar` | | Fecha a criação e ativa o personagem |
| `/personagem listar` | | Mostra as vagas e a situação de cada uma |
| `/personagem trocar <número ou nome>` | | Troca o personagem ativo (etapa 3) |
| `/personagem aposentar` | | Aposenta o ativo, com confirmação pelo nome |
| `/personagem skin <endereço ou nick>` | | Guarda a skin do personagem ativo (etapa 3) |
| `/ficha [nick]` | | Mostra a própria ficha, a de alguém, ou a de quem está na mira |
| `/ficha editar <campo> <valor>` | | Campos: `idade`, `tratamento`, `aparencia`, `oficio` |
| `/ficha evoluir <atributo>` | | Gasta um marco (etapa 3) |
| `/testar <atributo> [dificuldade] [±n] [oficio] [traco] [fraqueza]` | `/t` | Teste |
| `/disputar <atributo> [nick] [modificadores]` | `/d` | Pede disputa |
| `/responder <atributo>` | | Responde a disputa ou ataque pendente |
| `/recusar` | | Recusa disputa ou duelo pendente |
| `/dado [NdL]` | | Dado solto; padrão 1d12 |
| `/atacar [nick] [atributo]` | | Abre uma troca de golpes |
| `/fugir` | | Resposta a um ataque: tenta fugir |
| `/render` | | Rende-se; derrotado sem ferimento |
| `/duelo <nick>` e `/aceitar` | | Propõe e aceita duelo |
| `/estado` | | Fôlego, ferimentos, tempo fora de combate |
| `/tratar [nick] [oficio]` | | Tenta reduzir o ferimento de alguém |
| `/consentir <grave\|permanente\|morte> <nick>` | | Registra consentimento |
| `/consentir cancelar` | | Cancela os consentimentos ativos |
| `/desfecho <nick> <grave\|morte>` | | Aplica o desfecho, se houver consentimento válido |
| `/rolagens [quantidade]` | | Histórico das próprias rolagens |

A criação guiada usa mensagens com botões clicáveis no chat. Se a versão do
Paper escolhida tiver interface nativa de formulário estável, o `mc-dev` pode
usá-la; conferir na documentação do Paper antes.

### Narrador (permissão `nucleorp.narrador`)

| Comando | O que faz |
| --- | --- |
| `/pedirteste <atributo> <dificuldade> [nick \| raio]` | Envia botão de teste |
| `/npctestar <bônus> [dificuldade] <nome>` | Rola por um NPC, à vista da cena |
| `/testaroculto <bônus> [dificuldade] [rótulo]` | Rola só para narradores próximos |
| `/rolagens <nick> [quantidade]` | Histórico de qualquer jogador |
| `/premiar <nick> <valor>` | Deposita moeda, até o teto por cena configurado |

### Staff (permissão `nucleorp.staff`)

| Comando | O que faz |
| --- | --- |
| `/nucleorp fichas [novas\|devolvidas]` | Fila de revisão |
| `/nucleorp revisar <nick>` | Marca a ficha ativa como revisada |
| `/nucleorp devolver <nick> <recado>` | Devolve a ficha com um recado ao jogador |
| `/nucleorp renomear <nick> <nome novo>` | Troca o nome do personagem |
| `/nucleorp raca <nick> <raça>` e `/nucleorp atributo <nick> <atributo> <valor>` | Ajustes após aprovação |
| `/nucleorp liberar <nick> <raça restrita>` | Autoriza uma conta a criar personagem de raça restrita |
| `/nucleorp nota <nick> [texto]` | Lê ou acrescenta anotação da staff |
| `/nucleorp ferimento <nick> <nenhum\|leve\|grave>` | Aplica ou remove ferimento |
| `/nucleorp desfecho <nick> <grave\|morte> <motivo ou número do chamado>` | Desfecho pelos casos previstos nas regras da comunidade (cláusula aceita antes, decisão da staff). Motivo obrigatório |
| `/nucleorp reativar <nick> <personagem>` | Traz um aposentado de volta, se houver vaga |
| `/nucleorp consentimentos <nick>` | Histórico de consentimentos e desfechos |
| `/nucleorp ver <nick>` | Ficha completa, com atributos, marcos, dias e notas |

### Administração (permissão `nucleorp.admin`)

| Comando | O que faz |
| --- | --- |
| `/nucleorp recarregar` | Relê configuração e mensagens |
| `/nucleorp vagas <nick> <número>` | Ajusta as vagas de uma conta |
| `/nucleorp conta migrar <nick antigo> <nick novo>` | Move todos os dados de uma conta para outra |
| `/nucleorp conta apagar <nick>` | Apaga a conta e tudo ligado a ela (pedido de privacidade), com confirmação |
| `/nucleorp restaurar <nick> <personagem> [cópia]` | Restaura inventário e posição de uma cópia de segurança |
| `/nucleorp antessala definir` | Marca a posição atual como antessala |
| `/nucleorp zona <criar\|apagar\|listar> ...` | Zonas de paz (seção 7) |

## 4. Dados guardados

Banco SQLite em um arquivo dentro da pasta do plugin. O arquivo **não vai para
o Git**; o `mc-infra` inclui no backup. Toda leitura e escrita fora da thread
principal do servidor.

**Conta**: UUID (chave), último nick, data da primeira entrada, vagas, raças
restritas liberadas, se já recebeu a bolsa inicial, data da última diária,
personagem ativo.

**Personagem**: identificador, conta, vaga, nome (único sem diferenciar
maiúsculas; reservado também para mortos e aposentados), idade, raça,
tratamento, aparência, ofício e data da última troca de ofício, atributos,
marcos ganhos e gastos, dias de jogo, minutos ativos no dia corrente, situação
(`em_criacao`, `ativo`, `aposentado`, `morto`), revisão (`nova`, `revisada`,
`devolvida`, com recado), skin, datas de criação e de encerramento.

**Estado no mundo, por personagem** (etapa 3): inventário, armadura, mão
secundária, baú do Fim, posição, vida, fome, experiência, efeitos ativos, modo
de jogo. Mais as últimas 5 cópias de segurança [cfg] de cada troca.

**Ferimento**: personagem, grau, origem (derrota, desfecho, staff, falha de
atributo), início, fim, data do último tratamento.

**Consentimento**: quem deu, por qual personagem, para quem, grau, início,
validade, e como terminou (cancelado, expirado, usado).

**Rolagem**: data e hora, conta, personagem, tipo (teste, disputa, golpe, dado,
NPC, oculto), atributo, dificuldade, dado, modificadores com rótulo, total,
resultado, adversário. Guardar por 90 dias [cfg].

**Conflito recente**: quem atacou quem, quem foi derrotado por quem, quando
(para o prazo de 24 horas e para contestação).

**Nota da staff**: personagem, autor, data, texto.

Só em memória (perde-se ao reiniciar, de propósito): Fôlego atual, pedidos
pendentes de disputa, ataque e duelo, duelos em curso, esperas entre comandos.

## 5. Comportamentos que não são comando

1. **Nome de exibição.** Com personagem ativo, o nome de exibição do jogador
   passa a ser o nome do personagem. Sem personagem, é o nick.
2. **Antessala.** Conta sem personagem ativo é levada à antessala ao entrar e
   devolvida a ela se sair de um raio configurado. Lá não se ataca, não se
   duela e não se conta tempo de jogo.
3. **Bloqueio de dano entre jogadores.** Todo dano direto ou por projétil de um
   jogador em outro é cancelado, salvo entre os dois participantes de um duelo
   em curso. No duelo, o dano que levaria alguém abaixo do piso de vida é
   cortado e encerra o duelo.
4. **Morte no Minecraft.** Não altera ficha, ferimento nem Fôlego.
5. **Sair no meio do conflito.** Quem desconecta com ataque pendente, com
   Fôlego perdido ou em duelo volta como derrotado, com ferimento leve; fica
   registrado no histórico.
6. **Novato.** Conta com menos de 7 dias [cfg] desde a primeira entrada não
   pode ser alvo de `/atacar`, a menos que tenha atacado aquele jogador nos
   últimos 30 minutos [cfg].
7. **Troca segura** (etapa 3). Gravar no banco, em uma só transação, o estado
   do personagem que sai e a marca de qual passa a ser o ativo; só então
   aplicar o estado do que entra e mandar o servidor salvar o jogador. Guardar
   cópia de segurança antes de cada troca. Ao entrar, se o banco e o jogador
   discordarem sobre o personagem ativo, não aplicar nada, avisar a staff e
   registrar.
8. **Dia de jogo.** Soma minutos só com personagem ativo, fora da antessala e
   com movimento ou mensagem nos últimos 5 minutos [cfg]. Ao fechar o dia de
   jogo: soma 1 ao personagem, concede marco quando for o caso e deposita a
   diária na conta, uma vez por dia.
9. **Falha com custo.** Um atributo pode ser configurado para causar ferimento
   quando o teste falha. Fica desligado; existe para a magia.
10. **Atributo concedido.** Um atributo pode ser marcado como "só por
    concessão": começa em 0, não recebe pontos na criação nem de marco, só a
    staff altera, e quem tem 0 não pode testar com ele. Fica desligado; existe
    para a magia.

## 6. Placeholders

Expansão `nucleorp` para o PlaceholderAPI. Tudo relativo ao jogador
consultado.

| Placeholder | Valor |
| --- | --- |
| `%nucleorp_nome%` | Nome do personagem ativo; o nick, se não houver |
| `%nucleorp_nick%` | Nick da conta |
| `%nucleorp_tem_personagem%` | `sim` ou `nao` |
| `%nucleorp_raca%`, `%nucleorp_raca_id%` | Nome e identificador da raça |
| `%nucleorp_idade%`, `%nucleorp_tratamento%`, `%nucleorp_aparencia%` | Campos da ficha |
| `%nucleorp_oficio%` | Nome do ofício, ou vazio |
| `%nucleorp_ferimento%` | `nenhum`, `leve` ou `grave` |
| `%nucleorp_folego%`, `%nucleorp_folego_max%` | Fôlego |
| `%nucleorp_derrotado%`, `%nucleorp_em_duelo%` | `sim` ou `nao` |
| `%nucleorp_novato%` | `sim` enquanto durar a proteção de novato |
| `%nucleorp_vaga%` | Número da vaga ativa |
| `%nucleorp_dias_de_jogo%`, `%nucleorp_marcos_livres%` | Progressão |
| `%nucleorp_revisao%` | `nova`, `revisada` ou `devolvida` |
| `%nucleorp_atributo_<id>%` | Valor do atributo. **Não usar em nada que outros jogadores vejam** |

O nome de exibição (item 1 da seção 5) é o caminho principal para o plugin de
chat; os placeholders servem ao TAB, ao texto ao passar o mouse e a placares.
O `mc-plugins` confere se o plugin de chat lê placeholders do PlaceholderAPI
diretamente ou por alguma ponte.

## 7. Configuração

Um arquivo `config.yml` com tudo que é número de balanceamento, um
`mensagens.yml` com todos os textos e um `racas-e-oficios.yml` que o `mc-lore`
preenche. Esboço, com os valores iniciais:

```yaml
login:
  integracao: nenhuma          # nome do adaptador do plugin de login

personagem:
  vagas-por-conta: 2
  nome: { min: 3, max: 30 }
  aparencia-max: 200
  espera-apos-encerrar-dias: 3
  troca: { parado-segundos: 5, espera-minutos: 10, copias-de-seguranca: 5 }
  troca-de-oficio-dias: 30

atributos:
  pontos-na-criacao: 5
  maximo-na-criacao: 3
  teto: 4
  lista:
    braco: { nome: "Braço", ataque: true,  defesa: true  }
    passo: { nome: "Passo", ataque: true,  defesa: true  }
    tino:  { nome: "Tino" }
    voz:   { nome: "Voz" }
    # quinto atributo, desligado:
    # dom: { nome: "Dom", so-por-concessao: true, falha-causa-ferimento: leve }

testes:
  dado-lados: 12
  alcance-do-resultado: 16
  dificuldades: { facil: 4, normal: 7, dificil: 10, extrema: 13 }
  modificadores:
    circunstancia-max: 2
    oficio: 1
    traco: 2
    fraqueza: -2
    soma-limite: 3
  disputa: { alcance-da-mira: 6, expira-segundos: 60 }
  dado-solto: { max-dados: 10, max-lados: 100 }
  historico-dias: 90

conflito:
  folego: 3
  folego-volta-minutos: 10
  ataque: { alcance: 6, resposta-segundos: 60, espera-segundos: 20 }
  fuga-protege-minutos: 10
  fora-de-combate-minutos: 30
  protecao-ao-entrar-segundos: 60
  novato: { dias: 7, revide-minutos: 30 }
  duelo: { contagem: 5, piso-de-vida: 4, distancia-max: 32, duracao-minutos: 5 }
  ferimentos:
    leve:  { dias: 2, penalidade: -1, atributos: [braco, passo] }
    grave: { dias: 7, penalidade: -2, atributos: todos }
  tratar: { dificuldade: normal, vezes-por-dia: 1 }
  consentimento-minutos: 30
  zonas: {}                    # preenchido por /nucleorp zona

progressao:
  minutos-para-dia-de-jogo: 45
  inatividade-minutos: 5
  dias-por-marco: 10
  marcos-max: 3
  fuso: "America/Sao_Paulo"

economia:
  bolsa-inicial: 30
  diaria: 5
  premio-max-por-cena: 20
```

**Zona de paz**: uma caixa definida por dois cantos em um mundo, com nome e
duas chaves, `sem-ataque` e `sem-duelo`. Dentro dela o núcleo recusa `/atacar`
e/ou `/duelo`. Testes, disputas e emotes continuam valendo. As zonas ficam no
próprio núcleo para ele não depender da versão do WorldGuard.

## 8. Etapas de entrega

| Etapa | Conteúdo | O servidor pode abrir? |
| --- | --- | --- |
| 1 | Conta, ficha com uma vaga, nome de exibição, placeholders, antessala, testes, disputa, dado, comandos de narrador, histórico, revisão de fichas | Só para teste fechado |
| 2 | Ataque, Fôlego, ferimentos, tratar, duelo e bloqueio de dano, consentimento e desfecho, zonas de paz, proteção de novato | Sim |
| 3 | Várias vagas com troca de inventário, skin por personagem, dias de jogo, marcos, diária e prêmio | Melhoria após a abertura |

A etapa 3 é a única que mexe em inventário. Só entra em produção depois de
testada com troca repetida, queda do servidor no meio da troca e restauração
de cópia.

## 9. Testes que o `mc-dev` deve entregar

- Testes automáticos da conta: resultado, modificadores, limite da soma,
  Triunfo e Tropeço, empate em disputa, perda de Fôlego, validade e uso de
  consentimento.
- Roteiro manual com duas contas offline: criar personagem, testar, disputar,
  atacar até a derrota, tratar, duelar, consentir e aplicar morte, recriar.
- Roteiro de identidade: entrar sem autenticar e conferir que nenhum comando
  responde; migrar conta; apagar conta.

## Decisões para o dono confirmar

1. **O núcleo inclui o estado de conflito** (Fôlego, ferimentos, consentimento,
   duelo), além de personagens, nome e testes. Escolhi isso porque essas regras
   leem a ficha e não existem em plugin pronto. Alternativa: manter o núcleo só
   com ficha e testes e resolver conflito apenas por regra escrita e `/disputar`,
   sem Fôlego nem registro de consentimento no jogo.
2. **SQLite em arquivo**, sem servidor de banco. Escolhi isso porque não há
   nada para instalar nem manter e o backup é copiar um arquivo. Alternativa:
   MySQL ou MariaDB, que só compensa com mais de um servidor.
3. **O próprio núcleo bloqueia dano entre jogadores e guarda as zonas de paz**,
   em vez de usar regiões do WorldGuard. Escolhi isso para o duelo poder abrir
   exceção sem brigar com outro plugin e para não depender da versão dele.
   Alternativa: integrar com regiões do WorldGuard, o que aproveita a ferramenta
   de desenhar áreas que a staff já vai usar.
4. **Abrir o servidor com as etapas 1 e 2 (uma vaga por conta)** e ligar a
   segunda vaga quando a etapa 3 estiver testada. Escolhi isso porque perda de
   inventário é o defeito que mais custa confiança. Alternativa: segurar a
   abertura até a etapa 3.
5. **Comandos recebem nick da conta, não nome de personagem.** Escolhi isso
   porque nick não tem espaço, completa sozinho e não é ambíguo; a mira cobre
   o caso comum. Alternativa: aceitar nome de personagem entre aspas.
