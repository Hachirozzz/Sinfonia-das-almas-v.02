#!/usr/bin/env bash
# Baixa para servidor/plugins/ os plugins de plugins.lista, conferindo o hash.
# Remove jars que este script instalou antes e que saíram da lista (ou mudaram
# de nome). Jars colocados à mão não são tocados.
. "$(dirname "${BASH_SOURCE[0]}")/comum.sh"

LISTA="$RAIZ/plugins.lista"
DESTINO="$SERVIDOR/plugins"
CONTROLE="$DESTINO/.instalados-pela-lista"
mkdir -p "$DESTINO"

novos=()
while read -r nome versao arquivo hash url pagina resto; do
  case "$nome" in ''|'#'*) continue ;; esac
  if [ -z "${pagina:-}" ] || [ -n "${resto:-}" ]; then
    erro "linha inválida em plugins.lista (esperados 6 campos): $nome"
  fi
  case "$arquivo" in */*|.*) erro "nome de arquivo inválido em plugins.lista: $arquivo" ;; esac
  case "$arquivo" in *.jar) ;; *) erro "o arquivo de $nome precisa terminar em .jar" ;; esac
  [ "${#hash}" -eq 128 ] || erro "o hash de $nome não é um sha512 (128 caracteres)"
  baixar_conferido "$url" "$DESTINO/$arquivo" sha512 "$hash"
  info "plugin ok: $nome $versao ($arquivo)"
  novos+=("$arquivo")
done < <(tr -d '\r' < "$LISTA")

if [ -f "$CONTROLE" ]; then
  while read -r antigo; do
    [ -n "$antigo" ] || continue
    manter=0
    for n in "${novos[@]:-}"; do [ "$n" = "$antigo" ] && manter=1; done
    if [ "$manter" -eq 0 ] && [ -f "$DESTINO/$antigo" ]; then
      rm -f "$DESTINO/$antigo"
      info "removido (saiu da lista): $antigo"
    fi
  done < "$CONTROLE"
fi
printf '%s\n' "${novos[@]:-}" > "$CONTROLE"

for jar in "$DESTINO"/*.jar; do
  [ -e "$jar" ] || continue
  conhecido=0
  for n in "${novos[@]:-}"; do [ "$n" = "$(basename "$jar")" ] && conhecido=1; done
  [ "$conhecido" -eq 1 ] || info "aviso: $(basename "$jar") está em plugins/ mas não em plugins.lista"
done
