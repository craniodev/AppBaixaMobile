package br.com.a3rtecnologia.baixamobile.tab_mapa;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega.ListaItemDetalheDialog;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega.ListaItemDetalheEncerrarDialog;


/**
 * Created by maclemon on 31/07/16.
 */
public class TabItemMapaFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment fragment;
    public static GoogleMap map;
    public static Marker marker;
    private HashMap<Integer, Encomenda> mMarkersHashMap;
    private Context mContext;

    private ProgressDialog mProgressDialog;

    private LatLng mylatLng;

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

//    public static boolean isRemoveMarkerMap;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getContext();
        encomendaBusiness = new EncomendaBusiness(mContext);
        statusBusiness = new StatusBusiness(mContext);

        createLoading(mContext);

        mMarkersHashMap = new HashMap<>();

        return inflater.inflate(R.layout.activity_maps, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);

        if (fragment == null) {

            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

        fragment.getMapAsync(this);
    }



    @Override
    public void onStart() {
        super.onStart();

        System.out.print("Atualizar mapa");

//        if(isRemoveMarkerMap){
//
//            TabItemMapaFragment.isRemoveMarkerMap = false;
//            marker.remove();
//        }
    }



    /**
     * MINHA LOCALIZACAO
     */
    public void getMyLatLng(){

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
        map.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location != null){

            mylatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

        zoomMyLocation();
    }



    /**
     * ZOOM LOCALIZACAO ATUAL
     */
    private void zoomMyLocation(){

        if(mylatLng != null) {

            final CameraPosition cameraPosition = new CameraPosition.Builder().target(mylatLng).zoom(10).bearing(0).build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);

            map.animateCamera(update, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onCancel() {

                }
            });

            map.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
            map.animateCamera(update);
        }
    }







    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;


        getMyLatLng();






        /**
         * CAMERA POSITION
         */
//        if(latLngPrimeiraEntrega != null) {
//
//            final CameraPosition cameraPosition = new CameraPosition.Builder().target(latLngPrimeiraEntrega).zoom(15).bearing(0).build();
//            CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
//
//            map.animateCamera(update, new GoogleMap.CancelableCallback() {
//                @Override
//                public void onFinish() {
//
//                }
//
//                @Override
//                public void onCancel() {
//
//                }
//            });
//        }





        /**
         * WINDOW ADAPTER
         */
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {

//                TextView textView = new TextView(getContext());
//                textView.setText(Html.fromHtml("<b> <font color=\"#FF000\">" + marker.getTitle() + " </font> </b>"));

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getActivity().getLayoutInflater().inflate(R.layout.infowindow_layout, null);

                // Getting the position from the marker
                LatLng latLng = marker.getPosition();


                // Getting reference to the TextView to set latitude
                TextView nomeRemetenteTextView = (TextView) v.findViewById(R.id.nomeRemetente);

                // Getting reference to the TextView to set longitude
                TextView endereco1TextView = (TextView) v.findViewById(R.id.endereco_1);

                // Setting the latitude
                nomeRemetenteTextView.setText(marker.getTitle());

                // Setting the longitude
                endereco1TextView.setText(marker.getSnippet());
                //endereco2TextView.setText("Sao Paulo - SP - 05145-000");

                // Returning the view containing InfoWindow contents




                return v;
            }
        });






        /**
         * CAMERA MOVE
         */
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                if(marker != null){

                    marker.remove();
                }
            }
        });





        /**
         * MAP CLICK
         */
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {



            }
        });





        /**
         * MARKER CLICK
         */
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                return false;
            }
        });





        /**
         * WINDOW CLICK
         */
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                acaoEventClick(marker);
            }
        });

    }




    private void acaoEventClick(Marker marker){

        this.marker = marker;

        Encomenda encomenda = encomendaBusiness.buscarPorNome(marker.getTitle());



        boolean existe = statusBusiness.existeEncomendaEmAndamento();
        boolean iniciadoJornada = statusBusiness.verificarViagemIniciada();
        if(!existe && iniciadoJornada){

            /**
             * ABRIR DIALOG PARA INICIAR UMA ENTREGA
             */
            ListaItemDetalheDialog dialog = new ListaItemDetalheDialog(getActivity(), encomenda);

        }else {

            Status status = statusBusiness.getStatus();

            if(status != null) {

                if (status.getIdEncomendaCorrente() == encomenda.getIdEncomenda()) {

                    /**
                     * ABRIR DIALOG PARA ENCERRAR ENCOMENDA
                     */
                    ListaItemDetalheEncerrarDialog dialog = new ListaItemDetalheEncerrarDialog(getActivity(), encomenda);

                } else {

                    /**
                     * TENTATIVA DE ABRIR UMA NOVA ENCOMENDA
                     *
                     * NOTIFICAR QUE JA EXISTE UMA ENCOMENDA EM ANDAMENTO
                     */
                    StatusDialog dialog = new StatusDialog(getActivity(), "Status Encomenda", "Existe uma encomenda em andamento.", false);
                }

            }else{

                /**
                 * INICIAR JORNADA DE TRABALHO
                 */
                StatusDialog dialog = new StatusDialog(getActivity(), "Iniciar Viagem", "Você ainda não iniciou a viagem.", false);
            }
        }
    }








    private void createLoading(Context mContext){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Carregando encomendas no mapa");
    }


    public GoogleMap getMap() {
        return map;
    }

    public HashMap<Integer, Encomenda> getmMarkersHashMap() {
        return mMarkersHashMap;
    }

    public void setmMarkersHashMap(HashMap<Integer, Encomenda> mMarkersHashMap) {
        this.mMarkersHashMap = mMarkersHashMap;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
