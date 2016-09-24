package br.com.a3rtecnologia.baixamobile.encomenda;

/**
 * Created by maclemon on 30/07/16.
 */
public interface DelegateEncomendaAsyncResponse {

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
