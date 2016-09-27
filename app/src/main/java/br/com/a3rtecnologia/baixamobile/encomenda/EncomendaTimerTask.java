package br.com.a3rtecnologia.baixamobile.encomenda;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.menu.MenuDrawerActivity;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_lista.TabItemListaFragment;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class EncomendaTimerTask {

    public ProgressDialog mProgressDialog;

    private Context mContext;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;


    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();




    public EncomendaTimerTask(Context mContext){

        this.mContext = mContext;
//        createLoading(mContext);
    }



    public void startTimer() {

        //set a new Timer
//        if(timer == null) {
//
//
////            showProgress(true);
//
//            timer = new Timer();
//
//            //initialize the TimerTask's job
//            initializeTimerTask();
//
//            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//            timer.schedule(timerTask, 10000, 10000); //
//
//            Toast.makeText(mContext, "API - init - SUCESSO", Toast.LENGTH_LONG).show();
//        }
    }

    public void stoptimertask() {

        //stop the timer, if it's not already null
//        if (timer != null) {
//
////            showProgress(false);
//
//            timer.cancel();
//
//            timer = null;
//
//            Toast.makeText(mContext, "API - stop - SUCESSO", Toast.LENGTH_LONG).show();
//        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {



                        Toast.makeText(mContext, "API - buscando - SUCESSO", Toast.LENGTH_LONG).show();

                        if(InternetStatus.isNetworkAvailable(mContext)) {

                            buscarAPIOnline(timer);

                            //showProgress(false);

                        }else{

                            List<Encomenda> encomendas = null;

                            encomendas = encomendaBusiness.buscarEntregasEmRota();
                            int encomendasEmRota = encomendas.size();

                            if(encomendas != null) {

                                /**
                                 * SE NAO TEM ENCOMENDAS EM ROTA(LOCAL)
                                 * LIMPA A BASE(LOCAL) E BUSCA MAIS NA WEB
                                 *
                                 *
                                 * REGRA PARA ULTIMA ENCOMENDA
                                 *
                                 */
                                if (encomendasEmRota == 0) {

                                    encomendaBusiness.deleteAll();

                                    buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_EM_ROTA.getValue());
                                    buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_ENTREGUE.getValue());
                                    buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_PENDENTE.getValue());

                                    //showProgress(false);

                                } else {

                                    /**
                                     * EXISTE ENCOMENDAS EM ROTA(LOCAL)
                                     *
                                     */
                                    TabItemListaFragment.updateAdapter();

                                    //showProgress(false);
                                }
                            }
                        }
                    }
                });
            }
        };
    }



    private void buscarAPIOnline(String statusEncomenda){

        new EncomendaVolley(mContext, statusEncomenda, 0, new DelegateEncomendasAsyncResponse() {

            @Override
            public void processFinish(boolean finish, Encomendas encomendas) {

                System.out.println(finish);


                TabItemListaFragment.updateAdapter();

//                PainelFragment.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                //showProgress(false);
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println(cancel);
                //showProgress(false);
            }
        });
    }



    private void buscarAPIOnline(Timer timer){

        new EncomendaVolley(mContext, timer, new DelegateEncomendasAsyncResponse() {

            @Override
            public void processFinish(boolean finish, Encomendas encomendas) {

                System.out.println(finish);


                TabItemListaFragment.updateAdapter();

//                PainelFragment.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                //showProgress(false);
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println(cancel);
                //showProgress(false);
            }
        });
    }




//    private void createLoading(Context mContext){
//
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setMessage("Carregando encomendas - Timer");
//    }
//
//    private void createLoading(Context mContext, String message){
//
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setMessage(message);
//    }

//    public void showProgress(boolean isShow){
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

}
