package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class BaixaOcorrenciaVolley {

    private Context mContext;
    private DelegateEntregaAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;

    private Encomenda encomenda;



    /**
     * LISTA OCORRENCIAS
     *
     * @param mContext
     * @param delegate
     */
    public BaixaOcorrenciaVolley(Context mContext, Encomenda encomenda, DelegateEntregaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.sessionManager = new SessionManager(mContext);

        this.encomenda = encomenda;

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
        String id = sessionManager.getValue("id");

        String idEncomenda = String.valueOf(encomenda.getIdEncomenda());

        Double lat = encomenda.getLatitude() != null ? encomenda.getLatitude() : 0.0;
        Double lng = encomenda.getLongitude() != null ? encomenda.getLongitude() : 0.0;
        String latitude = String.valueOf(lat);
        String longitude = String.valueOf(lng);

        Ocorrencia ocorrencia = encomenda.getOcorrencia();
        int idOcorrencia = ocorrencia.getTipoOcorrencia().getId();
        String idStatus = String.valueOf(idOcorrencia);



        params.put("IdMotorista", id);
        params.put("IdEncomenda", idEncomenda);
        params.put("IdStatus", idStatus);
        params.put("FotoOcorrencia", ocorrencia.getFotoOcorrenciaBase64() != null ? ocorrencia.getFotoOcorrenciaBase64() : "");
        params.put("Latitude", latitude);
        params.put("Longitude", longitude);
        params.put("DataIteracao", encomenda.getDataBaixa());

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Ocorrencias> myReq = new GsonRequest<Ocorrencias>(
                Request.Method.POST,
                EnumAPI.BAIXA_OCORRENCIA.getValue(),
                Ocorrencias.class,
                headers,
                params,
                successListener(),
                errorListener());

        myReq.setRetryPolicy(VolleyTimeout.recuperarTimeout());
        queue.add(myReq);
    }



    private Response.Listener<Ocorrencias> successListener() {
        return new Response.Listener<Ocorrencias>() {

            @Override
            public void onResponse(Ocorrencias ocorrencias) {

                try {

                    Toast.makeText(mContext, "API - BAIXA OCORRENCIA - SUCESSO", Toast.LENGTH_LONG).show();

                    encomenda.setFlagEnviado(EnumStatusEnvio.SINCRONIZADO.getKey());
                    encomendaBusiness.update(encomenda);

                    delegate.processFinish(true, "BAIXA OCORRENCIA - OK");

                } catch (Exception e) {

                    Toast.makeText(mContext, "API - BAIXA OCORRENCIA - EXCEPTION", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "API - BAIXA OCORRENCIA - ERROR", Toast.LENGTH_LONG).show();

                if(InternetStatus.isNetworkAvailable(mContext)){

                    if(error.networkResponse != null) {

                        if (error.networkResponse.statusCode == EnumHttpError.ERROR_401.getErrorInt()) {

                            Toast.makeText(mContext, R.string.error_invalid_email_or_password, Toast.LENGTH_LONG).show();

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
