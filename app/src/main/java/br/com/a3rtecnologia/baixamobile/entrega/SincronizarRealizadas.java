package br.com.a3rtecnologia.baixamobile.entrega;

import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.ocorrencia.BaixaOcorrenciaVolley;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;

/**
 * Created by maclemon on 03/10/16.
 */

public class SincronizarRealizadas {

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

    private List<Encomenda> encomendaList;

    private Context mContext;
    private Timer timer;



    public SincronizarRealizadas(Context mContext, List<Encomenda> encomendaList, Timer timer){

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.encomendaList = encomendaList;

        this.mContext = mContext;
        this.timer = timer;

        sincronizar();
    }



    private void sincronizar(){

        for(Encomenda encomenda : encomendaList) {

            BaixaEntregueVolley baixaEntregueVolley = new BaixaEntregueVolley(mContext, encomenda, new DelegateEntregaAsyncResponse() {

                @Override
                public void processFinish(boolean finish, String resposta) {

                    int pendentesSincronizar = encomendaBusiness.countEntregueNaoSincronizadas();
                    if(pendentesSincronizar == 0){

                        stoptimertask();
                    }

                }

                @Override
                public void processCanceled(boolean cancel) {

                }
            });






//            BaixaOcorrenciaVolley baixaOcorrenciaVolley = new BaixaOcorrenciaVolley(mContext, encomenda, new DelegateEntregaAsyncResponse() {
//
//                @Override
//                public void processFinish(boolean finish, String resposta) {
//
//                    System.out.println("");
//
//                    int pendentesSincronizar = encomendaBusiness.countOcorrenciasNaoSincronizadas();
//                    if(pendentesSincronizar == 0){
//
//                        stoptimertask();
//                    }
//                }
//
//                @Override
//                public void processCanceled(boolean cancel) {
//
//                    System.out.println("");
//                }
//            });
        }
    }





    public void stoptimertask() {

        //stop the timer, if it's not already null
        if (timer != null) {

            timer.cancel();

            timer = null;

            Toast.makeText(mContext, "STOP - SINCRONIZAR OCORRENCIAS", Toast.LENGTH_LONG).show();
        }
    }

}
