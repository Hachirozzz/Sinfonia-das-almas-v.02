#!/usr/bin/env bash
# Deixa a pasta servidor/ pronta para o Paper subir: cria o .env se faltar,
# baixa os plugins da lista, copia as configs versionadas e gera o
# server.properties a partir do modelo. Roda igual no modo Java puro
# (chamado por iniciar.sh) e no Docker (serviço "preparar" do docker-compose).
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"

# --- 1. .env ---
if [ ! -f "$ARQ_ENV" ]; then
  info ".env não existe: criando a partir de .env.exemplo, com senhas aleatórias"
  : > "$ARQ_ENV.novo"; chmod 600 "$ARQ_ENV.novo"
  while IFS= read -r linha || [ -n "$linha" ]; do
    linha="${linha%$'\r'}"
    case "$linha" in
      [A-Z]*=GERAR) printf '%s=%s\n' "${linha%%=*}" "$(gerar_segredo)" ;;
      *) printf '%s\n' "$linha" ;;
    esac
  done < "$RAIZ/.env.exemplo" >> "$ARQ_ENV.novo"
  mv "$ARQ_ENV.novo" "$ARQ_ENV"
  # Dentro do contêiner roda-se como root: devolve o arquivo ao dono da pasta.
  if [ "$(id -u)" -eq 0 ]; then chown "$(stat -c '%u:%g' "$RAIZ")" "$ARQ_ENV" 2>/dev/null || true; fi
fi

ACEITO_EULA="$(ler_env ACEITO_EULA false)"
PORTA="$(ler_env PORTA 25565)"
MOTD="$(ler_env MOTD 'Servidor de RP')"
MAX_JOGADORES="$(ler_env MAX_JOGADORES 60)"
SEMENTE="$(ler_env SEMENTE '')"
PACK_URL="$(ler_env PACK_URL '')"
PACK_SHA1="$(ler_env PACK_SHA1 '')"
SENHA_RCON="$(ler_env SENHA_RCON '')"
SEGREDO_GERENCIAMENTO="$(ler_env SEGREDO_GERENCIAMENTO '')"

[ "$ACEITO_EULA" = "true" ] || erro "ACEITO_EULA precisa ser \"true\" no .env (leia https://aka.ms/MinecraftEULA)."
[[ "$PORTA" =~ ^[0-9]+$ ]] || erro "PORTA inválida no .env: $PORTA"
[[ "$MAX_JOGADORES" =~ ^[0-9]+$ ]] || erro "MAX_JOGADORES inválido no .env: $MAX_JOGADORES"
for par in "SENHA_RCON=$SENHA_RCON" "SEGREDO_GERENCIAMENTO=$SEGREDO_GERENCIAMENTO"; do
  valor="${par#*=}"
  [[ "$valor" =~ ^[A-Za-z0-9_-]{16,}$ ]] || erro \
    "${par%%=*} no .env precisa ter 16 ou mais caracteres, só letras, números, \"_\" e \"-\"."
  [ "$valor" != "GERAR" ] || erro "${par%%=*} ainda está como GERAR no .env."
done

PACK_OBRIGATORIO=false
PACK_AVISO=""
if [ -n "$PACK_URL" ]; then
  [[ "$PACK_URL" =~ ^https:// ]] || erro "PACK_URL precisa começar com https://"
  [[ "$PACK_SHA1" =~ ^[0-9a-fA-F]{40}$ ]] || erro "PACK_SHA1 precisa ser o SHA-1 do pack (40 caracteres hexadecimais)."
  PACK_OBRIGATORIO=true
  PACK_AVISO='{"text":"Este servidor usa um pacote de recursos próprio. Aceite o download para entrar."}'
fi

# --- 2. Pasta do servidor e plugins ---
mkdir -p "$SERVIDOR"
printf '# Aceito em .env (ACEITO_EULA=true). https://aka.ms/MinecraftEULA\neula=true\n' > "$SERVIDOR/eula.txt"
"$RAIZ/scripts/baixar-plugins.sh"

# --- 3. Configs versionadas (tudo de configs/, menos os modelos) ---
( cd "$CONFIGS" && find . -type f ! -name '*.modelo' -print ) | while IFS= read -r arq; do
  arq="${arq#./}"
  mkdir -p "$SERVIDOR/$(dirname "$arq")"
  cp -f "$CONFIGS/$arq" "$SERVIDOR/$arq"
done
info "configs copiadas de configs/ para servidor/"

# --- 4. server.properties a partir do modelo ---
conteudo="$(tr -d '\r' < "$CONFIGS/server.properties.modelo")"
for chave in PORTA MOTD MAX_JOGADORES SEMENTE PACK_URL PACK_SHA1 PACK_OBRIGATORIO PACK_AVISO SENHA_RCON SEGREDO_GERENCIAMENTO; do
  valor="${!chave}"
  conteudo="${conteudo//"{{$chave}}"/"$valor"}"
done
if grep -q '{{' <<< "$conteudo"; then
  erro "sobrou campo sem valor em server.properties.modelo: $(grep -o '{{[^}]*}}' <<< "$conteudo" | sort -u | tr '\n' ' ')"
fi
( umask 077
  printf '%s\n' "$conteudo" | grep -v '^#' | grep -v '^$' > "$SERVIDOR/server.properties"
  # Lido pela imagem itzg (RCON_PASSWORD_FILE) para parar o servidor com segurança.
  printf '%s' "$SENHA_RCON" > "$SERVIDOR/.senha-rcon" )
info "server.properties gerado"

# --- 5. No Docker, entrega a pasta ao usuário que roda o Minecraft ---
if [ -n "${DONO_DOS_DADOS:-}" ] && [ "$(id -u)" -eq 0 ]; then
  chown -R "$DONO_DOS_DADOS" "$SERVIDOR"
fi
info "pronto: $SERVIDOR"
