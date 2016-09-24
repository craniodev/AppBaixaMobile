package br.com.a3rtecnologia.baixamobile.cadastro;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.DelegateAsyncResponse;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 28/07/16.
 */
public class CadastroVolley extends AuthFailureError{

    private Usuario usuario;

    private Context mContext;
    private DelegateAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;




    public CadastroVolley(Context mContext, Usuario usuario, DelegateAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);

        this.usuario = usuario;

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

        params.put("IdContratante", EnumAPI.ID_CONTRATANTE.getValue());
        params.put("IdMotorista", "");
        params.put("NmMotorista", usuario.getNome());
        params.put("Cpf", usuario.getCpf());
        params.put("CNH", usuario.getCnh());
        params.put("Fone1", usuario.getFone1());
        params.put("Fone2", usuario.getFone2());
        params.put("Email", usuario.getEmail());

        return params;
    }



    public void requestAPI() {

        GsonRequest<Usuario> myReq = new GsonRequest<Usuario>(
                Request.Method.POST,
                EnumAPI.CADASTRO.getValue(),
                Usuario.class,
                header(),
                param(),
                successListener(),
                errorListener());

        myReq.setRetryPolicy(VolleyTimeout.recuperarTimeout());
        queue.add(myReq);
    }



    private Response.Listener<Usuario> successListener() {
        return new Response.Listener<Usuario>() {

            @Override
            public void onResponse(Usuario responseObject) {

                try {

                    delegate.processFinish(true);
                    StatusDialog dialog = new StatusDialog((Activity)mContext, "Cadastro", responseObject.getResposta(), true);

                } catch (Exception e) {

                    e.printStackTrace();
                    delegate.processCanceled(false);
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if(InternetStatus.isNetworkAvailable(mContext)){

                    delegate.processCanceled(false);
                    StatusDialog dialog = new StatusDialog((Activity)mContext, "Cadastro", "Ocorreu um erro ao cadastrar \n" + error.networkResponse.statusCode, false);

                }else{

                    delegate.processCanceled(false);
                    StatusDialog dialog = new StatusDialog((Activity)mContext, "Cadastro", "Sem conexão com internet", false);
                }
            }
        };
    }

}