# Como as regras se aplicam ao conceito Vazante

Versão 0.1. Vale só se o dono confirmar o conceito Vazante. As regras gerais
(`ficha.md`, `testes.md`, `conflito.md`, `progressao-e-economia.md`) não mudam:
este arquivo só preenche o molde com os nomes de `lore/` e responde aos pontos
que o `mc-lore` marcou como "a combinar com `mc-sistemas`". Tudo aqui é
proposta para o `mc-lore` revisar; os números são **[cfg]**.

## 1. Raças

Cada raça recebe o mesmo orçamento do molde de `ficha.md`: uma aptidão (+1),
um traço (+2 em situação estreita) e uma fraqueza (-2 em situação estreita).
Idades mínimas são proposta minha; as máximas vêm de `lore/racas.md`.

| Raça | Idade | Aptidão | Traço (+2) | Fraqueza (-2) | Altura |
| --- | --- | --- | --- | --- | --- |
| **Humano** | 16 a 70 | Livre: o jogador escolhe | **Conversa de cais**: ao pechinchar, pedir fiado ou negociar dívida | **Terra estranha**: ao se orientar ou achar caminho no Leito, fora de estrada e povoado | 1,00 |
| **Anão de casco** | 25 a 150 | Braço | **Mão de estaleiro**: ao construir, consertar ou avaliar obra de madeira e ferro | **Perna de convés**: ao correr, saltar ou perseguir | 0,85 |
| **Elfo** | 25 a 250 | Tino | **Canto de posse**: ao lembrar divisas, nomes, parentescos e o que diz um canto ou documento | **Orgulho de herdeiro**: ao pedir favor, pedir desculpa ou ceder em negociação | 1,10 |
| **Varado** | 16 a 60 (aparente) | Passo | **Filho da poça**: em qualquer teste feito dentro d'água (nadar, mergulhar, prender o fôlego, lutar na água) | **Pele que racha**: em esforço longo no seco (marcha, carga, calor) | 1,00 |

Respostas aos pontos marcados na lore:

- **Anões, "forja, construção ou resistência"**: construção entra no traço;
  força e resistência já estão na aptidão em Braço. Forja não dá bônus de raça:
  é assunto de ofício (ferreiro).
- **Elfos, "sentidos aguçados, memória"**: memória entra no traço. Sentidos
  aguçados ficam de fora: valeriam em testes demais.
- **Varados, "respirar debaixo d'água, nadar melhor, precisar de água"**:
  o molde proíbe efeito permanente do Minecraft, então **não** recebem
  respiração aquática nem velocidade de nado. Na história eles respiram na
  água; no jogo isso é o +2 do traço e a liberdade de narrar mergulhos longos.
  A necessidade de água é a fraqueza, sem barra de sede.
- **Varados puxam com mais facilidade** (`lore/magia.md`): sem efeito de regra.
  Fica como reputação dentro da história.
- Nenhuma raça é restrita. Varado tem mais a esconder, mas a lore já diz que o
  jogador não precisa saber os segredos.

## 2. Magia por achados

A magia de Vazante cabe nos dois ganchos que o núcleo já prevê (atributo só por
concessão e falha com custo), sem regra nova.

### Proposta

- **Quinto atributo: Ressaca**, de 0 a 3. Todo personagem começa com 0 e não
  pode pôr pontos nele, nem na criação nem com marcos. Só a staff altera.
- **Virar puxador** é pedido à staff (chamado no Discord), depois de o
  personagem ter vivido isso em cena. Aprovado, recebe Ressaca 1. O pedido
  inclui aceitar por escrito a cláusula de risco: é o caso de "cláusula aceita
  antes" das regras da comunidade. Subir para 2 e 3 também é decisão da staff,
  pela história.
- **Achado com ressaca** é um item entregue por narrador. Sem achado na mão,
  não há puxada. Achado comum é só item de cenário.
- **Puxar** é `/testar ressaca <dificuldade>`, com emote antes.

| Jeito | O que pode | Como se resolve |
| --- | --- | --- |
| **Eco** | Ver ou ouvir um pedaço do que um lugar ou objeto guardou | Teste; o narrador conta o que veio. Sem narrador, não há resposta: guarde o resultado e abra um chamado |
| **Corrente** | Empurrar, arrastar, segurar uma queda, desviar um objeto | Teste contra dificuldade. Contra outro personagem, é `/atacar` usando Ressaca: custa 1 de Fôlego a quem perder, como qualquer golpe |
| **Sal** | Estancar ferida, conservar, travar porta | Teste. Para ferida, é `/tratar` usando Ressaca no lugar de Tino |

- **Custo: a salga.** Toda **falha** ao puxar causa ferimento leve automático
  (a sede, as crostas). Quem já está com ferimento leve e falha de novo não
  piora sem consentimento, mas o achado **esvazia**.
- **O achado gasta.** Um **Tropeço** também esvazia o achado. Achado vazio vira
  item comum: o jogador o entrega a um narrador ou o descarta. É regra de
  honra, conferida pelo histórico de rolagens.
- **Afogar no seco** (morte) só acontece por consentimento do dono ou pela
  cláusula que o puxador aceitou, aplicada pela staff.

O que a magia **não** faz nesta fase: dano direto no Minecraft, efeito de
poção, voo, teleporte, ler ou mudar o pensamento de outro personagem, curar
ferimento grave de uma vez.

### Quando ligar

Sugiro abrir o servidor **sem puxadores jogadores**. Nas primeiras semanas a
magia aparece só como Eco conduzido por narrador e como achados que ninguém
sabe usar, que é o que `lore/magia.md` já descreve para personagem novo. O
atributo Ressaca fica desligado na configuração até a primeira aprovação.

## 3. Encalhe e a Lei do Casco

- Encalhe inteira é uma **zona de paz** do núcleo: `/atacar` e `/duelo` não
  funcionam do portão para dentro. É a tradução direta de "ninguém derrama
  sangue".
- Briga sem sangue continua possível: emote, `/disputar` (empurrar, segurar,
  tomar a frente) e discussão diante da Mesa.
- A antessala fica fora do mundo ou no alto da Escarpa (a Descida); o primeiro
  lugar do personagem no mundo é Encalhe.
- Quem quiser lutar sai pelos portões. Isso dá sentido à pena de "ser posto
  para fora ao anoitecer".
- A proteção de novato cobre os primeiros 7 dias também fora da cidade.

## 4. Posse de lote

A lore diz: quem finca um marco, levanta um teto e planta uma safra é dono do
lote. Proposta para a fase inicial, sem plugin novo:

1. A staff demarca **lotes prontos** nos povoados perto de Encalhe, como
   regiões do WorldGuard de 16 por 16 blocos [cfg].
2. O personagem pede um lote em cena (à Liga, à Companhia ou a um senhorio
   anão) e confirma por chamado. A staff o põe como membro da região.
3. **Um lote por conta** [cfg]. O lote é de um personagem na história, mas a
   proteção é da conta.
4. **Marco, teto e safra**: em 10 dias de jogo [cfg] o lote precisa ter um
   marco, uma construção coberta e uma plantação. Sem isso, volta para a
   staff.
5. **Abandono**: conta sem entrar há 45 dias [cfg] perde o lote; a construção
   é guardada em cópia antes de ser limpa.
6. **Disputa** (posseiro contra Herdeiro, Companhia contra Liga) corre dentro
   da história: Mesa dos Cascos, acordo, processo. O resultado muda quem é dono
   **na história**. A proteção só muda de mãos por decisão da staff em chamado,
   e a construção de um jogador nunca é demolida sem o acordo dele. Um
   personagem pode perder o título do lote e continuar morando lá como
   inquilino, por exemplo.
7. Fora dos povoados, construir é livre e sem proteção; vale a regra da
   comunidade contra destruir construção alheia, com o CoreProtect para
   desfazer.

O canto de posse de um elfo entra como **prova na história**: o traço "Canto de
posse" ajuda nos testes de Tino diante da Mesa, mas não dá direito automático a
terra nenhuma.

## 5. Ofícios e moeda

- **Ofícios iniciais sugeridos**, a partir de `lore/guia-do-personagem.md`:
  catador, carpinteiro, ferreiro, curandeiro, escrivão, cozinheiro, guarda,
  caçador, carroceiro, guia, mergulhador, músico. O `mc-lore` fecha a lista.
- **Moeda**: a lore ainda não deu nome. As saídas de moeda de
  `progressao-e-economia.md` já têm cara em Vazante: aluguel pago aos cascos
  em Encalhe e taxa de lote paga à Liga ou à Companhia.
- **Achados comuns** podem virar a principal mercadoria vendida a narradores
  (a Peneira compra), quando houver itens customizados. Fica para depois.

## 6. Afundados e outras ameaças

Monstros da lore (os afundados perto das poças) seguem a regra geral: morrer
para eles no Minecraft é desmaio, sem efeito na ficha. Em cena com narrador, o
narrador rola por eles com `/npctestar`.

## Decisões para o dono confirmar

1. **Varados sem respiração aquática mecânica.** Escolhi isso porque um efeito
   permanente daria a uma raça acesso a lugares e fugas que as outras não têm.
   Alternativa: abrir exceção no molde e dar respiração na água só aos varados,
   compensando com uma fraqueza mecânica (dano lento longe da água), o que
   exige desenvolvimento.
2. **Magia como atributo concedido pela staff, desligada na abertura.** Escolhi
   isso porque a lore diz que ninguém começa como puxador e porque poder
   sobrenatural é o que mais gera conflito fora do personagem em servidor
   novo. Alternativa: permitir puxadores desde o primeiro dia, por candidatura.
3. **Encalhe como zona de paz mecânica.** Escolhi isso para o jogador novo ter
   um lugar seguro garantido pelo jogo, sem depender de staff online.
   Alternativa: a Lei do Casco valer só na história, com punição dada pelos
   próprios personagens (expulsão), o que rende mais cena e mais abuso.
4. **Lotes prontos entregues pela staff, um por conta.** Escolhi isso porque
   usa só o WorldGuard, que já está no kit. Alternativa: instalar um plugin de
   terreno em que o próprio jogador demarca (a pesquisa cita o GriefPrevention),
   que tira trabalho da staff, mas acrescenta um plugin e permite lote em
   qualquer lugar do mapa.
5. **Disputa de terra nunca demole construção sem o acordo de quem construiu.**
   Escolhi isso porque perder semanas de obra por um processo dentro da
   história faz o jogador ir embora. Alternativa: derrota na Mesa vale também
   no mundo, com prazo para desocupar.
