package br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.controle_timertask.ControleTimerTask;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao.IniciarEntregaReceiver;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class SincronizaEncomendaPendenteTimerTask {

    private Context mContext;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private List<Encomenda> encomendasSincronizar;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();

    private boolean isAtivar;
    private SincronizaEncomendaPendenteTimerTask task;




    private static SincronizaEncomendaPendenteTimerTask instance = null;

    public static SincronizaEncomendaPendenteTimerTask getInstance(Context mContext, boolean isStart) {

        if(instance == null) {

            instance = new SincronizaEncomendaPendenteTimerTask(mContext);

            return instance;
        }

        if(isStart) {

            instance.init(mContext);

        }else{

            instance.stoptimertask();
        }

        return instance;
    }



    public SincronizaEncomendaPendenteTimerTask(Context mContext){

        init(mContext);
    }



    private void init(Context mContext){

        this.mContext = mContext;
        encomendaBusiness = new EncomendaBusiness(mContext);
        encomendasSincronizar = encomendaBusiness.buscarPendentesNaoSincronizadas();
        sessionManager = new SessionManager(mContext);
        task = this;

        /** 1 - ativar timertask quando existir encomenda pendente **/
        if(encomendasSincronizar != null && encomendasSincronizar.size() > 0){

            isAtivar = true;
            startTimer();

        } else{

            Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
            ocorrenciaIntent.putExtra("OPERACAO", "STOP");
            mContext.getApplicationContext().sendBroadcast(ocorrenciaIntent);
        }
    }



    public void startTimer() {

        //set a new Timer
        /** 2 - ativo **/
        if(timer == null && isAtivar){

            Toast.makeText(mContext, "START SINCRONIZAR OCORRENCIAS", Toast.LENGTH_LONG).show();

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
//        sessionManager.stopModoOcorrencia();

    }



    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {

                        if(InternetStatus.isNetworkAvailable(mContext)){

                            Toast.makeText(mContext, "OCORRENCIA ONLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                            SincronizarOcorrencia sincronizarOcorrencia = new SincronizarOcorrencia(mContext, encomendasSincronizar, task);

                        }else{

                            Toast.makeText(mContext, "OCORRENCIA OFFLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
    }

}
