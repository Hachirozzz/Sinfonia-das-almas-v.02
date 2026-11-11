package nucleorp.regra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** O "NdL" do comando /dado. */
public record DadoSolto(int quantidade, int lados) {
    private static final Pattern FORMATO = Pattern.compile("(\\d{0,3})[dD](\\d{1,4})");

    public static DadoSolto ler(String texto, int padraoLados, int maxDados, int maxLados) {
        if (texto == null || texto.isBlank()) return new DadoSolto(1, padraoLados);
        Matcher m = FORMATO.matcher(texto.strip());
        if (!m.matches()) throw new ErroRegra("dado.erro.formato");
        int n = m.group(1).isEmpty() ? 1 : Integer.parseInt(m.group(1));
        int l = Integer.parseInt(m.group(2));
        if (n < 1 || n > maxDados || l < 2 || l > maxLados)
            throw new ErroRegra("dado.erro.limite", "dados", maxDados, "lados", maxLados);
        return new DadoSolto(n, l);
    }

    public int[] rolar(Dado dado) {
        int[] r = new int[quantidade];
        for (int i = 0; i < quantidade; i++) r[i] = dado.rolar(lados);
        return r;
    }
}
