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
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
public class TabItemMapaAsyncTask extends AsyncTask<List<Encomenda>, Encomenda, Void> {

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private List<Encomenda> encomendas;
    private TabItemMapaFragment abaMapa;

    private Timer timer;
    private TimerTask timerTask;

    private int count;
    private GoogleMap map;
    private HashMap<Integer, Encomenda> mMarkersHashMap;

    private boolean primeiroCarregamentoLista = false;
    private LatLng latLngPrimeiraEntrega;
    private boolean primeiroLatLngEncontrado = false;





    public TabItemMapaAsyncTask(Context mContext, TabItemMapaFragment abaMapa, Timer timer, TimerTask timerTask, ProgressDialog mProgressDialog, GoogleMap map) {

        this.mContext = mContext;
        this.timer = timer;
        this.timerTask = timerTask;
        this.mProgressDialog = mProgressDialog;
        this.abaMapa = abaMapa;
        this.map = map;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }



    @Override
    protected Void doInBackground(List<Encomenda>... param) {

        List<Encomenda> encomendas = param[0];

        this.encomendas = encomendas;

        mMarkersHashMap = new HashMap<>();
        abaMapa.setmMarkersHashMap(mMarkersHashMap);

//        for (int i=0;  i< encomendas.size(); i++) {
        for (Encomenda encomenda : encomendas) {

//            Encomenda encomenda = encomendas.get(i);


            StringBuilder stringBuilder = new StringBuilder();
            validateAndAppend(encomenda.getEndereco(), ", ", stringBuilder);
            validateAndAppend(encomenda.getNrEndereco(), ", ", stringBuilder);
            validateAndAppend(encomenda.getBairro(), ", ", stringBuilder);
            validateAndAppend(encomenda.getCidade(), ", ", stringBuilder);
            validateAndAppend(encomenda.getUF(), ", ", stringBuilder);
            validateAndAppend(encomenda.getCep(), "", stringBuilder);

            encomenda.setLocation(stringBuilder.toString());

//            int cod = i+1;

            findByAddress(stringBuilder.toString(), encomenda, encomenda.getIdOrdem());
        }

        return null;
    }



    @Override
    protected void onProgressUpdate(Encomenda... encomendas) {
        super.onProgressUpdate(encomendas);

        drawMarker(abaMapa.getMap(), encomendas[0]);
    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        mProgressDialog.hide();

        if (timer != null) {

            timer.cancel();

            timer = null;
        }
    }




    private void drawMarker(GoogleMap gmap, Encomenda encomenda)
    {
        LatLng latLng = new LatLng(encomenda.getLatitude(), encomenda.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(encomenda.getNmDestinatario()).snippet(encomenda.getLocation());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.ic_marker_map, String.valueOf(encomenda.getIdOrdem()))));

        abaMapa.getMap().addMarker(markerOptions);


        Marker marker = abaMapa.getMap().addMarker(markerOptions);
        marker.setTag(encomenda.getId());



        abaMapa.getmMarkersHashMap().put(encomenda.getId(), encomenda);

        Runnable addMarker = new AddMarker(abaMapa.getMap(), markerOptions);
        ((Activity)mContext).runOnUiThread(addMarker);
    }




    private void validateAndAppend(String value, String separator, StringBuilder stringBuilder){

        if(value != null && !value.equals("")){

            stringBuilder.append(value);
            stringBuilder.append(separator);
        }
    }




    private void findByAddress(String location, Encomenda encomenda, int id) {

        List<android.location.Address> addressList = null;

        if (location != null || !location.equals("")) {

            Geocoder geocoder = new Geocoder(mContext);

            try {

                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {

                e.printStackTrace();
            }

            if (addressList != null && addressList.size() > 0) {

                android.location.Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                encomenda.setLatitude(address.getLatitude());
                encomenda.setLongitude(address.getLongitude());

                if(primeiroLatLngEncontrado == false){

                    primeiroLatLngEncontrado = true;

                    latLngPrimeiraEntrega = latLng;
                }

                publishProgress(encomenda);

                System.out.println("Marcado.. " + count);

            } else {

                System.out.println("Nao encontrado.. " + count);
            }

            count++;
        }

    }




    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(mContext, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(mContext, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }

    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

}
