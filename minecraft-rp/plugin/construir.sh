#!/usr/bin/env bash
# Compila e testa o plugin com o Java 25 que os scripts do servidor baixam em
# ../.ferramentas/ (o Gradle roda com o Java do sistema e usa o 25 como toolchain).
# Uso: ./construir.sh [tarefas do gradle; padrão: build]
set -euo pipefail
AQUI="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -z "${JAVA25_HOME:-}" ]; then
  JAVA25_HOME="$(dirname "$(dirname "$("$AQUI/../scripts/baixar-java.sh" | tail -n 1)")")"
fi
[ -x "$JAVA25_HOME/bin/javac" ] || { echo "Java 25 não encontrado em $JAVA25_HOME" >&2; exit 1; }
[ "$#" -gt 0 ] || set -- build
cd "$AQUI"
exec gradle \
  -Porg.gradle.java.installations.paths="$JAVA25_HOME" \
  -Porg.gradle.java.installations.auto-download=false "$@"
