package br.com.a3rtecnologia.baixamobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.util.Date;

public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "baixa_mobile_preferences";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    /**
     * CLEAR ALL
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }



    /**
     * JSON
     */
    public void saveJson(String pspReference, String json) {
        editor.putString(pspReference, json);
        editor.commit();
    }

    public String loadJson(String pspReference) {

        return pref.getString(pspReference, null);
    }



    /**
     * GENERIC VALUE
     * @param value
     * @param key
     */
    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
    public String getValue(String value) {

        return pref.getString(value, "");
    }



    public void setPrimeiroLogin(String primeiroLogin) {
        editor.putString("primeiroLogin", primeiroLogin);
        editor.commit();
    }
    public String isPrimeiroLogin() {

        return pref.getString("primeiroLogin", "");
    }



    public void setFinalizarViagemOcorrenciaForcado(String value) {
        editor.putString("finalizarViagemOcorrecianForcado", value);
        editor.commit();
    }
    public String getFinalizarViagemOcorrenciaForcado() {

        return pref.getString("finalizarViagemOcorrecianForcado", "");
    }


//    public void setEmail(String value) {
//        editor.putString("email", value);
//        editor.commit();
//    }
//    public String getEmail(String value) {
//
//        return pref.getString(value, "");
//    }




//    public void updateVersionLater() {
//
//        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
//        Date currentDate = Calendar.getInstance().getTime();
//        String dateStr = fmtOut.format(currentDate);
//        Calendar.getInstance().getTime().getTime();
//
//        editor.putString("updateVersionLater", dateStr);
//        editor.commit();
//    }



    /**
     * IMAGE
     * @param b
     * @param name
     */
    public void saveImg(byte[] b, String name) {
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        editor.putString(name, encodedImage);
        editor.commit();
    }

	public String getImg(String name) {

		return pref.getString(name, "");
	}



    /**
     * VERSION
     * @return
     */
    public void saveVersion(String version){

        editor.putString("version", version);
        editor.commit();
    }

    public String getVersion() {

        return pref.getString("version", "");
    }

}
