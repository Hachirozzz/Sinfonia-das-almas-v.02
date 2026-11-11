#!/usr/bin/env bash
# Baixa o Paper fixado em versoes.env para .ferramentas/, conferindo o sha256.
# Imprime na última linha o caminho do jar.
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"
carregar_versoes

JAR="$FERRAMENTAS/paper-$VERSION-$PAPER_BUILD.jar"
baixar_conferido "$PAPER_JAR_URL" "$JAR" sha256 "$PAPER_JAR_SHA256"
echo "$JAR"
