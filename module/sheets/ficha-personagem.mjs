import { SINFONIA } from "../config.mjs";

const { HandlebarsApplicationMixin } = foundry.applications.api;
const { ActorSheetV2 } = foundry.applications.sheets;

const TIPOS_INVENTARIO = ["arma", "armadura", "escudo", "conduite", "consumivel"];

export class FichaPersonagem extends HandlebarsApplicationMixin(ActorSheetV2) {
  /** Guarda qual aba está aberta entre um render e outro. */
  abaAtiva = "principal";

  static DEFAULT_OPTIONS = {
    classes: ["sinfonia-das-almas", "sheet", "actor"],
    position: { width: 760, height: 800 },
    window: { resizable: true },
    form: { submitOnChange: true, closeOnSubmit: false },
    actions: {
      trocarAba: FichaPersonagem.#trocarAba,
      rolarPericia: FichaPersonagem.#rolarPericia,
      criarItem: FichaPersonagem.#criarItem,
      editarItem: FichaPersonagem.#editarItem,
      apagarItem: FichaPersonagem.#apagarItem,
      usarItem: FichaPersonagem.#usarItem,
      rolarDano: FichaPersonagem.#rolarDano,
      equipar: FichaPersonagem.#equipar,
      ajustar: FichaPersonagem.#ajustar,
      descanso: FichaPersonagem.#descanso,
      novaSessao: FichaPersonagem.#novaSessao
    }
  };

  static PARTS = {
    corpo: { template: "systems/sinfonia-das-almas/templates/actor/personagem.hbs" }
  };

  /* -------------------------------------------- */

  async _prepareContext(options) {
    const sistema = this.actor.system;

    // Monta a lista de perícias já com os dados resolvidos, prontos para exibir.
    const pericias = Object.entries(SINFONIA.pericias).map(([chave, def]) => {
      const p = sistema.pericias[chave];
      return {
        chave,
        rotulo: def.rotulo,
        grau: p.grau,
        attrA: p.attrA,
        attrB: p.attrB,
        mod: p.mod,
        bonus: p.bonus,
        dadoA: sistema.atributos[p.attrA].dado,
        dadoB: sistema.atributos[p.attrB].dado,
        treinada: p.grau !== "nenhum"
      };
    });

    // Separa os itens por tipo para cada seção da ficha.
    const porTipo = {};
    for (const item of this.actor.items) (porTipo[item.type] ??= []).push(item);
    for (const lista of Object.values(porTipo)) lista.sort((a, b) => a.name.localeCompare(b.name));

    const magias = (porTipo.magia ?? []).sort(
      (a, b) => a.system.circulo - b.system.circulo || a.name.localeCompare(b.name)
    );

    return {
      actor: this.actor,
      system: sistema,
      config: SINFONIA,
      editavel: this.isEditable,
      abaAtiva: this.abaAtiva,
      atributos: Object.entries(SINFONIA.atributos).map(([chave, def]) => ({
        chave, ...def, dado: sistema.atributos[chave].dado
      })),
      pericias,
      inventario: TIPOS_INVENTARIO.flatMap((t) => porTipo[t] ?? []),
      magias,
      habilidades: porTipo.habilidade ?? [],
      listas: {
        dados: SINFONIA.dados,
        graus: Object.entries(SINFONIA.graus).map(([v, g]) => ({ valor: v, rotulo: g.rotulo })),
        atributos: Object.entries(SINFONIA.atributos).map(([v, a]) => ({ valor: v, rotulo: a.sigla }))
      }
    };
  }

  /* -------------------------------------------- */

  _onRender(context, options) {
    super._onRender?.(context, options);

    // Todo <select data-valor="..."> assume esse valor. Fazer assim (em vez de
    // marcar `selected` no HTML) mantém os templates simples e sem erro.
    for (const select of this.element.querySelectorAll("select[data-valor]")) {
      select.value = select.dataset.valor;
    }

    this.#aplicarAba();
    this.#ativarArrastarSoltar();
  }

  /** Mostra só a aba ativa. */
  #aplicarAba() {
    for (const botao of this.element.querySelectorAll("[data-aba-botao]")) {
      botao.classList.toggle("ativo", botao.dataset.abaBotao === this.abaAtiva);
    }
    for (const painel of this.element.querySelectorAll("[data-aba]")) {
      painel.classList.toggle("oculto", painel.dataset.aba !== this.abaAtiva);
    }
  }

  /** Permite arrastar itens de compêndios/pastas para dentro da ficha. */
  #ativarArrastarSoltar() {
    // A ficha é redesenhada várias vezes; sem esta trava os ouvintes se
    // acumulariam e um único item cairia duplicado na ficha.
    if (this.element.dataset.soltarLigado) return;
    this.element.dataset.soltarLigado = "sim";

    this.element.addEventListener("dragover", (ev) => ev.preventDefault());
    this.element.addEventListener("drop", async (ev) => {
      ev.preventDefault();
      if (!this.isEditable) return;

      const dados = foundry.applications.ux.TextEditor.implementation.getDragEventData(ev);
      if (dados?.type !== "Item") return;

      const item = await Item.implementation.fromDropData(dados);
      if (!item || item.parent === this.actor) return;
      await this.actor.createEmbeddedDocuments("Item", [item.toObject()]);
    });
  }

  /* -------------------------------------------- */
  /*  Ações                                       */
  /* -------------------------------------------- */

  static #trocarAba(event, alvo) {
    this.abaAtiva = alvo.dataset.abaBotao;
    this.#aplicarAba();
  }

  static async #rolarPericia(event, alvo) {
    // Segurar Shift pula a janela e rola direto.
    return this.actor.rolarPericia(alvo.dataset.pericia, { rapida: event.shiftKey });
  }

  static async #criarItem(event, alvo) {
    const tipo = alvo.dataset.tipo;
    const [criado] = await this.actor.createEmbeddedDocuments("Item", [
      { name: `Novo ${tipo}`, type: tipo }
    ]);
    criado?.sheet.render(true);
  }

  static #editarItem(event, alvo) {
    this.#item(alvo)?.sheet.render(true);
  }

  static async #apagarItem(event, alvo) {
    const item = this.#item(alvo);
    if (!item) return;

    const confirmado = await foundry.applications.api.DialogV2.confirm({
      window: { title: "Apagar item" },
      content: `<p>Apagar <strong>${item.name}</strong> da ficha? Isso não pode ser desfeito.</p>`
    });
    if (confirmado) await item.delete();
  }

  static async #usarItem(event, alvo) {
    return this.#item(alvo)?.usar();
  }

  static async #rolarDano(event, alvo) {
    const item = this.#item(alvo);
    if (!item) return;
    // Segurar Shift rola como crítico (dano dobrado).
    const critico = event.shiftKey;
    return item.type === "magia"
      ? this.actor.rolarDanoDeMagia(item, { critico })
      : this.actor.rolarDanoDeArma(item, { critico });
  }

  static async #equipar(event, alvo) {
    const item = this.#item(alvo);
    if (item) await item.update({ "system.equipado": !item.system.equipado });
  }

  /** Botões +/− das barras da Alma e dos recursos. */
  static async #ajustar(event, alvo) {
    const caminho = alvo.dataset.caminho;
    const delta = Number(alvo.dataset.delta);

    // Perder Determinação tem consequência: vira Corrupção.
    if (caminho === "system.alma.determinacao.value" && delta < 0) {
      return this.actor.perderDeterminacao(-delta);
    }
    if (caminho === "system.alma.corrupcao.value" && delta > 0) {
      return this.actor.ganharCorrupcao(delta);
    }

    const atual = foundry.utils.getProperty(this.actor, caminho) ?? 0;
    const maximo = Number(alvo.dataset.max ?? Infinity);
    const novo = Math.clamp(atual + delta, 0, maximo);
    await this.actor.update({ [caminho]: novo });
  }

  /** Descanso curto (50% de PV/PE) ou completo (tudo). */
  static async #descanso(event, alvo) {
    const completo = alvo.dataset.tipo === "completo";
    const r = this.actor.system.recursos;

    const recuperar = (barra) =>
      completo ? barra.max : Math.min(barra.max, barra.value + Math.floor(barra.max / 2));

    await this.actor.update({
      "system.recursos.pv.value": recuperar(r.pv),
      "system.recursos.pe.value": recuperar(r.pe),
      "system.recursos.pi.value": r.pi.max,
      "system.alma.pontos.value": this.actor.system.alma.determinacao.value
    });

    ui.notifications.info(
      `${this.actor.name} fez um descanso ${completo ? "completo" : "curto"}.`
    );
  }

  /** Zera o uso das Origens — chame no começo de cada sessão. */
  static async #novaSessao() {
    await this.actor.update({
      "system.origens.evento.usada": false,
      "system.origens.ocupacao.usada": false
    });
    ui.notifications.info("Origens disponíveis novamente.");
  }

  /* -------------------------------------------- */

  /** Acha o item a partir do elemento clicado. */
  #item(alvo) {
    const id = alvo.closest("[data-item-id]")?.dataset.itemId;
    return this.actor.items.get(id);
  }
}
