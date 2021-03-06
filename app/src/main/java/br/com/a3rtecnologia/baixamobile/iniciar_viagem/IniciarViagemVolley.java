package br.com.a3rtecnologia.baixamobile.iniciar_viagem;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
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
public class IniciarViagemVolley {

    private Context mContext;
    private DelegateEncomendaAsyncResponse delegate;
    private RequestQueue queue;

    private IniciarViagem iniciarViagem;




    /**
     * INICIAR VIAGEM
     *
     * @param mContext
     * @param iniciarViagem
     * @param delegate
     */
    public IniciarViagemVolley(Context mContext, IniciarViagem iniciarViagem, DelegateEncomendaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();

        this.iniciarViagem = iniciarViagem;

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

        params.put("IdMotorista", iniciarViagem.getIdMotorista());
        params.put("Latitude", iniciarViagem.getLatitude().toString());
        params.put("Longitude", iniciarViagem.getLongitude().toString());
        params.put("DataIteracao", iniciarViagem.getDataIteracao());

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Encomendas> myReq = new GsonRequest<Encomendas>(
                Request.Method.POST,
                EnumAPI.INICIA_VIAGEM.getValue(),
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

                    Toast.makeText(mContext, "API - INICIAR VIAGEM - SUCESSO", Toast.LENGTH_LONG).show();
                    delegate.processFinish(true, "INICIAR VIAGEM - OK");

                } catch (Exception e) {

                    Toast.makeText(mContext, "API - INICIAR VIAGEM - EXCEPTION", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "API - INICIAR VIAGEM - ERROR", Toast.LENGTH_LONG).show();

                if(InternetStatus.isNetworkAvailable(mContext)){

                    delegate.processCanceled(false);

                }else{

                    delegate.processCanceled(false);
                }
            }
        };
    }

}
