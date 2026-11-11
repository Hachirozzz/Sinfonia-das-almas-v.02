# Crônicas de Serenthia: como subir o servidor

Servidor Paper 26.2 em modo offline (aceita conta original e não original), com
login por senha (AuthMe). As decisões estão em `DECISOES.md`.

## No seu computador (Windows, com Docker Desktop)

1. Instale o [Docker Desktop](https://www.docker.com/products/docker-desktop/) e deixe-o aberto.
2. Abra o PowerShell dentro da pasta `minecraft-rp` e rode:

   ```
   docker compose up -d
   docker compose logs -f mc
   ```

   A primeira vez demora alguns minutos (baixa a imagem, o Paper e os plugins).
   Está pronto quando aparecer `Done (...)! For help, type "help"`. Saia do log com Ctrl+C.
3. Libere o seu nick na lista de acesso (troque `SeuNick`, respeitando maiúsculas):

   ```
   docker compose exec mc rcon-cli whitelist add SeuNick
   ```

Parar: `docker compose down`. Subir de novo: `docker compose up -d`.

Na primeira subida é criado o arquivo `.env`, com senhas aleatórias. Ele não vai
para o Git. É nele que se muda memória, porta e o texto da lista de servidores.

> O `docker-compose.yml` ainda não foi testado numa máquina com Docker. Se algo
> falhar na primeira subida, guarde a saída de `docker compose logs`.

## Entrando pelo jogo

1. No launcher, escolha a versão **26.2** do Minecraft Java.
2. Multijogador > Conexão direta > `localhost`.
3. Na primeira entrada aparece a janela de **registro**: crie uma senha de 8
   caracteres ou mais (não use uma senha que você usa em outro lugar). Nas
   próximas vezes aparece a janela de **login**. Sem janela, os comandos são
   `/register <senha> <senha>` e `/login <senha>`.
4. Conta original pode pular a senha: depois de logado, digite `/premium` uma
   vez. Para desfazer, `/freemium`.
5. Só depois de registrar a sua senha, dê a si mesmo o cargo de operador:

   ```
   docker compose exec mc rcon-cli op SeuNick
   ```

Outros comandos úteis, sempre com `docker compose exec mc rcon-cli` na frente:
`whitelist add Nick`, `whitelist remove Nick`,
`authme register Nick SenhaTemporaria` (cria a conta de alguém antes de ele entrar),
`authme password Nick NovaSenha` (senha esquecida) e `authme unregister Nick` (apaga a conta).

## Sem Docker (Linux, para teste)

```
./scripts/iniciar.sh                         # sobe do zero e espera a inicialização
./scripts/console.sh "whitelist add SeuNick"
./scripts/parar.sh                           # para salvando o mundo
```

## Onde fica cada coisa

| Caminho | O que é |
| --- | --- |
| `versoes.env` | Versão do Minecraft, build do Paper, Java e flags, com hash |
| `plugins.lista` | Plugins instalados: nome, versão, arquivo, hash, link |
| `configs/` | Configurações versionadas. São copiadas para `servidor/` a cada subida, então edite sempre aqui |
| `configs/server.properties.modelo` | Modelo do `server.properties` (os segredos vêm do `.env`) |
| `configs/world/datapacks/base-servidor/` | Borda do mundo (6000 blocos de largura) |
| `scripts/` | Preparar, iniciar, parar, console e conferência do log |
| `servidor/` | O servidor em execução: mundo, contas, logs. Fora do Git |
| `.env` | Senhas e ajustes locais. Fora do Git |

Para apagar tudo e recomeçar do zero: pare o servidor e apague a pasta `servidor/`
(isso apaga o mundo e as contas).
