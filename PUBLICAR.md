# Como publicar o sistema

Repositório: **[Hachirozzz/Sinfonia-das-almas-v.02](https://github.com/Hachirozzz/Sinfonia-das-almas-v.02)**

Link de instalação (esse endereço **nunca muda** — aponta sempre para a versão
mais recente):

```
https://github.com/Hachirozzz/Sinfonia-das-almas-v.02/releases/latest/download/system.json
```

No Foundry: **Game Systems → Install System → Manifest URL**.

---

## Lançar uma versão

```bash
bash publicar.sh v0.2.0
```

O script envia o que estiver pendente, cria a tag e dispara a publicação
automática. Se houver mudanças não salvas, ele pergunta o que mudou.

Em menos de um minuto a Release está no ar, e quem já tem o sistema instalado
vê **"Update Available"** sozinho.

Acompanhe em: [Actions](https://github.com/Hachirozzz/Sinfonia-das-almas-v.02/actions)

### Que número usar

O padrão é `vMAIOR.MENOR.CORREÇÃO`:

| Situação | Exemplo |
| --- | --- |
| Consertou um bug | `v0.1.0` → `v0.1.1` |
| Adicionou algo novo (uma aba, magias) | `v0.1.1` → `v0.2.0` |
| Mudou a estrutura e fichas antigas quebram | `v0.9.0` → `v1.0.0` |

**Nunca reutilize uma tag.** Cada versão precisa de um número novo, senão o
Foundry não percebe que houve atualização. O script barra isso automaticamente.

---

## O que acontece por baixo

Quando uma tag `v*` chega no GitHub, o robô em
[`.github/workflows/release.yml`](.github/workflows/release.yml):

1. Preenche `version`, `manifest` e `download` dentro do `system.json` com os
   endereços corretos daquela versão
2. Empacota `system.json`, `LEIA-ME.md`, `module/`, `templates/` e `css/` num
   `system.zip` — com o `system.json` na raiz, que é onde o Foundry procura
3. Publica os dois arquivos numa Release

Você nunca precisa editar esses endereços na mão.

---

## Detalhes que valem saber

**Teste antes de publicar.** Use o atalho simbólico descrito no
[LEIA-ME.md](LEIA-ME.md) e confirme na sua máquina que a ficha abre. O link é
público no instante em que a Release sai.

**O repositório precisa continuar Public.** O Foundry baixa o zip sem estar
logado — se virar privado, a instalação quebra para todo mundo.

**Licença.** Não há arquivo de licença no repositório. Sem isso, formalmente
ninguém pode redistribuir o sistema — o que talvez seja o que você quer. Se um
dia quiser abrir, a MIT é a mais comum nesse meio.

**Se a aba Actions ficar vermelha**, clique no passo que falhou e leia as
últimas linhas — costuma ser nome de arquivo errado.
