package nucleorp.regra;

import java.util.List;

/**
 * Resultado de um teste. {@code somaBruta} é a soma dos modificadores como
 * declarados; {@code somaAplicada} é a mesma soma depois do limite.
 */
public record ResultadoTeste(int dado, int atributo, List<Modificador> modificadores, int somaBruta,
                             int somaAplicada, int total, Integer dificuldade, Desfecho desfecho) {
    public boolean limitado() { return somaBruta != somaAplicada; }
}
