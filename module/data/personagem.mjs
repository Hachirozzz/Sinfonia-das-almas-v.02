import { SINFONIA } from "../config.mjs";

const fields = foundry.data.fields;
const R = SINFONIA.regras;

/** Campo numérico inteiro. */
function num(inicial = 0, extras = {}) {
  return new fields.NumberField({
    required: true,
    integer: true,
    nullable: false,
    initial: inicial,
    ...extras
  });
}

/** Campo de texto simples. */
function texto(inicial = "") {
  return new fields.StringField({ required: true, blank: true, initial: inicial });
}

/**
 * Barra com valor atual e máximo.
 * IMPORTANTE: os nomes precisam ser `value` e `max` (em inglês) — é assim que o
 * Foundry reconhece a barra para poder mostrá-la em cima do token.
 */
function barra(inicial = 0) {
  return new fields.SchemaField({
    value: num(inicial),
    max: num(inicial),
    /** Ajuste manual somado ao máximo calculado (ex.: habilidade Vigor). */
    bonus: num(0)
  });
}

/* -------------------------------------------- */

export class PersonagemData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    const atributos = {};
    for (const chave of Object.keys(SINFONIA.atributos)) {
      atributos[chave] = new fields.SchemaField({
        dado: num(8, { choices: SINFONIA.dados })
      });
    }

    const pericias = {};
    for (const [chave, def] of Object.entries(SINFONIA.pericias)) {
      pericias[chave] = new fields.SchemaField({
        grau: new fields.StringField({
          required: true,
          initial: "nenhum",
          choices: Object.keys(SINFONIA.graus)
        }),
        attrA: new fields.StringField({
          required: true,
          initial: def.attrs[0],
          choices: Object.keys(SINFONIA.atributos)
        }),
        attrB: new fields.StringField({
          required: true,
          initial: def.attrs[1],
          choices: Object.keys(SINFONIA.atributos)
        }),
        /** Bônus extra fixo, vindo de habilidades ou itens. */
        mod: num(0)
      });
    }

    return {
      atributos: new fields.SchemaField(atributos),
      pericias: new fields.SchemaField(pericias),

      recursos: new fields.SchemaField({
        pv: barra(30),
        pe: barra(30),
        pvTemporario: num(0),
        pi: new fields.SchemaField({
          value: num(R.inventarioBase),
          max: num(R.inventarioBase)
        })
      }),

      /** O Eixo da Alma: cabo de guerra entre Determinação e Corrupção. */
      alma: new fields.SchemaField({
        /** A trilha de "ferimento emocional". Perder 1 aqui = ganhar 1 de Corrupção. */
        determinacao: new fields.SchemaField({
          value: num(R.determinacaoMaxima),
          max: num(R.determinacaoMaxima)
        }),
        /** A reserva gasta no Empenho e na Perseverança. O máximo é a Determinação atual. */
        pontos: new fields.SchemaField({
          value: num(R.determinacaoMaxima),
          max: num(R.determinacaoMaxima)
        }),
        corrupcao: new fields.SchemaField({
          value: num(0),
          max: num(R.corrupcaoMaxima)
        }),
        estilhacos: num(0)
      }),

      combate: new fields.SchemaField({
        /** Bônus manual de Defesa (cobertura, buffs temporários). */
        defesaBonus: num(0),
        deslocamento: num(R.deslocamentoBase),
        /** Redução de Dano. */
        rd: num(0)
      }),

      detalhes: new fields.SchemaField({
        nivel: num(R.nivelInicial, { min: 1 }),
        classe: texto(""),
        pilar: texto(""),
        qualidades: texto(""),
        defeitos: texto(""),
        anotacoes: new fields.HTMLField({ required: true, initial: "" }),
        zenites: num(0),
        fragmentos: num(0)
      }),

      /** As duas Origens, cada uma ativável 1x por sessão. */
      origens: new fields.SchemaField({
        evento: new fields.SchemaField({
          texto: texto(""),
          usada: new fields.BooleanField({ initial: false })
        }),
        ocupacao: new fields.SchemaField({
          texto: texto(""),
          usada: new fields.BooleanField({ initial: false })
        })
      })
    };
  }

  /* -------------------------------------------- */

  /**
   * Valores CALCULADOS. Roda automaticamente sempre que o ator muda.
   * Nada aqui é digitado pelo jogador — tudo é derivado das regras.
   */
  prepareDerivedData() {
    const nivel = this.detalhes.nivel;
    const dado = (chave) => this.atributos[chave].dado;

    // --- PV e PE máximos: Nível + (5 × dado do atributo) ---
    this.recursos.pv.max = nivel + R.multiplicadorVital * dado("pod") + this.recursos.pv.bonus;
    this.recursos.pe.max = nivel + R.multiplicadorVital * dado("mis") + this.recursos.pe.bonus;

    // --- A reserva de Pontos de Determinação nunca passa da Determinação atual ---
    this.alma.pontos.max = this.alma.determinacao.value;
    this.alma.pontos.value = Math.min(this.alma.pontos.value, this.alma.pontos.max);

    // --- Iniciativa: metade do dado de Agilidade, arredondado para baixo ---
    this.combate.iniciativa = Math.floor(dado("agi") / 2);

    // --- ND Místico: 8 + dado do atributo de conjuração ---
    this.ndMisticoArcano = R.baseNdMistico + dado("mis");
    this.ndMisticoDivino = R.baseNdMistico + dado("car");

    // --- Equipamento vestido ---
    const equipados = this.parent.items.filter((i) => i.system.equipado === true);
    const armadura = equipados.find((i) => i.type === "armadura");
    const escudo = equipados.find((i) => i.type === "escudo");
    const conduite = equipados.find((i) => i.type === "conduite");

    // Defesa natural = dado de AGI. A armadura só ajuda se for maior que isso.
    const defesaNatural = dado("agi");
    const defesaArmadura = armadura?.system.defesa ?? 0;
    this.combate.defesa =
      Math.max(defesaNatural, defesaArmadura) +
      (escudo?.system.defesa ?? 0) +
      this.combate.defesaBonus;

    this.combate.rdTotal = this.combate.rd + (armadura?.system.rd ?? 0);

    // Valor Fixo do conduíte: entra no dano e na cura das magias.
    this.conduite = conduite
      ? { nome: conduite.name, valorFixo: conduite.system.valorFixo, tipo: conduite.system.tipo }
      : null;

    // --- Bônus total de cada perícia (maestria + modificador manual) ---
    for (const pericia of Object.values(this.pericias)) {
      pericia.bonus = SINFONIA.graus[pericia.grau].bonus + pericia.mod;
    }
  }
}
