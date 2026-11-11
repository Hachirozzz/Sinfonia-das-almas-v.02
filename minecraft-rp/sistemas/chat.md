# Canais de chat

Versão 0.1 (proposta inicial). Alcances marcados com **[cfg]** são números de
balanceamento. Pela decisão do projeto, **um único plugin de chat** formata e
roteia tudo; este arquivo diz o que ele precisa entregar. O `mc-plugins`
escolhe o plugin, confirma na documentação que cada item abaixo é possível e
avisa se algum não for.

## Regra geral

- Texto digitado sem nada na frente é **fala do personagem**.
- Dentro do personagem aparece o **nome do personagem**. Fora do personagem
  aparece o **nick da conta**.
- Não há prefixo de cargo, VIP ou doador em nenhum canal dentro do personagem.
- Alcance é distância em linha reta, em blocos, no mesmo mundo. Parede não
  bloqueia.

## Os canais

| Canal | Comando | Atalho | Alcance [cfg] | Quem pode |
| --- | --- | --- | --- | --- |
| Falar | `/f` ou texto puro | (nenhum) | 16 | Todos com personagem |
| Sussurrar | `/s` | `#` | 3 | Todos com personagem |
| Gritar | `/g` | `!` | 48 | Todos com personagem |
| Emote | `/me` | `*` | 16 | Todos com personagem |
| Narração local | `/n` | (nenhum) | 48 | Narradores |
| Narração global | `/ng` | (nenhum) | Servidor inteiro | Narradores |
| Fora do personagem local | `/l` | `(` | 16 | Todos |
| Fora do personagem global | `/ooc` | (nenhum) | Servidor inteiro | Todos |

O **atalho** é um caractere no começo da mensagem que a manda para aquele
canal só daquela vez. O **comando** sem texto fixa o canal até trocar de novo
(`/f` volta para a fala).

O canal global não tem atalho de propósito, para ninguém mandar no global por
engano. Cada jogador pode silenciar o global para si.

## Formato visual

Cores em hexadecimal para o `mc-plugins` aplicar; podem mudar quando o resource
pack tiver identidade própria.

| Canal | Como aparece | Cor e estilo |
| --- | --- | --- |
| Falar | `Aldara Venn: Fecha a porta, por favor.` | Nome `#E8D9B0`, texto branco |
| Sussurrar | `Aldara Venn sussurra: Ele está armado.` | Tudo `#9A9A9A`, itálico |
| Gritar | `Aldara Venn grita: Fogo no celeiro!` | Nome `#E8D9B0`, texto `#FF6B5E`, negrito |
| Emote | `* Aldara Venn puxa a cadeira e senta de costas para a parede.` | Tudo `#E0B45C`, itálico |
| Narração local | `» A chuva aperta e apaga as tochas da praça.` | Tudo `#7FC8A9` |
| Narração global | `»» Os sinos da capital tocam três vezes.` | Tudo `#7FC8A9`, negrito |
| Fora do personagem local | `(( joana_m: volto em 2 min ))` | Tudo `#6E6E6E` |
| Fora do personagem global | `[Global] joana_m: alguém para cena na taverna?` | Marca `#5B8DD9`, texto `#B8C4D6` |

Exemplos de uso:

```
Digita:   Fecha a porta, por favor.
Aparece:  Aldara Venn: Fecha a porta, por favor.            (quem está em 16 blocos)

Digita:   #Ele está armado.
Aparece:  Aldara Venn sussurra: Ele está armado.            (quem está em 3 blocos)

Digita:   !Fogo no celeiro!
Aparece:  Aldara Venn grita: Fogo no celeiro!               (quem está em 48 blocos)

Digita:   *puxa a cadeira e senta de costas para a parede.
Aparece:  * Aldara Venn puxa a cadeira e senta de costas para a parede.   (16 blocos)

Digita:   (volto em 2 min
Aparece:  (( joana_m: volto em 2 min ))                     (16 blocos)

Digita:   /ooc alguém para cena na taverna?
Aparece:  [Global] joana_m: alguém para cena na taverna?    (todos)

Digita (narrador):  /n A chuva aperta e apaga as tochas da praça.
Aparece:            » A chuva aperta e apaga as tochas da praça.          (48 blocos)
```

### Detalhes

- **Passar o mouse sobre o nome** em qualquer canal dentro do personagem mostra
  raça, idade, aparência e o nick da conta. Clicar abre a ficha (`/ficha
  <nick>`).
- **Narração** não mostra quem narrou. Narradores veem o nick ao passar o mouse.
- **Narrador falando por um NPC**: usa a narração local escrevendo a fala
  (`/n Capitão da guarda: Ninguém passa.`). Não há canal separado nesta fase.
- **Mensagens de dados e de conflito** (`[Teste]`, `[Disputa]`, `[Golpe]`,
  `[Duelo]`, `[Desfecho]`) não são canais: vêm do núcleo de RP, em `#C9A0DC`,
  para quem está a 16 blocos [cfg].
- **Emote longo**: o chat corta em 256 caracteres. Termine com `...` e continue
  na mensagem seguinte.
- **Quem está sem personagem** (na antessala) só usa os canais fora do
  personagem.
- **Mensagem privada** (`/msg`) é sempre fora do personagem. Carta e recado
  dentro da história ficam para depois.
- **Nome sobre a cabeça e lista do TAB**: nome do personagem. Na lista do TAB,
  o nick da conta vem ao lado, em cinza.

## Voz por proximidade

Opcional e fora do escopo deste arquivo. O texto é o canal oficial: o que foi
dito só por voz não vale como prova em contestação.

## Moderação

- A staff pode ler todos os canais à distância (modo espião do plugin de chat).
- Todos os canais vão para o registro do servidor.
- Intervalo mínimo entre mensagens: 1 segundo no local, 5 segundos no global e
  no grito [cfg].

## O que o plugin de chat precisa suportar

Lista de conferência para o `mc-plugins`:

1. Canal com alcance em blocos e canal sem alcance.
2. Atalho de um caractere por canal, inclusive `(`, `#`, `!` e `*`.
3. Comando curto por canal (`/f`, `/s`, `/g`, `/me`, `/n`, `/ng`, `/l`, `/ooc`),
   sem bater com comandos de outros plugins.
4. Formato por canal com cor, texto ao passar o mouse e ação ao clicar.
5. Nome do personagem no formato: pelo nome de exibição do jogador, que o
   núcleo de RP define, ou por placeholder do núcleo (ver
   `especificacao-nucleo-rp.md`).
6. Permissão por canal (para narração e para barrar canais dentro do personagem
   de quem não tem personagem).
7. Intervalo mínimo por canal, silenciar canal por jogador e modo espião.

Se o plugin escolhido não fizer emote ou narração como canal, a saída é trocar
de plugin de chat, e não mover o chat para o núcleo de RP.

## Fica para depois

- Idiomas da lore (texto embaralhado para quem não fala).
- Sussurro percebido de longe ("alguém cochicha") e som abafado por parede.
- Cartas e mensageiros.
- Ponte com o Discord.

## Decisões para o dono confirmar

1. **Fala a 16 blocos, sussurro a 3, grito a 48.** Escolhi 16 porque é o
   tamanho de uma sala grande ou de uma taverna, e mantém conversas vizinhas
   separadas com 20 a 60 jogadores. Alternativa: fala a 20 blocos, valor
   citado para o Lord of the Craft.
2. **Atalhos `#`, `!`, `*` e `(`.** Escolhi caracteres fáceis no teclado
   brasileiro e que ninguém usa para começar frase. Alternativa: só comandos,
   sem atalho, o que é mais lento durante a cena.
3. **Global fora do personagem ligado para todos, com opção de silenciar.**
   Escolhi isso porque, com poucos jogadores, é o canal que junta gente para
   cena. Alternativa: desligado por padrão, para proteger a imersão.
4. **Nenhuma marca de cargo ou de doador dentro do personagem.** Escolhi isso
   para a staff e quem apoia o servidor não terem peso extra na cena.
   Alternativa: marca discreta só no canal global.
5. **Parede não bloqueia o som.** Escolhi isso porque é o que os plugins
   prontos fazem sem código extra. Alternativa: sussurro que não atravessa
   parede, que exigiria desenvolvimento.
