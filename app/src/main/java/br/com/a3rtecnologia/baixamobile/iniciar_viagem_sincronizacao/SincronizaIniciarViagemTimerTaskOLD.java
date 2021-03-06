//package br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Handler;
//import android.widget.Toast;
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagem;
//import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemBusiness;
//import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
//
///**
// * Created by maclemon on 10/09/16.
// */
//public class SincronizaIniciarViagemTimerTask {
//
//    public static ProgressDialog mProgressDialog;
//    private Context mContext;
//
//    public static Timer timer;
//    private TimerTask timerTask;
//    private final Handler handler = new Handler();
//
//    private boolean isAtivar;
//    private SincronizaIniciarViagemTimerTask task;
//
//    private IniciarViagemBusiness iniciarViagemBusiness;
//    private List<IniciarViagem> iniciarViagemList;
//
//
//
//
//    public SincronizaIniciarViagemTimerTask(Context mContext){
//
//        this.mContext = mContext;
//        this.iniciarViagemBusiness = new IniciarViagemBusiness(mContext);
//
//        iniciarViagemList = iniciarViagemBusiness.buscarTodosNaoSincronizados();
//
//        task = this;
//
//        if(iniciarViagemList != null && iniciarViagemList.size() > 0){
//
//            isAtivar = true;
//            startTimer();
//        }
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
//            Toast.makeText(mContext, "START SINCRONIZAR INICIAR VIAGEM", Toast.LENGTH_LONG).show();
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
//            if(timer != null) {
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
//                            SincronizarIniciarViagem sincronizarIniciarViagem = new SincronizarIniciarViagem(mContext, iniciarViagemList, task);
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
