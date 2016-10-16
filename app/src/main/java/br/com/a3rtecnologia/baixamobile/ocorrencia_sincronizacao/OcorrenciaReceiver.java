package br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.a3rtecnologia.baixamobile.controle_timertask.ControleTimerTask;
import br.com.a3rtecnologia.baixamobile.controle_timertask.ControleTimerTaskBusiness;
import br.com.a3rtecnologia.baixamobile.controle_timertask.EnumOperacao;
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

//        /** sincronizacao em andamento **/
//        if(sessionManager.isModoOcorrencia()){
//
//
//
//        }

    }



    private void operacao(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {

            String OPERACAO = extras.getString("OPERACAO");

            if (OPERACAO.equalsIgnoreCase("START")) {
//            if (OPERACAO.equalsIgnoreCase("START") && !sessionManager.isModoOcorrencia()) {

                sessionManager.startModoOcorrencia();

                SincronizaEncomendaPendenteTimerTask.getInstance(mContext, true);

            } else if (OPERACAO.equalsIgnoreCase("STOP")) {
//            } else if (OPERACAO.equalsIgnoreCase("STOP") && sessionManager.isModoOcorrencia()) {

                sessionManager.stopModoOcorrencia();

                SincronizaEncomendaPendenteTimerTask.getInstance(mContext, false);
            }
        }
    }




//    private void init(){
//
//        ControleTimerTaskBusiness controleTimerTaskBusiness = new ControleTimerTaskBusiness(mContext);
//        Integer total = controleTimerTaskBusiness.verificarFinalizarEntrega();
//
//        if(total == null || total == 0){
//
//            ControleTimerTask controleTimerTask = new ControleTimerTask();
//            controleTimerTask.setIdOperacao(EnumOperacao.OP_FINALIZAR_ENTREGA.getValue());
//            controleTimerTaskBusiness.salvar(controleTimerTask);
//
//            SincronizaEncomendaPendenteTimerTask sincronizaEncomendaPendenteTimerTask = new SincronizaEncomendaPendenteTimerTask(mContext);
//        }
//    }



}
