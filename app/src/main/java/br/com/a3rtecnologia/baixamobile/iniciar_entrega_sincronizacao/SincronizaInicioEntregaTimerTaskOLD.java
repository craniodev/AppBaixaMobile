//package br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Handler;
//import android.widget.Toast;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
//import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
//import br.com.a3rtecnologia.baixamobile.status.Status;
//import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
//import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
//
///**
// * Created by maclemon on 10/09/16.
// */
//public class SincronizaInicioEntregaTimerTask {
//
//    public static ProgressDialog mProgressDialog;
//    private Context mContext;
//
//    private StatusBusiness statusBusiness;
//    private EncomendaBusiness encomendaBusiness;
//
//
//    public static Timer timer;
//    private TimerTask timerTask;
//    private final Handler handler = new Handler();
//
//    private boolean isAtivar;
//    private SincronizaInicioEntregaTimerTask task;
//
//    private Status status;
//
//
//
//
//    public SincronizaInicioEntregaTimerTask(Context mContext){
//
//        this.mContext = mContext;
//        encomendaBusiness = new EncomendaBusiness(mContext);
//        statusBusiness = new StatusBusiness(mContext);
//        task = this;
//
//        status = statusBusiness.getStatus();
//
////        if(status != null && status.getFlagEnviado() == 0){
//
//            isAtivar = true;
//            startTimer();
////        }
//
//    }
//
//
//
//    public void startTimer() {
//
//        //set a new Timer
//
//        if(isAtivar){
//
//            Toast.makeText(mContext, "START SINCRONIZAR INICIO ENTREGA", Toast.LENGTH_LONG).show();
//
//            timer = new Timer();
//            showProgress(true);
//
//            //initialize the TimerTask's job
//            initializeTimerTask();
//
//            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//            timer.schedule(timerTask, 10000, 10000); //
//        }
//    }
//
//
//
//    public void stoptimertask() {
//
//        //stop the timer, if it's not already null
////        if (timer != null) {
//        if(isAtivar){
//
//            if(timer != null){
//
//                timer.cancel();
//            }
//
//            timer = null;
//            isAtivar = false;
//        }
//    }
//
//
//
//    public void initializeTimerTask() {
//
//        timerTask = new TimerTask() {
//
//            public void run() {
//
//                //use a handler to run a toast that shows the current timestamp
//                handler.post(new Runnable() {
//
//                    public void run() {
//
//                        if(InternetStatus.isNetworkAvailable(mContext)){
//
//                            Toast.makeText(mContext, "COM INTERNET", Toast.LENGTH_LONG).show();
////                            SincronizarRealizadas sincronizarRealizadas = new SincronizarRealizadas(mContext, encomendasSincronizar, task);
//
//                            long id = statusBusiness.getIdEncomendaCorrente();
//                            Encomenda encomenda = encomendaBusiness.buscarEncomendaCorrente(id);
//
//                            if(encomenda != null){
//
//                                SincronizarInicioEntrega sincronizarInicioEntrega = new SincronizarInicioEntrega(mContext, encomenda, status, task);
//                            }
//
//                        }else{
//
//                            Toast.makeText(mContext, "SEM INTERNET", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
//            }
//        };
//    }
//
//
//
//
//
//
//
//
//    private void createLoading(Context mContext){
//
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setMessage("Carregando encomendas no mapa");
//    }
//
//    private void createLoading(Context mContext, String message){
//
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setMessage(message);
//    }
//
//    public static void showProgress(boolean isShow){
//
//        if(mProgressDialog != null && isShow && !mProgressDialog.isShowing()){
//
//            mProgressDialog.show();
//
//        }else if(mProgressDialog != null && !isShow && mProgressDialog.isShowing()){
//
//            mProgressDialog.hide();
//        }
//    }
//
//}
