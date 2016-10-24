package br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.ocorrencia.BaixaOcorrenciaVolley;
import br.com.a3rtecnologia.baixamobile.ocorrencia_tratada_sincronizacao.AtualizaEncomendaPendenteTimerTask;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 03/10/16.
 */

public class SincronizarOcorrencia {

    private EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

    private List<Encomenda> encomendaList;

    private Context mContext;
    private SincronizaEncomendaPendenteTimerTask task;
    private SessionManager sessionManager;




    public SincronizarOcorrencia(Context mContext, List<Encomenda> encomendaList, SincronizaEncomendaPendenteTimerTask task){

        this.encomendaBusiness = new EncomendaBusiness(mContext);
        this.statusBusiness = new StatusBusiness(mContext);
        this.sessionManager = new SessionManager(mContext);

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

                        Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
                        ocorrenciaIntent.putExtra("OPERACAO", "STOP");
                        mContext.getApplicationContext().sendBroadcast(ocorrenciaIntent);


                        /** 10 - ativa VERIFICADOR de encomendas TRATADAS **/
                        AtualizaEncomendaPendenteTimerTask atualizaEncomendaPendenteTimerTask = new AtualizaEncomendaPendenteTimerTask(mContext);
                    }
                }

                @Override
                public void processCanceled(boolean cancel) {

                    System.out.println("");
                }
            });
        }
    }

}
