package br.com.a3rtecnologia.baixamobile.login;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.Retorno;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.DelegateAsyncResponse;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 28/07/16.
 */
public class VerificaViagemIniciadaVolley {

    private Context mContext;
    private DelegateAsyncResponse delegate;
    private RequestQueue queue;

    private StatusBusiness statusBusiness;
    private SessionManager sessionManager;



    public VerificaViagemIniciadaVolley(Context mContext, DelegateAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);
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

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Retorno> myReq = new GsonRequest<Retorno>(
                Request.Method.POST,
                EnumAPI.VERIFICA_VIAGEM_INICIADA.getValue(),
                Retorno.class,
                headers,
                params,
                successListener(),
                errorListener());

        myReq.setRetryPolicy(VolleyTimeout.recuperarTimeout());
        queue.add(myReq);
    }



    private Response.Listener<Retorno> successListener() {
        return new Response.Listener<Retorno>() {

            @Override
            public void onResponse(Retorno responseObject) {

                try {

                    //objeto retorno server
                    if(responseObject.getResposta().equalsIgnoreCase("OK")){

                        statusBusiness.startJornadaTrabalho();
                    }

                    delegate.processFinish(true);

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

                        delegate.processCanceled(false);
                    }

                }else{

                    StatusDialog dialog = new StatusDialog((Activity)mContext, "Status", "Sem conexão com internet", false);
                }
            }
        };
    }

}
