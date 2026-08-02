import { SINFONIA } from "../config.mjs";
import { dialogoDeRolagem } from "../dialogos.mjs";

const R = SINFONIA.regras;
const CAMINHO_CARD = "systems/sinfonia-das-almas/templates/chat/rolagem.hbs";

export class SinfoniaActor extends Actor {
  /* -------------------------------------------- */
  /*  Rolagens                                    */
  /* -------------------------------------------- */

  /**
   * Rola um teste de perícia seguindo a Fórmula de Resolução:
   *   Dado A + Dado B + Modificador de Perícia ≥ ND
   *
   * @param {string} chave        Chave da perícia (ex.: "armasBrancas").
   * @param {object} [opcoes]
   * @param {boolean} [opcoes.rapida]  Pula a janela de configuração.
   * @param {number}  [opcoes.nd]      ND já conhecido, preenchido na janela.
   */
  async rolarPericia(chave, { rapida = false, nd = null } = {}) {
    const pericia = this.system.pericias[chave];
    const def = SINFONIA.pericias[chave];
    if (!pericia) return ui.notifications.error(`Perícia desconhecida: ${chave}`);

    const dadoA = this.system.atributos[pericia.attrA].dado;
    const dadoB = this.system.atributos[pericia.attrB].dado;
    const siglaA = SINFONIA.atributos[pericia.attrA].sigla;
    const siglaB = SINFONIA.atributos[pericia.attrB].sigla;

    let escolhas = { nd, mod: 0, empenho: null, sombras: false, origem: null };
    if (!rapida) {
      escolhas = await dialogoDeRolagem({
        titulo: def.rotulo, dadoA, dadoB, siglaA, siglaB,
        bonus: pericia.bonus, ator: this, nd
      });
      if (!escolhas) return null;
    }

    return this.#resolverTeste({
      rotulo: def.rotulo, dadoA, dadoB, siglaA, siglaB,
      bonusPericia: pericia.bonus, escolhas
    });
  }

  /* -------------------------------------------- */

  /** Monta a fórmula, rola, interpreta e cobra os custos. */
  async #resolverTeste({ rotulo, dadoA, dadoB, siglaA, siglaB, bonusPericia, escolhas }) {
    // --- Monta a fórmula ---
    // O Empenho adiciona um dado extra em um dos atributos e fica com o melhor
    // resultado: em notação de dados, isso é "2d8kh" (keep highest).
    const termoA = escolhas.empenho === "A" ? `2d${dadoA}kh` : `1d${dadoA}`;
    const termoB = escolhas.empenho === "B" ? `2d${dadoB}kh` : `1d${dadoB}`;

    const partes = [termoA, termoB];
    if (bonusPericia) partes.push(String(bonusPericia));
    if (escolhas.mod) partes.push(String(escolhas.mod));
    if (escolhas.sombras) partes.push(String(R.bonusForcaSombras));

    const rolagem = await new Roll(partes.join(" + ")).evaluate();

    // --- Lê o valor de cada um dos dois dados (para detectar o crítico) ---
    const valores = rolagem.dice.map((d) =>
      d.results.filter((r) => r.active).reduce((soma, r) => soma + r.result, 0)
    );
    const [valorA = 0, valorB = 0] = valores;

    // Crítico: os dois dados mostram o MESMO número, e esse número é acima de 6.
    const critico = valorA === valorB && valorA > R.limiarCritico;

    // --- ND, já com a redução da Origem ---
    const ndBase = escolhas.nd;
    const ndFinal = ndBase === null ? null : ndBase - (escolhas.origem ? R.reducaoOrigem : 0);
    const sucesso = ndFinal === null ? null : critico || rolagem.total >= ndFinal;

    // --- Custos e consequências ---
    const avisos = [];
    const mudancas = {};

    if (escolhas.empenho) {
      mudancas["system.alma.pontos.value"] = Math.max(0, this.system.alma.pontos.value - 1);
      avisos.push("Gastou 1 Ponto de Determinação (Empenho).");
    }

    let corrupcaoGanha = 0;
    if (escolhas.sombras) {
      corrupcaoGanha += 1;
      avisos.push(`Corrompeu a alma por +${R.bonusForcaSombras} (Força das Sombras).`);
    }
    // Falhou mesmo depois de se empenhar → a alma paga o preço.
    // A Origem ativada protege dessa Corrupção ("Segurança da Alma").
    if (escolhas.empenho && sucesso === false && !escolhas.origem) {
      corrupcaoGanha += 1;
      avisos.push("Falhou após o Empenho: +1 de Corrupção.");
    }
    if (escolhas.origem) {
      mudancas[`system.origens.${escolhas.origem}.usada`] = true;
      avisos.push(`Origem ativada: ND reduzido em ${R.reducaoOrigem}. Sem risco de Corrupção.`);
    }

    if (Object.keys(mudancas).length) await this.update(mudancas);
    if (corrupcaoGanha) await this.ganharCorrupcao(corrupcaoGanha);

    // --- Card no chat ---
    await this.#enviarCard({
      titulo: rotulo,
      subtitulo: `1d${dadoA} ${siglaA} + 1d${dadoB} ${siglaB}`,
      rolagem, valorA, valorB, critico, sucesso,
      nd: ndFinal, ndOriginal: ndBase, ndReduzido: ndFinal !== ndBase, avisos
    });

    return rolagem;
  }

  /* -------------------------------------------- */

  /** Rola o dano de uma arma. Crítico dobra o resultado. */
  async rolarDanoDeArma(item, { critico = false } = {}) {
    const d = item.system.dano;
    const dadoA = this.system.atributos[d.attrA].dado;
    const dadoB = this.system.atributos[d.attrB].dado;

    const formula = `1d${dadoA} + 1d${dadoB}${d.fixo ? ` + ${d.fixo}` : ""}`;
    const rolagem = await new Roll(critico ? `(${formula}) * 2` : formula).evaluate();

    await this.#enviarCard({
      titulo: `Dano — ${item.name}`,
      subtitulo: `${SINFONIA.tiposDano[d.tipo]}${critico ? " • CRÍTICO (dobrado)" : ""}`,
      rolagem, ehDano: true
    });
    return rolagem;
  }

  /** Rola o dano de uma magia, somando o Valor Fixo do conduíte equipado. */
  async rolarDanoDeMagia(item, { critico = false } = {}) {
    const d = item.system.dano;
    const conduite = this.system.conduite;
    const partes = [d.formula];

    if (d.usaConduite && conduite) partes.push(String(conduite.valorFixo));

    const formula = partes.join(" + ");
    const rolagem = await new Roll(critico ? `(${formula}) * 2` : formula).evaluate();

    const nota = d.usaConduite
      ? conduite
        ? `+${conduite.valorFixo} do conduíte (${conduite.nome})`
        : "sem conduíte equipado — só o efeito base"
      : "";

    await this.#enviarCard({
      titulo: `Dano — ${item.name}`,
      subtitulo: [SINFONIA.tiposDano[d.tipo], nota, critico ? "CRÍTICO (dobrado)" : ""]
        .filter(Boolean).join(" • "),
      rolagem, ehDano: true
    });
    return rolagem;
  }

  /* -------------------------------------------- */
  /*  Eixo da Alma                                */
  /* -------------------------------------------- */

  /**
   * Ganha pontos de Corrupção. Ao encher a barra, o personagem recebe um
   * Estilhaço da Alma: a Corrupção zera e a Determinação volta ao máximo.
   */
  async ganharCorrupcao(quantidade = 1) {
    const alma = this.system.alma;
    let corrupcao = alma.corrupcao.value + quantidade;
    let estilhacos = alma.estilhacos;
    let determinacao = alma.determinacao.value;
    let estilhacou = false;

    while (corrupcao >= alma.corrupcao.max) {
      corrupcao -= alma.corrupcao.max;
      estilhacos += 1;
      determinacao = R.determinacaoMaxima;
      estilhacou = true;
    }

    await this.update({
      "system.alma.corrupcao.value": corrupcao,
      "system.alma.estilhacos": estilhacos,
      "system.alma.determinacao.value": determinacao,
      ...(estilhacou ? { "system.alma.pontos.value": R.determinacaoMaxima } : {})
    });

    if (estilhacou) {
      await ChatMessage.create({
        speaker: ChatMessage.getSpeaker({ actor: this }),
        content: `<div class="sinfonia-card estilhaco">
          <h3>Estilhaço da Alma</h3>
          <p><strong>${this.name}</strong> perdeu o cabo de guerra. A Corrupção transbordou e
          deixou uma marca permanente — corpo e personalidade mudam, e sempre há um custo.</p>
          <p class="nota">Estilhaços acumulados: ${estilhacos}. A Corrupção voltou a 0 e a
          Determinação foi restaurada.</p>
        </div>`
      });
    }
  }

  /**
   * Perde pontos de Determinação (ferimento emocional).
   * Toda Determinação perdida vira Corrupção na mesma medida.
   */
  async perderDeterminacao(quantidade = 1) {
    const atual = this.system.alma.determinacao.value;
    const perdido = Math.min(quantidade, atual);
    if (perdido > 0) {
      await this.update({ "system.alma.determinacao.value": atual - perdido });
      await this.ganharCorrupcao(perdido);
    }
  }

  /* -------------------------------------------- */

  /** Monta e publica o card de resultado no chat. */
  async #enviarCard(dados) {
    const conteudo = await foundry.applications.handlebars.renderTemplate(CAMINHO_CARD, {
      ...dados,
      tooltip: await dados.rolagem.getTooltip(),
      total: dados.rolagem.total
    });

    return ChatMessage.create({
      speaker: ChatMessage.getSpeaker({ actor: this }),
      content: conteudo,
      rolls: [dados.rolagem],
      sound: CONFIG.sounds.dice
    });
  }
}
