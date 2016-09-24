package br.com.a3rtecnologia.baixamobile.entrega;

/**
 * Created by maclemon on 30/07/16.
 */
public interface DelegateEntregaAsyncResponse {

    /**
     * return true for finished
     * @param finish
     */
    void processFinish(boolean finish, String resposta);

    /**
     * return false for cancel
     * @param cancel
     */
    void processCanceled(boolean cancel);
}
