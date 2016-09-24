package br.com.a3rtecnologia.baixamobile.ocorrencia;

/**
 * Created by maclemon on 30/07/16.
 */
public interface DelegateOcorrenciaAsyncResponse {

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
