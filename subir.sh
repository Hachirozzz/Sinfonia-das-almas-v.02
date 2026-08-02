#!/usr/bin/env bash
#
# Sobe a Sinfonia das Almas para o GitHub e lança a primeira versão.
#
#   bash subir.sh SEU-USUARIO
#
# Antes de rodar, o repositório VAZIO precisa existir em github.com/new
# com o nome sinfonia-das-almas e marcado como Public.

set -e

USUARIO="$1"
REPO="sinfonia-das-almas"
VERSAO="${2:-v0.1.0}"

if [ -z "$USUARIO" ]; then
  echo "ERRO: faltou o seu usuário do GitHub."
  echo "Uso:  bash subir.sh SEU-USUARIO"
  exit 1
fi

cd "$(dirname "$0")"

echo "════════════════════════════════════════════"
echo " Usuário: $USUARIO"
echo " Repo:    $REPO"
echo " Versão:  $VERSAO"
echo "════════════════════════════════════════════"
echo

# --- 1. Escrever o usuário nos endereços do manifesto ---
if grep -q "SEU-USUARIO" system.json; then
  sed -i "s/SEU-USUARIO/$USUARIO/g" system.json
  git add system.json
  git commit -q -m "Aponta os endereços do manifesto para $USUARIO"
  echo "[1/4] Endereços preenchidos."
else
  echo "[1/4] Endereços já estavam preenchidos."
fi

echo "      manifest: $(grep '"manifest"' system.json | cut -d'"' -f4)"
echo

# --- 2. Ligar este projeto ao repositório do GitHub ---
DESTINO="https://github.com/$USUARIO/$REPO.git"
if git remote get-url origin >/dev/null 2>&1; then
  git remote set-url origin "$DESTINO"
else
  git remote add origin "$DESTINO"
fi
echo "[2/4] Destino: $DESTINO"
echo

# --- 3. Enviar o código ---
echo "[3/4] Enviando... (pode abrir o navegador pedindo login do GitHub)"
git push -u origin main
echo

# --- 4. Criar a tag que dispara a publicação automática ---
if git rev-parse "$VERSAO" >/dev/null 2>&1; then
  echo "[4/4] A tag $VERSAO já existe."
  echo "      Para lançar outra versão:  bash subir.sh $USUARIO v0.2.0"
else
  git tag "$VERSAO"
  git push origin "$VERSAO"
  echo "[4/4] Tag $VERSAO enviada — a publicação começou."
fi

echo
echo "════════════════════════════════════════════"
echo " PRONTO."
echo
echo " Acompanhe em:"
echo "   https://github.com/$USUARIO/$REPO/actions"
echo
echo " Quando terminar (menos de 1 minuto), seu link de instalação é:"
echo
echo "   https://github.com/$USUARIO/$REPO/releases/latest/download/system.json"
echo
echo " Cole no Foundry em: Game Systems > Install System > Manifest URL"
echo "════════════════════════════════════════════"
