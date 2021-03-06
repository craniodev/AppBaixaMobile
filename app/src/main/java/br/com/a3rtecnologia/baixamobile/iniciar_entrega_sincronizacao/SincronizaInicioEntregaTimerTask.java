package br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega_sincronizacao.EntregaReceiver;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagem;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao.SincronizarIniciarViagem;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.OcorrenciaReceiver;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class SincronizaInicioEntregaTimerTask {

    private Context mContext;

    private SessionManager sessionManager;
    private StatusBusiness statusBusiness;
    private EncomendaBusiness encomendaBusiness;

    private List<Encomenda> encomendaList;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();

    private boolean isAtivar;
    private SincronizaInicioEntregaTimerTask task;




    private static SincronizaInicioEntregaTimerTask instance = null;

    public static SincronizaInicioEntregaTimerTask getInstance(Context mContext, boolean isStart) {

        if(instance == null) {

            instance = new SincronizaInicioEntregaTimerTask(mContext);

            return instance;
        }

        if(isStart) {

            instance.init(mContext);

        }else{

            instance.stoptimertask();
        }

        return instance;
    }



    public SincronizaInicioEntregaTimerTask(Context mContext){

        init(mContext);
    }



    private void init(Context mContext){

        this.mContext = mContext;
        statusBusiness = new StatusBusiness(mContext);
        encomendaBusiness = new EncomendaBusiness(mContext);

        sessionManager = new SessionManager(mContext);
        task = this;

//        Status status = statusBusiness.getStatus();
//        long id = statusBusiness.getIdEncomendaCorrente();

        /** 1 - ativar timertask quando existir viagem **/
//        if(encomenda != null && encomenda.getFlagEnviado() == EnumStatusEnvio.NAO_SINCRONIZADO.getKey()){
        encomendaList = encomendaBusiness.buscarSaiuEntrega();
        if(encomendaList != null && encomendaList.size() > 0){

            isAtivar = true;
            startTimer();

        }else{

            /**
             * CHAMA OCORRENCIA / ENTREGA
             *
             * JA FOI SINCRONIZADO O PRIMEIRO STATUS "SAIU PARA ENTREGA"
             *
             */
            Intent inicioEntregaIntent = new Intent(mContext, IniciarEntregaReceiver.class);
            inicioEntregaIntent.putExtra("OPERACAO", "FINALIZAR");
            mContext.sendBroadcast(inicioEntregaIntent);

        }
    }




    public void startTimer() {

        //set a new Timer
        /** 2 - ativo **/
        if(timer == null && isAtivar){

            Toast.makeText(mContext, "START INICIAR VIAGEM ENTREGA", Toast.LENGTH_LONG).show();

            timer = new Timer();

            //initialize the TimerTask's job
            initializeTimerTask();

            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 1000, 30000); //
        }
    }



    public void stoptimertask() {

        //stop the timer, if it's not already null
        /** 3 - desativar **/
//        if (isAtivar) {
        if(timer != null){

//            if(timer != null) {

                timer.cancel();
//            }

            timer = null;
            isAtivar = false;
            task = null;//TESTE
        }
    }



    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {

                        if(InternetStatus.isNetworkAvailable(mContext)){

                            Toast.makeText(mContext, "INICIAR ENTREGA ONLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();

                            SincronizarInicioEntrega sincronizarInicioEntrega = new SincronizarInicioEntrega(mContext, encomendaList, task);

                        }else{

                            Toast.makeText(mContext, "INICIAR ENTREGA OFFLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
    }




//    private void sequenciaSincronizar(){
//
//        if(sessionManager.isModoOcorrencia()){
//
//            Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
//            ocorrenciaIntent.putExtra("OPERACAO", "START");
//            mContext.sendBroadcast(ocorrenciaIntent);
//        }
//
//        if(sessionManager.isModoEntrega()){
//
//            Intent entregaIntent = new Intent(mContext, EntregaReceiver.class);
//            entregaIntent.putExtra("OPERACAO", "START");
//            mContext.sendBroadcast(entregaIntent);
//        }
//
//    }

}
