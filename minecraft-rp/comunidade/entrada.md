# Entrada de jogadores

## 1. Modelo proposto

**Formulário curto no Discord, com entradas em turmas semanais e sessão de
boas-vindas marcada.**

Por quê: o dado mais forte da pesquisa é que o novato desiste quando entra e
não encontra ninguém para jogar no primeiro dia. Com poucos jogadores,
concentrar as chegadas resolve isso melhor do que qualquer filtro.

### 1.1 O caminho do jogador

1. Entra no Discord e aceita as regras na tela de entrada.
2. Lê `#como-entrar` e preenche o formulário (meta: 5 a 8 minutos).
3. Recebe a resposta em até 48 horas.
4. Aprovado, ganha o cargo **Morador** e acesso ao canal `#chegadas`, onde
   está a data da próxima sessão de boas-vindas.
5. No dia da sessão, a turma é liberada na whitelist cerca de uma hora antes.
   Cada um entra, cria a senha e monta a ficha do personagem no jogo.
6. Sessão de boas-vindas (45 a 60 minutos): o dono ou um Guia recebe a turma
   no ponto inicial, mostra os comandos de chat e conduz uma cena curta em
   que todos os personagens se conhecem.
7. No dia seguinte acontece o evento semanal fixo, já com a turma dentro.

### 1.2 Ritmo

| Item | Proposta |
| --- | --- |
| Sessão de boas-vindas | Toda sexta, 20h (horário de Brasília) |
| Evento semanal fixo | Todo sábado, 20h |
| Fechamento da turma | Quinta, 20h. Quem for aprovado depois entra na turma seguinte |
| Tamanho da turma | Até 10 pessoas. Passou disso, abre uma segunda sessão no domingo |
| Espera máxima | 7 dias entre a aprovação e a entrada |
| Quem não pode ir à sessão | É liberado na whitelist no mesmo horário e marca no `#chegadas` quando for entrar; um Guia ou o dono o recebe |

Os horários são suposição até a primeira enquete no Discord (ver
[lancamento.md](lancamento.md)).

### 1.3 Como a whitelist é liberada

- **Fase de 1 pessoa:** o dono adiciona os nicks da turma à mão, uma vez por
  semana. Com até 10 nicks por semana, isso custa menos do que manter uma
  integração.
- **Quando passar de cerca de 15 aprovações por semana:** automatizar com o
  vínculo entre Discord e Minecraft (DiscordSRV, previsto em `DECISOES.md`
  para depois). Depende do `mc-infra`.
- O nick aprovado **não é publicado** antes do primeiro login. Em modo
  offline, quem entrar primeiro com aquele nick cria a senha dele.

## 2. Formulário

Vai no Discord, pelo bot de candidaturas (ver [discord.md](discord.md)). O
usuário do Discord é capturado pelo bot e não precisa ser perguntado.

### 2.1 Aviso no topo (colar antes das perguntas)

> Usamos estas respostas só para avaliar a sua entrada e organizar as turmas.
> Só a equipe do servidor lê. Candidatura recusada é apagada em 90 dias. Para
> apagar seus dados a qualquer momento, abra um ticket. Não pedimos nome real,
> idade exata, cidade, foto nem documento: não coloque isso nas respostas.
> Política completa: [link para a página de privacidade].

### 2.2 Perguntas

| Nº | Pergunta | Tipo | Para que serve |
| --- | --- | --- | --- |
| 1 | Qual é o seu nick no Minecraft, exatamente como você escreve (maiúsculas e minúsculas contam)? | Texto curto | Whitelist |
| 2 | Você tem 13 anos ou mais? | Sim / Não | Idade mínima. Não pedimos a idade exata |
| 3 | Como você conheceu o servidor? | Lista: amigo, lista de servidores, Discord, vídeo, outro | Medir divulgação |
| 4 | Você já jogou RP? Onde? (Minecraft, GTA RP, RPG de mesa, por texto, nunca) | Texto curto | Não elimina. Serve para saber quem precisa de mais ajuda na chegada |
| 5 | Um amigo te conta no Discord que tem um baú escondido atrás da taverna. Seu personagem pode ir lá pegar? Por quê? | Texto curto | Metagaming |
| 6 | Três bandidos armados cercam o seu personagem e pedem a bolsa. O que ele faz? | Texto curto | Amor à vida e powergaming |
| 7 | Qual é a palavra de leitura que está no regulamento? | Texto curto | Prova de que leu |
| 8 | Apresente seu personagem em 3 a 5 linhas: nome, de onde vem e o que veio buscar. | Texto longo | Coerência com o mundo e ganchos para a sessão de boas-vindas |
| 9 | Em quais dias e horários você costuma jogar? | Texto curto | Escolha do horário dos eventos |
| 10 | Li e aceito o regulamento e o aviso de privacidade. | Sim | Aceite |

Notas:

- A ficha completa do personagem (aparência, personalidade, história) é feita
  **dentro do jogo**, não no formulário. Menos barreira na entrada.
- Não há amostra de RP nem entrevista por voz. Entrevista por voz não escala
  com uma pessoa e expõe menores a conversa a sós.
- Se o bot limitar o número de perguntas, as primeiras a sair são a 3 e a 9
  (viram enquete no Discord).

## 3. Critérios de aprovação

O formulário mede **compreensão das regras e disposição**, não qualidade de
escrita. Erro de português não reprova. A regra é aprovar.

### 3.1 Aprovado

- Respondeu "sim" à pergunta 2 e à 10.
- Palavra de leitura correta.
- Perguntas 5 e 6 mostram que entendeu a ideia, mesmo sem usar os termos.
  Exemplos aceitos: "não pode, o personagem não sabe disso" e "entrega a
  bolsa" (ou tenta negociar, ou foge se tiver por onde).
- Personagem cabe em fantasia medieval e começa sem poderes, títulos ou
  riquezas.

### 3.2 Ajuste e reenvie

Usado sempre que o problema tem conserto. Sem limite de tentativas.

- Palavra de leitura errada ou em branco.
- Pergunta 5 ou 6 mostra que não entendeu (por exemplo: "mato os três").
- Personagem fora do tema ou já poderoso (rei, semideus, personagem copiado
  de filme ou anime).
- Nick ofensivo ou parecido com o de outro jogador ou da staff.

### 3.3 Recusado

- Respondeu "não" à pergunta 2, ou há sinal claro de que tem menos de 13.
- Conteúdo ofensivo, sexual ou de ódio nas respostas.
- Pessoa banida tentando voltar por outra conta.
- Respostas de deboche em todo o formulário.

Recusa por idade ou por ofensa não tem "ajuste e reenvie". As demais recusas
podem tentar de novo em 30 dias.

### 3.4 Registro

Cada decisão fica anotada com data, resultado e motivo em uma linha, no canal
privado de candidaturas. Serve para medir o funil e para responder a quem
perguntar por que foi recusado.

## 4. Respostas-modelo

Troque o que está entre colchetes.

### 4.1 Aceite

> **Bem-vindo(a), [nick]!** Sua candidatura foi aprovada.
>
> **Próximo passo:** sua turma entra na **[sexta, dia/mês, às 20h, horário de
> Brasília]**. Cerca de uma hora antes, o seu nick é liberado e o endereço do
> servidor aparece no canal #chegadas.
>
> **No primeiro login:** o jogo vai pedir para você criar uma senha. Use uma
> senha que você não usa em nenhum outro lugar. A staff nunca vai pedir essa
> senha.
>
> **Na sessão de boas-vindas** (uns 45 minutos) a gente te recebe no jogo,
> mostra os comandos de chat e já começa uma cena com a sua turma. Não precisa
> de microfone: o RP é por texto.
>
> **Não pode nesse horário?** Sem problema. Avise no #chegadas quando for
> entrar e alguém te recebe.
>
> Gostei de [um detalhe do personagem]. Até sexta!

### 4.2 Ajuste e reenvie

> Oi, [nick]! Obrigado por se candidatar. Falta pouco: preciso que você ajuste
> **[um ou dois pontos]** e envie de novo.
>
> - [Exemplo: a palavra de leitura não confere. Ela está no meio do
>   regulamento, que leva uns cinco minutos para ler.]
> - [Exemplo: na pergunta dos bandidos, lembre que o personagem tem medo de
>   morrer. Dê uma olhada em "amor à vida" no regulamento.]
> - [Exemplo: todo personagem começa sem poderes nem títulos. O seu pode
>   *querer* ser cavaleiro; ainda não é.]
>
> Pode reenviar quando quiser, não há limite de tentativas. Qualquer dúvida,
> pergunte no #duvidas.

### 4.3 Recusa (com nova tentativa)

> Oi, [nick]. Obrigado pelo interesse, mas desta vez a candidatura não foi
> aprovada.
>
> **Motivo:** [uma frase objetiva, citando a parte do formulário ou a regra].
>
> Você pode se candidatar de novo a partir de **[data, 30 dias depois]**. Seus
> dados desta candidatura são apagados em 90 dias, ou antes, se você pedir por
> ticket.

### 4.4 Recusa por idade

> Oi! O servidor só aceita jogadores com 13 anos ou mais, que é também a idade
> mínima do Discord. Por isso não posso aprovar a candidatura agora, e os
> dados dela já foram apagados.
>
> Não é nada contra você: quando fizer 13, é só se candidatar de novo. Até lá!

### 4.5 Recusa definitiva

> Sua candidatura foi recusada por **[conteúdo ofensivo nas respostas /
> tentativa de voltar durante um banimento]**. Essa decisão não permite nova
> candidatura. Se você acha que houve engano, pode apelar uma vez por ticket.

## 5. Alternativa: servidor aberto com tutorial

Sem formulário. Qualquer pessoa entra, cai em uma área de visitante, faz um
tutorial curto dentro do jogo (regras essenciais e um mini teste) e, ao
terminar, recebe a permissão de morador automaticamente.

| | Formulário com turmas (proposto) | Aberto com tutorial |
| --- | --- | --- |
| Barreira para entrar | Média: formulário e espera de até 7 dias | Baixa: joga na hora |
| Primeiro dia do novato | Acompanhado, com a turma | Sozinho, a não ser que haja staff ou Guia online |
| Trabalho do dono | Ler formulários (2 a 3 minutos cada) e uma sessão por semana | Construir e manter o tutorial; moderar mais; estar online em mais horários |
| Qualidade do RP | Mais uniforme | Desigual |
| Aproveita tráfego de listas e vídeos | Mal: quem chega pelo endereço do servidor encontra a porta fechada | Bem |
| Risco em modo offline | Menor: só entra nick aprovado | Maior: sem barreira de conta, ataques de bots ficam mais fáceis |
| Depende de | Um bot de candidatura | Área de visitante, permissões e tutorial (`mc-sistemas`, `mc-infra`), proteção contra bots |

### 5.1 O que faria trocar de modelo

Trocar para o modelo aberto (ou para o híbrido: entra como visitante, vira
morador depois do tutorial) quando **a condição 1 e pelo menos uma das
outras** forem verdadeiras:

1. **Há quem receba novatos fora da sexta:** pelo menos 5 pessoas entre staff
   e Guias, com alguém online no horário de pico em 5 dos 7 dias.
2. **O formulário não filtra:** mais de 90% de aprovação de primeira por 4
   semanas seguidas.
3. **A fila estoura:** mais de 20% das candidaturas respondidas depois de 48
   horas, por 2 semanas seguidas.
4. **A espera espanta:** menos de 60% dos aprovados fazem o primeiro login em
   até 14 dias, por 4 semanas seguidas.
5. **O formulário espanta:** menos de 30% de quem entra no Discord envia a
   candidatura, por 4 semanas seguidas, depois de já ter encurtado o
   formulário uma vez.

Voltar do aberto para o formulário se as denúncias por 100 jogadores ativos
dobrarem em relação à média do período com formulário, por 2 semanas seguidas.

## 6. Decisões para o dono confirmar

1. **Modelo de entrada:** formulário curto com turmas semanais. Justificativa:
   ataca a principal causa de desistência (primeiro dia sozinho) com o menor
   trabalho para uma pessoa.
2. **Whitelist liberada só no dia da sessão**, com espera máxima de 7 dias.
   Justificativa: garante que o primeiro login aconteça com gente online; o
   custo é a espera, que é medida (condição 4 acima).
3. **Sessão na sexta às 20h e evento no sábado às 20h.** Justificativa: o
   novato entra e já tem o que fazer no dia seguinte; horário a validar por
   enquete.
4. **Não perguntar faixa etária, só "13 ou mais".** Justificativa: menos dado
   pessoal guardado, e as regras de proteção já tratam todos como possíveis
   menores.
5. **Sem entrevista por voz e sem amostra de RP.** Justificativa: barreira
   alta esvazia servidor pequeno, e voz a sós com menor é risco.
6. **Whitelist manual até cerca de 15 aprovações por semana.** Justificativa:
   menos manutenção do que integrar o Discord ao servidor desde o primeiro
   dia.
7. **Palavra de leitura "lamparina".** Trocar a cada temporada ou quando
   vazar.
