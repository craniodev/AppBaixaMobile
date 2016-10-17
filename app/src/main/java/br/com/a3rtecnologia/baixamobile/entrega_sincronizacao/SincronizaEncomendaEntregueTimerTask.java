package br.com.a3rtecnologia.baixamobile.entrega_sincronizacao;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.SincronizarOcorrencia;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class SincronizaEncomendaEntregueTimerTask {

    private Context mContext;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private List<Encomenda> encomendasSincronizar;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();

    private boolean isAtivar;
    private SincronizaEncomendaEntregueTimerTask task;




    private static SincronizaEncomendaEntregueTimerTask instance = null;

    public static SincronizaEncomendaEntregueTimerTask getInstance(Context mContext, boolean isStart) {

        if(instance == null) {

            instance = new SincronizaEncomendaEntregueTimerTask(mContext);

            return instance;
        }

        if(isStart) {

            instance.init(mContext);

        }else{

            instance.stoptimertask();
        }

        return instance;
    }



    public SincronizaEncomendaEntregueTimerTask(Context mContext){

        init(mContext);
    }



    private void init(Context mContext){

        this.mContext = mContext;
        encomendaBusiness = new EncomendaBusiness(mContext);
//        encomendasSincronizar = encomendaBusiness.buscarPendentesNaoSincronizadas();
        encomendasSincronizar = encomendaBusiness.buscarEntregueNaoSincronizadas();
        sessionManager = new SessionManager(mContext);
        task = this;

        /** 1 - ativar timertask quando existir encomenda entregue **/
        if(encomendasSincronizar != null && encomendasSincronizar.size() > 0){

            isAtivar = true;
            startTimer();

        }else{

//            sessionManager.stopModoOcorrencia();
        }
    }



    public void startTimer() {

        //set a new Timer
        /** 2 - ativo **/
        if(timer == null && isAtivar){

            Toast.makeText(mContext, "START SINCRONIZAR ENTREGA", Toast.LENGTH_LONG).show();

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

                            Toast.makeText(mContext, "ENTREGA ONLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                            SincronizarRealizadas sincronizarRealizadas = new SincronizarRealizadas(mContext, encomendasSincronizar, task);

                        }else{

                            Toast.makeText(mContext, "ENTREGA OFFLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
    }

}
