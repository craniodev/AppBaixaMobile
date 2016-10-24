package br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.IniciarEntregaVolley;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;

/**
 * Created by maclemon on 03/10/16.
 */

public class SincronizarInicioEntrega {

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;
    private List<Encomenda> encomendaList;

    private Context mContext;
    private SincronizaInicioEntregaTimerTask task;



    public SincronizarInicioEntrega(Context mContext, List<Encomenda> encomendaList, SincronizaInicioEntregaTimerTask task){

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.encomendaList = encomendaList;

        this.mContext = mContext;
        this.task = task;

        sincronizar();
    }



    private void sincronizar(){

        for(Encomenda encomenda : encomendaList) {

            IniciarEntregaVolley iniciarEntregaVolley = new IniciarEntregaVolley(mContext, encomenda, new DelegateEncomendaAsyncResponse() {

                @Override
                public void processFinish(boolean finish, String resposta) {

                    System.out.println(resposta);

                    int pendentesSincronizar = encomendaBusiness.countSaiuEntrega();
                    if (pendentesSincronizar == 0) {


                        /**
                         *  ATIVAR SINCRONISMO DA OCORRENCIA OU ENTREGA
                         *
                         *  JA FOI SINCRONIZADO O PRIMEIRO STATUS - "SAIU PARA ENTREGA"
                         */
                        Intent stopInicioEntrega = new Intent(mContext, IniciarEntregaReceiver.class);
                        stopInicioEntrega.putExtra("OPERACAO", "STOP");
                        mContext.sendBroadcast(stopInicioEntrega);



//                        Intent verificarFinalizar = new Intent(mContext, IniciarEntregaReceiver.class);
//                        verificarFinalizar.putExtra("OPERACAO", "FINALIZAR");
//                        mContext.sendBroadcast(verificarFinalizar);
                    }
                }

                @Override
                public void processCanceled(boolean cancel) {

                    System.out.println("ERRO INICIAR ENTREGA");
                }
            });
        }
    }


}
