package nucleorp.regra;

/** Grau de ferimento. A ordem importa: ferimentos não acumulam, vale o pior. */
public enum Grau {
    NENHUM, LEVE, GRAVE;

    public String id() { return name().toLowerCase(java.util.Locale.ROOT); }

    public static Grau deId(String id) {
        for (Grau g : values()) if (g.id().equalsIgnoreCase(id)) return g;
        return null;
    }

    public boolean piorQue(Grau outro) { return ordinal() > outro.ordinal(); }

    /** Tratamento bem-sucedido reduz um grau. */
    public Grau aposTratar() {
        return switch (this) { case GRAVE -> LEVE; default -> NENHUM; };
    }
}
