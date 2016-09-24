package br.com.a3rtecnologia.baixamobile.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by maclemon on 03/08/16.
 */
public class KeyboardUtil {

    public static void hideKeyboard(Context mContext, View editText){

        /* code to show keyboard on startup.this code is not working.*/
        InputMethodManager imm =  (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }



    public static void showKeyboard(Context mContext, View editText){


    }

}
