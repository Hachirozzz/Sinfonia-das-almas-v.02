package nucleorp.dados;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Fila única de acesso ao banco, em thread própria. Uma thread só garante a
 * ordem das gravações e mantém a thread principal do servidor livre de disco.
 */
public final class Banco {

    @FunctionalInterface public interface Tarefa<T> { T rodar(Repositorio r) throws SQLException; }

    private final Repositorio repositorio;
    private final ExecutorService fila = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "NucleoRP-banco");
        t.setDaemon(false);
        return t;
    });

    public Banco(Repositorio repositorio) { this.repositorio = repositorio; }

    public <T> CompletableFuture<T> pedir(Tarefa<T> tarefa) {
        CompletableFuture<T> f = new CompletableFuture<>();
        try {
            fila.execute(() -> {
                try { f.complete(tarefa.rodar(repositorio)); }
                catch (Throwable e) { f.completeExceptionally(e); }
            });
        } catch (java.util.concurrent.RejectedExecutionException e) {
            f.completeExceptionally(e);
        }
        return f;
    }

    /** Espera as gravações pendentes terminarem e fecha o arquivo. Devolve false se o prazo estourou. */
    public boolean fechar(long segundos) {
        fila.shutdown();
        boolean terminou;
        try { terminou = fila.awaitTermination(segundos, TimeUnit.SECONDS); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); terminou = false; }
        try { repositorio.close(); } catch (SQLException ignorado) { }
        return terminou;
    }
}
