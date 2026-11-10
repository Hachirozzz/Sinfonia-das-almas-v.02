# Relatório de infraestrutura — servidor de Minecraft RP brasileiro (20–60 jogadores, 1 mantenedor, orçamento baixo)

Data da pesquisa: 10/nov/2026.

## 0. Limitação da pesquisa (leia antes)

Nesta sessão só consegui abrir diretamente páginas do GitHub (github.com e raw.githubusercontent.com). Os hosts papermc.io, docs.papermc.io, fill.papermc.io, minecraft.net, geysermc.org, purpurmc.org e os sites dos hosts brasileiros estão fora da lista de rede permitida.

Por isso uso três níveis de confiança:

- **[GH]** — li diretamente no repositório oficial no GitHub (inclui o código-fonte da documentação do Paper/Velocity e do wiki do Geyser).
- **[BUSCA]** — veio de trecho de resultado de busca; a página em si não foi aberta. Todos os preços estão neste nível.
- **[NÃO CONFIRMADO]** — fontes conflitantes ou inexistentes.

Para verificação direta, é preciso liberar esses domínios nas configurações de rede da sessão, ou conferir manualmente os links.

---

## 1. Recomendação de stack

### Stack principal (Java apenas)

| Camada | Escolha | Por quê |
|---|---|---|
| Servidor | Paper 26.3, build do canal estável, fixado por número | Único software com o ecossistema de plugins de RPG (MythicMobs, ModelEngine/BetterModel etc.); o guia de otimização recomenda Paper ou Purpur |
| Java | Java 25 (Adoptium ou Corretto) | Exigido pelo Paper 26.1+ |
| Proxy | Nenhum no início | Servidor único não precisa; ver seção 4 |
| Autenticação | `online-mode=true` | Mais simples e seguro; a alternativa "pirata" está na seção 3.3 |
| Host | Plano gerenciado de Minecraft em São Paulo, 10–12 GB, CPU Ryzen 9 de geração recente, anti-DDoS incluso | Faixa de ~R$ 65–160/mês; menos operação para uma pessoa só |
| Empacotamento | Docker `itzg/minecraft-server` em dev local e, se for VPS, em produção | Mesma definição nos dois ambientes |
| Operação | Git para configs, Chunky, spark, backup off-site com restic/rclone | Ver seção 6 |
| Plugin próprio | Gradle Kotlin DSL + `paper-api` 26.3 + `plugin.yml`, Java 25, mapeamentos Mojang | Ver seção 2.3 |

### Alternativa se o dono aceitar Bedrock

Mesma base, mais:

- **Geyser + Floodgate** como plugins no próprio Paper (sem proxy). O Geyser suporta Java 26.3 e Bedrock 26.40–26.63 [GH].
- Porta **UDP 19132** aberta. Confirme antes de contratar que o host libera porta UDP extra e que o anti-DDoS cobre UDP.
- Um **segundo pipeline de arte**: o Geyser não converte resource pack Java para Bedrock. Cada item ou modelo custom precisa de pack Bedrock e mapeamento.
- Menus do plugin próprio feitos com forms do Floodgate/Cumulus para Bedrock e fallback de baú/chat para Java.

Decida isso antes de produzir arte e de escrever os menus. Adicionar Bedrock depois custa retrabalho em tudo que é visual.

---

## 2. Software de servidor

### 2.1 Versão atual do Minecraft Java

- A última release é a **26.3 "Wilderness Bound"**, de setembro de 2026 (um site dá 15/set). [BUSCA] https://www.gamingonlinux.com/2026/09/minecraft-java-edition-26-3-wilderness-bound-update-released/ e https://craftdex.net/ru/updates/wilderness-bound
- A numeração agora é por ano (26.1, 26.2, 26.3), substituindo 1.21.x. [BUSCA] https://beebom.com/minecraft-new-version-numbering-system-for-java-edition
- Um snapshot da 26.4 teria saído em 6/nov/2026, segundo um único site indonésio. [NÃO CONFIRMADO]
- Não encontrei hotfix 26.3.x. [NÃO CONFIRMADO]

### 2.2 Paper, Purpur, Folia e servidores com mods

**Paper**
- O repositório está na linha 26.3 e compila com JDK 25. [GH] https://github.com/PaperMC/Paper
- A documentação diz que o Paper exige Java 25 para 26.1 e posteriores (Java 21 para 1.20–1.21.11). [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/paper/admin/getting-started/getting-started.mdx
- Canal estável da 26.3: vários guias de terceiros dizem que o primeiro build estável foi o #175, em 11/out/2026. O trecho da página oficial de downloads mostra "Paper 26.3 Build #246". Guias de fim de setembro ainda diziam "alpha/experimental". [BUSCA, conflito de datas] https://papermc.io/downloads/paper e https://hostvalues.net/en/blog/minecraft-26-3-server-guide
- Um mundo atualizado para 26.3 não volta para 26.2. [BUSCA] https://mc-node.net/blog/en/minecraft-26-3-server-breaking-changes/
- Servidor 26.3 novo viria com `white-list=true` por padrão. [BUSCA, uma fonte] https://space-node.net/blog/minecraft-26-3-server-update-guide-2026
- O post oficial da 26.3 anuncia para a 26.4 a fusão das configs de Bukkit, Spigot e Paper em um lugar só. Isso afeta o que for versionado em Git. [BUSCA] https://papermc.io/news/26-3

**Purpur**
- É substituto direto do Paper com mais opções de configuração; o repositório tem branch `ver/26.3`. [GH] https://github.com/PurpurMC/Purpur
- Não consegui confirmar se os builds 26.3 já são estáveis; o trecho da página fala em builds experimentais. [NÃO CONFIRMADO] https://purpurmc.org/download/purpur/26.3
- Comece em Paper. Migrar para Purpur depois é trocar o jar; o caminho inverso perde as configs do Purpur.

**Folia**
- Não serve para este caso. O README sugere 16 ou mais núcleos físicos e jogadores espalhados, e manda assumir compatibilidade de plugins "essencialmente zero". [GH] https://github.com/PaperMC/Folia
- A API de scoreboard e parte da API de login estão quebradas nele. [GH]

**Fabric / NeoForge**
- Fabric suporta 26.3; NeoForge 26.3 aparecia como beta. [BUSCA] https://hub.tcno.co/games/minecraft/26.3/server/fabric/
- Fabric não implementa a API Bukkit, então MythicMobs e afins não rodam. [BUSCA] https://www.gameserverkings.com/knowledge-base/minecraft/fabric-server-setup/
- Mods de conteúdo exigem instalação no cliente, o que afasta o público de celular e de launcher alternativo. Geyser não suporta servidores que exigem mods no cliente. [GH] wiki do Geyser
- Só faz sentido se o projeto virar modpack fechado.

**Plugins de RPG na 26.3**
- ModelEngine 4.1.0 seria compatível com 26.3 (fonte indireta); BetterModel listava até 26.2. [BUSCA] https://modrinth.com/plugin/BetterModel
- Suporte do MythicMobs à 26.3: [NÃO CONFIRMADO].
- Antes de fixar a versão do servidor, confira cada plugin crítico. Se algum não suportar 26.3, é legítimo começar em 26.2 e subir depois.

### 2.3 API de plugins do Paper — o que muda para quem escreve plugin em 2026

**Dependência e build**
- Coordenadas: `compileOnly("io.papermc.paper:paper-api:26.3.build.+")`, repositório `https://repo.papermc.io/repository/maven-public/`. [GH] https://github.com/PaperMC/Paper
- Dá para fixar um build (`{VERSÃO}.build.25-stable`). O formato antigo `-R0.1-SNAPSHOT` vale só até 1.21.11. [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/paper/dev/getting-started/project-setup.mdx
- Toolchain Java 25, Gradle com Kotlin DSL (Maven é desencorajado), IntelliJ com o plugin Minecraft Development, e o plugin Gradle run-task para subir um Paper de teste. [GH] mesma página

**Mapeamentos e NMS**
- Desde 1.20.5 o runtime do Paper é mapeado com nomes da Mojang e o pacote do CraftBukkit não é mais realocado por versão. [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/paper/dev/getting-started/userdev.md
- Desde 26.1 a Mojang não ofusca mais o jar do servidor. O Paper não suporta plugins ofuscados e a reobfuscação não funciona com dev bundles 26.1+. [GH] mesma página
- Use `paperweight-userdev` só se precisar de código interno do servidor. A página não cita número de versão; manda usar a mais recente. Código interno quebra a cada versão do jogo, então prefira a API pública.

**Relação com Spigot**
- O Paper fez hard fork do Spigot em 13/dez/2024 e não puxa mais a API nova do Spigot. Plugins Spigot antigos seguem funcionando, salvo os que usam API adicionada ao Spigot depois do fork. [BUSCA] https://papermc.io/news/the-future-of-paper-hard-fork e https://forums.papermc.io/threads/the-future-of-paper-hard-fork.1451/
- O Paper avisa que enums como `Material` e `Biome` podem virar classes; evite `switch`, `EnumMap` e `EnumSet` sobre eles. [BUSCA] mesma fonte
- Escreva o plugin só para Paper. Mirar Spigot traz custo sem benefício.

**`paper-plugin.yml`**
- Continua marcado como experimental e sujeito a mudança; bootstrappers são "altamente experimentais". [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/paper/dev/getting-started/paper-plugins.md
- Diferenças em relação ao `plugin.yml`: comandos só via Brigadier, isolamento de classloader, dependências declaradas em blocos `bootstrap` e `server`, serialização de config precisa de registro manual, dependência cíclica impede o servidor de subir. [GH]
- Um jar pode conter os dois arquivos. [GH]
- Comece com `plugin.yml`. Passe para `paper-plugin.yml` só quando precisar de algo que exige bootstrap (registros no estilo datapack, por exemplo).
- Na 26.3 boa parte da API antes experimental, incluindo quase toda a de data components, perdeu a anotação de experimental. [BUSCA] https://papermc.io/news/26-3

---

## 3. Java, Bedrock ou ambos

### 3.1 Geyser + Floodgate

**Versões**
- Geyser suporta Java 26.3 e Bedrock 26.40–26.45, 26.50–26.52, 26.60–26.63. [GH] https://github.com/GeyserMC/Geyser
- Guias do início de outubro diziam que o Geyser ainda se apresentava como cliente 26.2 e precisava de ViaVersion/ViaBackwards; o README atual já lista 26.3. [BUSCA, desatualizado]

**Floodgate**
- Permite que jogadores Bedrock entrem em servidor `online-mode=true` sem conta Java. [GH] https://github.com/GeyserMC/Floodgate
- Tem comando `fwhitelist` e envia skins Bedrock via MineSkin, com fila que pode demorar. [GH] https://github.com/GeyserMC/GeyserWebsite/blob/master/wiki/floodgate/features.md
- Não consegui abrir as páginas sobre prefixo de nome, vínculo de contas e API de forms (Cumulus). [NÃO CONFIRMADO nesta pesquisa]

**O que quebra ou exige trabalho**
- **Resource packs**: o Geyser não converte packs Java. Você monta o pack Bedrock à mão e o coloca na pasta `packs` ou serve por URL (que exige `Content-Length` exato e `Content-Type: application/zip`). [GH] https://github.com/GeyserMC/GeyserWebsite/blob/master/wiki/geyser/packs.md
- O pedido de conversão automática foi fechado em 02/fev/2026. [BUSCA] https://github.com/GeyserMC/Geyser/issues/210
- **Ferramentas de conversão**: Rainbow (mod de cliente Java) cobre blocos, itens 2D simples e armadura simples; Thunder cobre packs que alteram conteúdo vanilla. [BUSCA] https://geysermc.org/wiki/other/rainbow/
- **Item displays**: conteúdo de plugins baseado em item displays ainda não é suportado pelo Geyser. Isso atinge a maioria dos plugins modernos de modelos e móveis. [BUSCA] mesma página
- **Itens custom**: usam mapeamentos próprios; o formato v1 está depreciado desde a API 2.9.3. [BUSCA] https://geysermc.org/wiki/geyser/custom-items/
- **Modelos de mobs**: existe a extensão GeyserModelEngine, que converte modelos ModelEngine/BetterModel. Exige GeyserUtils e exportar cada modelo por um plugin do Blockbench. É projeto comunitário e não lista as versões suportadas. [GH] https://github.com/zimzaza4/GeyserModelEngine
- **Limitações sem correção possível no Geyser** [GH] https://github.com/GeyserMC/GeyserWebsite/blob/master/wiki/geyser/current-limitations.md :
  - links clicáveis no chat;
  - efeito glowing;
  - distinguir clique esquerdo e direito em inventários (quebra muitos menus de baú);
  - recursos que dependem de tab-complete;
  - encantamentos custom e receitas custom de bigorna/ferraria;
  - blocos na cabeça de entidades;
  - segunda linha de toasts.
- Behavior packs e add-ons não são suportados; em consoles o jogador não escolhe packs. [GH] packs.md
- Movimento ainda não é perfeito, o que pode gerar falsos positivos em anticheat. [GH] README do Geyser

**Conclusão**: Bedrock para chat, construção e RP "vanilla" funciona bem. Para RP com mobs e itens modelados, cada asset custa trabalho dobrado e parte dos efeitos não aparece.

### 3.2 ViaVersion / ViaBackwards

- ViaVersion deixa clientes mais novos entrarem em servidor mais antigo; ViaBackwards deixa clientes mais antigos (até 1.9) entrarem. [GH] https://github.com/ViaVersion/ViaVersion
- ViaBackwards 5.12.0 (18/set/2026) adicionou suporte a servidor 26.3; a última estável vista foi 5.12.2 (27/out/2026). [BUSCA] https://hangar.papermc.io/ViaVersion/ViaBackwards/versions/5.12.2
- Instale só o ViaVersion, para que o servidor não derrube jogadores no dia em que sair a 26.4.
- Evite ViaBackwards se usar resource pack e modelos custom: clientes antigos têm outro formato de pack e de componentes de item, e o resultado visual quebra. Fixe uma versão mínima de cliente. (Juízo meu, não confirmado em fonte.)

### 3.3 Público brasileiro: online-mode e contas não originais

- Não encontrei dado confiável sobre a fatia de jogadores brasileiros em celular ou em launcher não original. [NÃO CONFIRMADO]
- Há redes brasileiras que se anunciam como "Original/Pirata" e crossplay, o que indica que o mercado local espera isso. [BUSCA] https://minecraftservers.org/server/645157

**As três opções**

| Opção | Quem entra | Custo e risco |
|---|---|---|
| A. `online-mode=true`, só Java | Contas Java originais | Zero manutenção de autenticação; exclui a maior parte do público de celular e pirata |
| B. `online-mode=true` + Geyser/Floodgate | Java original + Bedrock com conta Microsoft | Autenticação continua sólida; custo é o pipeline Bedrock da seção 3.1 |
| C. `online-mode=false` + plugin de login | Todos | Você passa a ser o responsável pela autenticação |

**Riscos da opção C**
- Sem verificação, qualquer um entra com o nick de um admin ou de um jogador original. [BUSCA] https://minecraft.pl/en/wiki/plugins/catalog/fastlogin
- Combinação mais citada: AuthMe (ou nLogin) + FastLogin para login automático de contas originais. O FastLogin original está sem manutenção e existe um fork "FastLogin Update" que depende de ProtocolLib. [BUSCA] https://www.spigotmc.org/resources/fastlogin-update.134322/
- Estado atual do LibreLogin: [NÃO CONFIRMADO].
- UUIDs passam a ser offline (derivados do nome). Migrar depois para online-mode troca o UUID de todo mundo: inventários, permissões e dados do plugin próprio.
- Você guarda senhas. Isso exige hash forte, backup protegido e traz obrigações de LGPD.
- Ataques de bots são mais fáceis, porque não há barreira de conta.
- A fonte consultada recomenda ficar em online-mode se possível. [BUSCA] https://soulfiremc.com/blog/testing-authentication-systems
- Não confirmei o que a EULA e as diretrizes da Mojang dizem hoje sobre servidores offline-mode. [NÃO CONFIRMADO]

**Minha recomendação**: comece em A ou B. Se o dono decidir por C, a decisão tem de ser tomada antes do lançamento, por causa dos UUIDs, e o plugin próprio deve tratar identidade por uma camada própria, sem assumir UUID da Mojang.

---

## 4. Proxy (Velocity)

- Última release: Velocity v4.3.1, de 27/out, marcada como atualização de segurança recomendada. [GH] https://github.com/PaperMC/Velocity/releases
- Velocity 4.0.x e posteriores exigem Java 25. [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/velocity/admin/getting-started/faq.md
- Com proxy, o backend roda `online-mode=false` com "modern forwarding" e segredo compartilhado em `config/paper-global.yml` (`proxies.velocity.*`). O backend precisa ficar inacessível de fora. [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/velocity/admin/getting-started/forwarding.md
- Chat assinado (1.19.1+) tem limitações no proxy. [GH] FAQ acima

**Para servidor único, não vale a pena.** É mais um processo, mais RAM e uma superfície de erro grave (backend offline-mode exposto).

**Passa a ser necessário quando** (juízo meu):
- houver um segundo servidor (lobby, eventos, mundo de recursos, servidor de teste público);
- você quiser reiniciar o servidor de jogo sem derrubar conexões;
- o serviço de proteção DDoS escolhido exigir um ponto de entrada próprio;
- Geyser precisar atender vários backends.

O que fazer desde já para não se arrepender: usar domínio próprio com registro SRV (trocar o destino depois é transparente) e não gravar IP ou porta em lugar nenhum.

---

## 5. Hospedagem

### 5.1 Dimensionamento (estimativa minha, não é número de fonte)

- A doc do Paper recomenda 6–10 GB "independentemente do número de jogadores" e, em host compartilhado, deixar 1–1,5 GB abaixo do limite do plano para overhead. [GH] https://github.com/PaperMC/docs/blob/main/src/content/docs/paper/admin/how-to/aikars-flags.md
- O tick principal é de thread única, então o que importa é desempenho por núcleo (Ryzen 9 7950X/9950X ou equivalente), não quantidade de núcleos.

| Fase | Plano | Heap (`-Xmx`) |
|---|---|---|
| Desenvolvimento e beta fechado (até ~20) | 8 GB | ~6,5 GB |
| Lançamento (20–40, MythicMobs + modelos) | 10–12 GB | 8–10 GB |
| 40–60 com muitos mobs ativos | 12–16 GB | 10–12 GB |

- Em VPS: 4 vCPU dedicados e NVMe; 40–60 GB de disco bastam com mundo de borda limitada e backups fora da máquina.
- Latência: São Paulo dá cerca de 10–40 ms para a América do Sul, contra 120–150 ms em hospedagem nos EUA. São números de marketing de fornecedor; trate como ordem de grandeza. [BUSCA] https://valebyte.com/en/blog/best-vps-in-sao-paulo-2026-review-and-prices/

### 5.2 Tabela de hosts

Todos os preços são [BUSCA] e não foram conferidos na página do fornecedor. Confirme no checkout.

**Hosts gerenciados de Minecraft em São Paulo**

| Host | Plano | Preço | Fonte | Observação |
|---|---|---|---|---|
| BedHosting | Plus 8 GB, Ryzen 9 5900XT | R$ 79,87/mês | https://bedhosting.com.br/minecraft | Anti-DDoS incluso; datacenter em SP |
| BedHosting | Ultra 8 GB DDR5, Ryzen 9 9950X/7950X | R$ 100,00/mês | https://bedhosting.com.br/minecraft | |
| BedHosting | Plus 12 GB / 16 GB DDR4 | R$ 120,00 / R$ 160,00/mês | https://bedhosting.com.br/crossplay | CPU desses planos não confirmada; preços inconsistentes entre páginas do próprio site |
| Hostzera | "Full Iron" 8 GB DDR5, Ryzen | R$ 64,90/mês promocional (referência R$ 80,80) | https://hostzera.com.br/hospedagem-minecraft | |
| HostSquare | 8 GB | R$ 100,00/mês | https://hostsquare.com.br/minecraft/brasil | CPU não confirmada |
| HeavyHost | 8 GB, EPYC | a partir de R$ 68,00/mês | https://heavyhost.com.br/hospedagem-minecraft.html | EPYC costuma ter single-thread inferior a Ryzen 9 |
| StreetHosting | 8 GB DDR5, Ryzen 9 9950X | US$ 21,50/mês | https://streethosting.com.br/en/minecraft-pro | Cobrado em dólar |
| ReisHosting | até 8 GB | R$ 34,47/mês | https://reishosting.com.br/en/minecraft | CPU não informada |
| LuraHosting | Minecraft | a partir de R$ 34,90 | https://lurahosting.com.br/ | Sem detalhes de hardware |
| Nodecraft (SP) | 8 GB | US$ 39,98/mês (US$ 27,99 no primeiro mês em promoção) | https://nodecraft.com/pricing | Internacional com nó em SP |
| Bisect Hosting (SP) | — | preço não encontrado | https://www.bisecthosting.com/locations/saopaulo-server-location | |

**VPS e dedicados em São Paulo**

| Fornecedor | Produto | Preço | Fonte | Observação |
|---|---|---|---|---|
| Hostinger | KVM 2 / KVM 4 | US$ 8,79 / US$ 12,99 por mês com 24 meses antecipados; renovação US$ 14,99 / US$ 28,99 | https://www.hostinger.com/vps | Disponibilidade de VPS em SP é conflitante: uma fonte diz que foi removida em 26/abr/2026 |
| Vultr (SP) | High Performance 1 vCPU/1 GB; 2 GB | US$ 9; US$ 18 | https://valebyte.com/en/vps/brazil/ | Planos de 8 GB+ não cotados |
| Faixa geral SP | 4 GB / 2 vCPU / 80 GB NVMe | US$ 18–30/mês | https://valebyte.com/en/blog/best-vps-in-sao-paulo-2026-review-and-prices/ | Banda costuma ser medida em SP |
| HypeHost | Cloud KVM 6 GB, 2 vCPU Xeon | R$ 89/mês | https://hypehost.com.br/en/cloud | Linha VPS Ryzen/Gamer com AntiDDoS: preço não encontrado |
| MagnoHost | VPS | a partir de R$ 20,99/mês | https://magnohost.com.br/en | Anuncia Ryzen próprio e anti-DDoS de 20 Tbps; preço de 8–16 GB não encontrado |
| QNAX | VPS 8 GB Linux | R$ 169,90/mês | https://www.whtop.com/amp/plans/qnax.com.br/153988 | Listagem de terceiro |
| ECXON | Dedicado Ryzen 9 7950X, 128 GB | R$ 2.200/mês | https://www.whtop.com/plans/ecxon.com.br/153911 | Localização em SP não confirmada; fora do orçamento |
| Valebyte | Dedicado Ryzen 9 7900, 64 GB, SP | US$ 469 | https://valebyte.com/en/servers/amd-ryzen-9-7900-37ghz-12-cores-sao-paulo-br/ | Fora do orçamento |

- **OVHcloud**: as páginas da própria OVH dizem que não há datacenter no Brasil; os servidores "otimizados para o Brasil" ficam nos EUA. [BUSCA] https://www.ovhcloud.com/en-gb/bare-metal/dedicated-server-brazil
- **Hetzner e Contabo em SP**: não encontrei. [NÃO CONFIRMADO]

### 5.3 Oracle Cloud Always Free (ARM)

- A cota gratuita de Ampere A1 foi cortada pela metade: de 4 OCPU/24 GB para **2 OCPU/12 GB** (1.500 OCPU-horas e 9.000 GB-horas por mês), com aplicação em 18/ago/2026. [BUSCA] https://community.oracle.com/customerconnect/discussion/970310/oci-always-free-updated-ampere-a1-compute-allocation e https://news.ycombinator.com/item?id=49183750
- Usuários relataram instâncias desativadas ou sumidas em 19/ago. [BUSCA] https://community.oracle.com/customerconnect/discussion/974491/always-free-a1-instances-disabled
- Não achei o aviso oficial da Oracle nem informação sobre capacidade na região de São Paulo. [NÃO CONFIRMADO]
- Com 2 OCPU ARM (single-thread fraco), sem SLA e com histórico de corte unilateral, serve para ambiente de staging ou para rodar backups e bots. Não use como produção de 20–60 jogadores com MythicMobs.

### 5.4 Hospedar em casa

- Muitas linhas de fibra no Brasil usam CGNAT, sem porta de entrada IPv4. Sinal típico: IP WAN na faixa 100.64.0.0/10. [BUSCA] https://natchecker.com/pt/blog/tirar-internet-da-cgnat
- Saídas: pedir IPv4 público à operadora, usar IPv6, ou túnel como playit.gg, que adiciona latência e instabilidade. [BUSCA] https://noping.com/pt/blog/cgnat-no-brasil-para-jogos-nat-portas-chat-e-ping
- Outros problemas: expõe o IP residencial a DDoS, upload limitado, queda de energia.
- Bom para o ambiente de desenvolvimento. Ruim para produção.

### 5.5 Painéis e Docker

- **Pterodactyl**: última estável v1.15.2 (21/out, correção de segurança de PHP). A 2.0 está em beta aberto (v2.0.0-beta.2) e os mantenedores pedem para manter produção na 1.x. [GH] https://github.com/pterodactyl/panel/releases
- **Pelican**: última release v1.0.0-rc.2 (22/out); ainda release candidate. [GH] https://github.com/pelican-dev/panel/releases
- Host gerenciado já entrega painel (em geral Pterodactyl ou derivado). Em VPS própria com um servidor só, painel é excesso: Docker Compose resolve. Se quiser painel, Pterodactyl 1.15.x é a opção estável hoje.
- **`itzg/minecraft-server`**: a tag `latest`/`stable`/`java25` roda Java 25 em amd64 e arm64. [GH] https://github.com/itzg/docker-minecraft-server/blob/master/docs/versions/java.md
- Variáveis úteis: `TYPE=PAPER`, `VERSION`, `PAPER_BUILD` (fixa o build), `PAPER_CHANNEL` (por padrão só pega builds de release). [GH] https://github.com/itzg/docker-minecraft-server/blob/master/docs/types-and-platforms/server-types/paper.md
- A imagem é só Java Edition; com Geyser como plugin basta publicar a porta 19132/udp.
- Backup em sidecar: `itzg/mc-backup`, com métodos tar, rsync, restic e rclone. Coordena `save-off`/`save-all`/`save-on` por RCON e faz prune configurável. [GH] https://github.com/itzg/docker-mc-backup

---

## 6. Operação

**Backups**
- Regra 3-2-1: cópia local, cópia fora da máquina (restic ou rclone para armazenamento de objetos), e teste de restauração.
- Inclua mundo, `plugins/` (dados e bancos) e dump do banco se usar MySQL/MariaDB.
- Em host gerenciado, confira se o backup do painel sai da máquina. Se não sair, agende exportação por SFTP.
- Faça backup antes de qualquer atualização de versão, porque o mundo não tem downgrade.

**Pré-geração e borda**
- Chunky pré-gera chunks e roda em Paper. [GH] https://github.com/pop4959/Chunky
- O guia de otimização (atualizado para 26.3) manda definir a borda antes, com borda vanilla (`/worldborder set`) separada para overworld, nether e end. [GH] https://github.com/YouHaveTrouble/minecraft-optimization
- O mesmo guia diz que pré-gerar hoje é em geral desnecessário, exceto em CPU fraca ou para plugin de mapa. Para RP com mapa fechado ainda vale: fixa o tamanho do mundo e do backup.

**Valores iniciais de config** [GH] guia acima
- `simulation-distance` 4, `view-distance` 7
- `network-compression-threshold` 256
- limites de spawn no `bukkit.yml`: monsters 20, animals 5
- `entity-activation-range` reduzido
- `max-entity-collisions` 2
- `redstone-implementation: ALTERNATE_CURRENT`
- `treasure-maps.enabled: false` enquanto o mundo não estiver pré-gerado e com borda
- Evite plugins de limpar itens do chão, empilhadores de mobs, plugins que recarregam plugins e o comando `/reload`.
- Flags de JVM: Aikar (G1GC), com `Xms` igual a `Xmx`. A doc não fala de ZGC nem de ajustes específicos para Java 25. [GH] aikars-flags.md

**Monitoramento**
- spark: profiler de CPU, memória e ticks. [GH] https://github.com/lucko/spark
- O repositório tem um módulo `spark-paper`; não confirmei nesta sessão se vem embutido no Paper 26.3. [NÃO CONFIRMADO]
- Acrescente um monitor externo de uptime e alerta no Discord.

**Proteção DDoS**
- Primeira linha: o anti-DDoS do próprio host. Os hosts brasileiros acima anunciam isso como incluso.
- TCPShield: plano grátis com 1 TB/mês, Pro a US$ 25/mês, Premium a US$ 100/mês. [BUSCA] https://tcpshield.com/plans
- Não encontrei evidência de ponto de presença do TCPShield em São Paulo. Um proxy sem PoP no Brasil aumenta o ping de todos. [NÃO CONFIRMADO]
- Alternativas que anunciam PoP em SP: BH Shield da BlazeHosting (https://blazehosting.com.br/antiddos) e DonutShield. São afirmações de fornecedor. [BUSCA]
- NeoProtect é focado na Europa. [BUSCA] https://rackdiff.com/en/blog/minecraft-ddos-schutz
- Com Purpur, `use-alternate-keepalive` é incompatível com TCPShield. [GH] guia de otimização
- Nunca publique o IP real: use domínio com SRV e mantenha o site em outro host.

**Git para configs**

Versionar:
- `server.properties`, `bukkit.yml`, `spigot.yml`, `config/paper-*.yml`
- configs dos plugins (YAMLs do MythicMobs, itens, mobs, skills)
- `docker-compose.yml`, scripts
- fontes do resource pack
- uma lista de plugins com versão e hash

Não versionar:
- mundos, logs, `cache/`, `libraries/`
- bancos (`*.db`), dados de jogador
- jars (guarde a lista de versões)
- segredos

Segredos a manter fora do repositório:
- `rcon.password` e `management-server-secret` do `server.properties`
- senhas de banco
- tokens de bot do Discord e de loja
- `key.pem` do Floodgate
- `forwarding.secret` do Velocity
- chaves de licença de plugins pagos

Como o `server.properties` mistura config e segredo, gere-o por template ou por variáveis de ambiente (a imagem itzg faz isso) e mantenha um `.env` fora do Git.

A 26.4 deve fundir os arquivos de config do Paper; espere um diff grande nessa atualização.

**Teste local e deploy**
- Local: o mesmo `docker-compose.yml` com `VERSION` e `PAPER_BUILD` fixos, um mundo pequeno de teste e o plugin próprio montado em `plugins/`. Para iterar no plugin, use o run-task do Gradle.
- Fluxo: branch → teste local → merge → deploy (git pull ou rsync/SFTP das configs + restart em horário anunciado).
- Nunca edite direto em produção. Se editar em emergência, traga a mudança de volta para o Git.
- Atualização de versão do Minecraft: sempre em cópia do mundo antes.

---

## 7. Resource pack

- **Config**: `resource-pack` (URL), `resource-pack-sha1` e `require-resource-pack=true`, que substituiu o antigo `force-resource-pack`. [BUSCA] https://mineguard.pro/en/blog/host-resource-pack-minecraft-server-guide
- **Hash**: sempre preencha o SHA-1. Sem ele o cliente não detecta pack novo de forma confiável. Mude a URL ou o nome do arquivo a cada versão (inclua o hash no nome, por exemplo).
- **Limite de tamanho**: 250 MiB para 1.18+ (100 MiB em 1.15–1.17, 50 MiB antes). [BUSCA] https://mcguide.pages.dev/vanilla/resource-pack e https://feedback.minecraft.net/hc/en-us/community/posts/39169301532301-Increase-the-250mb-server-side-resource-pack-limit-Java
- Alguns hosts ainda documentam 100 MB e não achei a fonte primária. [NÃO CONFIRMADO em fonte oficial]
- Na prática, mire abaixo de 50–80 MB: o público tem celular e internet móvel, e pack grande derruba gente na entrada.
- **Múltiplos packs**: clientes 1.20.3+ empilham vários packs enviados pelo servidor. O `server.properties` aceita uma URL só; empilhar exige API ou plugin. [BUSCA] https://github.com/PaperMC/Paper/issues/10027
- No Paper, o método antigo `setResourcePack` remove os packs anteriores; o caminho indicado é a API do Adventure. [BUSCA] mesma issue
- Não confirmei a assinatura atual da API na 26.3. [NÃO CONFIRMADO]
- Use um pack único mesclado por um passo de build. Plugins como Nexo, ItemsAdder e ModelEngine geram cada um o seu, e é preciso fundir.
- **Hospedagem do pack**: URL HTTPS de download direto, estável e rápida no Brasil. Opções: armazenamento de objetos com CDN, release do GitHub, ou a auto-hospedagem embutida nos plugins de pack (abre mais uma porta no servidor). Evite links de compartilhamento que não entregam o arquivo direto. (Recomendação minha.)
- **Forçar pack**: com `require-resource-pack=true`, quem recusa é desconectado. Para RP com modelos isso é necessário; escreva um prompt em português explicando.
- **Bedrock**: é outro pack, em outro formato, entregue pelo Geyser (seção 3.1).

---

## 8. EULA e diretrizes de uso da Mojang (monetização)

Não consegui abrir o texto oficial (minecraft.net bloqueado). Tudo abaixo é [BUSCA] em fontes secundárias. Leia https://www.minecraft.net/en-us/usage-guidelines antes de abrir a loja.

- As antigas "Commercial Usage Guidelines" não são mais documento separado; viraram uma seção das Usage Guidelines, com subseção de servidores. https://www.gameserverkings.com/knowledge-base/minecraft/monetising-your-server/
- A Mojang pode retirar as permissões a qualquer momento, sem aviso. Mesma fonte.

**Pode**
- Cobrar pelo acesso ao servidor, com preço igual para todos. https://conductatlas.com/platform/minecraft/minecraft-usage-guidelines/
- Aceitar doações opcionais.
- Vender cosméticos e ranks com vantagens cosméticas ou sociais (prefixo, cor de chat, partículas). https://minecraftstore.io/blog/can-you-sell-ranks-on-a-minecraft-server-what-mojangs-rules-allow
- Vender moeda do servidor, desde que não tenha valor real, não seja sacável nem transferível entre servidores, e não se pareça com Minecoins. https://minehut.wiki.gg/wiki/MUG_FAQ
- Vender chaves de caixa, desde que os prêmios não deem vantagem competitiva (fonte secundária única).

**Não pode**
- Vender qualquer coisa que dê vantagem de jogabilidade a quem paga: kits de equipamento, itens fortes, recursos, mesmo embutidos em rank.
- Vender capas ou qualquer coisa que imite capa oficial.
- Parecer oficial da Mojang/Microsoft.

**Obrigação**
- Dar a quem paga em dinheiro real um histórico de compras online.

**Zona cinzenta**
- Fila prioritária e slot reservado: uma fonte lista como vendável, mas está perto da linha de vantagem. [NÃO CONFIRMADO]
- "Boosters" globais que beneficiam todos igualmente: fonte antiga. [NÃO CONFIRMADO]

**Punição**: o servidor pode ser bloqueado pela Mojang (blacklist).

**Implicação para RP**: monetize com cosméticos (skins de item por modelo custom, pets visuais, títulos, emotes, móveis decorativos), slots extras de personagem e conveniências sem efeito em poder. Não venda XP, atributos, itens de combate, dinheiro de economia que compre poder, nem acesso antecipado a classes fortes. Se usar opção C (offline-mode), a situação perante as diretrizes fica mais incerta.

---

## 9. Checklist de setup inicial, em ordem

1. **Decisões de produto** (bloqueiam o resto):
   - Java apenas ou Java + Bedrock;
   - `online-mode` verdadeiro ou falso;
   - versão mínima de cliente.
2. **Compatibilidade**: listar os plugins críticos (MythicMobs, motor de modelos, itens custom, permissões, proteção de região) e conferir suporte de cada um à 26.3. Se faltar algum, começar em 26.2.
3. **Repositório Git** com `.gitignore`, `docker-compose.yml`, `.env.example` e pastas de config e de fontes do pack.
4. **Ambiente local**: `itzg/minecraft-server:java25`, `TYPE=PAPER`, `VERSION` e `PAPER_BUILD` fixos, `EULA=TRUE`. Subir e entrar.
5. **Projeto do plugin**: Gradle Kotlin DSL, Java 25, `paper-api:26.3.build.+` (fixar um build antes de lançar), `plugin.yml`, run-task. Só adicionar `paperweight-userdev` se houver necessidade real de código interno.
6. **Configs base** conforme o guia de otimização; `white-list=true` durante o desenvolvimento.
7. **Plugins base**: LuckPerms, proteção de região, CoreProtect ou equivalente, ViaVersion, Chunky, spark (se não vier embutido). Um de cada vez, com commit a cada passo.
8. **Pipeline do resource pack**: script que mescla, compacta, calcula o SHA-1, publica e atualiza `resource-pack` e `resource-pack-sha1`.
9. **Se Bedrock**: Geyser + Floodgate, porta 19132/udp, pack Bedrock e mapeamentos. Testar em um celular real antes de produzir mais arte.
10. **Contratar o host**: plano de 8 GB mensal (sem fidelidade longa), São Paulo, Ryzen 9, anti-DDoS. Antes de pagar, confirmar acesso SFTP, startup customizável ou Docker, Java 25, porta UDP extra e política de backup.
11. **Domínio e DNS**: registro SRV para o servidor; site e loja em outro host.
12. **Mundo**: definir a borda nas três dimensões e pré-gerar com Chunky.
13. **Backups**: agendar, enviar para fora da máquina e fazer um teste de restauração completo.
14. **Segurança**: senha de RCON forte e porta fechada para fora, 2FA no painel e no host, permissões de operador mínimas, segredos fora do Git.
15. **Teste de carga e beta fechado**: 10–20 pessoas, relatório do spark em horário de pico, ajustar distâncias e mobs.
16. **Loja**: revisar as diretrizes oficiais, vender só cosméticos e habilitar histórico de compras.
17. **Lançamento**: subir o plano para 10–12 GB se o spark indicar necessidade; anunciar janela de manutenção semanal.
18. **Rotina**: atualizar builds do Paper e plugins primeiro em staging; acompanhar a 26.4 por causa da fusão de configs.

---

## 10. Riscos

**Técnicos**
- **Versão recém-estabilizada**: a 26.3 ficou estável no Paper há cerca de um mês e plugins de RPG podem estar atrasados. Mitigação: checar cada plugin e considerar 26.2.
- **Mundo sem downgrade**: atualizar sem backup é irreversível.
- **Ritmo de releases**: três ou mais versões por ano na numeração nova; plugin próprio com código interno quebra em cada uma.
- **`paper-plugin.yml` experimental**: pode mudar entre versões.
- **Fusão de configs na 26.4**: retrabalho no que estiver versionado.
- **Plugin de modelos e Bedrock**: item displays não suportados no Geyser; extensões comunitárias sem garantia.
- **Resource pack pesado**: perda de jogadores na entrada, sobretudo em celular.

**Autenticação e público**
- **Offline-mode**: roubo de identidade, bots, guarda de senhas (LGPD), UUIDs incompatíveis com migração futura, plugins de login com manutenção irregular.
- **Online-mode**: exclui parte grande do público brasileiro. Não há dado para medir o quanto.

**Hospedagem e custo**
- **Preços não verificados diretamente**; promoções com renovação mais cara (Hostinger) e cobrança em dólar (StreetHosting, Nodecraft) expõem ao câmbio.
- **Hosts pequenos brasileiros**: overselling, CPU diferente da anunciada, anti-DDoS que não cobre UDP. Mitigação: contrato mensal e teste com spark na primeira semana.
- **Oracle free tier**: cortado pela metade em 2026 com instâncias desativadas. Não dependa dele.
- **Casa**: CGNAT e exposição do IP.
- **DDoS**: proxies de proteção sem PoP em São Paulo pioram o ping.

**Operação**
- **Pessoa única**: sem redundância humana. Mitigação: tudo em Git, backups automáticos testados, um documento de recuperação e um segundo admin de confiança com acesso de emergência.
- **Vazamento de segredos** no Git (RCON, `key.pem`, tokens).
- **Painéis em transição**: Pterodactyl 2.0 em beta e Pelican em RC; não use nenhum dos dois em produção ainda.

**Legal e comercial**
- **Diretrizes da Mojang**: mudam sem aviso e não foram lidas na fonte nesta pesquisa. Loja com vantagem de jogo pode levar a bloqueio do servidor.
- **Plugins pagos** (MythicMobs premium, ModelEngine, ItemsAdder/Nexo): custo de licença não levantado aqui.

---

## 11. O que ficou sem confirmação

- Número e data do build estável atual do Paper 26.3 (só trechos de busca; datas conflitantes).
- Existência do snapshot 26.4 e de hotfix 26.3.x.
- Status estável do Purpur 26.3.
- Versão atual do `paperweight-userdev` e do Gradle exigido.
- Suporte do MythicMobs, BetterModel, Nexo e ItemsAdder à 26.3.
- Todos os preços de hospedagem; CPU dos planos de 12 e 16 GB da BedHosting; VPS da Hostinger em São Paulo.
- Preços de VPS Ryzen da HypeHost e da MagnoHost para 8–16 GB.
- Capacidade ARM da Oracle em São Paulo e o texto oficial do corte.
- PoP do TCPShield no Brasil.
- Limite oficial de 250 MiB do resource pack e a API atual do Paper para múltiplos packs.
- Se o spark vem embutido no Paper 26.3.
- Prefixo, vínculo de contas e forms do Floodgate.
- Proporção de jogadores brasileiros em Bedrock/celular e em contas não originais.
- Texto atual das Usage Guidelines da Mojang, incluindo a posição sobre offline-mode e fila prioritária.
- Estado do LibreLogin e do nLogin.
