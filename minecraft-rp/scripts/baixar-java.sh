#!/usr/bin/env bash
# Baixa o Java 25 fixado em versoes.env para .ferramentas/ (modo Java puro).
# Imprime na última linha o caminho do executável "java".
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"
carregar_versoes

if [ -n "${JAVA_BIN:-}" ]; then
  [ -x "$JAVA_BIN" ] || erro "JAVA_BIN aponta para algo que não é executável: $JAVA_BIN"
  echo "$JAVA_BIN"; exit 0
fi

[ "$(uname -s)-$(uname -m)" = "Linux-x86_64" ] || erro \
  "o download automático do Java só cobre Linux x64. Instale um Java 25 e rode com JAVA_BIN=/caminho/do/java, ou use o Docker."

DIR="$FERRAMENTAS/java-$JAVA25_VERSAO"
if [ ! -x "$DIR/bin/java" ]; then
  PACOTE="$FERRAMENTAS/java-$JAVA25_VERSAO.tar.gz"
  baixar_conferido "$JAVA25_URL" "$PACOTE" sha256 "$JAVA25_SHA256"
  rm -rf "$DIR.extraindo"; mkdir -p "$DIR.extraindo"
  tar -xzf "$PACOTE" -C "$DIR.extraindo" --strip-components=1
  rm -rf "$DIR"; mv "$DIR.extraindo" "$DIR"
  rm -f "$PACOTE"
  info "Java $JAVA25_VERSAO instalado em .ferramentas/"
fi
echo "$DIR/bin/java"
