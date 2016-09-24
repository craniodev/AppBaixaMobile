package br.com.a3rtecnologia.baixamobile.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maclemon on 28/07/16.
 */
public class JsonObjectRequestCustom extends JsonObjectRequest {


    public JsonObjectRequestCustom(int method, String url, JSONObject requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map headers = new HashMap();
        headers.put("CodigoAutenticacao", "Api");
        headers.put("SenhaAutenticacao", "fernandoApi");

        return headers;
    }
}
