package br.com.a3rtecnologia.baixamobile.entrega;

/**
 * Created by maclemon on 04/09/16.
 */

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.EnumInicioEntregaEnvio;
import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class IniciarEntregaVolley {

    private Context mContext;
    private DelegateEncomendaAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;

    private Encomenda encomenda;
    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;




    /**
     * INICIAR ENTREGA,
     *
     * MANDAR LOCALIZACAO E ID DA LISTA_ENCOMENDA ENTREGUE
     *
     *
     * @param mContext
     * @param delegate
     */
    public IniciarEntregaVolley(Context mContext, Encomenda encomenda, DelegateEncomendaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.encomenda = encomenda;
        this.encomendaBusiness = new EncomendaBusiness(mContext);

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
        String idEncomendaStr = String.valueOf(encomenda.getIdEncomenda());

        //RECUPERA MINHA LOCALIZACAO ATUAL
        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mContext, TabItemMapaFragment.map);
        timerTaskLocation.startTimer();

        LatLng latLng = timerTaskLocation.getMyLatLng();
        timerTaskLocation.stoptimertask();

        String latitude = String.valueOf(latLng.latitude);
        String longitude = String.valueOf(latLng.longitude);

        params.put("Latitude", latitude);
        params.put("Longitude", longitude);
        params.put("IdMotorista", id);
        params.put("IdEncomenda", idEncomendaStr);
        params.put("DataIteracao", encomenda.getDataInicioEntrega());

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Encomendas> myReq = new GsonRequest<Encomendas>(
                Request.Method.POST,
                EnumAPI.ENTREGA.getValue(),
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

                    Toast.makeText(mContext, "API - INICIAR ENTREGA - SUCESSO", Toast.LENGTH_LONG).show();

//                    encomenda.setFlagEnviado(EnumStatusEnvio.SINCRONIZADO.getKey());
                    encomenda.setFlagInicioEntrega(EnumInicioEntregaEnvio.NAO.getKey());
                    encomendaBusiness.update(encomenda);

                    delegate.processFinish(true, "INICIAR ENTREGA - OK");

                } catch (Exception e) {

                    Toast.makeText(mContext, "API - INICIAR ENTREGA - EXCEPTION", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "INICIAR ENTREGA - ERROR", Toast.LENGTH_LONG).show();

                if(InternetStatus.isNetworkAvailable(mContext)){

                    delegate.processCanceled(false);

                }else{

                    delegate.processCanceled(false);
                }
            }
        };
    }

}
