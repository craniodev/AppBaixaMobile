package br.com.a3rtecnologia.baixamobile;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumStatusEnvio {

    SINCRONIZADO(1),
    NAO_SINCRONIZADO(0);

    private int key;



    private EnumStatusEnvio(int key){

        this.key = key;
    }



    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
