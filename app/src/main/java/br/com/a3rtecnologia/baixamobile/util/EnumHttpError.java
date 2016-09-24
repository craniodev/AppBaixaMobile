package br.com.a3rtecnologia.baixamobile.util;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumHttpError {

    ERROR_401(401, "Não autorizado"),
    ERROR_400(400, "?");




    private int errorInt;
    private String errorStr;

    private EnumHttpError(int errorInt, String errorStr){

        this.errorInt = errorInt;
        this.errorStr = errorStr;
    }



    public int getErrorInt() {
        return errorInt;
    }

    public void setErrorInt(int errorInt) {
        this.errorInt = errorInt;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }
}
