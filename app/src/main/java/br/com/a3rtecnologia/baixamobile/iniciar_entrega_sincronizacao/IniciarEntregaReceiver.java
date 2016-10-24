package br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.a3rtecnologia.baixamobile.entrega_sincronizacao.EntregaReceiver;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.OcorrenciaReceiver;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 13/10/16.
 */

public class IniciarEntregaReceiver extends BroadcastReceiver {

    private SessionManager sessionManager;
    private Context mContext;




    @Override
    public void onReceive(Context mContext, Intent intent) {

        this.sessionManager = new SessionManager(mContext);
        this.mContext = mContext;

        operacao(intent);
    }



    private void operacao(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {

            String OPERACAO = extras.getString("OPERACAO");

            if (OPERACAO.equalsIgnoreCase("START")) {

                sessionManager.startModoInicioEntrega();

                SincronizaInicioEntregaTimerTask.getInstance(mContext, true);

            } else if (OPERACAO.equalsIgnoreCase("STOP")) {

                sessionManager.stopModoInicioEntrega();

                SincronizaInicioEntregaTimerTask.getInstance(mContext, false);



                /** ativar verificacao - se existe finalizar **/
                sequenciaSincronizar();

            } else if (OPERACAO.equalsIgnoreCase("FINALIZAR")) {

                sequenciaSincronizar();

            }
//            else if (OPERACAO.equalsIgnoreCase("FINALIZAR_OCORRENCIA")) {
//
//                if(sessionManager.isModoOcorrencia()){
//
//                    Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
//                    ocorrenciaIntent.putExtra("OPERACAO", "START");
//                    mContext.sendBroadcast(ocorrenciaIntent);
//                }
//            }
        }
    }




    private void sequenciaSincronizar(){

        if(sessionManager.isModoOcorrencia()){

            Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
            ocorrenciaIntent.putExtra("OPERACAO", "START");
            mContext.sendBroadcast(ocorrenciaIntent);
        }

        if(sessionManager.isModoEntrega()){

            Intent entregaIntent = new Intent(mContext, EntregaReceiver.class);
            entregaIntent.putExtra("OPERACAO", "START");
            mContext.sendBroadcast(entregaIntent);
        }
    }

}
