package br.com.a3rtecnologia.baixamobile.recuperar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.a3rtecnologia.baixamobile.R;

/**
 * Created by maclemon on 01/08/16.
 */
public class StatusRecuperarDialog {



    public StatusRecuperarDialog(final Activity mActivity, boolean status, int codeRequest){

        LayoutInflater li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = li.inflate(R.layout.alertdialog_sucesso, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//        builder.setTitle("Titulo");
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();


        /**
         * retrieve findViewById
         */
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_exit_title);
        dialogTitle.setText("Recuperação de senha");

        TextView dialogSubTitle = (TextView) view.findViewById(R.id.dialog_exit_subtitle);

        if(status){

            dialogSubTitle.setText("Email enviado com sucesso!");

        }else{

            dialogSubTitle.setText("Erro ao enviar email! " + codeRequest);
        }

//        TextView dialogBtnSim = (TextView) view.findViewById(R.id.dialog_exit_btn_sim);
//        dialogBtnSim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new SessionManager(view.getContext()).clear();
//                alert.dismiss();
//
//                Intent intent = new Intent(view.getContext(), LoginActivity.class);
//                view.getContext().startActivity(intent);
//
//                mActivity.finish();
//            }
//        });

        TextView dialogBtnNao = (TextView) view.findViewById(R.id.dialog_exit_btn_nao);
        dialogBtnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();
            }
        });
    }

}
