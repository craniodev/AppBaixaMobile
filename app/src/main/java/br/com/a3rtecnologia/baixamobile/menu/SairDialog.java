package br.com.a3rtecnologia.baixamobile.menu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.login.LoginActivity;
import br.com.a3rtecnologia.baixamobile.ocorrencia.AtualizaEncomendaPendenteTimerTask;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.orm.TableUtil;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class SairDialog {



    public SairDialog(final Activity mActivity, final AtualizaEncomendaPendenteTimerTask atualizaEncomendaPendenteTimerTask){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Deseja realmente sair?");
        builder.setCancelable(false);
        builder.setPositiveButton("SIM",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                            /**
                                             * STOP TIMER TASK QUANDO DESLOGAR
                                             */
                                            if(AtualizaEncomendaPendenteTimerTask.timer != null){

                                                AtualizaEncomendaPendenteTimerTask.timer.cancel();
                                            }


                                            /**
                                             * LIMPA TABELAS PARA RECEBER NOVO USUARIO
                                             */
                                            TableUtil tableUtil = new TableUtil(mActivity);
                                            tableUtil.clearAllTables();


                                            /**
                                             * REMOVER USUARIO DA SESSAO E INICIAR TELA DE LOGIN
                                             */
                                            new SessionManager(mActivity).clear();
                                            Intent intent = new Intent(mActivity, LoginActivity.class);
                                            mActivity.startActivity(intent);
                                            mActivity.finish();
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
