/**
 * SINFONIA DAS ALMAS — ponto de entrada do sistema.
 *
 * Este arquivo é carregado pelo Foundry (declarado em system.json) e conecta
 * todas as peças: modelos de dados, documentos, fichas e configurações.
 */

import { SINFONIA } from "./config.mjs";
import { PersonagemData } from "./data/personagem.mjs";
import { modelosDeItem } from "./data/itens.mjs";
import { SinfoniaActor } from "./documents/actor.mjs";
import { SinfoniaItem } from "./documents/item.mjs";
import { FichaPersonagem } from "./sheets/ficha-personagem.mjs";
import { FichaItem } from "./sheets/ficha-item.mjs";

Hooks.once("init", function () {
  console.log("Sinfonia das Almas | inicializando o sistema");

  // Deixa as tabelas de regra acessíveis pelo console: CONFIG.SINFONIA
  CONFIG.SINFONIA = SINFONIA;

  // --- Classes de documento (onde vivem as rolagens) ---
  CONFIG.Actor.documentClass = SinfoniaActor;
  CONFIG.Item.documentClass = SinfoniaItem;

  // --- Modelos de dados (o "molde" de cada tipo de ficha) ---
  CONFIG.Actor.dataModels.personagem = PersonagemData;
  for (const [tipo, modelo] of Object.entries(modelosDeItem)) {
    CONFIG.Item.dataModels[tipo] = modelo;
  }

  // --- Iniciativa é valor fixo, não rolagem: metade do dado de AGI ---
  CONFIG.Combat.initiative = { formula: "@combate.iniciativa", decimals: 0 };

  // --- Registro das fichas ---
  const { Actors, Items } = foundry.documents.collections;

  Actors.registerSheet("sinfonia-das-almas", FichaPersonagem, {
    types: ["personagem"],
    makeDefault: true,
    label: "Ficha de Personagem"
  });

  Items.registerSheet("sinfonia-das-almas", FichaItem, {
    types: Object.keys(modelosDeItem),
    makeDefault: true,
    label: "Ficha de Item"
  });

  console.log("Sinfonia das Almas | pronto");
});

/* -------------------------------------------- */

Hooks.once("ready", function () {
  // Aviso amigável se alguém abrir o sistema numa versão antiga do Foundry.
  const versao = Number(game.version?.split(".")[0] ?? 0);
  if (versao < 13) {
    ui.notifications.error(
      "Sinfonia das Almas precisa do Foundry v13 ou superior."
    );
  }
});
