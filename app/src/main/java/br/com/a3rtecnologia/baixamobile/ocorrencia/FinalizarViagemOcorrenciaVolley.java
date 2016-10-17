package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.OcorrenciaReceiver;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 01/08/16.
 */
public class FinalizarViagemOcorrenciaVolley {

    private Context mContext;
    private DelegateEntregaAsyncResponse delegate;
    private RequestQueue queue;

    private SessionManager sessionManager;

    Ocorrencia ocorrencia;
//    Encomenda encomenda;
    EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;


    /**
     * FINALIZA VIAGEM OCORRENCIA - FORCADO
     *
     * @param mContext
     * @param delegate
     */
    public FinalizarViagemOcorrenciaVolley(Context mContext, Ocorrencia ocorrencia, DelegateEntregaAsyncResponse delegate) {

        this.mContext = mContext;
        this.delegate = delegate;
        this.queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();
        this.sessionManager = new SessionManager(mContext);
        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.ocorrencia = ocorrencia;

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

        String dataFimViagem = DateUtil.getDataAtual();
        Status status = statusBusiness.getStatus();
        status.setDataFimViagem(dataFimViagem);

        statusBusiness.salvar(status);

        String idStatus = String.valueOf(ocorrencia.getTipoOcorrencia().getId());

        //RECUPERA MINHA LOCALIZACAO ATUAL
        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mContext, TabItemMapaFragment.map);
        timerTaskLocation.startTimer();

        LatLng latLng = timerTaskLocation.getMyLatLng();
        timerTaskLocation.stoptimertask();

        params.put("IdMotorista", id);
        params.put("DataFinalizacao", dataFimViagem);
        params.put("IdStatus", idStatus);

        if(latLng != null){

            String latitude = String.valueOf(latLng.latitude);
            String longitude = String.valueOf(latLng.longitude);

            params.put("Latitude", latitude);
            params.put("Longitude", longitude);
        }

        return params;
    }



    private void prepare(Encomenda encomendaCorrente){

        encomendaCorrente.setIdStatus(EnumEncomendaStatus.OCORRENCIA.getKey());
        encomendaCorrente.setDescStatus(EnumEncomendaStatus.OCORRENCIA.getValue());


        /** 3 - recupera localizacao atual **/
        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mContext, TabItemMapaFragment.map);
        timerTaskLocation.startTimer();

        /** 3.1 - monta objeto latlng **/
        LatLng latLng = timerTaskLocation.getMyLatLng();
        timerTaskLocation.stoptimertask();

        if(latLng != null){

            encomendaCorrente.setLatitude(latLng.latitude);
            encomendaCorrente.setLongitude(latLng.longitude);
        }

        /** 5 - data atual da baixa **/
        encomendaCorrente.setDataBaixa(DateUtil.getDataAtual());
        encomendaBusiness.update(encomendaCorrente);


        /** 6 - remove COR do circulo se for uma encomenda TRATADA **/
        encomendaCorrente.setFlagTratado(false);

        /** 7 - atualiza STATUS da encomenda para OCORRENCIA **/
        encomendaBusiness.atualizarStatusEncomendaOcorrencia(encomendaCorrente);

        /** 8 - DESMARCA encomenda como CORRENTE **/
        statusBusiness.removeEncomendaCorrente();
    }





    public void requestAPI() {

        Map<String, String> headers = header();
        Map<String, String> params = param();

        GsonRequest<Ocorrencias> myReq = new GsonRequest<Ocorrencias>(
                Request.Method.POST,
                EnumAPI.FINALIZA_VIAGEM_OCORRENCIA.getValue(),
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

                    Toast.makeText(mContext, "API - FINALIZA VIAGEM OCORRENCIA - SUCESSO", Toast.LENGTH_LONG).show();

                    List<Encomenda> encomendaList = encomendaBusiness.buscarEntregasEmRota();

                    for(Encomenda encomenda : encomendaList){

//                        encomenda.setIdStatus(EnumEncomendaStatus.OCORRENCIA.getKey());
//                        encomenda.setDescStatus(EnumEncomendaStatus.OCORRENCIA.getValue());
                        prepare(encomenda);

                        encomendaBusiness.atualizarEncomenda(encomenda);
                    }

                    /**
                     * ATIVAR SINCRONISMO
                     */
                    Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
                    ocorrenciaIntent.putExtra("OPERACAO", "START");
                    mContext.sendBroadcast(ocorrenciaIntent);

                    delegate.processFinish(true, "FINALIZA VIAGEM OCORRENCIA - FORCADO - OK");

                } catch (Exception e) {

                    Toast.makeText(mContext, "API - FINALIZA VIAGEM OCORRENCIA - EXCEPTION " + e.getMessage(), Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            };
        };
    }



    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "API - FINALIZA VIAGEM OCORRENCIA - ERROR", Toast.LENGTH_LONG).show();

                if(InternetStatus.isNetworkAvailable(mContext)){

                    delegate.processCanceled(false);

                }else{

                    delegate.processCanceled(false);
                }
            }
        };
    }

}
