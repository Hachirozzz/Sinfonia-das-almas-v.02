import { SINFONIA } from "../config.mjs";

const fields = foundry.data.fields;

function num(inicial = 0, extras = {}) {
  return new fields.NumberField({
    required: true, integer: true, nullable: false, initial: inicial, ...extras
  });
}
function texto(inicial = "") {
  return new fields.StringField({ required: true, blank: true, initial: inicial });
}
function escolha(opcoes, inicial) {
  return new fields.StringField({ required: true, initial, choices: Object.keys(opcoes) });
}
function descricao() {
  return new fields.HTMLField({ required: true, initial: "" });
}

/** Campos que todo item tem. */
function base() {
  return {
    descricao: descricao(),
    custo: num(0),          // em Zênites
    propriedades: texto("") // texto livre: "Duas mãos. Veloz: +1 Iniciativa"
  };
}

/* -------------------------------------------- */
/*  ARMA                                        */
/* -------------------------------------------- */

export class ArmaData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      equipado: new fields.BooleanField({ initial: false }),
      categoria: escolha(SINFONIA.categoriasArma, "espadas"),
      /** Perícia usada para o teste de acerto. */
      pericia: escolha(SINFONIA.pericias, "armasBrancas"),
      /** Dano = 1d[attrA] + 1d[attrB] + fixo. */
      dano: new fields.SchemaField({
        attrA: escolha(SINFONIA.atributos, "pod"),
        attrB: escolha(SINFONIA.atributos, "agi"),
        fixo: num(0),
        tipo: escolha(SINFONIA.tiposDano, "fisico")
      }),
      alcance: texto("Corpo a corpo")
    };
  }
}

/* -------------------------------------------- */
/*  ARMADURA / ESCUDO                           */
/* -------------------------------------------- */

export class ArmaduraData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      equipado: new fields.BooleanField({ initial: false }),
      categoria: escolha(SINFONIA.categoriasArmadura, "leve"),
      /** Valor fixo de Defesa enquanto vestida. */
      defesa: num(9),
      rd: num(0)
    };
  }
}

export class EscudoData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      equipado: new fields.BooleanField({ initial: false }),
      /** Bônus somado à Defesa. */
      defesa: num(1)
    };
  }
}

/* -------------------------------------------- */
/*  CONDUÍTE MÁGICO                             */
/* -------------------------------------------- */

export class ConduiteData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      equipado: new fields.BooleanField({ initial: false }),
      tipo: escolha(SINFONIA.tiposConduite, "arcano"),
      /** Somado ao dano e à cura das magias conjuradas com ele. */
      valorFixo: num(4)
    };
  }
}

/* -------------------------------------------- */
/*  CONSUMÍVEL                                  */
/* -------------------------------------------- */

export class ConsumivelData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      /** Quanto custa de Pontos de Inventário para usar. */
      pi: num(1),
      quantidade: num(1),
      /** Fórmula opcional aplicada ao usar, ex.: "20". */
      cura: new fields.SchemaField({
        pv: texto(""),
        pe: texto("")
      })
    };
  }
}

/* -------------------------------------------- */
/*  MAGIA                                       */
/* -------------------------------------------- */

export class MagiaData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      circulo: num(1, { choices: [1, 2, 3, 4, 5] }),
      escola: escolha(SINFONIA.escolas, "evocacao"),
      custoPE: num(10),
      /** Custo por rodada de magias marcadas como (Contínuo). 0 = não é contínua. */
      custoContinuo: num(0),
      tempoConjuracao: texto("1 Ação Inicial"),
      alcance: texto("18 metros"),
      duracao: texto("Instantânea"),

      /** Como resolve: ataque contra Defesa, resistência contra ND, ou automático. */
      resolucao: escolha(SINFONIA.resolucoes, "ataque"),
      /** Perícia do ataque de conjuração (Arcanismo por padrão). */
      pericia: escolha(SINFONIA.pericias, "arcanismo"),
      /** Perícia que o ALVO usa para resistir. */
      periciaResistencia: escolha(SINFONIA.pericias, "reflexos"),

      dano: new fields.SchemaField({
        formula: texto("1d10"),
        tipo: escolha(SINFONIA.tiposDano, "fogo"),
        /** Se true, soma o Valor Fixo do conduíte equipado. */
        usaConduite: new fields.BooleanField({ initial: true })
      })
    };
  }
}

/* -------------------------------------------- */
/*  HABILIDADE (árvore de talentos)             */
/* -------------------------------------------- */

export class HabilidadeData extends foundry.abstract.TypeDataModel {
  static defineSchema() {
    return {
      ...base(),
      /** Nível de Habilidade atual e o teto — o [5X] da sua ficha de classe. */
      nh: num(1),
      nhMax: num(1),
      custoPE: num(0),
      custoPI: num(0),
      classe: texto(""),
      /** "manobra", "truque", "passiva", "ativa"... texto livre. */
      tipo: texto("ativa")
    };
  }
}

/* -------------------------------------------- */

export const modelosDeItem = {
  arma: ArmaData,
  armadura: ArmaduraData,
  escudo: EscudoData,
  conduite: ConduiteData,
  consumivel: ConsumivelData,
  magia: MagiaData,
  habilidade: HabilidadeData
};
