package br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagem;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemVolley;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao.SincronizaIniciarViagemTimerTask;

/**
 * Created by maclemon on 08/10/16.
 */
public class SincronizarIniciarViagem {

    private IniciarViagemBusiness iniciarViagemBusiness;

    private List<IniciarViagem> iniciarViagemList;

    private Context mContext;
    private SincronizaIniciarViagemTimerTask task;




    public SincronizarIniciarViagem(Context mContext, List<IniciarViagem> iniciarViagemList, SincronizaIniciarViagemTimerTask task){

        this.iniciarViagemBusiness = new IniciarViagemBusiness(mContext);

        this.iniciarViagemList = iniciarViagemList;

        this.mContext = mContext;
        this.task = task;

        sincronizar();
    }



    private void sincronizar() {

        for (final IniciarViagem iniciarViagem : iniciarViagemList) {

            IniciarViagemVolley iniciarViagemVolley = new IniciarViagemVolley(mContext, iniciarViagem, new DelegateEncomendaAsyncResponse() {
                @Override
                public void processFinish(boolean finish, String resposta) {

                    iniciarViagem.setFgSincronizado(EnumStatusEnvio.SINCRONIZADO.getKey());
                    iniciarViagemBusiness.update(iniciarViagem);

                    int pendentesSincronizar = iniciarViagemBusiness.count();
                    if (pendentesSincronizar == 0) {

                        iniciarViagemBusiness.deleteAll();

                        Intent iniciarViagemIntent = new Intent(mContext, IniciarViagemReceiver.class);
                        iniciarViagemIntent.putExtra("OPERACAO", "STOP");
                        mContext.sendBroadcast(iniciarViagemIntent);
                    }
                }

                @Override
                public void processCanceled(boolean cancel) {

                }
            });
        }
    }

}
