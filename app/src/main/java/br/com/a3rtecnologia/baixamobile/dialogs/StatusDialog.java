package br.com.a3rtecnologia.baixamobile.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Created by maclemon on 01/08/16.
 */
public class StatusDialog {



    public StatusDialog(final Activity mActivity, String title, String mensagem, final boolean encerrar) {


        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(mensagem);
        builder.setCancelable(false);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(encerrar){

                            mActivity.finish();
                        }
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

}
