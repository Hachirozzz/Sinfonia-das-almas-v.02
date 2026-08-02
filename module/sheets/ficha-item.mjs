import { SINFONIA } from "../config.mjs";

const { HandlebarsApplicationMixin } = foundry.applications.api;
const { ItemSheetV2 } = foundry.applications.sheets;

export class FichaItem extends HandlebarsApplicationMixin(ItemSheetV2) {
  static DEFAULT_OPTIONS = {
    classes: ["sinfonia-das-almas", "sheet", "item"],
    position: { width: 480, height: "auto" },
    window: { resizable: true },
    form: { submitOnChange: true, closeOnSubmit: false }
  };

  static PARTS = {
    corpo: { template: "systems/sinfonia-das-almas/templates/item/item.hbs" }
  };

  async _prepareContext(options) {
    const tipo = this.item.type;

    return {
      item: this.item,
      system: this.item.system,
      config: SINFONIA,
      editavel: this.isEditable,
      // Booleanos prontos: assim o template não precisa de comparações.
      ehArma: tipo === "arma",
      ehArmadura: tipo === "armadura",
      ehEscudo: tipo === "escudo",
      ehConduite: tipo === "conduite",
      ehConsumivel: tipo === "consumivel",
      ehMagia: tipo === "magia",
      ehHabilidade: tipo === "habilidade",
      temEquipar: ["arma", "armadura", "escudo", "conduite"].includes(tipo),
      listas: {
        atributos: Object.entries(SINFONIA.atributos).map(([v, a]) => ({ valor: v, rotulo: a.rotulo })),
        pericias: Object.entries(SINFONIA.pericias).map(([v, p]) => ({ valor: v, rotulo: p.rotulo })),
        tiposDano: Object.entries(SINFONIA.tiposDano).map(([v, r]) => ({ valor: v, rotulo: r })),
        categoriasArma: Object.entries(SINFONIA.categoriasArma).map(([v, r]) => ({ valor: v, rotulo: r })),
        categoriasArmadura: Object.entries(SINFONIA.categoriasArmadura).map(([v, r]) => ({ valor: v, rotulo: r })),
        tiposConduite: Object.entries(SINFONIA.tiposConduite).map(([v, r]) => ({ valor: v, rotulo: r })),
        escolas: Object.entries(SINFONIA.escolas).map(([v, r]) => ({ valor: v, rotulo: r })),
        resolucoes: Object.entries(SINFONIA.resolucoes).map(([v, r]) => ({ valor: v, rotulo: r })),
        circulos: [1, 2, 3]
      }
    };
  }

  _onRender(context, options) {
    super._onRender?.(context, options);
    for (const select of this.element.querySelectorAll("select[data-valor]")) {
      select.value = select.dataset.valor;
    }
  }
}
