package br.com.a3rtecnologia.baixamobile.entrega;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.ocorrencia.SincronizarOcorrencia;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class SincronizaEncomendaEntregueTimerTask {

    public static ProgressDialog mProgressDialog;

    private Context mContext;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

    private List<Encomenda> encomendasSincronizar;


    public static Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    private boolean isAtivar;




    public SincronizaEncomendaEntregueTimerTask(Context mContext){

        this.mContext = mContext;
        encomendaBusiness = new EncomendaBusiness(mContext);
        encomendasSincronizar = encomendaBusiness.buscarRealizadasNaoSincronizadas();
        if(encomendasSincronizar != null && encomendasSincronizar.size() > 0){

            isAtivar = true;
        }

    }



    public void startTimer() {

        //set a new Timer

        if(timer == null && isAtivar){

            Toast.makeText(mContext, "START SINCRONIZAR REALIZADAS", Toast.LENGTH_LONG).show();

            timer = new Timer();
            showProgress(true);
            //initialize the TimerTask's job
            initializeTimerTask();

            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 5000, 5000); //
        }

    }

    public void stoptimertask() {

        //stop the timer, if it's not already null
        if (timer != null) {

            timer.cancel();

            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {


                        if(InternetStatus.isNetworkAvailable(mContext)){

                            Toast.makeText(mContext, "COM INTERNET", Toast.LENGTH_LONG).show();
                            SincronizarRealizadas sincronizarRealizadas = new SincronizarRealizadas(mContext, encomendasSincronizar, timer);

                        }else{

                            Toast.makeText(mContext, "SEM INTERNET", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        };
    }








    private void createLoading(Context mContext){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Carregando encomendas no mapa");
    }

    private void createLoading(Context mContext, String message){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    public static void showProgress(boolean isShow){

        if(mProgressDialog != null && isShow && !mProgressDialog.isShowing()){

            mProgressDialog.show();

        }else if(mProgressDialog != null && !isShow && mProgressDialog.isShowing()){

            mProgressDialog.hide();
        }
    }

}
