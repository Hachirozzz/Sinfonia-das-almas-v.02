#!/usr/bin/env bash
# Para o servidor iniciado por iniciar.sh, de forma limpa (comando "stop":
# desliga os plugins e salva o mundo antes de sair).
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"

if ! servidor_rodando; then
  info "o servidor não está rodando"
  rm -f "$ARQ_PID" "$ARQ_CONSOLE"
  exit 0
fi
PID="$(cat "$ARQ_PID")"

esperar_sair() {
  local segundos="$1" i
  for ((i = 0; i < segundos; i++)); do
    kill -0 "$PID" 2>/dev/null || return 0
    sleep 1
  done
  return 1
}

info "mandando \"stop\" para o servidor (PID $PID)"
echo "stop" > "$ARQ_CONSOLE"
if ! esperar_sair 120; then
  info "não parou em 120 s com \"stop\"; mandando SIGTERM (o Paper também salva ao receber)"
  kill -TERM "$PID" 2>/dev/null || true
  esperar_sair 120 || erro "o servidor não parou. PID $PID continua vivo; investigue antes de matar à força."
fi
rm -f "$ARQ_PID" "$ARQ_CONSOLE"
info "servidor parado"
