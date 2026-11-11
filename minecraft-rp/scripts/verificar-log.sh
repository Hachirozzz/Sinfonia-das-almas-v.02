#!/usr/bin/env bash
# Procura avisos e erros no log do servidor. Sai com 0 se só houver os avisos
# esperados; sai com 1 e mostra as linhas se houver qualquer outro.
# Uso: ./scripts/verificar-log.sh [arquivo de log]
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"

LOG="${1:-$SERVIDOR/logs/latest.log}"
[ -f "$LOG" ] || erro "log não encontrado: $LOG"

# Avisos esperados por decisão do projeto: o servidor roda em modo offline
# (DECISOES.md) e o próprio Minecraft avisa disso a cada inicialização.
ESPERADOS='SERVER IS RUNNING IN OFFLINE/INSECURE MODE|The server will make no attempt to authenticate usernames|While this makes the game possible to play without internet access|To change this, set "online-mode" to "true"'

achados="$(grep -nE '(WARN|ERROR|FATAL)\]|Exception|SEVERE' "$LOG" | grep -vE "$ESPERADOS" || true)"
if [ -z "$achados" ]; then
  info "log sem erros nem avisos (fora o aviso de modo offline, que é esperado)"
  exit 0
fi
info "o log tem avisos ou erros que precisam ser olhados:"
printf '%s\n' "$achados" >&2
exit 1
