package br.com.a3rtecnologia.baixamobile.tab_lista;

import android.content.Context;

import java.util.List;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.BaixaEntregueVolley;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.entrega.IniciarEntregaVolley;
import br.com.a3rtecnologia.baixamobile.entrega.SincronizaEncomendaEntregueTimerTask;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;

/**
 * Created by maclemon on 03/10/16.
 */

public class SincronizarInicioEntrega {

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;
    private Encomenda encomenda;

    private Status status;

    private Context mContext;
    private SincronizaInicioEntregaTimerTask task;



    public SincronizarInicioEntrega(Context mContext, Encomenda encomenda, Status status, SincronizaInicioEntregaTimerTask task){

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.status = status;
        this.encomenda = encomenda;

        this.mContext = mContext;
        this.task = task;

        sincronizar();
    }



    private void sincronizar(){


//        long id = statusBusiness.getIdEncomendaCorrente();
//        Encomenda encomenda = encomendaBusiness.buscarEncomendaCorrente(id);




        /**
         * ATUALIZAR DATA BAIXA
         */
        encomenda.setDataInicioEntrega(DateUtil.getDataAtual());
        encomendaBusiness.update(encomenda);

        IniciarEntregaVolley iniciarEntregaVolley = new IniciarEntregaVolley(mContext, encomenda, new DelegateEncomendaAsyncResponse() {

            @Override
            public void processFinish(boolean finish, String resposta) {

                System.out.println(resposta);



                Status status = statusBusiness.getStatus();
//                if(status.getFlagEnviado() == EnumStatusEnvio.SINCRONIZADO.getKey()){

                    task.stoptimertask();
//                }






//                int pendentesSincronizar = encomendaBusiness.countEntregueNaoSincronizadas();
//                if(pendentesSincronizar == 0){
//
//                    task.stoptimertask();
//                }

            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println("ERRO INICIAR ENTREGA");
            }
        });







//            BaixaEntregueVolley baixaEntregueVolley = new BaixaEntregueVolley(mContext, encomenda, new DelegateEntregaAsyncResponse() {
//
//                @Override
//                public void processFinish(boolean finish, String resposta) {
//
//                    int pendentesSincronizar = encomendaBusiness.countEntregueNaoSincronizadas();
//                    if(pendentesSincronizar == 0){
//
//                        task.stoptimertask();
//                    }
//
//                }
//
//                @Override
//                public void processCanceled(boolean cancel) {
//
//                }
//            });


    }


}
