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
    private EncomendaBusiness encomendaBusiness;
    private Timer timer;
    private String statusEncomenda;
    private int isDownload;



    public EncomendaVolley(Context mContext, String statusEncomenda, int isDownload, DelegateEncomendasAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);
        this.statusEncomenda = statusEncomenda;

        this.isDownload = isDownload;

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
        params.put("TipoEncomenda", statusEncomenda);

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
                     * removido
                     */
//                    /**
//                     * LIMPAR BASE PARA GARANTIR
//                     */
//                    encomendaBusiness.deleteAll();

                    /**
                     * INSERE TODAS ENCOMENDAS
                     */
                    int indexCount = 1;
                    for (Encomenda encomenda : encomendas.getEncomendas()) {

                        /**
                         * id para visualizacao(bolinhas)
                         */
                        encomenda.setIdOrdem(indexCount);


                        if(isDownload == 1) {
                            if (statusEncomenda.equalsIgnoreCase(EnumAPI.ID_TIPO_ENCOMENDA_EM_ROTA.getValue())) {

                                /**
                                 * MANTEM STATUS
                                 */
                                encomenda.setIdStatus(EnumEncomendaStatus.EM_ROTA.getKey());
                                encomenda.setDescStatus(EnumEncomendaStatus.EM_ROTA.getValue());

                                System.out.println("Donwload encomendas do TIPO " + statusEncomenda + " - EM ROTA - ID " + encomenda.getIdOrdem());


                            } else if (statusEncomenda.equalsIgnoreCase(EnumAPI.ID_TIPO_ENCOMENDA_ENTREGUE.getValue())) {

                                encomenda.setIdStatus(EnumEncomendaStatus.ENTREGUE.getKey());
                                encomenda.setDescStatus(EnumEncomendaStatus.ENTREGUE.getValue());

                                System.out.println("Donwload encomendas do TIPO " + statusEncomenda + " - EM ROTA - ID " + encomenda.getIdOrdem());

                            } else if (statusEncomenda.equalsIgnoreCase(EnumAPI.ID_TIPO_ENCOMENDA_PENDENTE.getValue())) {

                                encomenda.setIdStatus(EnumEncomendaStatus.OCORRENCIA.getKey());
                                encomenda.setDescStatus(EnumEncomendaStatus.OCORRENCIA.getValue());

                                System.out.println("Donwload encomendas do TIPO " + statusEncomenda + " - EM ROTA - ID " + encomenda.getIdOrdem());
                            }

                        }

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

                    }else if (error.networkResponse.statusCode == EnumHttpError.ERROR_400.getErrorInt()) {

                        delegate.processCanceled(false);

                    }else if(error.networkResponse.statusCode == 404){

                        delegate.processCanceled(false);
                    }

                }else{

                    delegate.processCanceled(false);
                }
            }
        };
    }
}
