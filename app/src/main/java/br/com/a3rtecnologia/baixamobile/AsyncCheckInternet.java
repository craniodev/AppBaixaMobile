package br.com.a3rtecnologia.baixamobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by maclemon on 16/10/16.
 */

public class AsyncCheckInternet extends AsyncTask<String, Void, Boolean> {

    public static final int TIME_OUT = 10 * 1000;

    private OnCallBack listener;
    private Context mContext;



    public interface OnCallBack {

        void onBack(Boolean isOnline);
    }



    public AsyncCheckInternet(Context mContext, OnCallBack listener) {
        this.listener = listener;
        this.mContext = mContext;
    }



    @Override
    protected void onPreExecute() {
    }



    @Override
    protected Boolean doInBackground(String... params) {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ( (networkInfo != null && networkInfo.isConnected()) &&
                ((networkInfo.getType() == ConnectivityManager.TYPE_WIFI) ||
                        (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE))) {

            HttpURLConnection urlc;

            try {

                urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setConnectTimeout(TIME_OUT);
                urlc.connect();

                if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return true;
                } else {
                    return false;
                }

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return false;

            } catch (IOException e) {

                e.printStackTrace();
                return false;
            }

        } else {

            return false;
        }
    }



    @Override
    protected void onPostExecute(Boolean result) {

        if (listener != null) {

            listener.onBack(result);
        }
    }

}
