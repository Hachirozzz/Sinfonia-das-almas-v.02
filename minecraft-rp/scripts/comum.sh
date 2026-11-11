#!/usr/bin/env bash
# Funções e caminhos usados pelos outros scripts. Não é para rodar sozinho.
set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVIDOR="${SERVIDOR:-$RAIZ/servidor}"
FERRAMENTAS="$RAIZ/.ferramentas"
CONFIGS="$RAIZ/configs"
ARQ_ENV="$RAIZ/.env"
ARQ_PID="$SERVIDOR/.servidor.pid"
ARQ_CONSOLE="$SERVIDOR/.console"

info() { printf '[infra] %s\n' "$*" >&2; }
erro() { printf '[infra] ERRO: %s\n' "$*" >&2; exit 1; }

# Carrega as versões fixadas (versoes.env).
carregar_versoes() {
  set -a
  # shellcheck disable=SC1091
  . "$RAIZ/versoes.env"
  set +a
}

# Lê uma chave do .env sem executar o arquivo (o valor pode ter espaços).
ler_env() {
  local chave="$1" padrao="${2-}" linha
  linha="$(grep -m1 -E "^${chave}=" "$ARQ_ENV" 2>/dev/null || true)"
  if [ -n "$linha" ]; then
    linha="${linha#*=}"
    printf '%s' "${linha%$'\r'}"
  else
    printf '%s' "$padrao"
  fi
}

# Senha aleatória de 32 caracteres (letras e números).
gerar_segredo() {
  local s=""
  while [ "${#s}" -lt 32 ]; do
    s="$s$(head -c 64 /dev/urandom | base64 | tr -dc 'A-Za-z0-9')"
  done
  printf '%s' "${s:0:32}"
}

# conferir_hash <arquivo> <sha256|sha512> <hash esperado>
conferir_hash() {
  local arquivo="$1" algoritmo="$2" esperado="$3" obtido
  obtido="$("${algoritmo}sum" "$arquivo" | cut -d' ' -f1)"
  [ "$obtido" = "$esperado" ]
}

# baixar_conferido <url> <destino> <sha256|sha512> <hash esperado>
# Não baixa de novo se o arquivo já existe com o hash certo. Arquivo com hash
# errado nunca fica no destino.
baixar_conferido() {
  local url="$1" destino="$2" algoritmo="$3" esperado="$4" tmp
  if [ -f "$destino" ] && conferir_hash "$destino" "$algoritmo" "$esperado"; then
    return 0
  fi
  mkdir -p "$(dirname "$destino")"
  tmp="$destino.baixando"
  info "baixando $(basename "$destino")"
  curl --fail --silent --show-error --location --retry 3 \
    --user-agent "servidor-minecraft-scripts" --output "$tmp" "$url" \
    || { rm -f "$tmp"; erro "falha ao baixar $url"; }
  if ! conferir_hash "$tmp" "$algoritmo" "$esperado"; then
    rm -f "$tmp"
    erro "hash $algoritmo de $(basename "$destino") não confere com o esperado. Download descartado."
  fi
  mv -f "$tmp" "$destino"
}

# Devolve 0 se o servidor (modo Java puro) está rodando.
servidor_rodando() {
  [ -f "$ARQ_PID" ] && kill -0 "$(cat "$ARQ_PID")" 2>/dev/null
}
