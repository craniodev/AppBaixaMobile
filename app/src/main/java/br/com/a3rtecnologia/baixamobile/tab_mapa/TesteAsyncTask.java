package br.com.a3rtecnologia.baixamobile.tab_mapa;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;

/**
 * Created by maclemon on 19/08/16.
 */
public class TesteAsyncTask extends AsyncTask<List<Encomenda>, Encomenda, Void> {

    private Context mContext;
    private ProgressDialog mProgressDialog;





    public TesteAsyncTask(Context mContext, ProgressDialog mProgressDialog) {

        this.mContext = mContext;
        this.mProgressDialog = mProgressDialog;

//        showProgress(true);
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }



    @Override
    protected Void doInBackground(List<Encomenda>... param) {


        return null;
    }



    @Override
    protected void onProgressUpdate(Encomenda... encomendas) {
        super.onProgressUpdate(encomendas);


    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }
}
