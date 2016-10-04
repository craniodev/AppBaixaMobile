package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_lista.TabItemListaFragment;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 10/09/16.
 */
public class AtualizaEncomendaPendenteTimerTask {

    public static ProgressDialog mProgressDialog;

    private Context mContext;

    private SessionManager sessionManager;
    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;


    public static Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    private boolean isAtivar;




    public AtualizaEncomendaPendenteTimerTask(Context mContext){

        this.mContext = mContext;
        encomendaBusiness = new EncomendaBusiness(mContext);
        List<Encomenda> encomendaPendentes = encomendaBusiness.buscarEntregasOcorrencia();
        if(encomendaPendentes != null && encomendaPendentes.size() > 0){

            isAtivar = true;
        }

    }



    public void startTimer() {

        //set a new Timer

        if(timer == null && isAtivar){

            Toast.makeText(mContext, "START - SINCRONISMO ENCOMENDAS TRATADAS", Toast.LENGTH_LONG).show();

            timer = new Timer();
            showProgress(true);
            //initialize the TimerTask's job
            initializeTimerTask();

            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 50000, 50000); //
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

                        List<Encomenda> encomendaList = encomendaBusiness.buscarEntregasOcorrencia();
                        List<Encomenda> listaPendentes = new ArrayList<Encomenda>();

                        Encomendas e = new Encomendas();
                        for (Encomenda encomendaRecuperada : encomendaList){

                            Encomenda enc = new Encomenda();
                            enc.setIdEncomenda(encomendaRecuperada.getIdEncomenda());

                            listaPendentes.add(enc);
                        }
                        e.setEncomendas(listaPendentes);

                        AtualizaEncomendaPendenteVolley atualizaEncomendaPendenteVolley = new AtualizaEncomendaPendenteVolley(mContext, e, new DelegateEntregaAsyncResponse() {
                            @Override
                            public void processFinish(boolean finish, String resposta) {

                                if(finish){

                                    List<Encomenda> encomendasPendentes = encomendaBusiness.buscarEntregasOcorrencia();

                                    if(encomendasPendentes == null || encomendasPendentes.size() == 0){

                                        stoptimertask();
                                        Toast.makeText(mContext, "API - STOP - ATUALIZA ENCOMENDA PENDENTE(TRATADA) - SUCESSO", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void processCanceled(boolean cancel) {

                            }
                        });
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
