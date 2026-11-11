package nucleorp.regra;

/**
 * Recusa de uma regra. Carrega a chave da mensagem (em mensagens.yml) e os
 * valores que entram nela, em pares nome/valor. O texto nunca fica no código.
 */
public class ErroRegra extends RuntimeException {
    private final String chave;
    private final String[] pares;

    public ErroRegra(String chave, Object... pares) {
        super(chave, null, false, false);
        if (pares.length % 2 != 0) throw new IllegalArgumentException("pares nome/valor incompletos");
        this.chave = chave;
        this.pares = new String[pares.length];
        for (int i = 0; i < pares.length; i++) this.pares[i] = String.valueOf(pares[i]);
    }

    public String chave() { return chave; }
    public String[] pares() { return pares.clone(); }
}
