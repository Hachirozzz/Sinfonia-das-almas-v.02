import { SINFONIA } from "../config.mjs";

export class SinfoniaItem extends Item {
  /**
   * Usa o item: cada tipo faz uma coisa diferente.
   * Chamado pelo botão de uso na ficha do personagem.
   */
  async usar() {
    const ator = this.actor;
    if (!ator) return ui.notifications.warn("Este item não pertence a nenhum personagem.");

    switch (this.type) {
      case "arma": return this.#atacar(ator);
      case "magia": return this.#conjurar(ator);
      case "consumivel": return this.#consumir(ator);
      case "habilidade": return this.#usarHabilidade(ator);
      default: return this.#exibirNoChat(ator);
    }
  }

  /* -------------------------------------------- */

  /** Arma: rola o teste de acerto com a perícia configurada. */
  async #atacar(ator) {
    if (!this.system.equipado) {
      ui.notifications.warn(`${this.name} não está equipada.`);
    }
    return ator.rolarPericia(this.system.pericia);
  }

  /** Magia: cobra o PE e resolve conforme o tipo de resolução. */
  async #conjurar(ator) {
    const custo = this.system.custoPE;
    const pe = ator.system.recursos.pe.value;

    if (pe < custo) {
      return ui.notifications.warn(
        `${ator.name} tem ${pe} PE — ${this.name} custa ${custo} PE.`
      );
    }
    await ator.update({ "system.recursos.pe.value": pe - custo });

    await this.#exibirNoChat(ator, `Gastou ${custo} PE (restam ${pe - custo}).`);

    // Ataque de conjuração: rola contra a Defesa do alvo.
    if (this.system.resolucao === "ataque") return ator.rolarPericia(this.system.pericia);

    // Resistência: o card já informa o ND que o alvo precisa alcançar.
    return null;
  }

  /** Consumível: gasta Pontos de Inventário e aplica cura. */
  async #consumir(ator) {
    const custoPI = this.system.pi;
    const pi = ator.system.recursos.pi.value;

    if (pi < custoPI) {
      return ui.notifications.warn(
        `${ator.name} tem ${pi} Pontos de Inventário — ${this.name} custa ${custoPI}.`
      );
    }

    const mudancas = { "system.recursos.pi.value": pi - custoPI };
    const efeitos = [];

    for (const [recurso, rotulo] of [["pv", "PV"], ["pe", "PE"]]) {
      const formula = this.system.cura[recurso];
      if (!formula) continue;
      const rolagem = await new Roll(formula, ator.getRollData()).evaluate();
      const atual = ator.system.recursos[recurso].value;
      const max = ator.system.recursos[recurso].max;
      mudancas[`system.recursos.${recurso}.value`] = Math.min(max, atual + rolagem.total);
      efeitos.push(`+${rolagem.total} ${rotulo}`);
    }

    await ator.update(mudancas);

    if (this.system.quantidade > 1) {
      await this.update({ "system.quantidade": this.system.quantidade - 1 });
    } else {
      await this.delete();
    }

    return this.#exibirNoChat(
      ator,
      [`Gastou ${custoPI} Ponto(s) de Inventário.`, ...efeitos].join(" ")
    );
  }

  /** Habilidade: cobra PE/PI se houver e mostra o texto no chat. */
  async #usarHabilidade(ator) {
    const custoPE = this.system.custoPE;
    const pe = ator.system.recursos.pe.value;

    if (custoPE > pe) {
      return ui.notifications.warn(`${ator.name} não tem PE suficiente (precisa de ${custoPE}).`);
    }
    if (custoPE) await ator.update({ "system.recursos.pe.value": pe - custoPE });

    return this.#exibirNoChat(ator, custoPE ? `Gastou ${custoPE} PE.` : "");
  }

  /* -------------------------------------------- */

  /** Publica a descrição do item no chat. */
  async #exibirNoChat(ator, nota = "") {
    const linhas = [];
    const s = this.system;

    if (this.type === "magia") {
      linhas.push(`${SINFONIA.circulos[s.circulo]?.rotulo ?? `${s.circulo}º Círculo`} — ${SINFONIA.escolas[s.escola]}`);
      linhas.push(`Conjuração: ${s.tempoConjuracao} • Alcance: ${s.alcance} • Duração: ${s.duracao}`);
      if (s.resolucao === "resistencia") {
        const nd = s.escola === "divina" ? ator.system.ndMisticoDivino : ator.system.ndMisticoArcano;
        linhas.push(`<strong>O alvo testa ${SINFONIA.pericias[s.periciaResistencia].rotulo} contra ND ${nd}.</strong>`);
      }
    }
    if (this.type === "habilidade") {
      linhas.push(`NH ${s.nh}/${s.nhMax}`);
    }
    if (s.propriedades) linhas.push(s.propriedades);

    return ChatMessage.create({
      speaker: ChatMessage.getSpeaker({ actor: ator }),
      content: `<div class="sinfonia-card item">
        <h3>${this.name}</h3>
        ${linhas.length ? `<p class="meta">${linhas.join("<br/>")}</p>` : ""}
        <div class="descricao">${s.descricao ?? ""}</div>
        ${nota ? `<p class="nota">${nota}</p>` : ""}
      </div>`
    });
  }
}
