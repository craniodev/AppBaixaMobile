package br.com.a3rtecnologia.baixamobile.encomenda;

/**
 * Created by maclemon on 03/09/16.
 */

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class EncomendaVolley {

    private Context mContext;
    private DelegateEncomendasAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;
//    private Dao<Encomenda, Integer> encomendaDao;
//    private Dao<Atualizacao, Integer> atualizacaoDao;
//    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private EncomendaBusiness encomendaBusiness;
    private Timer timer;



    public EncomendaVolley(Context mContext, DelegateEncomendasAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);

        encomendaBusiness = new EncomendaBusiness(mContext);

        requestAPI();
    }

    public EncomendaVolley(Context mContext, Timer timer, DelegateEncomendasAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.timer = timer;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);

        encomendaBusiness = new EncomendaBusiness(mContext);

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
        params.put("TipoEncomenda", EnumAPI.ID_TIPO_ENCOMENDA.getValue());

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Encomendas> myReq = new GsonRequest<Encomendas>(
                Request.Method.POST,
                EnumAPI.LISTA_ENCOMENDA.getValue(),
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

//                    timer.cancel();

                    /**
                     * LIMPAR BASE PARA GARANTIR
                     */
                    encomendaBusiness.deleteAll();

                    /**
                     * INSERE TODAS ENCOMENDAS
                     */
                    int indexCount = 1;
                    for (Encomenda encomenda : encomendas.getEncomendas()) {

                        encomenda.setIdOrdem(indexCount);
//                        encomendaDao.create(encomenda);
                        encomendaBusiness.salvar(encomenda);

                        indexCount++;
                    }

                    delegate.processFinish(true, encomendas);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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
