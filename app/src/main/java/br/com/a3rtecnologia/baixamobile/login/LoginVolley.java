package br.com.a3rtecnologia.baixamobile.login;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
public class LoginVolley {

    private Usuario usuario;

    private Context mContext;
    private DelegateAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;



    public LoginVolley(Context mContext, Usuario usuario, DelegateAsyncResponse delegate) {

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
        params.put("Usuario", usuario.getEmail());
        params.put("Senha", usuario.getPassword());

        return params;
    }



    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Usuario> myReq = new GsonRequest<Usuario>(
                Request.Method.POST,
                EnumAPI.LOGIN.getValue(),
                Usuario.class,
                headers,
                params,
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

                    //objeto retorno server
                    String id = String.valueOf(responseObject.getId());
                    sessionManager.setValue("id", id);
                    sessionManager.setValue("nome", responseObject.getNome());

                    //objeto com valor os dados da autenticacao
                    sessionManager.setValue("email", usuario.getEmail());
                    sessionManager.setValue("password", usuario.getPassword());


                    new VerificaViagemIniciadaVolley(mContext, new DelegateAsyncResponse() {
                        @Override
                        public void processFinish(boolean finish) {

                            delegate.processFinish(true);
                        }

                        @Override
                        public void processCanceled(boolean cancel) {

                        }
                    });

//                    delegate.processFinish(true);

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

                        StatusDialog dialog = new StatusDialog((Activity)mContext, "Login", "Usuário ou senha inválido", false);
                    }

                }else{

                    StatusDialog dialog = new StatusDialog((Activity)mContext, "Login", "Sem conexão com internet", false);
                }
            }
        };
    }

}
