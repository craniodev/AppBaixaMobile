package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;

/**
 * Created by maclemon on 03/10/16.
 */

public class SincronizarOcorrencia {

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

    private List<Encomenda> encomendaList;

    private Context mContext;
    private SincronizaEncomendaPendenteTimerTask task;



    public SincronizarOcorrencia(Context mContext, List<Encomenda> encomendaList, SincronizaEncomendaPendenteTimerTask task){

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.encomendaList = encomendaList;

        this.mContext = mContext;
        this.task = task;

        sincronizar();
    }



    private void sincronizar(){

        for(Encomenda encomenda : encomendaList) {

            BaixaOcorrenciaVolley baixaOcorrenciaVolley = new BaixaOcorrenciaVolley(mContext, encomenda, new DelegateEntregaAsyncResponse() {

                @Override
                public void processFinish(boolean finish, String resposta) {

                    System.out.println("");

                    int pendentesSincronizar = encomendaBusiness.countOcorrenciasNaoSincronizadas();
                    if(pendentesSincronizar == 0){

                        task.stoptimertask();
                    }
                }

                @Override
                public void processCanceled(boolean cancel) {

                    System.out.println("");
                }
            });
        }
    }





//    public void stoptimertask() {
//
//        //stop the timer, if it's not already null
//        if (timer != null) {
//
//            timer.cancel();
//
//            timer = null;
//
//            Toast.makeText(mContext, "STOP - SINCRONIZAR OCORRENCIAS", Toast.LENGTH_LONG).show();
//        }
//    }

}
