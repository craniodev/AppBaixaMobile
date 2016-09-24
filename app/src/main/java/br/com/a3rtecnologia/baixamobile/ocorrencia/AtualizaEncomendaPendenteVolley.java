package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.tab_lista.TabItemListaFragment;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class AtualizaEncomendaPendenteVolley {

    private Context mContext;
    private DelegateEntregaAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;

    Encomendas encomendas;


    /**
     * LISTA OCORRENCIAS
     *
     * @param mContext
     * @param delegate
     */
    public AtualizaEncomendaPendenteVolley(Context mContext, Encomendas encomendas, DelegateEntregaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);
        this.encomendaBusiness = new EncomendaBusiness(mContext);

        this.encomendas = encomendas;


        String id = sessionManager.getValue("id");
        Integer idInt = Integer.valueOf(id);
        this.encomendas.setIdMotorista(idInt);

        requestAPI();
    }




    private Map<String, String> header(){

        Map<String, String> headers = new HashMap();
        headers.put("CodigoAutenticacao", EnumAPI.HEADER_PARAM_1.getValue());
        headers.put("SenhaAutenticacao", EnumAPI.HEADER_PARAM_2.getValue());

        return headers;
    }

    private Map<String, String> param(){

        Map<String, String> params = new HashMap<>();

//        String id = sessionManager.getValue("id");
//        params.put("IdMotorista", id);

        return null;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();


        GsonRequest<Encomendas> myReq = new GsonRequest<Encomendas>(
                Request.Method.POST,
                EnumAPI.ATUALIZA_ENCOMENDA_PENDENTE.getValue(),
                encomendas,
                Encomendas.class,
                params,
                headers,
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

                    boolean resposta = false;
                    if(encomendas.getEncomendas() != null && encomendas.getEncomendas().size() > 0){

                        List<Long> encomendasId = new ArrayList<Long>();
                        for (Encomenda encomenda : encomendas.getEncomendas()){

                            encomendasId.add(encomenda.getIdEncomenda());
                        }

                        List<Encomenda> encomendasTratadas = encomendaBusiness.getEncomendasByIds(encomendasId);

                        if(encomendasTratadas != null && encomendasTratadas.size() > 0) {
                            for (Encomenda encomenda : encomendasTratadas) {

                                encomenda.setIdStatus(EnumEncomendaStatus.EM_ROTA.getKey());
                                encomenda.setDescStatus(EnumEncomendaStatus.EM_ROTA.getValue());
                                encomenda.setFlagTratado(true);

                                encomendaBusiness.update(encomenda);
                            }

                            resposta = true;
                        }
                    }

                    /**
                     * UPDATE LISTA ENCOMENDAS
                     *
                     * ALTERACAO STATUS BOLINHAS
                     */
                    TabItemListaFragment.updateAdapter();


                    Toast.makeText(mContext, "API - ATUALIZA ENCOMENDA PENDENTE - SUCESSO", Toast.LENGTH_LONG).show();



                    delegate.processFinish(resposta, "ATUALIZA ENCOMENDA PENDENTE - OK");

                } catch (Exception e) {

                    Toast.makeText(mContext, "API - ATUALIZA ENCOMENDA PENDENTE - EXCEPTION", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "API - ATUALIZA ENCOMENDA PENDENTE - ERROR", Toast.LENGTH_LONG).show();

                if(InternetStatus.isNetworkAvailable(mContext)){

                    if(error.networkResponse != null) {

                        if (error.networkResponse.statusCode == EnumHttpError.ERROR_401.getErrorInt()) {

                            delegate.processCanceled(false);

                        } else if (error.networkResponse.statusCode == EnumHttpError.ERROR_400.getErrorInt()) {

                            delegate.processCanceled(false);

                        } else if (error.networkResponse.statusCode == 404) {

                            delegate.processCanceled(false);
                        }

                    }else{

                        delegate.processCanceled(false);
                    }

                }else{

                    delegate.processCanceled(false);
                }
            }
        };
    }

}
