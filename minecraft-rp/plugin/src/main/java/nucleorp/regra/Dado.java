package nucleorp.regra;

/** Fonte de números dos dados. Nos testes é trocada por uma sequência fixa. */
@FunctionalInterface
public interface Dado {
    /** Devolve um número de 1 até {@code lados}, inclusive. */
    int rolar(int lados);
}
