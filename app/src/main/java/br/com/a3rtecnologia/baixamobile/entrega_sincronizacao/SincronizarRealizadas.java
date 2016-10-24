package br.com.a3rtecnologia.baixamobile.entrega_sincronizacao;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.BaixaEntregueVolley;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;

/**
 * Created by maclemon on 03/10/16.
 */

public class SincronizarRealizadas {

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

    private List<Encomenda> encomendaList;

    private Context mContext;
    private SincronizaEncomendaEntregueTimerTask task;



    public SincronizarRealizadas(Context mContext, List<Encomenda> encomendaList, SincronizaEncomendaEntregueTimerTask task) {

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);

        this.encomendaList = encomendaList;

        this.mContext = mContext;
        this.task = task;

        sincronizar();
    }


    private void sincronizar() {

        for (Encomenda encomenda : encomendaList) {

            BaixaEntregueVolley baixaEntregueVolley = new BaixaEntregueVolley(mContext, encomenda, new DelegateEntregaAsyncResponse() {

                @Override
                public void processFinish(boolean finish, String resposta) {

                    int pendentesSincronizar = encomendaBusiness.countEntregueNaoSincronizadas();
                    if (pendentesSincronizar == 0) {

                        Intent entregaIntent = new Intent(mContext, EntregaReceiver.class);
                        entregaIntent.putExtra("OPERACAO", "STOP");
                        mContext.getApplicationContext().sendBroadcast(entregaIntent);
                    }

                }

                @Override
                public void processCanceled(boolean cancel) {

                }
            });

        }
    }

}