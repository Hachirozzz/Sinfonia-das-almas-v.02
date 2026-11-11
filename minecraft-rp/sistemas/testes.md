# Testes com dados

Versão 0.1 (proposta inicial). Sistema criado para este servidor. Valores
marcados com **[cfg]** são números de balanceamento e ficam no arquivo de
configuração.

## A regra inteira

**Role um dado de 12 lados, some o atributo, compare com a dificuldade.**
Igualou ou passou, conseguiu. O jogo faz a conta e mostra o resultado.

- Tirar **12** no dado é **Triunfo**: sucesso sempre, e quem rolou descreve um
  detalhe a mais a seu favor.
- Tirar **1** no dado é **Tropeço**: falha sempre, e quem rolou descreve algo
  que saiu errado.

Teste só quando o resultado é incerto e importa para a cena. Abrir uma porta
destrancada não pede teste.

## Atributos

Quatro atributos, de 0 a 4. Nomes e descrições ficam no arquivo de configuração
e podem mudar com a lore.

| Atributo | Cobre |
| --- | --- |
| **Braço** | Força, briga, arma pesada, aguentar dor, cansaço e veneno |
| **Passo** | Agilidade, furtividade, pontaria, esquiva, mãos leves |
| **Tino** | Perceber, lembrar, saber, consertar, curar, ofício manual |
| **Voz** | Convencer, mentir, intimidar, comandar, manter a calma |

Na criação: 5 pontos [cfg], máximo 3 [cfg] em cada. A raça soma +1 em um deles.
O teto absoluto é 4 [cfg].

O núcleo aceita um quinto atributo desligado por padrão, reservado para magia.

## Dificuldade

| Palavra | Número [cfg] | Com atributo 0 | Com atributo 2 | Com atributo 4 |
| --- | --- | --- | --- | --- |
| Fácil | 4 | 75% | 92% | 92% |
| Normal | 7 | 50% | 67% | 83% |
| Difícil | 10 | 25% | 42% | 58% |
| Extrema | 13 | 8% | 17% | 33% |

Quem define a dificuldade: o narrador, se houver; sem narrador, quem é afetado
pelo resultado. Na dúvida, Normal.

## Modificadores

Somam-se ao atributo. A soma de todos os modificadores fica entre -3 e +3 [cfg].

| Modificador | Valor [cfg] | Como entra |
| --- | --- | --- |
| Circunstância | -2 a +2 | O jogador digita, por combinação com a cena ou a pedido do narrador |
| Ofício | +1 | Palavra `oficio`, quando a ação é do ofício do personagem |
| Traço de raça | +2 | Palavra `traco`, quando a situação do traço vale |
| Fraqueza de raça | -2 | Palavra `fraqueza`, quando a situação da fraqueza vale |
| Ferimento leve | -1 em Braço e Passo | Automático |
| Ferimento grave | -2 em tudo | Automático |

Todo modificador aparece escrito no resultado, então quem está na cena vê e
pode contestar.

## Comandos do jogador

Todos os resultados aparecem para quem está a até 16 blocos [cfg] de quem rolou,
em uma linha, e ficam registrados no histórico de rolagens.

### `/testar <atributo> [dificuldade] [modificador] [oficio] [traco] [fraqueza]`

Atalho: `/t`. O atributo aceita as primeiras letras (`b`, `p`, `t`, `v`). Sem
dificuldade, o jogo mostra só o total e o narrador decide.

Exemplo. Aldara (Tino 3, escriba) tenta ler um selo raspado:

```
Digita:   /t tino dificil oficio
Aparece:  [Teste] Aldara Venn testa Tino (Difícil 10): 6 +3 +1 ofício = 10. Sucesso
```

Com Triunfo ou Tropeço, a palavra final muda:

```
[Teste] Aldara Venn testa Passo (Normal 7): 1 +1 = 2. Tropeço
[Teste] Aldara Venn testa Voz (Extrema 13): 12 +1 = 13. Triunfo
```

### `/disputar <atributo> [nick]`

Atalho: `/d`. Para quando dois personagens querem coisas opostas fora de
combate: segurar a porta, passar despercebido, vencer a discussão. Sem nick,
vale quem você está olhando, a até 6 blocos [cfg].

O desafiado vê botões e escolhe com que atributo responde, ou recusa:

```
Digita (Aldara):    /d passo bruno_k
Aparece para Bruno: [Disputa] Aldara Venn disputa com você usando Passo.
                    [Braço] [Passo] [Tino] [Voz] [Recusar]
Bruno clica:        [Tino]      (equivale a /responder tino)
Aparece para todos em 16 blocos:
  [Disputa] Aldara Venn (Passo) 9 +1 = 10  x  Bruno Calado (Tino) 5 +2 = 7. Aldara Venn vence
```

- Maior total vence. **Empate: nada muda**, a situação continua como estava.
- Sem resposta em 60 segundos [cfg], a disputa expira e nada acontece.
- `/recusar` encerra o pedido; quem pediu é avisado.
- Os modificadores valem como no teste (`/d passo bruno_k +1 traco`).

A disputa comum nunca obriga ninguém. O que acontece quando alguém ataca um
personagem está em `conflito.md`.

### `/dado [quantidade]d[lados]`

Dado solto, sem ficha: sorteios, jogos de taverna. Sem argumento, rola 1d12.
Limite de 10 dados de até 100 lados [cfg].

```
Digita:   /dado 2d6
Aparece:  [Dado] Aldara Venn rola 2d6: 4, 2 = 6
```

## Comandos do narrador

Exigem a permissão de narrador.

### `/pedirteste <atributo> <dificuldade> [nick | raio]`

Manda um botão de teste para um jogador ou para todos em um raio. O jogador
clica e o teste sai normalmente, visível para a cena.

```
Digita:              /pedirteste tino normal 12
Aparece para quem
está em 12 blocos:   [Narrador] Teste de Tino (Normal). [Rolar]
```

### `/npctestar <bônus> [dificuldade] <nome do NPC>`

Rola por um personagem do narrador, à vista de todos em 16 blocos.

```
Digita:   /npctestar 2 dificil Capitão da guarda
Aparece:  [Teste] Capitão da guarda testa (Difícil 10): 9 +2 = 11. Sucesso
```

Para um NPC disputar com um jogador, o narrador usa `/npctestar` sem dificuldade
e o jogador usa `/testar` sem dificuldade: vence o maior total.

### `/testaroculto <bônus> [dificuldade] [rótulo]`

Rola sem a cena ver. Só o próprio narrador e outros narradores a até 16 blocos
recebem o resultado.

```
Digita:   /testaroculto 1 normal emboscada percebe o grupo
Aparece só para narradores:
          [Oculto] emboscada percebe o grupo (Normal 7): 3 +1 = 4. Falha
```

### `/rolagens [nick] [quantidade]`

Mostra as últimas rolagens (as próprias, ou de um jogador). Serve para resolver
contestação: o que foi rolado, quando, e com quais modificadores.

```
Digita:   /rolagens bruno_k 3
Aparece só para quem pediu:
          21:14 Bruno Calado  Disputa Tino  5 +2 = 7   (perdeu de Aldara Venn, 10)
          21:09 Bruno Calado  Teste Braço Normal  8 +3 = 11  Sucesso
          21:02 Bruno Calado  Dado 1d12 = 4
```

## O que não se faz

- Rolar de novo o mesmo teste porque não gostou. Um teste por tentativa; nova
  tentativa só se a situação mudou.
- Usar `traco` ou `oficio` fora da situação descrita na ficha.
- Usar dado para obrigar outro personagem a sentir, acreditar ou querer algo.
  Voz convence um guarda do narrador; diante de outro jogador, o resultado é
  uma sugestão forte, e o dono do personagem decide como ele reage.

## Decisões para o dono confirmar

1. **Um d12 mais atributo de 0 a 4.** Escolhi isso porque, num dado curto, cada
   ponto de atributo pesa (cerca de 8%) e a conta cabe em uma linha de chat.
   Alternativa: d20, mais familiar para quem vem de RPG de mesa, mas em que
   atributo de 0 a 4 quase não se nota.
2. **Quatro atributos com nomes próprios (Braço, Passo, Tino, Voz).** Escolhi
   nomes curtos e com cara do servidor; todos são trocáveis no arquivo de
   configuração. Alternativa: nomes genéricos (Força, Agilidade, Mente,
   Presença).
3. **Modificadores de ofício, traço e fraqueza declarados pelo próprio
   jogador**, visíveis no resultado. Escolhi isso porque o jogo não tem como
   saber se a situação vale, e a visibilidade deixa a cena fiscalizar.
   Alternativa: só o narrador concede modificador, o que trava cenas sem
   narrador.
4. **O dado solto (`/dado`) fica no núcleo de RP**, em vez de instalar um plugin
   de dados separado. Escolhi isso porque é uma dúzia de linhas e tira um
   plugin da lista de atualizações. Alternativa: usar o PocketDice, citado na
   pesquisa.
5. **Diante de outro jogador, Voz não obriga.** Escolhi isso porque tirar do
   jogador o controle do que o personagem pensa é a queixa mais comum em
   servidor com dados. Alternativa: resultado de Voz vincula como qualquer
   outro.
