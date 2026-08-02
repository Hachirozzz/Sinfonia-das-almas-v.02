#!/usr/bin/env bash
#
# Lança uma versão nova da Sinfonia das Almas.
#
#   bash publicar.sh v0.2.0
#
# Envia o que estiver pendente, cria a tag e dispara a publicação automática
# no GitHub. Em menos de um minuto o link de instalação já serve a versão nova.

set -e

VERSAO="$1"
REPO="Hachirozzz/Sinfonia-das-almas-v.02"

if [ -z "$VERSAO" ]; then
  echo "ERRO: faltou o número da versão."
  echo "Uso:  bash publicar.sh v0.2.0"
  echo
  echo "Versões já publicadas:"
  git tag -l "v*" | sort -V | sed 's/^/  /' || echo "  (nenhuma ainda)"
  exit 1
fi

case "$VERSAO" in
  v*) ;;
  *) echo "ERRO: a versão precisa começar com 'v'. Exemplo: v0.2.0"; exit 1 ;;
esac

cd "$(dirname "$0")"

if git rev-parse "$VERSAO" >/dev/null 2>&1; then
  echo "ERRO: a tag $VERSAO já existe. Cada versão precisa de um número novo,"
  echo "senão o Foundry não percebe que houve atualização."
  exit 1
fi

echo "════════════════════════════════════════════"
echo " Publicando $VERSAO"
echo "════════════════════════════════════════════"
echo

# --- 1. Guardar o que estiver pendente ---
if [ -n "$(git status --porcelain)" ]; then
  echo "[1/3] Há mudanças não salvas. Descreva o que mudou:"
  read -r MENSAGEM
  git add .
  git commit -q -m "${MENSAGEM:-Ajustes para $VERSAO}"
  echo "      Salvo."
else
  echo "[1/3] Nada pendente."
fi

# --- 2. Enviar o código ---
echo "[2/3] Enviando o código..."
git push -q origin main
echo "      Enviado."

# --- 3. Criar a tag que dispara a publicação ---
git tag "$VERSAO"
git push -q origin "$VERSAO"
echo "[3/3] Tag $VERSAO enviada."

echo
echo "════════════════════════════════════════════"
echo " Acompanhe em:"
echo "   https://github.com/$REPO/actions"
echo
echo " Link de instalação (não muda nunca):"
echo "   https://github.com/$REPO/releases/latest/download/system.json"
echo "════════════════════════════════════════════"
