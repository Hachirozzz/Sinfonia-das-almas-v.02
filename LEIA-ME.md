# Sinfonia das Almas — sistema para Foundry VTT

Versão 0.1.0 · Foundry v13+

---

## 1. Instalar

1. Descubra sua pasta de dados do Foundry. No Windows costuma ser:
   `C:\Users\Hachi\AppData\Local\FoundryVTT\Data`
   (na tela inicial do Foundry: **Configuration** → o caminho aparece no topo)

2. Copie a pasta `sinfonia-das-almas` inteira para dentro de `Data\systems\`.

3. Reinicie o Foundry. O sistema aparece na aba **Game Systems**.

4. Crie um mundo novo escolhendo "Sinfonia das Almas".

5. Crie um Ator do tipo **personagem**.

### Melhor: um atalho em vez de copiar

Assim você edita os arquivos aqui na Área de Trabalho e o Foundry vê a mudança
na hora. Abra o **PowerShell como Administrador** e rode:

```bash
New-Item -ItemType SymbolicLink -Path "$env:LOCALAPPDATA\FoundryVTT\Data\systems\sinfonia-das-almas" -Target "C:\Users\Hachi\Desktop\RPG Automações teste\sinfonia-das-almas"
```

---

## 2. Ciclo de trabalho

| Tecla | O que faz |
| --- | --- |
| **F5** | Recarrega o Foundry depois de você editar um arquivo |
| **F12** | Abre o Console — é onde os erros aparecem |

Se a ficha abrir em branco, o motivo está no Console, em vermelho.

---

## 3. Onde mexer em quê

| Quero mudar... | Arquivo |
| --- | --- |
| Perícias, atributos, graus, escolas, condições, constantes de regra | `module/config.mjs` |
| Quais campos o personagem tem, e as fórmulas de PV/PE/Defesa/ND | `module/data/personagem.mjs` |
| Quais campos armas, magias e habilidades têm | `module/data/itens.mjs` |
| Como a rolagem funciona (Empenho, crítico, Corrupção) | `module/documents/actor.mjs` |
| O que acontece ao usar um item ou conjurar | `module/documents/item.mjs` |
| Botões e comportamento da ficha | `module/sheets/ficha-personagem.mjs` |
| O visual da ficha | `templates/actor/personagem.hbs` |
| O visual da ficha de item | `templates/item/item.hbs` |
| O card que aparece no chat | `templates/chat/rolagem.hbs` |
| Cores e layout | `css/sinfonia.css` |

**Regra prática:** mudança de *regra* → `module/`. Mudança de *aparência* → `templates/` ou `css/`.

---

## 4. O que já funciona

- **5 atributos** como dados (d6 a d12), com PV, PE, PI, Defesa, Iniciativa e
  ND Místico calculados automaticamente pelas suas fórmulas.
- **30 perícias**, cada uma com os dois atributos **editáveis** na ficha — é assim
  que o Sacerdote troca Arcanismo para CAR + INT.
- **Rolagem completa**: janela com ND, modificador situacional, Empenho,
  Força das Sombras e ativação de Origem. O card no chat diz se passou, detecta
  crítico e registra o que foi gasto.
- **Eixo da Alma automático**: perder Determinação gera Corrupção; encher a
  Corrupção gera um Estilhaço, zera a barra e restaura a Determinação — com
  anúncio no chat.
- **Origens**: cada uma ativável 1× por sessão, reduz o ND em 5 e protege da
  Corrupção. Botão "Nova sessão" libera as duas de novo.
- **Itens**: armas (dano por dois atributos + fixo), armaduras, escudos,
  conduítes (Valor Fixo somado ao dano mágico), consumíveis (gastam PI e curam),
  magias (gastam PE, mostram o ND de resistência) e habilidades com NH.
- **Descanso curto** (50% de PV/PE) e **completo**.

### Atalhos

- **Shift + clique** no nome de uma perícia: rola direto, sem a janela.
- **Shift + clique** no botão "Dano": rola já dobrado (crítico).
- Arrastar um item de um compêndio ou pasta para a ficha adiciona ele.

---

## 5. Como interpretei algumas regras

Onde o documento deixava margem, escolhi uma leitura. Se alguma estiver errada,
é rápido de trocar — todas moram em `module/config.mjs` ou `documents/actor.mjs`.

1. **Determinação vs. Pontos de Determinação** são duas coisas separadas: a
   *trilha* (0–10, o ferimento emocional) e a *reserva* gasta no Empenho, cujo
   máximo é a Determinação atual. Gastar um Ponto não fere a trilha.

2. **"Gaste 1 ponto de Corrupção"** (Força das Sombras) foi implementado como
   **ganhar** 1 de Corrupção — o custo é se aproximar do Estilhaço.

3. **Crítico** = os dois dados mostram o mesmo número **e** esse número é maior
   que 6. Ou seja, só acontece com dados d8 ou maiores. (`limiarCritico` no config.)

4. **Empenho** = um dado extra em um dos dois atributos, ficando com o melhor
   resultado daquele par. Você escolhe qual atributo na janela de rolagem.

---

## 6. O que ainda não está no sistema

Ficaram de fora nesta primeira versão, em ordem sugerida de implementação:

1. **Condições** (Atordoado, Enfraquecido, etc.) como efeitos que aplicam os
   modificadores sozinhos.
2. **Compêndios** prontos: as armas, armaduras, conduítes e as ~60 magias do
   documento cadastradas, para arrastar direto na ficha.
3. **Crepúsculo da Morte** — a contagem automática ao chegar a 0 PV.
4. **Perseverança, Limiar do Desespero e Negar a Sorte** (as outras opções de
   Determinação e Corrupção).
5. **Árvore de habilidades** clicável por classe.
6. **Ficha de NPC/criatura** e o Bestiário.
7. **Laços & Vínculos**.
