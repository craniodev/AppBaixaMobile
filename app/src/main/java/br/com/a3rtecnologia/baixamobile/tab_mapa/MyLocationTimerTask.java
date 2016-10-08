package br.com.a3rtecnologia.baixamobile.tab_mapa;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class MyLocationTimerTask {

    public static ProgressDialog mProgressDialog;

    private Context mContext;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;


    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    private GoogleMap map;
    private LatLng mylatLng;



    public MyLocationTimerTask(Context mContext, GoogleMap map){

        this.mContext = mContext;
        this.map = map;
    }



    public void startTimer() {

        //set a new Timer
        timer = new Timer();

        showProgress(true);
        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 500, 1000); //
    }

    public void stoptimertask() {

        //stop the timer, if it's not already null
        if (timer != null) {

            timer.cancel();

            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {

                        getMyLatLng();
                    }
                });
            }
        };
    }




    public LatLng getMyLatLng(){

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return;
        }
        if(map != null){

            map.setMyLocationEnabled(true);
        }

        LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location != null){

            mylatLng = new LatLng(location.getLatitude(), location.getLongitude());

        }else{

            mylatLng = new LatLng(0, 0);
        }

        return mylatLng;
    }



    private void createLoading(Context mContext){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Carregando encomendas no mapa");
    }

    private void createLoading(Context mContext, String message){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    public static void showProgress(boolean isShow){

        if(mProgressDialog != null && isShow && !mProgressDialog.isShowing()){

            mProgressDialog.show();

        }else if(mProgressDialog != null && !isShow && mProgressDialog.isShowing()){

            mProgressDialog.hide();
        }
    }

}
