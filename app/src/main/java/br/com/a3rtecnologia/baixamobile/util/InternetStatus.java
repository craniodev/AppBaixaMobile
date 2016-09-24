package br.com.a3rtecnologia.baixamobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by maclemon on 31/07/16.
 */
public class InternetStatus {


    public static boolean isNetworkAvailable(Context mContext){

        ConnectivityManager ConnectionManager=(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();

        boolean status = false;

        if(networkInfo != null && networkInfo.isConnected()==true ) {

            status = true;
        }

        return status;
    }

}
