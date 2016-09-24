package br.com.a3rtecnologia.baixamobile.tab_lista;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class FinalizarViagemVolley {


    private Context mContext;
    private DelegateEncomendaAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;


    /**
     * FINALIZAR VIAGEM,
     *
     * ENCERRAR VIAGEM ENVIANDO ID DO MOTORISTA
     *
     * @param mContext
     * @param delegate
     */
    public FinalizarViagemVolley(Context mContext, DelegateEncomendaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);
        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        requestAPI();
    }




    private Map<String, String> header(){

        Map<String, String> headers = new HashMap();
        headers.put("CodigoAutenticacao", EnumAPI.HEADER_PARAM_1.getValue());
        headers.put("SenhaAutenticacao", EnumAPI.HEADER_PARAM_2.getValue());

        return headers;
    }

    private Map<String, String> param(){

        Map<String, String> params = new HashMap();

        String id = sessionManager.getValue("id");

        params.put("IdMotorista", id);


        //RECUPERA MINHA LOCALIZACAO ATUAL
        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mContext, TabItemMapaFragment.map);
        timerTaskLocation.startTimer();

        LatLng latLng = timerTaskLocation.getMyLatLng();
        timerTaskLocation.stoptimertask();

        String dataFimViagem = DateUtil.getDataAtual();
        Status status = statusBusiness.getStatus();
        status.setDataFimViagem(dataFimViagem);
        statusBusiness.salvar(status);

        params.put("DataFinalizacao", status.getDataFimViagem());

        if(latLng != null){

            String latitude = String.valueOf(latLng.latitude);
            String longitude = String.valueOf(latLng.longitude);
            params.put("Latitude", latitude);
            params.put("Longitude", longitude);

        }else{

            params.put("Latitude", "0");
            params.put("Longitude", "0");
        }


//        params.put("DataFinalizacao", "2016-09-04T19:05:16.5809712+00:00");


        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Encomendas> myReq = new GsonRequest<Encomendas>(
                Request.Method.POST,
                EnumAPI.FINALIZA_VIAGEM.getValue(),
                Encomendas.class,
                headers,
                params,
                successListener(),
                errorListener());

        myReq.setRetryPolicy(VolleyTimeout.recuperarTimeout());
        queue.add(myReq);
    }



    private Response.Listener<Encomendas> successListener() {
        return new Response.Listener<Encomendas>() {

            @Override
            public void onResponse(Encomendas encomendas) {

                try {

                    if((encomendas != null && encomendas.getEncomendas() != null) && encomendas.getEncomendas().size() > 0){

                        String n = String.valueOf(encomendas.getEncomendas().size());

                        delegate.processFinish(true, "Você ainda possui " + n + " encomendas para dar baixa.\n\nDeseja finalizar todas?");

                    }else{

                        delegate.processFinish(true, "SUCESSO");
                    }

                    Toast.makeText(mContext, "FINALIZAR VIAGEM - SUCESSO", Toast.LENGTH_LONG).show();


                } catch (Exception e) {

                    Toast.makeText(mContext, "FINALIZAR VIAGEM - EXCEPTION", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "FINALIZAR VIAGEM - ERROR", Toast.LENGTH_LONG).show();

                if(InternetStatus.isNetworkAvailable(mContext)){

                    if (error.networkResponse.statusCode == EnumHttpError.ERROR_401.getErrorInt()) {

                        Toast.makeText(mContext, R.string.error_invalid_email_or_password, Toast.LENGTH_LONG).show();

                        delegate.processCanceled(false);
//                        StatusDialog dialog = new StatusDialog((Activity)mContext, false, error.networkResponse.statusCode);

                    }else if (error.networkResponse.statusCode == EnumHttpError.ERROR_400.getErrorInt()) {

                        delegate.processCanceled(false);
//                        StatusDialog dialog = new StatusDialog((Activity)mContext, false, error.networkResponse.statusCode);
                    }else if(error.networkResponse.statusCode == 404){

                        delegate.processCanceled(false);
                    }

                }else{

                    delegate.processCanceled(false);
//                    StatusDialog dialog = new StatusDialog((Activity)mContext, false, error.networkResponse.statusCode);
                }
            }
        };
    }

}
