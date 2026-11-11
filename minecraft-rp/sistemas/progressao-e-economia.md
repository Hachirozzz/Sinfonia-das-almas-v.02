# Progressão, ofícios e moeda

Versão 0.1: proposta enxuta para a fase inicial. Valores marcados com **[cfg]**
são números de balanceamento e ficam em arquivo de configuração.

## Princípios

- O personagem cresce **pouco e devagar**. A diferença entre um novato e um
  veterano é de 3 pontos de atributo, nunca um abismo.
- Cresce por **presença**, não por repetição de tarefa. Ninguém precisa minerar
  mil blocos para ficar mais forte.
- Depois do teto, o que cresce é o que o personagem **tem e é** na história:
  casa, cargo, fama, aliados.
- Nada disto pode ser comprado com dinheiro de verdade.

## 1. Evolução do personagem

### Dia de jogo

Um **dia de jogo** é um dia do calendário (horário de Brasília) em que o
personagem ficou ativo por pelo menos 45 minutos [cfg]. Conta no máximo um por
dia, por personagem. Tempo parado não conta.

### Marcos

A cada 10 dias de jogo [cfg], o personagem ganha um **marco**: 1 ponto para
subir um atributo, até o teto de 4. O máximo é de 3 marcos [cfg].

| Momento | Pontos de atributo |
| --- | --- |
| Criação | 5, mais 1 da raça |
| Depois de 10, 20 e 30 dias de jogo | +1 em cada |
| Teto | 8, mais 1 da raça |

```
Aparece ao completar o 10º dia de jogo:
  [Ficha] Aldara Venn alcançou um marco. Use /ficha evoluir <atributo>.
Digita:   /ficha evoluir voz
Aparece:  [Ficha] Voz de Aldara Venn passou de 1 para 2.
```

Quem joga três noites por semana chega ao teto em cerca de dois meses e meio.

### O que não existe

Níveis, experiência por matar monstro, árvore de habilidades, classes. O
plugin de habilidades por atividade citado na pesquisa (AuraSkills) **não**
entra na fase inicial.

## 2. Ofícios

O ofício é um campo da ficha, escolhido de uma lista que o `mc-lore` define
(de 6 a 10 ofícios para começar, por exemplo ferreiro, curandeiro, escriba,
taverneiro, caçador, lavrador). Cada personagem tem um, ou nenhum.

Na fase inicial o ofício dá **uma coisa só**: +1 [cfg] nos testes ligados a
ele, declarado com a palavra `oficio` (ver `testes.md`).

```
Digita:   /t tino normal oficio
Aparece:  [Teste] Bruno Calado testa Tino (Normal 7): 4 +1 +1 ofício = 6. Falha
```

Qualquer personagem pode fazer qualquer coisa do Minecraft (forjar, plantar,
cozinhar). O ofício não bloqueia receita nem área. Trocar de ofício: uma vez a
cada 30 dias [cfg].

## 3. Moeda

### Como funciona na fase inicial

- **Uma moeda só**, com nome a definir pelo `mc-lore`.
- Saldo guardado pela economia do EssentialsX, que já está no kit inicial.
  Nenhum plugin novo. O `mc-plugins` confirma os comandos e traduz as
  mensagens.
- O saldo é **da conta**: os personagens da mesma conta dividem a mesma bolsa.
  É uma limitação conhecida desta fase.
- Pagamento entre jogadores pelo comando de pagar da economia. A regra de
  conduta pede que o pagamento aconteça em cena, com os dois personagens
  presentes.

### De onde a moeda entra

| Fonte | Valor [cfg] | Observação |
| --- | --- | --- |
| Bolsa inicial | 30 | Uma vez por conta, ao confirmar o primeiro personagem |
| Diária | 5 por dia de jogo | Paga uma vez por dia por conta, mesmo com dois personagens ativos no dia |
| Prêmio de narrador | Até 20 por cena | Dado pela staff em eventos |

### Para onde a moeda sai

Sem saída, o preço de tudo sobe. Na fase inicial bastam duas saídas, ambas
operadas pela staff com os comandos da economia:

- **Aluguel** de casa, banca ou oficina em áreas do servidor.
- **Serviços do mundo** vendidos por narrador (viagem, licença, taxa de feira).

### Comércio entre jogadores

Mão a mão: combina-se em cena, entrega-se o item, paga-se. Sem loja automática
nesta fase, para o comércio gerar encontro entre personagens.

## 4. Fica para depois

| Assunto | Por que esperar |
| --- | --- |
| Moeda física (item com modelo próprio, bolsa por personagem, roubo, cunhagem) | Exige desenvolvimento e regra de roubo fechada com a comunidade |
| Saldo separado por personagem | Exige o núcleo de RP assumir a economia |
| Lojas em baú (QuickShop-Hikari) | Só faz sentido com moeda circulando e cidades de pé |
| Ofício com efeito mecânico (receitas exclusivas, qualidade de item) | Depende de itens customizados e da lore |
| Habilidades por atividade (AuraSkills) | Puxa o servidor para MMO; decidir depois de ver como a comunidade joga |
| Impostos e tesouro de reino | Depende de haver reinos de jogadores |
| Marcos concedidos por feito na história | Depende de ter narradores além do dono |
| Sistema de magia | Depende do `mc-lore` |
| Calendário da lore e envelhecimento | A idade é campo livre por enquanto |

## 5. O que nunca será vendido

Pontos de atributo, marcos, dias de jogo, moeda do jogo, vagas de personagem,
raça restrita, aprovação de ficha, itens com efeito, e remoção de ferimento.

## Decisões para o dono confirmar

1. **Evolução automática por dias de jogo, com teto baixo.** Escolhi isso
   porque não depende de staff avaliar ninguém e não premia repetição de
   tarefa. Alternativa: marcos dados só por narrador, por feito na história; é
   mais narrativo, mas vira fila e suspeita de favoritismo com staff de uma
   pessoa.
2. **Personagem evolui (pouco).** Escolhi isso porque dá motivo para voltar nas
   primeiras semanas, que é quando servidor novo perde jogador. Alternativa:
   atributos fixos para sempre, com toda a progressão na história.
3. **Saldo virtual por conta, usando a economia do EssentialsX.** Escolhi isso
   porque é zero código e zero plugin novo. Alternativa: moeda física em item,
   por personagem, que combina mais com RP mas precisa de desenvolvimento e de
   regra de roubo.
4. **Diária por presença como principal entrada de moeda.** Escolhi isso
   porque dá a todos o mínimo para participar do comércio sem obrigar a
   minerar. Alternativa: moeda só entra por venda de recursos a NPC da staff,
   o que cria economia de coleta, mas exige montar e balancear os NPCs.
5. **Sem loja automática no início.** Escolhi isso para o comércio acontecer
   entre personagens. Alternativa: instalar o QuickShop-Hikari desde o primeiro
   dia para quem joga em horário vazio.
6. **Ofício sem bloqueio de receita.** Escolhi isso porque, com 20 a 60
   jogadores, bloquear receita deixa o servidor travado quando o único ferreiro
   está offline. Alternativa: receitas exclusivas por ofício.
