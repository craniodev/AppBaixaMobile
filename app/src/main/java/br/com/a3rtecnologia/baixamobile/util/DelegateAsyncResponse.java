package br.com.a3rtecnologia.baixamobile.util;

/**
 * Created by maclemon on 30/07/16.
 */
public interface DelegateAsyncResponse {

    /**
     * return true for finished
     * @param finish
     */
    void processFinish(boolean finish);

    /**
     * return false for cancel
     * @param cancel
     */
    void processCanceled(boolean cancel);
}
