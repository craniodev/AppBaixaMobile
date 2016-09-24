package br.com.a3rtecnologia.baixamobile.entrega;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;


import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.ListaOcorrencia;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class BaixaEntregueVolley {

    private Context mContext;
    private DelegateEntregaAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;

    private Encomenda encomenda;
    private Recebedor recebedor;
    private LatLng latLng;



    /**
     * LISTA OCORRENCIAS
     *
     * @param mContext
     * @param delegate
     */
    public BaixaEntregueVolley(Context mContext, Encomenda encomenda, Recebedor recebedor, LatLng latLng, DelegateEntregaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();

        this.sessionManager = new SessionManager(mContext);
        this.encomendaBusiness = new EncomendaBusiness(mContext);

        this.encomenda = encomenda;
        this.recebedor = recebedor;
        this.latLng = latLng;

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

        params.put("IdMotorista", id);

        String idEncomenda = String.valueOf(encomenda.getIdEncomenda());
        params.put("IdEncomenda", idEncomenda);

        String idTipoRecebedor = String.valueOf(recebedor.getTipoRecebedor().getId());
        params.put("IdTipoRecebedor", idTipoRecebedor);
        params.put("NmRecebedor", recebedor.getNome());

        params.put("FotoRecebimento", recebedor.getFotoComprovanteBase64());
        params.put("FotoAssinatura", recebedor.getFotoAssinaturaDigitalBase64() != null ? recebedor.getFotoAssinaturaDigitalBase64() : "");

        String idTipoDocumento = String.valueOf(recebedor.getTipoDocumento().getId());
        params.put("IdTipoDocumento", idTipoDocumento);
        params.put("NrDocumento", recebedor.getNrDocumento());

        if(latLng != null){

            String latitude = String.valueOf(latLng.latitude);
            String longitude = String.valueOf(latLng.longitude);
            params.put("Latitude", latitude);
            params.put("Longitude", longitude);

        }else{

            params.put("Latitude", "0");
            params.put("Longitude", "0");
        }

        params.put("DataIteracao", encomenda.getDataBaixa());

        return params;
    }






    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<ListaOcorrencia> myReq = new GsonRequest<ListaOcorrencia>(
                Request.Method.POST,
                EnumAPI.BAIXA_ENTREGUE.getValue(),
                ListaOcorrencia.class,
                headers,
                params,
                successListener(),
                errorListener());

        myReq.setRetryPolicy(VolleyTimeout.recuperarTimeout());
        queue.add(myReq);
    }







    private Response.Listener<ListaOcorrencia> successListener() {
        return new Response.Listener<ListaOcorrencia>() {

            @Override
            public void onResponse(ListaOcorrencia listaOcorrencia) {

                try {

                    Toast.makeText(mContext, "API - BAIXA ENTREGUE - SUCESSO", Toast.LENGTH_LONG).show();

                    delegate.processFinish(true, "BAIXA ENTREGUE - OK");

                } catch (Exception e) {

                    Toast.makeText(mContext, "API - BAIXA ENTREGUE - EXCEPTION", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            };
        };
    }






    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "API - BAIXA ENTREGUE - SUCESSO", Toast.LENGTH_LONG).show();

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
