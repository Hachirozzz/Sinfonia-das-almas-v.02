/**
 * TABELAS DO SISTEMA
 *
 * Este arquivo é a "planilha de regras". Quase toda mudança de balanceamento
 * (nova perícia, novo grau de maestria, nova condição) começa aqui.
 */

export const SINFONIA = {};

/* -------------------------------------------- */
/*  Atributos                                   */
/* -------------------------------------------- */

SINFONIA.atributos = {
  pod: { rotulo: "Poder", sigla: "POD" },
  agi: { rotulo: "Agilidade", sigla: "AGI" },
  int: { rotulo: "Intelecto", sigla: "INT" },
  car: { rotulo: "Carisma", sigla: "CAR" },
  mis: { rotulo: "Misticismo", sigla: "MIS" }
};

/** Tamanhos de dado permitidos para um atributo (d6 a d12). */
SINFONIA.dados = [6, 8, 10, 12];

/** Perfis de atributos da criação de personagem (Passo 1). */
SINFONIA.perfis = {
  especializado: { rotulo: "Especializado", dados: [12, 10, 6, 6, 6] },
  mediano: { rotulo: "Mediano", dados: [10, 8, 8, 8, 6] },
  versatil: { rotulo: "Pau pra toda obra", dados: [8, 8, 8, 8, 8] }
};

/* -------------------------------------------- */
/*  Graus de Maestria                           */
/* -------------------------------------------- */

SINFONIA.graus = {
  nenhum: { rotulo: "Destreinado", bonus: 0 },
  iniciante: { rotulo: "Iniciante", bonus: 2 },
  treinado: { rotulo: "Treinado", bonus: 4 },
  experiente: { rotulo: "Experiente", bonus: 6 }
};

/* -------------------------------------------- */
/*  Perícias                                    */
/* -------------------------------------------- */

/**
 * `attrs` é só a combinação PADRÃO. Cada personagem pode trocar os dois
 * atributos de qualquer perícia na própria ficha (o Sacerdote, por exemplo,
 * troca Arcanismo para CAR + INT).
 */
SINFONIA.pericias = {
  armasBrancas: { rotulo: "Armas Brancas", attrs: ["pod", "agi"] },
  armasFogo: { rotulo: "Armas de Fogo", attrs: ["agi", "int"] },
  atletismo: { rotulo: "Atletismo", attrs: ["pod", "pod"] },
  briga: { rotulo: "Briga", attrs: ["pod", "agi"] },
  conducao: { rotulo: "Condução", attrs: ["agi", "int"] },
  furtividade: { rotulo: "Furtividade", attrs: ["agi", "int"] },
  fortitude: { rotulo: "Fortitude", attrs: ["pod", "pod"] },
  forcaVontade: { rotulo: "Força de Vontade", attrs: ["pod", "car"] },
  reflexos: { rotulo: "Reflexos", attrs: ["agi", "agi"] },
  ladinagem: { rotulo: "Ladinagem", attrs: ["agi", "int"] },
  oficios: { rotulo: "Ofícios", attrs: ["int", "int"] },
  sobrevivencia: { rotulo: "Sobrevivência", attrs: ["pod", "int"] },
  empatiaAnimais: { rotulo: "Empatia com Animais", attrs: ["car", "car"] },
  etiqueta: { rotulo: "Etiqueta", attrs: ["car", "int"] },
  intimidacao: { rotulo: "Intimidação", attrs: ["car", "car"] },
  lideranca: { rotulo: "Liderança", attrs: ["car", "car"] },
  malandragem: { rotulo: "Malandragem", attrs: ["int", "int"] },
  performance: { rotulo: "Performance", attrs: ["car", "agi"] },
  persuasao: { rotulo: "Persuasão", attrs: ["car", "car"] },
  intuicao: { rotulo: "Intuição", attrs: ["int", "car"] },
  subterfugio: { rotulo: "Subterfúgio", attrs: ["car", "int"] },
  ciencia: { rotulo: "Ciência", attrs: ["int", "int"] },
  erudicao: { rotulo: "Erudição", attrs: ["int", "int"] },
  financas: { rotulo: "Finanças", attrs: ["int", "int"] },
  investigacao: { rotulo: "Investigação", attrs: ["int", "int"] },
  medicina: { rotulo: "Medicina", attrs: ["int", "int"] },
  arcanismo: { rotulo: "Arcanismo", attrs: ["mis", "mis"] },
  percepcao: { rotulo: "Percepção", attrs: ["int", "int"] },
  politica: { rotulo: "Política", attrs: ["int", "int"] },
  tecnologia: { rotulo: "Tecnologia", attrs: ["int", "int"] }
};

/* -------------------------------------------- */
/*  Magia                                       */
/* -------------------------------------------- */

SINFONIA.escolas = {
  abjuracao: "Abjuração",
  conjuracao: "Conjuração",
  adivinhacao: "Adivinhação",
  encantamento: "Encantamento",
  evocacao: "Evocação",
  ilusao: "Ilusão",
  necromancia: "Necromancia",
  transmutacao: "Transmutação",
  divina: "Divina"
};

SINFONIA.circulos = {
  1: { rotulo: "1º Círculo", custos: [5, 10] },
  2: { rotulo: "2º Círculo", custos: [10, 20] },
  3: { rotulo: "3º Círculo", custos: [20, 30] }
};

/** Como a magia é resolvida contra o alvo. */
SINFONIA.resolucoes = {
  ataque: "Ataque contra Defesa",
  resistencia: "Alvo testa resistência contra ND",
  nenhuma: "Efeito automático"
};

/* -------------------------------------------- */
/*  Itens                                       */
/* -------------------------------------------- */

SINFONIA.categoriasArma = {
  espadas: "Espadas",
  haste: "Armas de Haste",
  pesadas: "Armas Pesadas",
  precisao: "Armas de Precisão",
  leves: "Armas Leves",
  desarmado: "Desarmado"
};

SINFONIA.tiposDano = {
  fisico: "Físico",
  fogo: "Fogo",
  gelo: "Gelo",
  raio: "Raio",
  acido: "Ácido",
  necrotico: "Necrótico",
  psiquico: "Psíquico",
  trovejante: "Trovejante",
  luz: "Luz",
  sombrio: "Sombrio"
};

SINFONIA.categoriasArmadura = {
  leve: "Leve",
  media: "Média",
  pesada: "Pesada"
};

SINFONIA.tiposConduite = {
  arcano: "Arcano",
  sagrado: "Sagrado"
};

/* -------------------------------------------- */
/*  Constantes de regra                         */
/* -------------------------------------------- */

SINFONIA.regras = {
  determinacaoMaxima: 10,
  corrupcaoMaxima: 10,
  inventarioBase: 6,
  deslocamentoBase: 9,
  nivelInicial: 5,
  /** Multiplicador de PV/PE: nível + (5 × dado do atributo). */
  multiplicadorVital: 5,
  /** Base do ND Místico: 8 + dado do atributo de conjuração. */
  baseNdMistico: 8,
  /** Crítico: os dois dados iguais E acima deste valor. */
  limiarCritico: 6,
  /** Bônus da Força das Sombras (gasta 1 Corrupção). */
  bonusForcaSombras: 5,
  /** Redução de ND concedida por uma Origem. */
  reducaoOrigem: 5
};
