# Ficha de personagem

Versão 0.1 (proposta inicial). Valores marcados com **[cfg]** são números de
balanceamento: ficam no arquivo de configuração do núcleo de RP e serão
ajustados depois de testar com jogadores de verdade.

## 1. Conta e personagem

- **Conta** é quem entra no servidor (o nick autenticado pelo plugin de login).
  Tudo que é fora do personagem pertence à conta: punições, saldo de moedas,
  conversa OOC.
- **Personagem** é quem existe no mundo. Nome no chat, ficha, inventário,
  posição, ferimentos e progresso pertencem ao personagem.
- Cada conta tem **2 vagas de personagem** [cfg]. Só um fica ativo por vez.
- Personagens mortos ou aposentados não ocupam vaga.
- Vagas extras não são vendidas nem dadas como prêmio na fase inicial.

## 2. Campos dentro do jogo

A ficha do jogo é curta de propósito: cabe em uma tela de chat.

| Campo | Regra | Quem altera depois |
| --- | --- | --- |
| Nome | 3 a 30 caracteres; letras, espaço, apóstrofo e hífen; único no servidor | Só a staff |
| Idade | Número inteiro dentro da faixa da raça | O jogador |
| Raça | Uma da lista do servidor (ver seção 6) | Só a staff |
| Tratamento (opcional) | Até 12 caracteres, por exemplo "ela", "ele" | O jogador |
| Aparência | Até 200 caracteres: o que qualquer um vê ao olhar para o personagem | O jogador |
| Atributos | Braço, Passo, Tino e Voz (ver `testes.md`) | Travados após a confirmação; sobem por marcos (ver `progressao-e-economia.md`) |
| Ofício | Um da lista do servidor, ou "nenhum" | O jogador, uma vez a cada 30 dias [cfg] |

Campos que o jogo preenche sozinho: ferimentos ativos, marcos ganhos, dias de
jogo e situação da ficha (nova, revisada, devolvida).

A aparência descreve só o que se vê. Pensamentos, passado e segredos não entram
nela.

## 3. O que fica fora do jogo

- História do personagem, família, objetivos, relações: no Discord ou na wiki,
  em formato livre. Não é obrigatória para personagem comum.
- Pedidos que precisam de aprovação (seção 5).
- Anotações da staff sobre o personagem (ficam no jogo, mas só a staff lê).

## 4. Criar, trocar e aposentar

### Criar

O jogador digita `/personagem criar`. O jogo pergunta um campo por vez, no chat,
com opções clicáveis para raça, atributos e ofício. No fim mostra a ficha
inteira e pede `/personagem confirmar`.

- Atributos: 5 pontos [cfg] para distribuir, no máximo 3 [cfg] em cada um, antes
  do bônus da raça.
- Até confirmar, tudo pode ser refeito. Depois de confirmar, nome, raça e
  atributos travam.
- Conta sem personagem fica na **antessala** (área de chegada) e só sai de lá
  com um personagem ativo.

Exemplo (o que aparece só para quem está criando):

```
[Ficha] Nome do personagem?
> Aldara Venn
[Ficha] Raça?  [Raça A] [Raça B] [Raça C]
[Ficha] Idade? (entre 16 e 80 para esta raça)
> 34
[Ficha] Distribua 5 pontos.  Braço [-] 0 [+]  Passo [-] 1 [+]  Tino [-] 3 [+]  Voz [-] 1 [+]
[Ficha] Aparência, em até 200 caracteres?
> Mulher magra de mãos manchadas de tinta, capa cinza remendada, fala baixo.
[Ficha] Ofício?  [Nenhum] [Ofício A] [Ofício B] ...
[Ficha] Confira e digite /personagem confirmar
```

### Ver

- `/ficha` mostra a própria ficha.
- `/ficha <nick>` mostra a de outro jogador que esteja online. Sem nick, mostra
  a de quem você está olhando.
- Passar o mouse sobre um nome no chat mostra nome, raça, idade, aparência e o
  nick da conta.

O que os outros veem: nome, raça, idade, tratamento, aparência, ofício e se há
ferimento visível. Atributos, marcos e anotações não aparecem para outros
jogadores.

```
---- Aldara Venn (conta: joana_m) ----
Raça A, 34 anos, ela. Ofício: Escriba
Mulher magra de mãos manchadas de tinta, capa cinza remendada, fala baixo.
Ferimento: leve (some em 1 dia e 6 horas)
```

### Editar

`/ficha editar <idade|tratamento|aparencia|oficio> <valor>`. Vale na hora.

### Trocar

`/personagem trocar <número ou nome>`. Cada personagem guarda inventário, baú do
Fim, posição, vida, fome e experiência próprios. Regras da troca:

- parado por 5 segundos [cfg], sem ter levado dano nesse tempo;
- fora de conflito (sem Fôlego perdido, sem duelo, não derrotado);
- no máximo uma troca a cada 10 minutos [cfg].

O personagem que sai some do mundo; o que entra aparece onde foi deixado.
`/personagem listar` mostra as vagas.

Trocar de personagem não transfere nada: nem item, nem conhecimento. Usar um
personagem para ajudar o outro da mesma conta é quebra de regra (o texto da
regra é do `mc-comunidade`).

### Aposentar

`/personagem aposentar` pede confirmação digitando o nome do personagem. O
personagem sai de jogo vivo (foi embora, recolheu-se). Nome e ficha ficam
guardados; os itens ficam arquivados com ele. A staff pode trazer um aposentado
de volta se houver vaga livre.

### Morte

Só acontece com consentimento do dono (ver `conflito.md`). O personagem morto é
arquivado, o nome fica reservado e os itens ficam arquivados com ele. Herança
de itens para outro personagem é pedido à staff.

Depois de aposentar ou perder um personagem, a vaga libera para criação após 3
dias [cfg]. Isso evita criar e descartar personagens em série.

## 5. O que precisa de aprovação

Personagem comum **não** espera aprovação: confirmou, joga. A ficha entra numa
fila de revisão e a staff olha depois. Se houver problema (nome fora do tema,
aparência que descreve poder ou posto que o personagem não tem), a staff
**devolve** a ficha com um recado; o jogador corrige com `/ficha editar` ou pede
a troca de nome.

Precisam de aprovação **antes**:

| Pedido | Onde |
| --- | --- |
| Raça marcada como restrita | Discord |
| Qualquer uso de magia ou dom sobrenatural (até o sistema de magia existir) | Discord |
| Título, nobreza, comando de tropa, liderança de povoado | Discord |
| Mudar nome, raça ou redistribuir atributos depois de confirmar | Discord |
| Herança de itens de personagem morto | Discord |
| Voltar com personagem aposentado | Discord |

Nenhuma aprovação pode ser comprada ou apressada por pagamento.

## 6. Formato de raça

As raças ainda não foram fechadas pelo `mc-lore`. O que está fechado é o
**formato**: toda raça, de três a cinco, cabe no mesmo molde e tem o mesmo
"orçamento".

### O que uma raça dá

| Item | Regra |
| --- | --- |
| Faixa de idade | Idade mínima e máxima jogável |
| Aptidão | Exatamente **+1** [cfg] em um atributo fixo. A lore pode definir uma raça de "aptidão livre", em que o jogador escolhe o atributo na criação |
| Traço | Exatamente um. Uma frase que descreve uma situação estreita em que o personagem recebe **+2** [cfg] no teste |
| Fraqueza | Exatamente uma. Uma frase que descreve uma situação estreita em que o personagem recebe **-2** [cfg] no teste |
| Altura (opcional, cosmético) | Escala do corpo entre 0,85 e 1,10 [cfg]. O `mc-dev` confirma se a versão do Paper permite; se não permitir, a raça fica sem isso |
| Restrita | Sim ou não. Raça restrita exige aprovação antes de criar |

"Situação estreita" quer dizer que o traço não pode valer na maioria dos
testes. "Enxergar no escuro" serve; "ser mais forte" não serve.

### O que uma raça não pode dar

- Efeito permanente do Minecraft que mude combate ou sobrevivência (velocidade,
  força, vida extra, regeneração, respirar na água, voo).
- Item, receita, área ou ofício exclusivo.
- Mais de uma aptidão, mais de um traço, ou traço sem fraqueza.
- Exceção às regras de ferimento, derrota, morte ou consentimento.
- Vaga extra, moeda inicial maior ou progresso mais rápido.
- Qualquer coisa que dependa de pagamento.

### Como fica no arquivo de configuração

```yaml
racas:
  raca_a:                      # identificador; o mc-lore define nomes e valores
    nome: "Raça A"
    idade: { min: 16, max: 80 }
    aptidao: tino              # ou "escolha" para aptidão livre
    traco:
      nome: "Nome do traço"
      texto: "Recebe +2 ao ... (situação estreita)"
    fraqueza:
      nome: "Nome da fraqueza"
      texto: "Recebe -2 ao ... (situação estreita)"
    altura: 1.0
    restrita: false
```

### Magia

Sem sistema de magia fechado, a ficha não tem campo de magia. O núcleo aceita
ligar um quinto atributo pelo arquivo de configuração (ver `testes.md`), e a
magia, quando existir, será resolvida pelo mesmo teste e pela mesma disputa.
Nada mais precisa mudar na ficha.

## Decisões para o dono confirmar

1. **Duas vagas de personagem por conta, com inventário e posição separados.**
   Escolhi isso porque a segunda vaga é o que deixa o jogador continuar jogando
   quando o primeiro personagem está preso, ferido ou longe. Alternativa: uma
   vaga só no lançamento, o que reduz o plugin próprio pela metade e elimina o
   risco de perda de itens na troca (a especificação já separa essa parte numa
   segunda etapa, então dá para abrir com uma vaga).
2. **Personagem comum joga na hora e é revisado depois.** Escolhi isso porque,
   com uma pessoa só na staff, fila de aprovação prévia deixa jogador novo
   parado na porta. Alternativa: toda ficha espera aprovação antes de sair da
   antessala.
3. **Atributos escondidos dos outros jogadores.** Escolhi isso para ninguém
   escolher alvo pela ficha. Alternativa: mostrar tudo.
4. **Mortos e aposentados levam os itens junto.** Escolhi isso para a morte não
   virar forma de saque nem de transferência entre personagens. Alternativa:
   os itens caem no chão no local da morte.
5. **Vagas extras não são vendidas.** Escolhi isso porque uma terceira vaga dá
   mais inventário e mais presença no mundo, o que encosta em poder. Alternativa:
   tratar vaga extra como conveniência vendável.
