import { SINFONIA } from "./config.mjs";

const { DialogV2 } = foundry.applications.api;

/**
 * Janela "Configurar Rolagem", aberta antes de rolar os dados.
 *
 * Devolve um objeto com as escolhas do jogador, ou `null` se ele cancelar.
 */
export async function dialogoDeRolagem({ titulo, dadoA, dadoB, siglaA, siglaB, bonus, ator, nd = null }) {
  const alma = ator.system.alma;
  const temPonto = alma.pontos.value > 0;
  const origens = ator.system.origens;
  const origensLivres = [];
  if (origens.evento.texto && !origens.evento.usada) origensLivres.push(["evento", origens.evento.texto]);
  if (origens.ocupacao.texto && !origens.ocupacao.usada) origensLivres.push(["ocupacao", origens.ocupacao.texto]);

  const conteudo = `
    <div class="sinfonia-dialogo">
      <p class="formula">1d${dadoA} <span>${siglaA}</span> + 1d${dadoB} <span>${siglaB}</span>
        ${bonus ? `+ ${bonus}` : ""}</p>

      <div class="campo">
        <label>Nível de Dificuldade (ND)</label>
        <input type="number" name="nd" value="${nd ?? ""}" placeholder="deixe vazio se não souber" />
      </div>

      <div class="campo">
        <label>Modificador situacional</label>
        <input type="number" name="mod" value="0" />
      </div>

      <hr />

      <div class="campo-check ${temPonto ? "" : "desativado"}">
        <label>
          <input type="checkbox" name="empenho" ${temPonto ? "" : "disabled"} />
          <strong>Empenho</strong> — gasta 1 Ponto de Determinação (${alma.pontos.value} disponíveis).
          Dado extra, fica com o melhor. Se ainda assim falhar, ganha 1 de Corrupção.
        </label>
        <select name="empenhoAlvo">
          <option value="A">Dado extra em ${siglaA} (d${dadoA})</option>
          <option value="B">Dado extra em ${siglaB} (d${dadoB})</option>
        </select>
      </div>

      <div class="campo-check">
        <label>
          <input type="checkbox" name="sombras" />
          <strong>Força das Sombras</strong> — +${SINFONIA.regras.bonusForcaSombras} no resultado,
          ao custo de 1 ponto de Corrupção (atual: ${alma.corrupcao.value}/${alma.corrupcao.max}).
        </label>
      </div>

      ${origensLivres.length ? `
      <div class="campo-check">
        <label><strong>Ativar Origem</strong> — reduz o ND em ${SINFONIA.regras.reducaoOrigem}
          e protege da Corrupção em caso de falha.</label>
        <select name="origem">
          <option value="">— não ativar —</option>
          ${origensLivres.map(([k, t]) => `<option value="${k}">${foundry.utils.escapeHTML(t)}</option>`).join("")}
        </select>
      </div>` : ""}
    </div>`;

  return DialogV2.wait({
    window: { title: `Rolagem — ${titulo}` },
    classes: ["sinfonia-das-almas"],
    content: conteudo,
    buttons: [
      {
        action: "rolar",
        label: "Rolar",
        default: true,
        callback: (event, botao, dialogo) => lerFormulario(dialogo.element)
      },
      { action: "cancelar", label: "Cancelar" }
    ],
    rejectClose: false
  }).then((r) => (r === "cancelar" || r === null ? null : r));
}

/** Lê os campos da janela e devolve um objeto limpo. */
function lerFormulario(elemento) {
  const pega = (nome) => elemento.querySelector(`[name="${nome}"]`);
  const ndBruto = pega("nd")?.value;

  return {
    nd: ndBruto === "" || ndBruto === undefined ? null : Number(ndBruto),
    mod: Number(pega("mod")?.value || 0),
    empenho: pega("empenho")?.checked ? pega("empenhoAlvo").value : null,
    sombras: pega("sombras")?.checked ?? false,
    origem: pega("origem")?.value || null
  };
}
