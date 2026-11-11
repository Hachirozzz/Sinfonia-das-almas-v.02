package nucleorp.regra;

/** Um modificador de teste. O rótulo é um identificador (oficio, traco...), traduzido nas mensagens. */
public record Modificador(String rotulo, int valor) {
    public static final String CIRCUNSTANCIA = "circunstancia";
    public static final String OFICIO = "oficio";
    public static final String TRACO = "traco";
    public static final String FRAQUEZA = "fraqueza";
    public static final String FERIMENTO = "ferimento";
}
