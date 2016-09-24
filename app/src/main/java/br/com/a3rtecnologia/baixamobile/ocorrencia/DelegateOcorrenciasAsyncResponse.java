package br.com.a3rtecnologia.baixamobile.ocorrencia;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;

/**
 * Created by maclemon on 30/07/16.
 */
public interface DelegateOcorrenciasAsyncResponse {

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
