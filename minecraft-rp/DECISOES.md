# Decisões do projeto

Fonte única de verdade para a equipe de agentes. O que está aqui foi decidido
pelo dono do servidor. Para mudar, edite este arquivo primeiro.

## Decidido

| Assunto | Decisão |
| --- | --- |
| Projeto | Servidor de Minecraft de roleplay, do zero, sem aproveitar projetos anteriores |
| Idioma e público | Português, público brasileiro |
| Nome do servidor | Crônicas de Serenthia |
| Tema | Alta fantasia: mundo original chamado Serenthia, com magia presente e várias raças |
| Plataforma | Só Java (sem Bedrock) |
| Contas | Originais e não originais (modo offline com plugin de login) |
| Estilo de RP | Por texto, com chat por distância. Voz por proximidade opcional |
| Cliente | Nenhum mod obrigatório. Customização visual só por resource pack do servidor |
| Escala inicial | Uma pessoa mantendo, meta de 20 a 60 jogadores simultâneos |

## Stack técnica (vinda da pesquisa de novembro de 2026)

| Assunto | Decisão |
| --- | --- |
| Software | Paper, build estável fixado por número. Sem proxy, sem Folia, sem mods no servidor |
| Versão do Minecraft | A mais recente que **todos** os plugins do kit inicial suportam (26.3 se já suportarem, senão 26.2). **Escolhida em 11/nov/2026: 26.2, Paper build 135** (motivo e ressalva logo abaixo) |
| Java | 25 |
| Empacotamento | `docker-compose` com a imagem `itzg/minecraft-server` para a máquina do dono e para produção, mais scripts que sobem o mesmo servidor com Java puro (para teste onde não há Docker) |
| Plugins pagos | Nenhum no início. Qualquer compra passa pelo dono |
| Chat | Um único plugin formata e roteia o chat |
| Plugin próprio | Um só, o "núcleo de RP": personagens, nome de RP, testes com dados. Tudo o mais vem de plugins prontos |
| Identidade do jogador | Em modo offline o UUID deriva do nick. O plugin próprio e as configs não podem assumir UUID da Mojang |
| Monetização | Só cosmético e conveniência, nunca poder dentro do RP |
| Hospedagem | Plano gerenciado em São Paulo, 8 GB para começar, contrato mensal. Só contratar depois do servidor local funcionando |

### Versão escolhida: Minecraft 26.2, Paper build 135 (registro do `mc-infra`, 11/nov/2026)

Fixada em `versoes.env`, com URL e sha256 vindos de `https://fill.papermc.io/v3/projects/paper/versions/26.2/builds`.

- **Por que não 26.3:** o CoreProtect 24.1 (o mais novo no Modrinth e no Hangar) só declara até 26.2 e, testado no Paper 26.3 build 248, recusa-se a ligar ("Minecraft 26.3 is not supported"). Pela regra acima, isso leva à 26.2.
- **Suporte declarado à 26.2:** LuckPerms 5.5.71, CoreProtect 24.1, FastAsyncWorldEdit 2.16.2, PlaceholderAPI 2.12.3, TAB 6.2.1, AuthMe 6.0.1 e PacketEvents 2.14.0. WorldGuard: usar a 7.0.19 (declara 26.2 e 26.3); a 7.0.21 só carrega em 26.3.
- **Exceção, EssentialsX:** a 2.22.0 (última versão estável, de maio) declara só 26.1.2. Carrega na 26.2 e na 26.3, mas escreve "You are running an unsupported server version!" no log. Não existe versão estável que declare 26.2 nem 26.3.
- **Ressalva importante:** o Paper marcou a 26.2 como **sem suporte desde 03/nov/2026**. O build 135 é o último e não recebe mais correções; a cada inicialização o log avisa que há versão mais nova. É aceitável durante o desenvolvimento com whitelist, não para abrir ao público.
- **Quando subir para 26.3:** assim que o CoreProtect publicar versão com suporte (ou o dono decidir lançar sem ele). A troca é editar `versoes.env` e as linhas de `plugins.lista`. O mundo sobe de 26.2 para 26.3, mas não volta: fazer backup antes.

### Login (registro do `mc-infra`)

- **Plugin:** AuthMe Reloaded 6.0.1 (versão para Paper) com PacketEvents 2.14.0. Foi o único candidato com versão estável, mantido e declarando a versão escolhida: LibreLoginProd 0.25.11 e OpeNLogin 2.0.0 são beta/alfa, nLogin e FastLogin não estão no Modrinth nem no Hangar.
- Senha com hash Argon2id, banco SQLite em `servidor/plugins/AuthMe/authme.db`.
- Conta original entra sem senha depois de digitar `/premium` uma vez. O UUID continua sendo o offline.
- A janela de login anterior à entrada no mundo fica desligada: nela, senha errada não contava para o limite de tentativas (testado). O motivo está comentado em `configs/plugins/AuthMe/config.yml`.

### Kit inicial de plugins (todos gratuitos, a confirmar versão por versão)

- Administração: LuckPerms, EssentialsX (sem o módulo de chat), CoreProtect, LibertyBans, PlaceholderAPI, TAB, SkinsRestorer
- Login: plugin de login com senha e login automático para conta original
- RP: chat por distância, ficha de personagem, dados, sentar e deitar
- Mundo: WorldGuard, FastAsyncWorldEdit, Chunky
- Depois, quando a lore pedir: mobs e itens customizados, NPCs e missões, mapa web, voz por proximidade, ponte com Discord

## Pesquisa

Os quatro relatórios completos estão em `pesquisa/`. A rede bloqueou a maioria
dos sites durante a pesquisa, então muita coisa lá veio de trechos de busca:
confira na fonte antes de tratar preço ou versão como fato.

## Em aberto

- Hospedagem: qual empresa e qual plano
- Modelo de entrada de jogadores: a proposta é formulário curto no Discord, com entradas em turmas (o `mc-comunidade` detalha, o dono decide)

## Prioridade atual (definida pelo dono)

Foco total na estrutura e no funcionamento do servidor: base, login, plugins,
plugin próprio e testes. Regulamento, privacidade, loja e demais textos de
comunidade ficam como estão, sem novas rodadas por enquanto.

A lore em `lore/` foi escrita antes da escolha do tema e descreve o conceito
"Vazante", que **não** foi adotado. Ela será reescrita para Serenthia depois que
o servidor estiver funcionando. Até lá, as regras em `sistemas/` valem para
qualquer mundo e `sistemas/vazante.md` serve só de exemplo.
