package nucleorp.regra;

/** Como um teste terminou. */
public enum Desfecho {
    SUCESSO, FALHA, TRIUNFO, TROPECO,
    /** Teste sem dificuldade: só o total importa e o narrador decide. */
    SEM_DIFICULDADE;

    public boolean sucesso() { return this == SUCESSO || this == TRIUNFO; }
    public boolean falha() { return this == FALHA || this == TROPECO; }
}
