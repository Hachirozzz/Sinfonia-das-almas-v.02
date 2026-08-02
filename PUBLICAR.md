# Como publicar o sistema online

Objetivo: chegar num link que qualquer pessoa cola no Foundry em
**Game Systems → Install System → Manifest URL** e pronto — instalado.

Você só passa por esta configuração **uma vez**. Depois, lançar uma versão nova
são dois comandos.

---

## Parte 1 — Configuração inicial (uma vez só)

### Passo 1: criar o repositório no GitHub

1. Entre em [github.com/new](https://github.com/new)
2. **Repository name:** `sinfonia-das-almas`
3. Marque **Public**
   *(Private também funciona, mas aí só você consegue instalar — o Foundry
   dos seus jogadores não conseguiria baixar.)*
4. **Não** marque nenhuma das caixinhas de "Initialize this repository"
   (README, .gitignore, license). O projeto aqui já tem tudo.
5. Clique em **Create repository**

### Passo 2: escrever seu usuário nos arquivos

Rode o comando abaixo **trocando `SEU-USUARIO`** pelo seu nome de usuário do
GitHub (o que aparece no endereço do seu perfil):

```bash
sed -i "s/SEU-USUARIO/coloque-seu-usuario-aqui/g" system.json && grep manifest system.json
```

A última linha impressa deve mostrar o endereço já com o seu usuário.

> Isso é só cosmético — o robô do Passo 4 preenche os endereços corretos sozinho
> a cada versão. Mas deixa o arquivo coerente para quem for ler.

### Passo 3: enviar o código

Rode um de cada vez, trocando `SEU-USUARIO`:

```bash
git add .
```

```bash
git commit -m "Primeira versão da Sinfonia das Almas"
```

```bash
git remote add origin https://github.com/SEU-USUARIO/sinfonia-das-almas.git
```

```bash
git push -u origin main
```

> **Vai pedir login.** No Windows abre uma janela do navegador para você entrar
> na sua conta GitHub. Isso acontece só na primeira vez.

### Passo 4: lançar a versão 0.1.0

```bash
git tag v0.1.0
```

```bash
git push origin v0.1.0
```

Agora vá ao seu repositório no GitHub, aba **Actions**. Você vai ver
"Publicar versão" rodando. Leva menos de um minuto.

Quando terminar, aparece uma **Release** com dois arquivos: `system.json` e
`system.zip`.

### Passo 5: seu link de instalação

```
https://github.com/SEU-USUARIO/sinfonia-das-almas/releases/latest/download/system.json
```

Esse é o link. Cole no Foundry em **Game Systems → Install System → Manifest URL**.

Ele aponta sempre para a versão mais recente — você nunca precisa trocá-lo.

---

## Parte 2 — Lançar uma atualização

Toda vez que quiser publicar mudanças:

```bash
git add . && git commit -m "descreva o que mudou"
```

```bash
git push
```

```bash
git tag v0.2.0 && git push origin v0.2.0
```

Só isso. O Foundry de quem já instalou vai mostrar **"Update Available"** sozinho.

### Que número usar na versão?

O padrão é `vMAIOR.MENOR.CORREÇÃO`:

| Situação | Exemplo |
| --- | --- |
| Consertou um bug | `v0.1.0` → `v0.1.1` |
| Adicionou algo novo (uma aba, magias) | `v0.1.1` → `v0.2.0` |
| Mudou a estrutura e fichas antigas quebram | `v0.9.0` → `v1.0.0` |

**Nunca reutilize uma tag.** Cada versão precisa de um número novo, senão o
Foundry não percebe que houve atualização.

---

## Detalhes que valem saber

**Licença.** O repositório está sem arquivo de licença. Sem isso, legalmente
ninguém pode redistribuir seu sistema — o que talvez seja exatamente o que você
quer. Se um dia quiser abrir, a MIT é a mais comum nesse meio.

**Repositório privado não funciona para instalar.** O Foundry baixa o zip sem
estar logado na sua conta. Se o repositório for privado, o download falha.

**Se a aba Actions mostrar erro vermelho**, clique no passo que falhou e me
mande o texto — normalmente é uma bobagem de nome de arquivo.

**Testar antes de publicar** continua sendo pelo atalho simbólico descrito no
[LEIA-ME.md](LEIA-ME.md). Publique só quando estiver funcionando na sua máquina.
