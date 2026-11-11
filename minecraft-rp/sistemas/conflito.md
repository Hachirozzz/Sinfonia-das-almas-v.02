# Conflito entre personagens

Versão 0.1 (proposta inicial). Valores marcados com **[cfg]** são números de
balanceamento e ficam no arquivo de configuração.

Este arquivo é a mecânica. A conduta (motivo para atacar, amor à vida, conteúdo
sensível, memória depois da derrota) está em `comunidade/regulamento.md` e
`comunidade/regras-detalhadas.md`. A **derrota** daqui é o que aqueles textos
chamam de PK; a **morte do personagem** é o CK.

## Resumo em cinco linhas

1. Todo conflito começa com um **emote**: escreva o que o personagem faz.
2. Enquanto todos concordam, resolve-se por emote.
3. Qualquer envolvido pode **pedir os dados**. Pediu, vale dado.
4. **PvP do Minecraft** só por duelo aceito pelos dois.
5. Perder tira o personagem da cena e deixa um ferimento leve. **Ferimento
   grave, marca permanente e morte só com consentimento do dono do personagem**,
   ou nos dois casos de exceção escritos nas regras da comunidade.

## Os três modos

| Modo | Quando vale | Como funciona |
| --- | --- | --- |
| **Emote** | Enquanto ninguém pedir outra coisa | Cada um escreve a tentativa do seu personagem; quem recebe o golpe decide se pegou |
| **Dados** | Quando qualquer envolvido pedir | Trocas de golpe com `/atacar`; quem zera o Fôlego é derrotado |
| **Duelo (PvP)** | Só se os dois aceitarem | `/duelo`; luta com as mecânicas do Minecraft até um chegar a 2 corações |

Fora de um duelo aceito, jogador não causa dano a jogador. O jogo bloqueia.

### Quem escolhe e o que acontece quando discordam

- Emote e duelo exigem acordo de todos. Dados não: é o modo que vale quando
  não há acordo.
- Discordância sobre um golpe no emote ("pegou" contra "não pegou"): resolve-se
  aquele golpe com `/atacar` e a cena segue.
- Discordância sobre regra: uma mensagem curta no canal fora do personagem
  local. Se não resolver, vale o que o dado mostrou, a cena termina da forma
  menos grave para os dois lados, e quem se sentiu prejudicado abre um chamado
  depois. A staff consulta `/rolagens` e só
  desfaz resultado quando houve quebra de regra.
- Ninguém é obrigado a aceitar duelo, e recusar não tem custo.

## Combate por emote

Escreva a **tentativa**, nunca o resultado no outro. "Aldara tenta acertar o
ombro dele com o cabo da faca" está certo. "Aldara acerta e quebra o ombro
dele" está errado. Quem é alvo responde dizendo o que aconteceu com o próprio
personagem. Espera-se que cada um aceite perder de vez em quando; quem nunca
aceita vai receber pedido de dados.

## Combate por dados

Cada personagem tem **3 de Fôlego** [cfg]. Fôlego não é vida: é o quanto ele
aguenta antes de ser dominado.

### Uma troca de golpes

1. O atacante escreve o emote e digita `/atacar [nick] [braco|passo]`. Sem
   nick, vale quem ele está olhando. O alvo precisa estar a até 6 blocos [cfg].
   Sem atributo, usa Braço.
2. O alvo vê quatro botões e tem 60 segundos [cfg]:

```
[Conflito] Bruno Calado ataca você usando Braço.
[Aparar (Braço)] [Esquivar (Passo)] [Fugir] [Render-se]
```

3. O jogo rola a disputa (ver `testes.md`) e mostra para todos em 16 blocos:

```
[Golpe] Bruno Calado (Braço) 8 +3 = 11  x  Aldara Venn (Passo) 6 +1 = 7.
        Aldara Venn perde 1 de Fôlego (2 de 3)
```

- **Quem perde a disputa perde 1 de Fôlego**, seja atacante ou alvo. Se o alvo
  vence, entende-se que ele revidou. Empate: ninguém perde.
- Quem venceu escreve o emote do que aconteceu, sem exagerar o efeito.
- Sem resposta em 60 segundos, o jogo defende sozinho com o melhor atributo do
  alvo entre Braço e Passo. Ignorar o pedido não livra ninguém.
- Um mesmo atacante espera 20 segundos [cfg] entre ataques. Não há ordem de
  turnos: como toda troca custa Fôlego a alguém, a ordem não importa.

### Fugir e render-se

- **Fugir**: disputa de Passo contra Passo. Se o alvo vence, sai do conflito, e
  aquele atacante não pode atacá-lo de novo por 10 minutos [cfg]. Se perde,
  perde 1 de Fôlego.
- **Render-se** (`/render`): o personagem é derrotado na hora, **sem
  ferimento**.

### Fim da luta

Quem chega a 0 de Fôlego é **derrotado**. O Fôlego volta ao máximo 10 minutos
[cfg] depois da última troca.

`/estado` mostra o próprio Fôlego e os ferimentos.

### Proteções

- Não se pode atacar quem entrou no servidor ou trocou de personagem há menos
  de 60 segundos [cfg], quem está na antessala, nem quem já está derrotado.
- **Novato**: conta com menos de 7 dias [cfg] de servidor não pode ser alvo de
  `/atacar`, a menos que tenha atacado aquele jogador nos últimos 30 minutos
  [cfg]. Passar o mouse sobre o nome mostra a marca de novato.
- **Zona de paz**: a staff pode marcar áreas onde `/atacar` e `/duelo` não
  funcionam (por exemplo, a cidade de chegada). Emote, teste e disputa
  continuam valendo lá dentro.
- **Sair no meio**: quem desconecta com ataque pendente, com Fôlego perdido ou
  em duelo volta como derrotado, com ferimento leve, e o caso fica registrado.
  Se foi queda de conexão, a regra de conduta manda voltar e avisar; a staff
  remove o ferimento.
- Vários contra um: cada atacante faz a sua troca. Estar em menor número já
  custa caro; não há bônus extra.

## Duelo (PvP do Minecraft)

```
Digita (Bruno):      /duelo joana_m
Aparece para Aldara: [Duelo] Bruno Calado propõe um duelo. [Aceitar] [Recusar]
Aldara clica:        [Aceitar]
Aparece para os dois e para quem está em 16 blocos:
                     [Duelo] Bruno Calado e Aldara Venn vão duelar. 5... 4... 3... 2... 1... Lutem!
```

- O dano entre os dois é liberado após a contagem de 5 segundos [cfg].
- Termina quando um chega a 2 corações [cfg]: o jogo corta o dano, encerra o
  duelo e declara o derrotado. Ninguém morre nem perde itens.
- Sair do servidor durante o duelo conta como derrota. Afastar-se mais de 32
  blocos [cfg] conta como fuga: o duelo acaba sem vencedor. Passados 5 minutos
  [cfg], acaba em empate.
- Só um contra um nesta fase. Briga em grupo usa dados.

No duelo, equipamento e habilidade do jogador contam mais que a ficha. Por isso
ele nunca é imposto.

## Derrota

Vale igual para os três modos. O personagem derrotado:

- recebe **ferimento leve** (exceto se rendeu-se);
- fica **fora de combate** por 30 minutos [cfg]: não ataca, não duela, não pode
  ser atacado de novo. Continua onde caiu e com os seus itens;
- sofre **um** desfecho escolhido por quem venceu, narrado por emote:

| Desfecho | Limite |
| --- | --- |
| Deixar ir | Nenhum |
| Expulsar do local | O derrotado sai da construção ou da área da cena |
| Desarmar | Só na narração; nenhum item muda de dono |
| Deter | Até 30 minutos [cfg] de tempo real, com os dois online. Depois disso o detido é solto ou a cena passa para um narrador |

Tomar item ou moeda do derrotado, nesta fase, só com o acordo do dono, dito no
canal fora do personagem. O derrotado não pode iniciar novo conflito contra
quem o venceu por 24 horas [cfg] de tempo real (regra de conduta; o jogo só
registra).

## Ferimentos

| Grau | Efeito | Duração [cfg] | Como se recebe |
| --- | --- | --- | --- |
| Leve | -1 em testes de Braço e Passo | 2 dias reais | Derrota |
| Grave | -2 em todos os testes | 7 dias reais | Só com consentimento |
| Marca permanente (cicatriz, perda de membro, sequela) | Só narrativo; o dono descreve na aparência | Para sempre | Só com consentimento |

- Ferimentos não acumulam: vale o pior.
- **Tratar**: `/tratar [nick]` é um teste de Tino, Normal [cfg], de quem trata
  (soma o bônus se o ofício for de cura). Sucesso reduz um grau: grave vira
  leve, leve some. Cada paciente pode ser tratado uma vez por dia real [cfg].

```
Digita (Aldara):  /tratar bruno_k oficio
Aparece:          [Teste] Aldara Venn trata Bruno Calado, Tino (Normal 7): 5 +3 +1 ofício = 9. Sucesso
                  [Estado] Bruno Calado não está mais ferido.
```

Ferimento existe para dar trabalho a quem cura e peso a quem perde, sem tirar
ninguém do jogo.

## Morte no Minecraft não é morte do personagem

Cair, afogar ou morrer para um monstro é um **desmaio**: o personagem volta ao
ponto de retorno, sem ferimento e sem nenhuma consequência de ficha. O que
acontece com os itens nessa hora é configuração do servidor (`mc-plugins`).

## Morte do personagem e consentimento

**Nenhum resultado de dado, duelo ou comando de outro jogador mata, fere
gravemente ou marca para sempre um personagem sem o dono digitar o
consentimento.**

As regras da comunidade preveem só dois casos sem o comando do dono, e os dois
passam pela staff, nunca por outro jogador:

1. **Cláusula aceita antes**, por escrito: o jogador pediu um poder, um cargo
   de risco ou entrou em evento anunciado como letal.
2. **Decisão da staff por chamado**, depois de ouvir o jogador-alvo.

Nesses casos a staff aplica com `/nucleorp desfecho <nick> <grave|morte>
<motivo ou número do chamado>`. O motivo é obrigatório e fica registrado.

### Como se consente

```
Digita (dona de Aldara):  /consentir morte bruno_k
Aparece para os dois:     [Consentimento] joana_m autoriza que Bruno Calado mate Aldara Venn.
                          Vale por 30 minutos. Para desistir: /consentir cancelar
```

- Graus: `grave`, `permanente`, `morte`. Cada consentimento vale para **um
  personagem, diante de um jogador ou narrador, por 30 minutos** [cfg].
- Pode ser cancelado a qualquer momento antes do desfecho.
- Ninguém pode exigir, cobrar ou pressionar por consentimento. Pedir uma vez,
  fora do personagem, é permitido; insistir é quebra de regra.
- Todo consentimento, cancelamento e desfecho fica registrado.

### Como se aplica

Quem recebeu o consentimento escreve o emote e digita `/desfecho <nick>
<grave|morte>`. O jogo confere se o consentimento existe e ainda vale.

```
Digita (Bruno):  /desfecho joana_m morte
Aparece para todos em 16 blocos:
                 [Desfecho] Aldara Venn morreu.
Aparece para a dona:
                 [Ficha] Aldara Venn foi arquivada. Você pode criar outro personagem em 3 dias.
```

Para marca permanente não há comando de desfecho: o consentimento fica
registrado e o próprio dono reescreve a aparência.

O dono também pode decidir sozinho o fim do personagem, a qualquer momento:
`/personagem aposentar` (ver `ficha.md`) ou, numa cena com narrador, pedir o
desfecho de morte.

### Cenas com narrador

O narrador pode avisar que uma cena é perigosa. Isso não muda a regra: para
alguém morrer nela, o dono consente ao narrador com `/consentir morte <nick do
narrador>`.

## Para fechar com o `mc-comunidade`

Já alinhado com `comunidade/regras-detalhadas.md`: derrota sem consentimento
e com motivo, morte e marca permanente com consentimento ou pelos dois casos
de exceção, proteção de novato, prazo de 30 minutos fora de combate.

Pontos em que este arquivo responde a um "a combinar" de lá, para o
`mc-comunidade` conferir:

- Como o resultado é decidido: emote, dados quando alguém pede, duelo por
  acordo.
- Terceiros na luta: sem limite de número; cada um faz a sua troca.
- Quem desconecta em combate: volta derrotado e com ferimento leve.
- Depois da derrota: o personagem fica onde caiu, com os itens.
- Depois da morte: ficha arquivada, nome reservado, itens arquivados; vaga
  livre em 3 dias.

Pontos em que os dois textos ainda **divergem**:

- **Assalto.** As regras da comunidade permitem assalto. Aqui, nesta fase,
  nenhum item ou moeda muda de dono sem o acordo do dono (decisão 6 abaixo).
- **Ferimento grave.** Lá só existe ferimento comum e permanente. Aqui há um
  grau intermediário (7 dias) que também pede consentimento.

## Decisões para o dono confirmar

1. **Sem acordo, vale dado.** Escolhi isso porque o dado encerra a discussão em
   segundos sem precisar de staff presente, e o dono está sozinho. Alternativa:
   sem acordo vale emote (como no Lord of the Craft), o que preserva a escrita,
   mas depende de boa-fé ou de moderador para destravar.
2. **O alvo de um ataque não pode simplesmente ignorar**: ou defende, ou foge,
   ou se rende. Escolhi isso porque o pior que pode acontecer sem consentimento
   é leve e temporário. Alternativa: todo conflito por dados exige aceite, como
   a disputa comum; fica mais seguro, mas permite escapar de qualquer
   consequência só não clicando.
3. **Morte e marca permanente por comando de consentimento do dono, mais os
   dois casos de exceção das regras da comunidade, aplicados só pela staff e
   com motivo registrado.** Segui o que o `mc-comunidade` escreveu. Alternativa:
   nenhuma exceção, nem para a staff, que é mais fácil de explicar e de provar,
   mas deixa sem saída o personagem que ignora todo perigo.
4. **Derrota dá ferimento leve automático.** Escolhi isso para perder ter peso
   e para o ofício de cura ter serventia. Alternativa: derrota sem efeito
   mecânico, só narrativo.
5. **Morte no Minecraft é desmaio, sem efeito na ficha.** Escolhi isso para
   queda de conexão, lag ou monstro não ferirem personagem. Alternativa: desmaio
   causa ferimento leve.
6. **Nada de tomar item ou moeda sem acordo, na fase inicial.** Escolhi isso
   para não abrir espaço a assalto como passatempo antes de a comunidade ter
   regras. Alternativa: permitir levar até um valor fixo em moedas.
7. **Duelo só um contra um.** Escolhi isso porque PvP em grupo exige controle
   de times e de área. Alternativa: a staff liberar arenas de grupo em eventos.
