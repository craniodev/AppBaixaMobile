package br.com.a3rtecnologia.baixamobile.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.ocorrencia.FinalizarViagemOcorrenciaVolley;
import br.com.a3rtecnologia.baixamobile.ocorrencia.OcorrenciaActivity;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class EncerrarViagemDialog {

    private SessionManager sessionManager;


    public EncerrarViagemDialog(final Activity mActivity, String title, String mensagem) {

        this.sessionManager = new SessionManager(mActivity);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(mensagem);
        builder.setCancelable(false);
        builder.setPositiveButton("SIM",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(mActivity, OcorrenciaActivity.class);
                        mActivity.startActivity(intent);

                        sessionManager.setFinalizarViagemOcorrenciaForcado("1");
                    }
                });

        builder.setNegativeButton("NÃO",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

}
