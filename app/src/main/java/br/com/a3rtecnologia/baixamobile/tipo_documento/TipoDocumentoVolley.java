package br.com.a3rtecnologia.baixamobile.tipo_documento;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class TipoDocumentoVolley {

    private Context mContext;
    private RequestQueue queue;

    private SessionManager sessionManager;
    private TipoDocumentoBusiness tipoDocumentoBusiness;



    /**
     * LISTA OCORRENCIAS
     *
     * @param mContext
     */
    public TipoDocumentoVolley(Context mContext) {

        this.mContext = mContext;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();

        this.sessionManager = new SessionManager(mContext);
        tipoDocumentoBusiness = new TipoDocumentoBusiness(mContext);

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

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<ListaDocumento> myReq = new GsonRequest<ListaDocumento>(
                Request.Method.POST,
                EnumAPI.LISTA_DOCUMENTO.getValue(),
                ListaDocumento.class,
                headers,
                params,
                successListener(),
                errorListener());

        myReq.setRetryPolicy(VolleyTimeout.recuperarTimeout());
        queue.add(myReq);
    }



    private Response.Listener<ListaDocumento> successListener() {
        return new Response.Listener<ListaDocumento>() {

            @Override
            public void onResponse(ListaDocumento listaDocumento) {

                try {


                    /**
                     * ONLINE
                     *
                     * SEMPRE REMOVER TODOS
                     * GARANTIR A ATUALIZACAO DO SERVIDOR
                     */

                    /** desativado **/
                    //tipoDocumentoBusiness.deleteAll();


//                    tipoDocumentoBusiness.deleteAll(listaDocumento.getDocumentos());
                    int total = tipoDocumentoBusiness.count();

                    if(total == 0) {

                        TipoDocumento init = new TipoDocumento();
                        init.setDescricao("Selecione");
                        init.setId(0);

                        tipoDocumentoBusiness.salvarOrUpdate(init);

                        //SALVAR SQLITE
                        for (TipoDocumento tipoDocumento : listaDocumento.getDocumentos()) {

                            tipoDocumentoBusiness.salvarOrUpdate(tipoDocumento);
                            System.out.println("SALVAR " + tipoDocumento.getId() + " ITEM " + tipoDocumento.getDescricao());
                        }
                    }


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

                    if(error.networkResponse != null) {

                        if (error.networkResponse.statusCode == EnumHttpError.ERROR_401.getErrorInt()) {

                            Toast.makeText(mContext, R.string.error_invalid_email_or_password, Toast.LENGTH_LONG).show();


//                        StatusDialog dialog = new StatusDialog((Activity)mContext, false, error.networkResponse.statusCode);

                        } else if (error.networkResponse.statusCode == EnumHttpError.ERROR_400.getErrorInt()) {


//                        StatusDialog dialog = new StatusDialog((Activity)mContext, false, error.networkResponse.statusCode);
                        } else if (error.networkResponse.statusCode == 404) {


                        }

                    }else{


                    }

                }else{


//                    StatusDialog dialog = new StatusDialog((Activity)mContext, false, error.networkResponse.statusCode);
                }
            }
        };
    }

}
