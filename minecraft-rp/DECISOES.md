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
| Versão do Minecraft | A mais recente que **todos** os plugins do kit inicial suportam (26.3 se já suportarem, senão 26.2). O `mc-infra` confere e registra aqui a versão escolhida |
| Java | 25 |
| Empacotamento | `docker-compose` com a imagem `itzg/minecraft-server` para a máquina do dono e para produção, mais scripts que sobem o mesmo servidor com Java puro (para teste onde não há Docker) |
| Plugins pagos | Nenhum no início. Qualquer compra passa pelo dono |
| Chat | Um único plugin formata e roteia o chat |
| Plugin próprio | Um só, o "núcleo de RP": personagens, nome de RP, testes com dados. Tudo o mais vem de plugins prontos |
| Identidade do jogador | Em modo offline o UUID deriva do nick. O plugin próprio e as configs não podem assumir UUID da Mojang |
| Monetização | Só cosmético e conveniência, nunca poder dentro do RP |
| Hospedagem | Plano gerenciado em São Paulo, 8 GB para começar, contrato mensal. Só contratar depois do servidor local funcionando |

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
