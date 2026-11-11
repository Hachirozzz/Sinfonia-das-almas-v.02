#!/usr/bin/env bash
# Manda um comando para o console do servidor (modo Java puro) e mostra o fim
# do log. Exemplo: ./scripts/console.sh "whitelist add Fulano"
# No Docker use: docker compose exec mc rcon-cli "whitelist add Fulano"
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"

[ "$#" -ge 1 ] || erro "uso: ./scripts/console.sh \"comando\""
servidor_rodando || erro "o servidor não está rodando"
linhas_antes="$(wc -l < "$SERVIDOR/logs/latest.log")"
echo "$*" > "$ARQ_CONSOLE"
sleep 2
tail -n +"$((linhas_antes + 1))" "$SERVIDOR/logs/latest.log"
