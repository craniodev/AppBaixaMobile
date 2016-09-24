package br.com.a3rtecnologia.baixamobile.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by maclemon on 30/07/16.
 */
public class GsonRequest<T> extends Request<T> {

    private Gson mGson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Response.Listener<T> listener;
    private Object obj;



    public GsonRequest(int method,
                       String url,
                       Class clazz,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        super(method, url, errorListener);

        this.clazz = clazz;
        this.listener = listener;
        this.mGson = new Gson();
    }



    public GsonRequest(int method,
                       String url,
                       Object obj,
                       Class clazz,
                       Map<String, String> params,
                       Map<String, String> headers,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        super(method, url, errorListener);

        this.obj = obj;
        this.clazz = clazz;
        this.listener = listener;
        this.params = params;
        this.headers = headers;
        this.mGson = new Gson();
    }



    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       Map<String, String> params,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        super(method, url, errorListener);

        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
        this.headers = headers;

        mGson = new Gson();
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{

        return headers != null ? headers : super.getHeaders();
    }



    @Override
    public Map<String, String> getParams() {
        return params;
    }



    @Override
    public String getBodyContentType() {

        if(obj != null){

            return "application/json; charset=utf-8";
        }

        return super.getBodyContentType();
    }



    @Override
    public byte[] getBody() throws AuthFailureError {

        String json = null;

        if(obj != null) {

            json = mGson.toJson(obj);
        }

//        return json != null ? json.getBytes(Charset.forName("UTF-8")) : super.getBody();
        return json != null ? json.getBytes() : super.getBody();
    }



    @Override
    protected void deliverResponse(T response) {

        this.listener.onResponse(response);
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {

            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(mGson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {

            return Response.error(new ParseError(e));

        } catch (JsonSyntaxException e) {

            return Response.error(new ParseError(e));
        }

    }
}
