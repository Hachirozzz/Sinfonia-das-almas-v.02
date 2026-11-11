#!/usr/bin/env bash
# Sobe o servidor com Java puro (sem Docker), em segundo plano, e só devolve o
# terminal quando a inicialização termina. Um comando, do zero:
#
#   ./scripts/iniciar.sh
#
# Parar: ./scripts/parar.sh      Comando no console: ./scripts/console.sh "list"
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"
carregar_versoes

if servidor_rodando; then
  info "o servidor já está rodando (PID $(cat "$ARQ_PID")). Para parar: ./scripts/parar.sh"
  exit 0
fi

"$RAIZ/scripts/preparar.sh"
JAVA="$("$RAIZ/scripts/baixar-java.sh" | tail -n 1)"
JAR="$("$RAIZ/scripts/baixar-paper.sh" | tail -n 1)"
MEMORIA="$(ler_env MEMORIA 4G)"
PORTA="$(ler_env PORTA 25565)"
ESPERA="${ESPERA_MAXIMA:-600}"

cd "$SERVIDOR"
# O console do servidor lê de um FIFO: é por ele que parar.sh manda "stop".
rm -f "$ARQ_CONSOLE"; mkfifo -m 600 "$ARQ_CONSOLE"
LOG="$SERVIDOR/logs/latest.log"
mkdir -p "$SERVIDOR/logs"; rm -f "$LOG"

info "iniciando Paper $VERSION build $PAPER_BUILD com $MEMORIA de memória"
# shellcheck disable=SC2086
nohup "$JAVA" -Xms"$MEMORIA" -Xmx"$MEMORIA" $JVM_XX_OPTS -jar "$JAR" --nogui \
  0<>"$ARQ_CONSOLE" > "$SERVIDOR/.saida-console.log" 2>&1 &
echo $! > "$ARQ_PID"

for ((i = 0; i < ESPERA; i++)); do
  if ! servidor_rodando; then
    tail -n 40 "$SERVIDOR/.saida-console.log" >&2 || true
    rm -f "$ARQ_PID" "$ARQ_CONSOLE"
    erro "o servidor encerrou durante a inicialização. Veja a saída acima e servidor/logs/latest.log."
  fi
  if [ -f "$LOG" ] && grep -q 'Done (' "$LOG"; then
    info "$(grep -m1 'Done (' "$LOG")"
    # Alguns avisos (checagem de versão do Paper e de plugins) saem logo depois do "Done".
    sleep 10
    "$RAIZ/scripts/verificar-log.sh" || true
    info "servidor no ar na porta $PORTA. No jogo: Multijogador > Conexão direta > localhost:$PORTA"
    exit 0
  fi
  sleep 1
done
erro "o servidor não terminou de iniciar em $ESPERA segundos. Veja servidor/logs/latest.log."
