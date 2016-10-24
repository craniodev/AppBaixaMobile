package br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 13/10/16.
 */

public class OcorrenciaReceiver extends BroadcastReceiver {

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

                SincronizaEncomendaPendenteTimerTask.getInstance(mContext, true);

            } else if (OPERACAO.equalsIgnoreCase("STOP")) {

                sessionManager.stopModoOcorrencia();
                SincronizaEncomendaPendenteTimerTask.getInstance(mContext, false);
            }
        }
    }


}
