package br.com.a3rtecnologia.baixamobile;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumInicioEntregaEnvio {

    SIM(1),
    NAO(0);

    private int key;



    private EnumInicioEntregaEnvio(int key){

        this.key = key;
    }



    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
