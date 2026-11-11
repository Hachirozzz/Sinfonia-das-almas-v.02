# Roteiro de lançamento

Da alpha fechada à operação normal. Cada fase tem um **critério de saída
objetivo**: só se passa adiante quando todos os itens estiverem cumpridos, e
não há data fixa. Quando uma previsão mudar, avise no Discord com a nova data;
"em breve" sem prazo é uma das queixas mais comuns em servidores que morreram.

As metas numéricas são propostas minhas. A pesquisa não encontrou referência
confiável de retenção para servidores de Minecraft RP; ajuste depois das
primeiras turmas.

## Visão geral

| Fase | Quem joga | Duração esperada | Objetivo |
| --- | --- | --- | --- |
| 0. Fundação | Ninguém | Até ficar pronto | Ter o mínimo para receber gente |
| 1. Alpha fechada | 5 a 10 convidados | 2 a 4 semanas | Testar regras e sistemas |
| 2. Beta por turmas | 20 a 40, em turmas de até 10 | 4 a 8 semanas | Testar a recepção e o ritmo semanal |
| 3. Lançamento público | Aberto a candidaturas, com divulgação | 4 semanas | Crescer sem quebrar |
| 4. Operação | Todos | Contínua | Manter gente e não esgotar o dono |

## Fase 0: Fundação

Sem jogadores. O que é do `mc-comunidade` já está nesta pasta; o restante
depende dos outros agentes.

**Critério de saída (todos):**

- [ ] Servidor sobe na máquina do dono com o kit inicial, login e chat por
      distância funcionando (`mc-infra`).
- [ ] Dá para criar personagem, falar, fazer emote e resolver um conflito por
      dados do começo ao fim (`mc-sistemas`).
- [ ] Existe um ponto de chegada construído e uma página de "como é o mundo"
      que se lê em cinco minutos (`mc-lore`).
- [ ] Discord montado conforme [discord.md](discord.md), com o bot de tickets
      conferido e instalado.
- [ ] Regulamento, regras detalhadas e privacidade publicados em um lugar com
      link (ver "Onde publicar os textos").
- [ ] Nome do servidor decidido e trocado nos textos.
- [ ] Backup automático configurado e **uma restauração testada**
      (`mc-infra`).
- [ ] Conselheiro escolhido, ou o texto do canal de tickets ajustado para
      dizer que não há (ver [equipe.md](equipe.md)).
- [ ] As "Decisões para o dono confirmar" dos arquivos desta pasta
      respondidas.

### Onde publicar os textos

Proposta: publicar os arquivos desta pasta em uma wiki gratuita (a pesquisa
aponta o GitBook como o começo mais simples para uma pessoa) e pôr o resumo
no Discord. O plano gratuito do GitBook e seus limites não foram conferidos
na fonte: confira antes de adotar. Plano B sem custo nem conta nova: os
próprios arquivos no repositório, se ele for público.

## Fase 1: Alpha fechada

**Quem:** 5 a 10 pessoas convidadas pelo dono, de preferência com alguma
experiência de RP e paciência para bug. Entram sem formulário de aprovação,
mas **preenchem o formulário mesmo assim**, para testá-lo.

**Onde roda:** pode ser na máquina do dono, em horários marcados. A
hospedagem só é contratada com o servidor local funcionando (`DECISOES.md`).

**Avise antes:** tudo pode ser apagado ao fim da alpha, inclusive
personagens.

**O que fazer:**

- 3 sessões marcadas de 2 horas, com uma cena conduzida pelo dono em cada.
- Provocar de propósito os casos difíceis: conflito sem acordo, pedido de
  consentimento recusado, detenção, alguém que sai no meio, conta com senha
  esquecida.
- Anotar cada dúvida de regra em uma lista ("caso, o que a regra diz, o que
  faltou").
- Pedir a cada convidado, ao fim: o que foi confuso, o que foi chato, o que
  faria voltar.

**Critério de saída (todos):**

- [ ] 3 sessões realizadas com pelo menos 4 jogadores ao mesmo tempo em cada.
- [ ] Pelo menos 5 pessoas preencheram o formulário, com tempo mediano de até
      10 minutos (pergunte a cada uma quanto levou).
- [ ] Cada tipo de ticket foi aberto e fechado pelo menos uma vez, incluindo
      um pedido de exclusão de dados feito de ponta a ponta.
- [ ] Um caso de "perdi a senha" resolvido pelo procedimento escrito.
- [ ] Todos os casos da lista de dúvidas têm resposta: virou regra, virou
      ajuste de mecânica ou foi descartado por escrito.
- [ ] Nenhum defeito conhecido que derrube o servidor, perca item ou perca
      personagem.
- [ ] Regulamento versão 2 publicado, com o que mudou.
- [ ] Hospedagem contratada e o servidor rodando nela por 7 dias sem queda
      (`mc-infra`).

## Fase 2: Beta por turmas

**Quem:** candidaturas abertas só no Discord, sem divulgação em listas. O
convite circula por indicação dos jogadores da alpha e por uma ou duas
comunidades de RPG onde o dono já participa.

**Ritmo:** o de [entrada.md](entrada.md): turma na sexta, evento no sábado,
crônica no domingo.

**O que fazer:**

- Primeira semana: enquete de horários no Discord. Ajustar sexta e sábado ao
  resultado.
- Um arco de história curto, de 4 semanas, com começo, clímax e consequência
  registrada na linha do tempo (com o `mc-lore`).
- Convidar 2 ou 3 jogadores acolhedores para Guias.
- Preencher a planilha de métricas todo domingo (seção "O que medir").
- Perguntar a quem sumiu por 14 dias, uma vez, sem insistir: "o que faltou?"

**Critério de saída (todos):**

- [ ] Pelo menos 3 turmas recebidas.
- [ ] Nas 3 últimas turmas, pelo menos **50% de quem fez o primeiro login
      voltou a entrar entre o 2º e o 8º dia**.
- [ ] Pelo menos 90% das candidaturas respondidas em até 48 horas, nas
      últimas 4 semanas.
- [ ] Evento de sábado com pelo menos **10 jogadores ao mesmo tempo** por 3
      semanas seguidas.
- [ ] Pelo menos 2 Guias ativos (presentes em 2 das 3 últimas sessões de
      boas-vindas).
- [ ] O dono gastou no máximo **15 horas por semana** com o servidor nas
      últimas 3 semanas. Se passou disso, recrute antes de abrir (equipe.md,
      seção 3).
- [ ] No máximo 1 queda do servidor durante evento nas últimas 4 semanas.
- [ ] Decisão tomada sobre apagar ou não o mundo e os personagens antes do
      lançamento, e avisada com 14 dias de antecedência.
- [ ] Revisão jurídica da página de privacidade feita, ou a decisão
      consciente do dono de lançar sem ela, anotada em `DECISOES.md`.

**Sinais para parar e consertar em vez de avançar:**

- Menos de 30% de retorno em duas turmas seguidas: o problema está na
  primeira semana do jogador. Converse com quem saiu antes de trazer mais
  gente.
- Mais de uma denúncia de assédio no mês: reveja as regras e a moderação
  antes de crescer.

## Fase 3: Lançamento público

**Antes do dia:**

- Material: 3 a 5 capturas de tela e 1 ou 2 vídeos curtos de **cenas reais**
  do beta, com autorização de quem aparece (nick e personagem) e sem mostrar
  chat fora do personagem.
- Texto de anúncio com: o que é, para quem é, como entrar, link do Discord,
  versão do jogo, e que aceita conta original e não original. **Divulgue o
  Discord, não o endereço do servidor**: quem chega direto encontra a
  whitelist fechada.
- Evento de abertura marcado para um sábado, com o começo de um arco novo.
- Turmas: abrir uma segunda sessão de boas-vindas no domingo durante as 4
  semanas do lançamento.

**Onde divulgar**, do menor esforço para o maior:

| Canal | Como | Observação |
| --- | --- | --- |
| Jogadores atuais | Pedir que cada um convide uma pessoa | A fonte de novatos que mais fica, porque já chega com alguém para jogar |
| Disboard (disboard.org) | Cadastrar o Discord com etiquetas como "minecraft-brasil", "roleplay" e "whitelist" | A pesquisa confirmou que essas etiquetas existem em português |
| Descoberta de servidores do próprio Discord | Ativar quando o servidor cumprir os requisitos do Discord | Requisitos não conferidos; ver no painel |
| Listas de servidores de Minecraft | minecraft-mp.com, minecraft-server-list.com, minecraftservers.org, craftdex.net, reishosting.com.br (etiqueta "Roleplay") | Sites listados na pesquisa. Confira as regras de cada um; alguns podem não aceitar servidor em modo offline ou com whitelist. Na descrição, deixe claro que a entrada é pelo Discord |
| Comunidades brasileiras de RPG e de Minecraft | Um post de apresentação, onde as regras do lugar permitirem | Nunca spam, nunca em servidor de RP alheio |
| Vídeos curtos (TikTok, Shorts, Reels) | Cenas de RP com legenda e convite para o Discord | Hipótese a testar: a pesquisa não achou evidência de resultado para servidores brasileiros de RP. Faça 3 e meça |
| Criadores pequenos de Minecraft RP | Convite para jogar, com personagem na história | Sem pagamento e sem vantagem no jogo |

Votação em listas com recompensa dentro do jogo: não usar. Recompensa por
voto é vantagem de jogo por atividade fora dele, e dá manutenção.

**Critério de saída (todos, ao fim de 4 semanas):**

- [ ] Retorno entre o 2º e o 8º dia de pelo menos 40% dos novos (a meta cai
      em relação ao beta porque o público vem de mais longe).
- [ ] Pelo menos 90% das candidaturas respondidas em 48 horas e dos tickets
      em 24 horas.
- [ ] Pico semanal de pelo menos 20 jogadores ao mesmo tempo (o piso da meta
      em `DECISOES.md`).
- [ ] Dono dentro do limite de horas que definiu, ou equipe da fase de 5
      pessoas formada.

Se o pico não chegar a 20, o servidor não fracassou: continua na cadência do
beta, e o lançamento se repete na abertura da temporada seguinte.

## Fase 4: Operação

**Rotina semanal (fase de 1 pessoa):** a de discord.md, seção 7. Um evento
fixo, uma turma, uma crônica, um gancho pequeno no mural.

**Calendário de história:**

| Ciclo | Duração | O que acontece |
| --- | --- | --- |
| Semana | 7 dias | Turma (sexta), evento (sábado), crônica (domingo), um boato ou cartaz no mural no meio da semana |
| Arco | 4 a 8 semanas | Uma história com começo, clímax e consequência anotada na linha do tempo |
| Temporada | 3 a 6 meses | Fecha com um salto de tempo ou virada no mundo. É o ponto de reentrada para quem saiu: campanha de retorno e nova rodada de divulgação |

**Mensal:** resumo de punições, limpeza de dados vencidos, revisão das
métricas, e changelog de regras se algo mudou.

**Contra o esgotamento do dono:**

- Uma semana de pausa de eventos por arco, avisada antes. O servidor fica
  aberto; só não há evento conduzido.
- Jogadores que queiram conduzir cenas propõem por ticket; o dono confere a
  coerência com a lore e o evento entra no calendário. O jogador-narrador não
  ganha poder de staff: os comandos de narração são do cargo Narrador
  (equipe.md).
- Se for parar por mais de 2 semanas, diga antes, com data de volta. Dono
  que some sem avisar é causa conhecida de morte de servidor.

**Gatilhos:**

| Sinal | Ação |
| --- | --- |
| Dono acima do limite de horas por 3 semanas | Recrutar (equipe.md, seção 3) |
| Condições da seção 5.1 de entrada.md | Trocar o modelo de entrada |
| Pico regular acima de 40 | Passar para a estrutura de 15 pessoas |
| Retorno abaixo de 30% por 4 semanas | Pausar a divulgação e investigar a primeira semana do jogador |

## O que medir

Uma planilha, atualizada aos domingos, em uns 15 minutos. Sem ferramenta
nova.

### Funil de entrada (por turma)

| Medida | De onde vem |
| --- | --- |
| Entraram no Discord na semana | Estatísticas do Discord |
| Candidaturas enviadas | Bot de candidaturas |
| Aprovadas / ajuste / recusadas | Canal `#casos` |
| Fizeram o primeiro login | Registro do servidor |
| Foram à sessão de boas-vindas | Contagem na hora |
| Voltaram entre o 2º e o 8º dia | Registro do servidor |
| Ainda ativos 30 dias depois | Registro do servidor |
| Como conheceram o servidor | Pergunta 3 do formulário |

### Atendimento

| Medida | Meta |
| --- | --- |
| Tempo de resposta de candidatura | 90% em até 48 horas |
| Tempo até a primeira resposta em ticket | 90% em até 24 horas |
| Tempo até a decisão de denúncia e apelação | Até 7 dias |

### Atividade

| Medida | Para quê |
| --- | --- |
| Pico de jogadores ao mesmo tempo na semana, e em que dia e hora | Escolher horário de evento |
| Jogadores diferentes que entraram na semana | Tamanho real da comunidade |
| Presença no evento de sábado | Saúde do evento |

### Saúde

| Medida | Sinal de alerta |
| --- | --- |
| Denúncias no mês por 100 jogadores ativos | O dobro da média anterior |
| Punições por degrau e apelações aceitas | Muitas apelações aceitas indicam regra mal escrita ou mal aplicada |
| Horas do dono na semana | Acima do limite por 3 semanas |
| Respostas de quem saiu ("o que faltou?") | Mesmo motivo citado 3 vezes |

Como extrair do servidor o primeiro login, o último login e o pico por hora
sem trabalho manual é pendência para o `mc-infra`. Não proponho plugin de
estatística aqui porque não conferi nenhum.

## Loja e apoio financeiro

> **PRECISA DE CONFERÊNCIA ANTES DE ABRIR.** Nesta sessão a página oficial
> https://www.minecraft.net/en-us/usage-guidelines foi recusada pela rede
> (domínio fora da lista liberada), como já tinha acontecido na pesquisa.
> Nada abaixo foi conferido no texto oficial. Não abra loja, não aceite
> doação com recompensa e não anuncie benefício pago enquanto o dono, ou um
> agente com acesso, não ler a seção "Servers and hosting" dessa página e
> anotar aqui a data da leitura.

**Proposta: nenhuma venda e nenhuma recompensa por apoio até o fim da
fase 3.** Justificativa: um servidor sem jogadores não tem o que vender, e a
loja traz obrigações (regras da Mojang, consumidor, compra feita por menor,
dados de pagamento) que uma pessoa sozinha não precisa carregar no começo.

Três pontos que a conferência precisa responder antes de qualquer venda:

1. **Contas não originais.** Trechos de busca citados na pesquisa indicam
   que as diretrizes restringem dar acesso a quem não tem cópia paga do jogo.
   O servidor aceita contas não originais por decisão do dono. É preciso ler
   o texto oficial para saber o que isso significa para um servidor que
   **vende** alguma coisa. Esta é a dúvida mais importante da lista, e pode
   significar que a loja não é viável com a decisão atual de contas.
2. **O que pode ser vendido.** Segundo fontes secundárias: cosméticos podem,
   desde que não sejam capas nem imitem capas; nada que dê vantagem de jogo;
   quem paga tem de ter acesso ao histórico de compras. Conferir cada item.
3. **Aviso obrigatório.** A frase de que o servidor não é oficial nem
   associado à Mojang ou à Microsoft já está no regulamento e no Discord;
   conferir se a redação exigida é essa.

O que já está decidido, independentemente da conferência (`DECISOES.md` e os
arquivos de sistemas):

- Só cosmético e conveniência; nunca poder dentro do RP.
- Não se vende: item, moeda do jogo, atributo, raça, magia, vaga de
  personagem, aprovação de pedido, prioridade em ticket, cargo de staff.
- Nenhuma marca de apoiador aparece no chat dentro do personagem.
- **Nada de caixa-surpresa paga.** A pesquisa indica que o ECA Digital
  (Lei 15.211/2025) proíbe esse tipo de mecânica em jogos acessíveis a
  menores.
- Plataforma de pagamento, preços e taxas: nada escolhido. Os números da
  pesquisa vieram de fontes indiretas.

## Decisões para o dono confirmar

1. **Avançar de fase por critério, sem data fixa.** Justificativa: uma
   pessoa sozinha não controla o prazo, mas controla a lista.
2. **Limite de 15 horas semanais do dono como critério de saída do beta.**
   Justificativa: esgotamento do dono é causa conhecida de morte de servidor;
   o número é seu, troque pelo real.
3. **Meta de retorno de 50% no beta e 40% no lançamento.** Justificativa:
   metas internas, acima do cerca de 25% relatado por um servidor grande
   estrangeiro; sem referência brasileira.
4. **Apagar ou não o mundo e os personagens entre o beta e o lançamento.**
   Minha escolha: apagar ao fim da alpha, **manter** ao fim do beta.
   Justificativa: quem jogou o beta são os primeiros moradores e os futuros
   Guias; apagar o que construíram custa justamente essas pessoas.
5. **Divulgar o Discord, nunca o endereço do servidor.** Justificativa: com
   whitelist, o endereço sozinho leva a uma porta fechada.
6. **Sem recompensa por voto em listas.** Justificativa: é vantagem de jogo
   por atividade externa e mais um plugin para manter.
7. **Sem loja até o fim da fase 3, e só depois da conferência das regras da
   Mojang.** Justificativa: ver a seção acima; a dúvida sobre contas não
   originais precisa de resposta antes.
8. **Publicar os textos em uma wiki gratuita.** Justificativa: link fácil de
   colar no Discord; depende de conferir o plano gratuito.
9. **Temporadas de 3 a 6 meses.** Justificativa: dão ponto de reentrada a
   quem saiu e um motivo natural para divulgar de novo.
