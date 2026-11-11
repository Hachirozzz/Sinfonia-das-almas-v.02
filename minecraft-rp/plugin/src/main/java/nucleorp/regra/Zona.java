package nucleorp.regra;

/** Zona de paz: caixa entre dois cantos de um mundo, em coordenadas de bloco (cantos inclusos). */
public record Zona(String nome, String mundo, int x1, int y1, int z1, int x2, int y2, int z2,
                   boolean semAtaque, boolean semDuelo) {

    public Zona {
        int a;
        if (x1 > x2) { a = x1; x1 = x2; x2 = a; }
        if (y1 > y2) { a = y1; y1 = y2; y2 = a; }
        if (z1 > z2) { a = z1; z1 = z2; z2 = a; }
    }

    public boolean contem(String mundoAlvo, int x, int y, int z) {
        return mundo.equals(mundoAlvo) && x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }
}
