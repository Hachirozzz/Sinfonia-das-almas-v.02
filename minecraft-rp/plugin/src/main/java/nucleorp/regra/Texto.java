package nucleorp.regra;

import java.text.Normalizer;
import java.util.Locale;

/** Limpeza de texto digitado por jogador. Toda entrada é tratada como hostil. */
public final class Texto {
    private Texto() {}

    public static String semAcento(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
    }

    /** Forma usada para comparar identificadores e nomes sem diferenciar maiúsculas nem acentos. */
    public static String chave(String s) {
        return semAcento(s.strip()).toLowerCase(Locale.ROOT);
    }

    /**
     * Texto livre de uma linha: normaliza, troca qualquer espaço em branco por
     * espaço simples e remove caracteres de controle, de formatação invisível e
     * o símbolo de cor antigo do Minecraft.
     */
    public static String linha(String s) {
        String n = Normalizer.normalize(s, Normalizer.Form.NFC);
        StringBuilder sb = new StringBuilder(n.length());
        n.codePoints().forEach(c -> {
            if (Character.isWhitespace(c) || Character.isSpaceChar(c)) { sb.append(' '); return; }
            int tipo = Character.getType(c);
            if (tipo == Character.CONTROL || tipo == Character.FORMAT || tipo == Character.SURROGATE
                    || tipo == Character.PRIVATE_USE || tipo == Character.UNASSIGNED || c == '§') return;
            sb.appendCodePoint(c);
        });
        return sb.toString().replaceAll(" {2,}", " ").strip();
    }

    public static int tamanho(String s) { return s.codePointCount(0, s.length()); }
}
