package br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega_sincronizacao.SincronizarRealizadas;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagem;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemBusiness;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class SincronizaIniciarViagemTimerTask {

    private Context mContext;

    private SessionManager sessionManager;
    private IniciarViagemBusiness iniciarViagemBusiness;

    private List<IniciarViagem> iniciarViagemList;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();

    private boolean isAtivar;
    private SincronizaIniciarViagemTimerTask task;




    private static SincronizaIniciarViagemTimerTask instance = null;

    public static SincronizaIniciarViagemTimerTask getInstance(Context mContext, boolean isStart) {

        if(instance == null) {

            instance = new SincronizaIniciarViagemTimerTask(mContext);

            return instance;
        }

        if(isStart) {

            instance.init(mContext);

        }else{

            instance.stoptimertask();
        }

        return instance;
    }



    public SincronizaIniciarViagemTimerTask(Context mContext){

        init(mContext);
    }



    private void init(Context mContext){

        this.mContext = mContext;
        iniciarViagemBusiness = new IniciarViagemBusiness(mContext);
//        encomendasSincronizar = encomendaBusiness.buscarPendentesNaoSincronizadas();
        sessionManager = new SessionManager(mContext);
        task = this;

        /** 1 - ativar timertask quando existir viagem **/
        iniciarViagemList = iniciarViagemBusiness.buscarTodosNaoSincronizados();
        if(iniciarViagemList != null && iniciarViagemList.size() > 0){

            isAtivar = true;
            startTimer();
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

                            Toast.makeText(mContext, "INICIAR VIAGEM ONLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                            SincronizarIniciarViagem sincronizarIniciarViagem = new SincronizarIniciarViagem(mContext, iniciarViagemList, task);

                        }else{

                            Toast.makeText(mContext, "INICIAR VIAGEM OFFLINE - Tempo - " + Calendar.getInstance().getTime().getSeconds(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
    }

}
