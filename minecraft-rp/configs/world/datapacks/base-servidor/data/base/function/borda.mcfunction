# Borda do mundo, aplicada a cada inicialização do servidor.
# 6000 = largura em blocos (do -3000 ao +3000), com centro em 0,0.
# Para mudar o tamanho, troque o número nas três linhas e reinicie.
# O Nether fica com a mesma largura em blocos do mundo normal.
execute in minecraft:overworld run worldborder center 0 0
execute in minecraft:overworld run worldborder set 6000
execute in minecraft:the_nether run worldborder center 0 0
execute in minecraft:the_nether run worldborder set 6000
execute in minecraft:the_end run worldborder center 0 0
execute in minecraft:the_end run worldborder set 6000
