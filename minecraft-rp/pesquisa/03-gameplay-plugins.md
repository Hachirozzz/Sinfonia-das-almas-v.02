# Mapa de plugins para servidor de RP brasileiro (Minecraft Java) — novembro de 2026

## 0. Leia antes: limites da pesquisa e versão-alvo

**Limite de rede.** Só consegui abrir diretamente páginas do `github.com` / `raw.githubusercontent.com`. Modrinth, Hangar, SpigotMC, BuiltByBit, Polymart/Voxel.shop, papermc.io, mythiccraft.io e docs.nexomc.com estão bloqueados nesta sessão (precisariam ser liberados nas configurações de rede). Para esses, usei só trechos de resultados de busca e espelhos, o que deixa **quase todos os preços sem confirmação primária**.

Legenda de confiança usada nas tabelas:
- **[C]** confirmado em fonte primária (repositório GitHub oficial, lido nesta sessão).
- **[I]** indireto: trecho de busca de Modrinth/Hangar/BuiltByBit ou agregador/espelho. Conferir antes de comprar ou instalar.
- **[N]** não confirmado (não achei fonte, ou é só memória).

O GitHub não mostra o ano em várias datas de release. Onde escrevo "01 nov", o ano é inferido como 2026 pelo suporte a 26.x.

**Versão-alvo.** O Minecraft adotou numeração por ano (26.1, 26.2, 26.3). O Paper está em **26.3** [C] (`mcVersion=26.3` em https://raw.githubusercontent.com/PaperMC/Paper/main/gradle.properties). Vários plugins centrais só declaram suporte até **26.2**: MythicMobs 5.13.0, WorldGuard 7.0.17, CoreProtect 24.1, PlaceholderAPI 2.12.3, LibertyBans 1.1.4, VaultUnlocked 2.20.1, FancyNpcs 2.11.0, ZNPCsPlus. Recomendo abrir em **Paper 26.2**, ou testar 26.3 em ambiente de homologação antes, e subir de versão só quando o plugin mais lento do kit acompanhar. Java 25 é exigido por vários (BetterModel, PocketDice, FancyNpcs 2.10+, Iris).

---

## 1. Personagem e identidade de RP

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| Medieval Roleplay Engine (Dans-Plugins) | Fichas, emotes, dados, chat local/global/sussurro/grito/OOC local, "pássaros" (cartas), PlaceholderAPI | Grátis, GPL-3.0 [C] | 1.19.4, 1.21.11, 26.2, 26.3 [C] | Mantido (espelho indica update em abr/2026 [I]). Envia telemetria por padrão, desligável [C] | https://github.com/Dans-Plugins/Medieval-Roleplay-Engine |
| RPCore | Troca de personagem (até 10), nome, skin, "flavor text", chats de RP | Grátis [I], licença [N] | 1.21–1.21.11, única versão "1.0-SNAPSHOT" [I] | Novo e imaturo; 26.x não confirmado | https://modrinth.com/mod/rpcore |
| KrispyRoleplay | Personagens ilimitados + chat; exige SkinsRestorer | Grátis [I] | Paper até 1.20.6 (v1.4.2, mai/2024) [I] | **Parado** (v2 "em desenvolvimento" segundo agregador) | https://hangar.papermc.io/Krisp/KrispyRoleplay |
| Character Cards | Ficha: nome, idade, raça, lore, gênero, descrição | Grátis, MIT [I] | [N] | [N]; não confirmei múltiplas fichas | https://minecraftbible.com/plugins/project/character-cards |
| MMOProfiles / PerPlayerProfiles / OumProfile | "Slots" de perfil com inventário, vida e posição separados (não é ficha de RP) | MMOProfiles pago, preço [N]; os outros grátis [I] | OumProfile: Paper 1.20–26.2 [I] | [I] | https://gitlab.com/phoenix-dvpmt/mmoprofiles · https://hangar.papermc.io/sun-dev/OumProfile |
| SkinsRestorer (apoio) | Trocar skin por personagem | Grátis | 26.3 (15.12.6, 20 set) [C] | Ativo | https://github.com/SkinsRestorer/SkinsRestorer |

- **Servidores grandes:** o Lord of the Craft usa um sistema próprio de `/persona` (idade, bio, descrição, ver ficha alheia). Não achei código aberto dele [I] (https://www.lordofthecraft.net/forums/topic/227126-persona-card-guide/).
- **Armadilha de nome:** o "Characters" do TheNextLvl no Hangar é plugin de **NPC**, não de ficha [C] (https://github.com/TheNextLvl-net/characters).
- **Recomendação:** comece com o Medieval Roleplay Engine, o único pronto e gratuito confirmado em 26.3. Trate como provisório: ficha com múltiplos personagens, inventário/posição por personagem, skin, nome no chat e raça/idade validadas pela lore é o candidato nº 1 a plugin próprio. O MRE é GPL-3.0 e serve de base ou referência.

## 2. Chat de RP

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| CarbonChat | Chat por canais; cada canal tem `radius` (confirmado no código), prefixo rápido, permissão, cooldown | Grátis, GPL-3.0 [C] | Paper 26.3 testado (v3.0.0-beta.39, 18 set) [C] | Ativo, mas a linha 3.0 ainda é "beta" | https://github.com/Hexaoxide/Carbon |
| FlectonePulse | Canais com alcance, `/me`, `/do`, `/try`, `/dice`, `/ball`, ponte com Discord embutida | Grátis, GPL-3.0 [C] | "1.8.8 até a mais recente" em Paper/Folia; Java 21+ [C] | Ativo [C]; último release [N] | https://github.com/Flectone/FlectonePulse |
| rpchat | Só emotes: `/me`, `/gme`, `/do`, `/try`, `/dice`, `/coin`, `/whisper` com raio; MiniMessage | Grátis, GPL-3.0 [C] | 1.15–1.21.11 [C]; 26.x [N] | Pequeno | https://github.com/realdivided/rpchat |
| VentureChat | Canais com distância (clássico) | Grátis | v3.8.0 (20 ago, ano não exibido) cita formato JSON de 1.21.5 [C]; 26.x [N] | Lento | https://github.com/Aust1n46/VentureChat |
| ChatControl (MineAcademy) | Formatação, filtros, canais, Discord | Pago, US$ 29,97 no BuiltByBit [I] | Paper/Folia/Purpur [I]; versão [N] | Ativo [I] | https://builtbybit.com/resources/chatcontrol-format-filter-chat.18217/ |
| The Linguistic Barrier | Chat por distância + "dialetos" aprendidos por livros; texto distante vira glifos | Grátis, MIT [I] | 1.21–1.21.11, só v1.0.0, ~21 downloads [I] | Experimental | https://modrinth.com/project/qlxhBKJr |

- Não pesquisei LPC nem HuskChat a fundo [N]. LPC só formata, não tem canais.
- O "Roleplay Chat" do Modrinth (projeto BLylB8sp) é **mod Fabric**, não plugin Paper [I].
- **Idiomas fictícios:** não há plugin maduro. O único achado é o The Linguistic Barrier, sem tração.
- **Recomendação:** CarbonChat, com canais falar/sussurrar/gritar/OOC local por raio e OOC global sem raio, mais rpchat ou os comandos do MRE para `/me` e `/do`. A alternativa "tudo em um" é o FlectonePulse, que dispensa DiscordSRV para a ponte de chat. Idiomas fictícios ficam para o plugin próprio.

## 3. Dados e resolução de testes

| Plugin | Função | Preço / licença | Última versão MC | Link |
|---|---|---|---|---|
| PocketDice | `/roll NdM` por proximidade, `/groll` global, `/proll` privado, anti-spam | Grátis; licença não identificada [C] | 26.3 (v0.6.0), Paper/Folia, Java 25, sem Spigot [C] | https://github.com/Semarina/PocketDice |
| Rollbound | Dados com "estados de sucesso" configuráveis | Grátis [I] | até 1.21.8 [I] | https://modrinth.com/plugin/rollbound |
| DndRoll | Notação D&D com modificadores, destaque de críticos | Grátis [I] | Paper 1.21.1+ [I] | https://modrinth.com/plugin/dndroll |
| MRE / FlectonePulse / rpchat | `/roll`, `/dice`, `/try` embutidos | ver seções 1 e 2 | — | — |

- **Nenhum** lê atributos ou perícias da ficha do personagem.
- **Recomendação:** PocketDice agora. Teste de perícia ("d20 + Força do personagem ativo vs. dificuldade", com log para o narrador) entra no plugin próprio. É a parte mais simples de escrever, 1–2 dias sobre a API do Paper, e a que mais depende do sistema de regras ainda não definido.

## 4. Sistemas de RPG, itens, mobs e modelos

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| AuraSkills | Skills por atividade, atributos, habilidades, mana | Grátis, GPL-3.0 [C] | 26.3 (v2.4.0, 20 set) [C] | Ativo | https://github.com/Archy-X/AuraSkills |
| Fabled (ex-ProSkillAPI) | Classes, skills e atributos com editor | Grátis [C]; licença [N] | v1.1.0 (01 nov) [C]; MC [N] | Ativo | https://github.com/magemonkeystudio/fabled |
| MMOCore + MMOItems + MythicLib | Classes, níveis, profissões, itens RPG | Pagos; preço [N] | 26.x [N] (só packs de terceiros citam 26.1) | [N] | https://gitlab.com/phoenix-dvpmt |
| MythicMobs | Mobs, chefes e skills | Versão grátis + Premium; preço [N] | v5.13.0: Paper 1.21.1, 1.21.4–26.2 [I] | Ativo (wiki atualizada em ago/2026 [I]) | https://hangar.papermc.io/Lumine/MythicMobs · https://wiki.mythiccraft.io/mythicmobs/Premium-Features |
| ModelEngine | Modelos 3D animados | Pago; preço [N] | R4.1.0 testado em 1.21.11 por terceiros; 26.x [N] | [N] | — |
| BetterModel | Motor de modelos Blockbench, animação de jogador (12 membros), gera resource pack | Grátis, MIT [C] | 1.21.4–26.3 (v3.6.0, 27 out), Java 25 [C] | Muito ativo | https://github.com/toxicity188/BetterModel |
| CraftEngine | Itens, blocos e móveis customizados | Community grátis, GPL-3.0; Premium = suporte, recursos exclusivos e dev builds, preço [N] [C] | release 26.9 (16 set); faixa de MC não declarada na página [C] | Muito ativo | https://github.com/Xiao-MoMi/craft-engine |
| Nexo | Itens, blocos, móveis | Pago; preço [N] | Site citava 1.20.4–1.21.8 (pode estar velho); 26.x [N] | Ativo [I] | https://www.nexomc.com |
| ItemsAdder | Itens, blocos, móveis, HUD, emotes | Pago, ~US$ 25 segundo blog [I] | 1.20.6–1.21.11 + 26.1.1/26.1.2 (v4.0.17, mai/2026) [I] | Ativo, mas atrás do Paper | https://builtbybit.com/resources/itemsadder.10839/ |
| Oraxen | Itens, blocos, móveis | Pago e proprietário [C]; ~US$ 15 segundo blog [I] | Paper/Folia 1.20.1–26.3 [C] | Ativo; removeu Spigot [I] | https://github.com/oraxen/oraxen |
| ExecutableItems | Itens com gatilhos | Grátis limitado (500 itens, 1 ativador por item, marca d'água) / Premium, preço [N] [I] | até 26.1/26.2 [I] | Ativo | https://ssomar-developement.github.io/Wiki/docs/executableitems/questions-or-guides/frequently-asked-questions/free-vs-premium |
| Blockbench | Modelagem e animação | Grátis | app v5.2.2 (08 out 2026) [C] | Ativo | https://github.com/JannisX11/blockbench |

- **Recomendação gratuita:** AuraSkills + MythicMobs grátis + BetterModel + CraftEngine Community + Blockbench. É uma pilha 100% gratuita e atualizada.
- A integração BetterModel ↔ MythicMobs não aparece no README [N]; teste antes de modelar chefes.
- Evite a suíte MMO no início: é paga, tem três dependências e não consegui confirmar suporte a 26.x.

## 5. NPCs, diálogos e missões

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| Citizens | NPCs com IA e traits; maior ecossistema | OSL-3.0 [C]; premium no Spigot, preço [N]; builds de desenvolvimento no CI próprio [C] (gratuidade delas [N]) | módulos v26_1 a v26_3 no repositório [C] | Ativo | https://github.com/CitizensDev/Citizens2 |
| FancyNpcs | NPCs leves por pacote; inclui conversor de Citizens | Grátis, MIT [C] | 2.10.0 → 26.1.2; 2.11.0 → 26.2 [I] | Ativo; FancyDialogs ainda "work in progress" [C] | https://github.com/FancyInnovations/FancyPlugins |
| ZNPCsPlus | NPCs leves | Grátis, GPL-3.0 [C] | 1.8–26.2 [C] | Ativo | https://github.com/Pyrbu/ZNPCsPlus |
| BetonQuest | Missões e conversas em YAML, 40+ integrações | Grátis, GPL-3.0 [C] | v3.3.0 (08 nov 2026); exige Paper desde a 3.0; correções para 26.2+ [C] | Muito ativo | https://github.com/BetonQuest/BetonQuest |
| Typewriter | Missões, diálogos e cinemáticas com editor web | Grátis para usar; licença própria que proíbe modificar e redistribuir [C] | estável v0.8.0 (mai/2025) [C]; betas 0.9.0 para 1.21.3–26.2 [I] | Ativo, mas em beta longo | https://github.com/gabber235/Typewriter |
| Quests (PikaMug) | Missões por GUI | Grátis [C] | v5.3.2 (03 ago); 26.1 testado só em Spigot na 5.2.9 [C] | Ativo | https://github.com/PikaMug/Quests |
| Quests (LMBishop) | Missões simples | Grátis [C] | v3.16.1: 1.8–1.21.10/11 [C]; 26.x [N] | Lento | https://github.com/LMBishop/Quests |

- **Recomendação:** FancyNpcs + BetonQuest. Use Citizens só se precisar de NPC que anda ou luta, ou do Denizen. Considere o Typewriter se o editor visual compensar o risco de beta e a licença restritiva.

## 6. Mundo e território

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| WorldGuard | Regiões administrativas | Grátis | 7.0.17 → 1.21.11–26.2 [I] | Ativo | https://modrinth.com/plugin/worldguard |
| FastAsyncWorldEdit | Edição em massa | Grátis | 26.3 desde a 2.16.0; última 2.16.2 (01 nov) [C] | Ativo | https://github.com/IntellectualSites/FastAsyncWorldEdit |
| Towny | Cidades e nações, impostos, guerra via addons | Grátis | 1.19–26.3 (0.103.2.15, pré-release de 02 nov) [C] | Muito ativo | https://github.com/TownyAdvanced/Towny |
| GriefPrevention | Claims simples com pá | Grátis | 26.3+ (16.18.8, 01 nov) [C] | Ativo | https://github.com/GriefPrevention/GriefPrevention |
| Lands | Claims por GUI, nações, guerras, aluguel | Pago, ~€ 20 (citado em review) [I] | "26.x Support" no título do Spigot [I] | Ativo (updates até mai/2026 no espelho) | https://www.polymart.org/product/876/lands-land-claim-plugin |
| HuskTowns | Cidades por chunk | Código Apache-2.0; binário oficial pago, preço [N] [C] | 1.17.1+ [C]; 26.x [N] | [N] | https://github.com/WiIIiam278/HuskTowns |
| Iris | Gerador de mundo customizável | Grátis [N: licença] | 26.1.2–26.3 (v4.3.8, 07 nov) [C] | Muito ativo | https://github.com/VolmitSoftware/Iris |
| Terra | Gerador por packs | Grátis | v6.2.0-BETA (08 jul); MC [N] | [N] | https://github.com/PolyhedralDev/Terra |
| Terralith (datapack) | Biomas e relevo vanilla+ | Grátis, "Stardust Labs License" [I] | 2.6.3 datapack para 26.2 [I] | Ativo | https://modrinth.com/mod/terralith |
| WorldPainter | Pintar o mapa à mão, fora do jogo | Grátis [N] | [N] (o GitHub não tem releases) | [N] | https://github.com/Captain-Chaos/WorldPainter |
| BlueMap | Mapa web 3D | Grátis | Paper 26.1.1–26.3 (v5.29, 10 out) [C] | Ativo | https://github.com/BlueMap-Minecraft/BlueMap |
| squaremap | Mapa web 2D leve | Grátis | 26.3 (v1.4.3, 16 out) [C] | Ativo | https://github.com/jpenilla/squaremap |
| Pl3xMap | Mapa 2D | Grátis | builds para 26.2 [I]; repositório original arquivado em abr/2024, continua em granny/Pl3xMap [C] | Ativo sob novo mantenedor | https://github.com/granny/Pl3xMap |
| Dynmap | Mapa clássico | Grátis | v3.8 (jan/2026) até 1.21.11; 26.x só em builds de desenvolvimento [I] | Atrasado | https://github.com/webbukkit/dynmap |

- **Recomendação:** WorldGuard (áreas de staff, cidades de lore) + Towny se o RP tiver reinos geridos por jogadores, ou só GriefPrevention se não tiver. FAWE para construir.
- **Mapa do mundo:** mapa feito à mão (WorldPainter/FAWE) costuma servir melhor a RP com lore fixa. Terralith ou Iris servem se quiser geração procedural.
- **Mapa web:** squaremap é mais leve; BlueMap é mais bonito. Decida se quer mapa público, porque ele revela bases e construções secretas.
- "Factions-like" (KingdomsX etc.) não pesquisei [N].

## 7. Economia e profissões

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| VaultUnlocked | API de economia, permissões e chat (sucessor do Vault) | Grátis | 26.2 (v2.20.1, 16 jun) [C] | Ativo | https://github.com/TheNewEconomy/VaultUnlocked |
| Vault | API clássica | Grátis | 1.7.3, antigo [C] | Parado, mas ainda funciona como API | https://github.com/MilkBowl/Vault |
| EssentialsX Economy | Saldo, `/pay`, `/bal` | Grátis | ver seção 9 | Ativo | — |
| The New Economy | Multi-moeda, moeda em item | AGPLv3 | — | **Arquivado em 01/01/2026** [C]; existe fork comunitário [I] | https://github.com/TheNewEconomy/EconomyCore |
| QuickShop-Hikari | Lojas de jogador em baú | Grátis | 26.3 (v6.3.0.4, 07 nov 2026) [C] | Muito ativo | https://github.com/QuickShop-Community/QuickShop-Hikari |
| Jobs Reborn | Profissões que pagam por ação | Grátis [C] | v5.2.6.3 (15 ago); MC [N] | Ativo | https://github.com/Zrips/Jobs |
| Gringotts (fork nikosgram) | Moeda física em item, cofres | Grátis, BSD-2 [C] | [N] | [N] | https://github.com/nikosgram/gringotts |
| ItemEconomy II | Economia baseada em item via VaultUnlocked | Grátis [I] | [N] | Pequeno | https://modrinth.com/project/X1Qaqqr3 |
| BreweryX | Fabricação de bebidas (ofício de taverneiro) | Grátis | 26.1 (v3.7.1, 27 set) [C] | Ativo | https://github.com/BreweryTeam/BreweryX |

- **Recomendação:** VaultUnlocked + economia do EssentialsX + QuickShop-Hikari. Profissões: AuraSkills já cobre progressão por ofício; some o Jobs só se quiser salário por ação (os dois se sobrepõem).
- Moeda física com a identidade do servidor (moedas com modelo próprio, casa da moeda, falsificação) é melhor feita sob medida ou com Skript sobre itens do CraftEngine. Os plugins prontos são pequenos ou não confirmados.

## 8. Imersão

| Plugin | Função | Preço / licença | Última versão MC | Exige mod no cliente? | Link |
|---|---|---|---|---|---|
| Simple Voice Chat | Voz por proximidade | Grátis | branch 26.3; há plugin Bukkit/Paper [C] | **Sim.** Quem não tem o mod entra normalmente, mas sem voz [I] | https://github.com/henkelmax/simple-voice-chat |
| Plasmo Voice | Voz por proximidade | Grátis | 2.1.17 (15 set 2026); 2.2.0-beta.3 (09 nov) abandona Forge e MC < 1.21 [C] | **Sim** (Fabric/Forge) [I] | https://github.com/plasmoapp/plasmo-voice |
| GSit | Sentar, deitar, rastejar | Grátis | 26.3 (v3.8.0, 04 nov) [C] | Não | https://github.com/Gecolay/GSit |
| Móveis | Via CraftEngine, Nexo, ItemsAdder ou Oraxen (seção 4); MyFurniture (1.21.4–1.21.11) e FurnitureLib 3.3.1 (até 26.1.2) [I] | — | — | Não (resource pack) | — |
| BetterModel | Emotes e animações de jogador | Grátis, MIT [C] | 26.3 | Não | ver seção 4 |
| LocoEmotes | Danças de corpo inteiro só no servidor | Pago, preço [N] [I] | Paper 1.21+ [I] | Não | https://builtbybit.com/resources/locoemotes.112390/ |
| HMCCosmetics | Cosméticos (chapéus, mochilas) | Código GPL-3.0, vendido como premium; preço [N] [C] | [N] | Não | https://github.com/HibiscusMC/HMCCosmetics |
| RealisticSeasons | Estações, temperatura, calendário | Pago, **US$ 13** no BuiltByBit [I] | 26.1–26.2 (v11.12.0) [I] | Não | https://builtbybit.com/resources/realisticseasons.57767/ |
| Hydration / H2Oh / SimpleThirst | Sede | Grátis [I] | Hydration: Paper 26.1.2 (MIT); H2Oh: até 1.21.11 [I] | Não | https://hangar.papermc.io/voidspace1x/Hydration |

- **Voz:** não existe voz por proximidade sem mod no cliente. Trate como opcional (SVC é o mais difundido) e mantenha o chat de texto por distância como base. A voz também exige porta UDP extra aberta no host [N].
- **RealisticSeasons:** há reclamação de suporte ruim em review [I].
- **Calendário próprio** (meses e anos da lore, feriados, idade dos personagens avançando) não existe pronto fora do calendário do RealisticSeasons; é desenvolvimento sob medida.
- Sede + temperatura integradas e confirmadas para 26.x: não encontrei.

## 9. Base administrativa

| Plugin | Função | Preço / licença | Última versão MC | Status | Link |
|---|---|---|---|---|---|
| LuckPerms | Permissões | Grátis, MIT [C] | [N] na página | Ativo | https://github.com/LuckPerms/LuckPerms |
| EssentialsX | Comandos básicos, homes, kits, economia | Grátis | 2.22.0 (31 mai): suporte ativo a 26.1.2; Paper é a plataforma oficial [C]. 26.2/26.3 só em dev builds [N] | Ativo | https://github.com/EssentialsX/Essentials |
| CoreProtect CE | Log e rollback | Grátis, mas o download da v24.1 aponta para o Patreon e alguns recursos são exclusivos de apoiadores [C] | 26.2 (v24.1, 24 set) [C] | Ativo | https://github.com/PlayPro/CoreProtect |
| LibertyBans | Punições | Grátis | Paper 26.2 (v1.1.4, 04 jul) [C] | Ativo | https://github.com/A248/LibertyBans |
| LiteBans | Punições | Pago, ~US$ 15 segundo blog [I] | v2.19 (abr/2026) [I] | Ativo | https://mineguard.pro/en/blog/litebans-vs-advancedban-moderation-comparison |
| Grim | Anti-cheat | Grátis, GPL-3.0 [C] | 1.8–26.3 [C] | Ativo | https://github.com/GrimAnticheat/Grim |
| Vulcan | Anti-cheat | Pago, ~US$ 19,99 segundo roundup [I] | até 26.1.2 em espelho [I] | [I] | https://www.itechguides.com/best/minecraft-anti-cheat-plugins/ |
| PlaceholderAPI | Placeholders | Grátis | 26.2, "experimental" em Paper (v2.12.3, 03 jul) [C] | Ativo | https://github.com/PlaceholderAPI/PlaceholderAPI |
| TAB | Tablist, nametag, scoreboard | Grátis | 26.3 (v6.2.1, 18 out) [C] | Ativo | https://github.com/NEZNAMY/TAB |
| BetterHud | HUD por resource pack gerado automaticamente | Grátis, MIT [C] | Bukkit/Folia 1.21–26.1.x [C] | Ativo; 26.2+ [N] | https://github.com/toxicity188/BetterHud |
| DiscordSRV | Ponte de chat, vínculo de conta, sincronia de cargos | Grátis | v1.30.5 (24 abr) com correção para 26.x [C] | Ativo; a v3 ("Ascension") está em desenvolvimento, API instável [C] | https://github.com/DiscordSRV/DiscordSRV · https://github.com/DiscordSRV/Ascension |
| ExodusWhitelist, WhiteCord, NekoList, AuthCord | Whitelist via Discord | Grátis [I] | variam (1.21.x na maioria) [I] | Projetos pequenos e novos | https://modrinth.com/plugin/whitecord · https://hangar.papermc.io/hanamuramiyu/NekoList |

- **Whitelist por candidatura:** não achei solução madura que una formulário, avaliação da staff e vínculo de conta. O mais próximo é o ExodusWhitelist (modal de até 5 campos, aprovação por admin, lançado em mai/2026, pouquíssimos downloads) [I]. O caminho prático é formulário no Discord (bot próprio ou de tickets), a staff aprova dando um cargo, e o DiscordSRV, com vínculo de conta obrigatório e cargo exigido, libera a entrada.
- **Anti-cheat:** para RP com whitelist, Grim basta.
- Alternativas ao EssentialsX (CMI, SunLight, HuskHomes) não pesquisei [N].

## 10. Scripts vs. plugin próprio

| Ferramenta | Preço / licença | Última versão MC | Link |
|---|---|---|---|
| Skript | Grátis | Paper 1.21.5–26.3 (v2.17.1, 21 out) [C] | https://github.com/SkriptLang/Skript |
| Denizen | Grátis, MIT | Spigot 1.17.1 … 26.1.2, 26.2, 26.3 (v1.3.4); builds em ci.citizensnpcs.co [C] | https://github.com/DenizenScript/Denizen |

- **Skript:** bom para protótipos e regras pequenas (comandos de staff, itens especiais, eventos pontuais, moeda física simples). Fica frágil com dados relacionais, como personagens com atributos em banco, e com muito script.
- **Denizen:** mais poderoso, com curva de aprendizado maior. Melhor quando combinado com Citizens.
- **Plugin próprio (Java/Kotlin, API do Paper):** vale a pena quando há dados persistentes por personagem, integração entre sistemas (ficha ↔ chat ↔ dados ↔ idiomas) e necessidade de testes. Para este projeto, sugiro **um único plugin "núcleo de RP"**: personagens, nome de RP no chat, testes de atributo, idiomas e calendário, expondo placeholders para PlaceholderAPI, TAB e Carbon. Todo o resto vem de plugins prontos.

---

## Kit inicial (100% gratuito)

Paper 26.2 (ou 26.3 após teste), Java 25.

- **Administração:** LuckPerms, EssentialsX, CoreProtect CE, LibertyBans, Grim, PlaceholderAPI, TAB, DiscordSRV, SkinsRestorer.
- **RP:** Medieval Roleplay Engine (fichas provisórias), CarbonChat (canais por raio) ou FlectonePulse, PocketDice, GSit.
- **Mundo:** WorldGuard, FAWE, Towny ou GriefPrevention, squaremap ou BlueMap.
- **Economia:** VaultUnlocked, economia do EssentialsX, QuickShop-Hikari, BreweryX (opcional).
- **RPG e conteúdo (acrescentar quando a lore pedir):** AuraSkills, MythicMobs grátis, BetterModel, CraftEngine Community, BetterHud, Blockbench, FancyNpcs, BetonQuest.
- **Voz (opcional):** Simple Voice Chat.
- **Custo em plugins:** US$ 0.

## Kit ideal (com pagos)

Tudo do kit inicial, mais:

| Item | Para quê | Preço |
|---|---|---|
| ItemsAdder **ou** Nexo **ou** Oraxen (um só) | Itens e móveis com suporte e packs prontos do mercado | ItemsAdder ~US$ 25 [I]; Oraxen ~US$ 15 [I]; Nexo [N] |
| MythicMobs Premium | Mecânicas premium e builds antecipados | [N] |
| RealisticSeasons | Estações, temperatura, calendário | US$ 13 [I] |
| Lands (no lugar do Towny, se preferir GUI) | Territórios e nações | ~€ 20 [I] |
| Opcionais: ModelEngine, ChatControl, LiteBans, Citizens premium, HMCCosmetics, ExecutableItems Premium | — | ChatControl US$ 29,97 [I]; LiteBans ~US$ 15 [I]; demais [N] |

**Custo total estimado:** só os itens com preço encontrado (ItemsAdder + RealisticSeasons + Lands) somam cerca de **US$ 60**, pagamento único. Com MythicMobs Premium e um ou dois opcionais, minha estimativa é de **US$ 90–150**, pagamento único. Essa faixa inclui valores que não consegui confirmar: nenhum preço veio de página oficial de loja. Packs de modelos e texturas são vendidos à parte e podem custar mais que os plugins.

O ganho real dos pagos é pequeno no início: BetterModel + CraftEngine + MythicMobs grátis cobrem quase tudo. Eu só compraria RealisticSeasons, se estações fizerem parte da lore, e Premium do MythicMobs, quando faltar uma mecânica específica.

---

## Incompatibilidades e sobreposições

1. **Itens customizados / resource pack:** CraftEngine, Nexo, ItemsAdder e Oraxen fazem a mesma coisa e cada um gera seu pack. Use um só. BetterModel e BetterHud também geram pack, então é preciso mesclar ou hospedar um pack único.
2. **Motor de modelos:** ModelEngine e BetterModel: um só. Packs comprados costumam vir configurados para ModelEngine + MythicMobs Premium (um deles exige MythicCrucible 2.4.0, MythicMobs Premium 5.12.0 e ModelEngine 4.1.0 [I]).
3. **Chat:** só um plugin deve formatar e rotear o chat. Carbon, FlectonePulse, VentureChat, ChatControl, EssentialsXChat e o chat do Medieval Roleplay Engine ou RPCore brigam entre si. Se usar Carbon, desligue o chat do MRE e não instale EssentialsXChat. FlectonePulse já tem ponte com Discord, o que duplica o DiscordSRV.
4. **Vault vs. VaultUnlocked:** instale só um; o VaultUnlocked substitui o Vault [N: pelo que sei, não reconfirmei].
5. **WorldEdit vs. FAWE:** FAWE substitui o WorldEdit; não use os dois.
6. **Claims:** Towny, Lands, GriefPrevention e HuskTowns são mutuamente exclusivos na prática. WorldGuard convive com qualquer um deles.
7. **NPCs:** Citizens, FancyNpcs e ZNPCsPlus: escolha um e confira se o plugin de missões o suporta. A lista de integrações do BetonQuest não aparece no README [N].
8. **Progressão:** AuraSkills, MMOCore e Fabled se sobrepõem (atributos, mana, níveis). AuraSkills e Jobs Reborn recompensam as mesmas ações. A suíte MMO exige MythicLib.
9. **Economia:** um só provedor. O The New Economy foi arquivado em 01/01/2026 [C]; não comece nele.
10. **Mapas web:** um só. Dynmap está atrasado para 26.x [I].
11. **Voz:** SVC e Plasmo usam mods de cliente diferentes. Existe ponte ("Voice Bridge", Paper 1.21.4+ [I]), mas é mais simples padronizar um.
12. **Anti-cheat vs. mecânicas:** Grim ou Vulcan podem acusar falso positivo com GSit, montarias e modelos, e habilidades de movimento [N: experiência geral]. Configure exceções.
13. **Dependências de pacote:** plugins baseados em ProtocolLib ou packetevents quebram a cada versão nova do Minecraft. O QuickShop-Hikari 6.3.0.4 corrigiu problemas de ProtocolLib em 26.x [C].
14. **Skript em versão nova:** o Skript 2.17.1 só suporta Paper 1.21.5+ [C]; addons antigos podem não acompanhar.
15. **Licenças:** Typewriter proíbe modificar e redistribuir [C]. Oraxen é proprietário [C]. Fork do MRE (GPL-3.0) obriga a abrir o código se você distribuir o binário.
16. **Não pesquisado, mas a decidir cedo [N]:** modo offline com plugin de login (comum em servidores BR) afeta UUID, skins e vínculo com Discord. Suporte a Bedrock via Geyser é incompatível com boa parte do que depende de resource pack e modelos.

---

## O que não existe pronto (desenvolvimento sob medida)

1. **Sistema de personagens completo:** múltiplos personagens por conta com ficha (nome, idade, raça, descrição, atributos), inventário, posição e skin por personagem, troca segura, aprovação de ficha pela staff e nome de RP refletido em chat, TAB e nametag. Os prontos estão parados (KrispyRoleplay), imaturos (RPCore) ou cobrem só a ficha (MRE, Character Cards).
2. **Testes de perícia ligados à ficha:** dado + atributo/perícia do personagem ativo, dificuldade, vantagem, rolagem oculta do narrador, log.
3. **Idiomas fictícios:** fluência por personagem, texto embaralhado para quem não fala, aprendizado gradual, integrado aos canais do chat.
4. **Calendário e tempo da lore:** datas próprias, envelhecimento de personagem, eventos sazonais (a menos que o calendário do RealisticSeasons baste).
5. **Fluxo de whitelist por candidatura:** formulário no Discord → avaliação → vínculo de conta → liberação, com histórico. Provavelmente um bot de Discord próprio + DiscordSRV.
6. **Moeda física com identidade própria** e economia ligada à lore (cunhagem, impostos de reino).
7. **Ferramentas de narrador:** NPC "falado" pela staff no chat local, cenas e eventos, anotações por personagem, morte e ferimentos de personagem (permadeath, lesões).
8. **Sede + temperatura integradas** e confirmadas para 26.x.
9. **Cola entre sistemas:** placeholders da ficha para Carbon, TAB e BetterHud; raça/classe da lore conversando com AuraSkills e MythicMobs. Sempre será específico do servidor.
10. **Tradução para pt-BR** das mensagens de praticamente todos os plugins (trabalho de configuração, não de código).

Para uma pessoa só, a ordem sugerida é: itens 1 + 2 num único plugin (o coração do servidor), depois 5 (pode começar manual), depois 3. Itens 4, 6 e 7 começam em Skript e migram para o plugin quando estabilizarem.

---

## Fontes

**Lidas diretamente (GitHub):** todos os links `github.com` das tabelas, mais https://raw.githubusercontent.com/PaperMC/Paper/main/gradle.properties e o arquivo `ConfigChatChannel.java` do Carbon (campo `radius`).

**Indiretas (trechos de busca; conferir antes de decidir):**
- https://modrinth.com/mod/rpcore
- https://hangar.papermc.io/Krisp/KrispyRoleplay
- https://modrinth.com/plugin/rollbound
- https://modrinth.com/plugin/dndroll
- https://modrinth.com/project/qlxhBKJr
- https://hangar.papermc.io/Lumine/MythicMobs/versions/v5.13.0
- https://wiki.mythiccraft.io/mythicmobs/Premium-Features
- https://builtbybit.com/resources/itemsadder.10839/
- https://mineguard.pro/en/blog/itemsadder-vs-oraxen-custom-items-comparison-2026
- https://www.nexomc.com
- https://builtbybit.com/resources/realisticseasons.57767/
- https://builtbybit.com/resources/chatcontrol-format-filter-chat.18217/
- https://www.polymart.org/product/876/lands-land-claim-plugin
- https://mineguard.pro/en/blog/litebans-vs-advancedban-moderation-comparison
- https://www.itechguides.com/best/minecraft-anti-cheat-plugins/
- https://modrinth.com/plugin/fancynpcs/version/2.10.0
- https://modrinth.com/plugin/worldguard/changelog
- https://modrinth.com/mod/terralith/version/eihdCshF
- https://hangar.papermc.io/granny/Pl3xMap
- https://modrinth.com/mod/plasmo-voice
- https://hangar.papermc.io/voidspace1x/Hydration
- https://ssomar-developement.github.io/Wiki/docs/executableitems/questions-or-guides/frequently-asked-questions/free-vs-premium
- https://www.lordofthecraft.net/forums/topic/227126-persona-card-guide/
- https://hangar.papermc.io/sun-dev/OumProfile
- https://modrinth.com/plugin/whitecord

**Não consegui confirmar:**
- Preços oficiais de MythicMobs Premium, ModelEngine, Nexo, MMOCore/MMOItems, Citizens, HuskTowns, HMCCosmetics, ExecutableItems Premium e CraftEngine Premium.
- Suporte a 26.x de ModelEngine, Nexo, suíte MMO, Fabled, Terra, Jobs Reborn, Gringotts, WorldPainter e LuckPerms (este quase certamente atualizado, mas a página não mostra versão).
- LPC, HuskChat, CMI, KingdomsX e Axiom não foram pesquisados.

Para fechar essas lacunas, é preciso liberar na rede da sessão: modrinth.com, api.modrinth.com, hangar.papermc.io, www.spigotmc.org, builtbybit.com, polymart.org / voxel.shop, mythiccraft.io, docs.nexomc.com e papermc.io.
