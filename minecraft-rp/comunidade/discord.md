# Discord

Estrutura para a fase de 1 pessoa, com o que acrescentar nas fases de 5 e 15.
Regra geral: canal vazio passa impressão de servidor morto. Só crie um canal
quando houver conversa para ele.

Onde aparece `Crônicas de Serenthia`, troque pelo nome quando ele for decidido.

## 1. Configurações do servidor

Tudo aqui é recurso nativo do Discord, sem bot.

| Configuração | Valor | Por quê |
| --- | --- | --- |
| Modo Comunidade | Ativado | É pré-requisito para a tela de regras, o Onboarding e o AutoMod |
| Tela de regras na entrada (Rules Screening) | Ativada, com o texto da seção 6.2 resumido em 5 itens | A pessoa aceita as regras antes de poder falar. Dispensa bot de verificação |
| Onboarding | Uma pergunta: "Quer ser avisado de quê?" com as opções Eventos e Anúncios | Entrega os cargos de notificação sem bot |
| Nível de verificação | Exigir e-mail verificado e conta com mais de 5 minutos | Barra o grosso das contas de spam |
| Filtro de conteúdo explícito | Analisar mídia de todos os membros | Há adolescentes |
| Exigir verificação em duas etapas para moderar | Ativado | Protege contra conta de staff roubada |
| AutoMod | Filtros prontos de spam e de palavras ofensivas, mais uma regra própria (seção 5) | Modera enquanto o dono dorme |
| Canal com restrição de idade | Nenhum | O servidor não tem conteúdo adulto |
| Transmissão ao vivo e vídeo nos canais de voz | Desativados | Menos risco e menos moderação |

Os nomes dessas opções mudam de tempos em tempos na interface em português.
Confira no painel do Discord na hora de montar.

## 2. Cargos

De cima para baixo. A ordem importa: um cargo só gerencia os que estão abaixo
dele.

| Cargo | Fase | Quem recebe | Permissões além das de um membro comum |
| --- | --- | --- | --- |
| **Dono** | 1 | O dono | Administrador |
| **Bots** | 1 | Os bots | Só o que cada bot pede. Fica acima dos cargos que os bots entregam |
| **Moderação** | 5 | Moderadores | Castigar membros (timeout), expulsar, gerenciar mensagens, ver o registro de auditoria. **Sem** banir, **sem** gerenciar cargos ou canais |
| **Narração** | 5 | Narradores | Gerenciar eventos; gerenciar mensagens só na categoria MUNDO |
| **Conselheiro** | 1 | Pessoa de segunda opinião | Ler os canais `#registro-de-punicoes` e `#casos` |
| **Guia** | 1 | Jogadores que recebem novatos | Nenhuma permissão extra. Cargo com cor e exibido separado na lista |
| **Morador** | 1 | Candidatura aprovada | Ver e escrever nas categorias COMUNIDADE e MUNDO |
| **Eventos**, **Anúncios** | 1 | Quem escolher no Onboarding | Nenhuma. Servem para menção |
| **@everyone** (visitante) | 1 | Todo mundo | Ver COMECE AQUI e SUPORTE. Escrever só em `#duvidas`. **Sem** mencionar @everyone, **sem** enviar links e anexos fora dos tickets |

Acrescentar depois:

- Fase de 15: **Administração**, **Recepção**, **Mundo**, **Técnica**,
  **Ouvidoria** e **Em experiência**, conforme [equipe.md](equipe.md).
- **Apoiador** (cosmético): só se a loja existir e passar pela conferência
  descrita em [lancamento.md](lancamento.md).
- Cargos de facção: só quando houver facções de jogadores aprovadas.

## 3. Canais e permissões

Legenda: **L** lê, **E** escreve, **—** não vê.

### 3.1 Fase de 1 pessoa (15 canais)

| Categoria | Canal | Para que serve | @everyone | Morador | Guia |
| --- | --- | --- | --- | --- | --- |
| **COMECE AQUI** | `#boas-vindas` | O que é o servidor e os 3 passos para entrar | L | L | L |
| | `#regras` | Regulamento resumido e link do texto completo | L | L | L |
| | `#como-entrar` | Painel do formulário de candidatura | L | L | L |
| | `#anuncios` | Avisos do dono; changelog de regras | L | L | L |
| | `#duvidas` | Perguntas antes de entrar | L E | L E | L E |
| **COMUNIDADE** (OOC) | `#chegadas` | Data da próxima turma, endereço do servidor, novatos avisando que vão entrar | — | L E | L E |
| | `#geral` | Conversa fora do personagem | — | L E | L E |
| | `#midia` | Prints, clipes, skins, artes | — | L E | L E |
| | `#sugestoes` | Canal de fórum: um tópico por sugestão | — | L E | L E |
| **MUNDO** | `#mural` | Avisos dentro da história: boatos, cartazes, editais | — | L | L |
| | `#cronica` | Resumo semanal do que aconteceu na história | — | L | L |
| | `#procuro-rp` | Jogadores combinam cenas e horários | — | L E | L E |
| **SUPORTE** | `#abrir-ticket` | Painel de tickets | L | L | L |
| **STAFF** (privada) | `#registro-de-punicoes` | Uma mensagem por punição, no formato de equipe.md | — | — | — |
| | `#casos` | Candidaturas recebidas, notas de denúncias, transcrições de tickets | — | — | — |

Voz, um canal só: **Praça (OOC)**, visível a Morador. Sem salas privadas.
É onde acontece a conversa de voz da sessão de boas-vindas, se a turma quiser;
o RP continua por texto.

Categorias de ticket no painel:

| Categoria | Quem vê o ticket além de quem abriu |
| --- | --- |
| Suporte (problema técnico, conta, senha) | Dono; na fase de 5, Moderação |
| Denúncia | Dono; na fase de 5, Moderação |
| Apelação | Dono; nas fases seguintes, quem julga conforme equipe.md |
| Pedido especial (raça restrita, magia, título, facção, herança) | Dono; na fase de 5, Narração |
| Privacidade (ver ou apagar meus dados) | Dono |
| Denúncia contra staff | Só o Dono; na fase de 15, só a Ouvidoria |

Calendário: use os **Eventos** nativos do Discord para a sessão de
boas-vindas e o evento semanal. Eles mostram a hora no fuso de cada pessoa e
dispensam um canal de calendário.

### 3.2 O que acrescentar depois

| Quando | Canais |
| --- | --- |
| Houver wiki | `#mundo-e-lore` em MUNDO, com os links e perguntas de lore |
| Houver 5 ou mais fichas que os donos queiram mostrar | `#personagens`, canal de fórum, um tópico por personagem |
| Houver facções aprovadas | `#faccoes`, canal de fórum |
| Fase de 5 | Em STAFF: `#staff-geral`, `#planejamento-de-eventos`, `#manual-da-staff`; separar `#casos` em `#candidaturas` e `#denuncias` |
| Ponte com o jogo instalada | `#status-do-servidor`; `#chat-ooc-do-jogo` só se os jogadores pedirem (não espelhar o chat IC: vira fonte de metagaming) |
| `#geral` passar de umas 300 mensagens por dia | `#off-topic` |
| Fase de 15 | Uma subcategoria privada por equipe; canal da Ouvidoria |

## 4. Bots

Critério: o mínimo possível. Cada bot é um terceiro com acesso a mensagens e
mais uma coisa que pode quebrar.

### 4.1 Necessário desde o início

| Bot | Para quê | O que foi confirmado | O que falta confirmar |
| --- | --- | --- | --- |
| **Tickety** (https://tickety.top) | Painel de tickets e formulário de candidatura | Em trechos do site oficial e da documentação (novembro de 2026): faz tickets e candidaturas, tem painel web, as candidaturas correm por mensagem direta com o bot, converte candidatura em ticket, e o plano gratuito tem painéis de ticket ilimitados | Limite de perguntas e de formulários no plano gratuito; se gera transcrição de ticket; se tem interface em português; onde guarda os dados e por quanto tempo (isso entra em privacidade.md) |

Alternativa confirmada para o formulário: **Appy** (https://appy.bot). O site
oficial diz que criar formulários e receber respostas é gratuito, e que o
plano pago aumenta o número de formulários e perguntas. O limite exato do
plano gratuito não foi confirmado.

Plano B sem bot de formulário: o candidato abre um ticket de candidatura e
responde a uma mensagem fixada com as dez perguntas. Funciona com qualquer
bot de tickets e basta para a alpha.

As páginas dos dois bots não puderam ser abertas na íntegra nesta sessão; a
confirmação veio de resultados de busca. **Antes de instalar, abra o site,
confira o plano gratuito e anote a data.**

### 4.2 Recursos nativos que substituem bots

| Necessidade | Recurso nativo |
| --- | --- |
| Verificação de entrada | Tela de regras (Rules Screening) |
| Cargos de notificação | Onboarding |
| Filtro de palavras e spam | AutoMod |
| Calendário | Eventos |
| Suspensão temporária | Castigo (timeout) |
| Registro de ações da staff | Registro de auditoria |

### 4.3 Depois, não agora

| Bot ou plugin | Para quê | Quando |
| --- | --- | --- |
| **DiscordSRV** (https://github.com/DiscordSRV/DiscordSRV) | Plugin de Minecraft que liga o servidor ao Discord: vínculo de conta por código e, segundo a pesquisa, exigência de cargo para entrar | Quando passar de cerca de 15 aprovações por semana (ver entrada.md). `DECISOES.md` já o prevê para depois. A versão e a compatibilidade são conferidas pelo `mc-infra`. O repositório foi aberto nesta sessão e existe; a lista de recursos vem da pesquisa, não da página |

Não proponho bot de moderação, de níveis, de música nem de boas-vindas: os
recursos nativos cobrem a moderação, e o resto não ajuda um servidor de RP
pequeno.

## 5. AutoMod: regra própria

Além dos filtros prontos, uma regra de palavras-chave personalizada que
bloqueia a mensagem e avisa no `#casos`:

- convites para outros servidores de Discord;
- endereços de outros servidores de Minecraft;
- pedidos de dado pessoal: "manda foto", "quantos anos você tem", "me chama
  no privado", "qual seu insta", e variações.

Administradores ficam isentos das regras do AutoMod; os demais cargos, não.
A lista de palavras é mantida pelo dono e não é publicada.

## 6. Textos prontos para colar

Cada bloco cabe em uma mensagem do Discord (limite de 2000 caracteres).

### 6.1 `#boas-vindas`

```
# Bem-vindo(a) a Crônicas de Serenthia

Um servidor brasileiro de **roleplay de fantasia medieval** no Minecraft Java. Aqui você cria um personagem e vive a história dele junto com os outros jogadores, **por texto**. Não é survival, não é minigame e ninguém "zera": o que conta é a história.

**O que você precisa**
• Minecraft Java (conta original ou não)
• Nenhum mod. O visual do servidor vem por um pacote de recursos que o jogo baixa sozinho
• 13 anos ou mais
• Não precisa de microfone nem de experiência com RP. A gente ensina

**Como entrar, em 3 passos**
1. Leia as regras em #regras. Leva cinco minutos.
2. Preencha o formulário em #como-entrar. Leva uns oito. A resposta sai em até 48 horas.
3. Aprovado(a), você entra com a próxima **turma**: toda sexta, às 20h (horário de Brasília), a gente recebe os novatos dentro do jogo e começa uma cena com todo mundo junto.

**Por que turma?** Porque entrar num servidor de RP e não achar ninguém é a pior primeira impressão possível. Aqui você chega acompanhado(a).

**Dúvidas?** Pergunte em #duvidas.
**Problemas?** Abra um ticket em #abrir-ticket. A staff não atende por mensagem privada e **nunca** pede a sua senha.

Servidor não oficial de Minecraft. Não aprovado nem associado à Mojang ou à Microsoft.
```

### 6.2 `#regras` (duas mensagens)

Mensagem 1:

```
# Regras de Crônicas de Serenthia

Resumo. O texto completo, com exemplos, está em: [LINK DO REGULAMENTO]

**O princípio:** você joga para contar uma boa história com os outros, não para vencer. Na dúvida, dê ao outro a chance de reagir.

**Convivência**
1. Respeito sempre. Proibido assediar, discriminar, ameaçar, perseguir ou expor dados de alguém. Vale no jogo, aqui e por mensagem privada.
2. Idade mínima: 13 anos.
3. Conteúdo sexual é proibido em qualquer forma.
4. Sem cheats, sem abuso de bugs, sem divulgar outros servidores.
5. Problema com alguém? Ticket. Nunca no meio da cena nem em canal público.

**Sua conta**
6. Uma conta por pessoa.
7. A senha é sua responsabilidade: use uma que você não usa em mais nenhum lugar. A staff nunca pede senha.
8. Conta roubada ou senha perdida: ticket, pelo mesmo Discord da candidatura.

**Na história**
9. **Sem metagaming:** seu personagem só sabe o que viu ou ouviu dentro do jogo. O que você leu aqui no Discord, ele não sabe.
10. **Sem powergaming:** descreva a tentativa ("tenta acertar"), não o resultado ("acerta e mata").
11. **Amor à vida:** seu personagem tem medo de morrer e age de acordo.
12. **Sem RDM:** nada de atacar sem motivo construído na história.
13. **Sem combat logging:** não saia do jogo para fugir de uma cena.
14. Perder uma briga não precisa do seu "sim". Morte do personagem (CK), ferimento grave e marca permanente só com a sua autorização pelo comando do jogo, salvo dois casos raros decididos pela staff. Nenhum jogador decide isso por você nem pode te pressionar.
15. Tortura e violência detalhada só com o "sim" de todos na cena. Qualquer um pode sair.
```

Mensagem 2:

```
**Staff e punições**
16. A staff segue estas mesmas regras, inclusive o dono.
17. Denúncia: por ticket, com print ou clipe, em até 7 dias.
18. Escala: orientação → advertência → suspensão de 3 dias → suspensão de 7 dias → banimento. Assédio, conteúdo sexual e ameaça à segurança de alguém levam a banimento direto.
19. Toda punição é registrada e avisada com a regra que foi quebrada. Você pode apelar uma vez, por ticket.

**Neste Discord**
20. Cada assunto no seu canal. Sem spam e sem marcar a staff à toa.
21. Não peça nem publique dado pessoal de ninguém: foto, idade, cidade, redes sociais.
22. O que você lê aqui é fora do personagem. Não leve para dentro da história.

**Seus dados:** guardamos o mínimo e você pode pedir para apagar. Detalhes em [LINK DA PRIVACIDADE].

Ao continuar no servidor, você declara que leu e aceita o regulamento completo.
```

### 6.3 `#como-entrar`

```
# Como entrar

1. Clique no botão abaixo para abrir o formulário. O bot vai te fazer as perguntas por mensagem direta, então deixe as mensagens diretas deste servidor ativadas.
2. São dez perguntas e leva uns oito minutos. Não é prova de redação: queremos saber se você leu as regras e conhecer o seu personagem.
3. A resposta sai em até 48 horas, por mensagem do bot.
4. Aprovado(a), você ganha o cargo Morador e vê o canal #chegadas, com a data da sua turma.

**Antes de preencher:** leia #regras. Uma das perguntas é a palavra de leitura que está no regulamento completo.

**Privacidade:** não pedimos nome real, idade exata, cidade, foto nem documento. Não coloque isso nas respostas. Candidatura recusada é apagada em 90 dias.
```

### 6.4 `#abrir-ticket`

```
# Precisa de ajuda?

Escolha a categoria e abra um ticket. Só você e a staff veem a conversa.

• **Suporte:** não consigo entrar, perdi a senha, conta roubada, bug
• **Denúncia:** alguém quebrou uma regra. Tenha em mãos: nicks, data e hora, o que aconteceu e a prova (print ou clipe)
• **Apelação:** quero contestar uma punição que recebi
• **Pedido especial:** raça restrita, magia, título, facção, herança de itens, evento letal
• **Privacidade:** quero ver ou apagar meus dados
• **Denúncia contra staff:** lida só pelo dono

**Prazos:** confirmamos que lemos em até 24 horas. Denúncias e apelações são decididas em até 7 dias.

**Denúncia contra o dono:** [fale com o Conselheiro, @usuario, que não faz parte da administração / ainda não temos uma segunda pessoa para isso; a denúncia é lida pelo próprio dono, registrada e respondida por escrito em até 7 dias].

A staff não atende por mensagem privada. Se alguém te chamar no privado dizendo ser da staff e pedir senha ou dado pessoal, é golpe: abra um ticket.
```

### 6.5 Mensagem fixada em `#chegadas`

```
# Sua turma

**Próxima sessão de boas-vindas:** [use o Evento do Discord, que mostra a hora no seu fuso]

**No dia:**
1. Cerca de uma hora antes, o endereço do servidor aparece aqui e o seu nick é liberado.
2. Entre com o nick **exatamente** como escreveu no formulário. Maiúsculas e minúsculas contam.
3. O jogo vai pedir para criar uma senha. Use uma que você não usa em mais nenhum lugar.
4. Aceite o pacote de recursos quando o jogo perguntar.
5. Crie o seu personagem com `/personagem criar` (o jogo pergunta um campo por vez) e siga as placas até o ponto de encontro. Alguém com o cargo Guia ou o dono vai estar lá.

**Não pode nesse horário?** Escreva aqui o dia e a hora em que vai entrar e alguém te recebe.

**No sábado, 20h,** tem o evento da semana. Venha com o personagem que você acabou de criar.
```

## 7. Rotina de manutenção (fase de 1 pessoa)

| Frequência | Tarefa | Tempo |
| --- | --- | --- |
| Diária | Responder tickets e candidaturas | 15 a 30 min |
| Quinta | Fechar a turma, criar o Evento da sexta | 10 min |
| Sexta | Liberar a whitelist da turma, conduzir a sessão | 1h30 |
| Sábado | Evento semanal | 2h |
| Domingo | Publicar a crônica; atualizar a planilha de métricas | 30 min |
| Mensal | Apagar candidaturas recusadas com mais de 90 dias; publicar o resumo de punições; rever a lista do AutoMod | 30 min |

## 8. Decisões para o dono confirmar

1. **Tickety como bot único** de tickets e candidaturas. Justificativa: um
   bot em vez de dois; mas o plano gratuito precisa ser conferido no site
   antes (seção 4.1).
2. **Começar com 15 canais.** Justificativa: canal vazio espanta, e criar
   canal depois custa um minuto.
3. **Não espelhar o chat do jogo no Discord.** Justificativa: chat IC visível
   de fora é convite a metagaming.
4. **Sem cargo de Candidato.** Justificativa: quem ainda não foi aprovado é
   simplesmente visitante; um cargo a menos para gerenciar.
5. **Vídeo e transmissão desativados na voz.** Justificativa: menos risco com
   menores e menos moderação.
6. **Texto do canal de tickets sobre denúncia contra o dono:** escolher uma
   das duas versões entre colchetes, conforme haja ou não Conselheiro.
