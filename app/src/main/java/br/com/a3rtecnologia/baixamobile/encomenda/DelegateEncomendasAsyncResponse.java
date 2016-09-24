package br.com.a3rtecnologia.baixamobile.encomenda;

/**
 * Created by maclemon on 30/07/16.
 */
public interface DelegateEncomendasAsyncResponse {

    /**
     * return true for finished
     * @param finish
     */
    void processFinish(boolean finish, Encomendas encomendas);

    /**
     * return false for cancel
     * @param cancel
     */
    void processCanceled(boolean cancel);
}
