package br.com.a3rtecnologia.baixamobile.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.menu.PainelFragment;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemVolley;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class IniciarViagemDialog {

    private SessionManager sessionManager;
    private StatusBusiness statusBusiness;



    public IniciarViagemDialog(final Activity mActivity, String title, String mensagem, final Button painelIniciarViagem, final Button painelFinalizarViagem, final boolean encerrar, final TextView viagemTextView) {

        sessionManager = new SessionManager(mActivity);
        statusBusiness = new StatusBusiness(mActivity);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(mensagem);
        builder.setCancelable(false);
        builder.setPositiveButton("SIM",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PainelFragment.showProgress(true);

                        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mActivity, TabItemMapaFragment.map);
                        timerTaskLocation.startTimer();

                        LatLng latLng = timerTaskLocation.getMyLatLng();
                        timerTaskLocation.stoptimertask();

                        IniciarViagemVolley iniciarViagemVolley = new IniciarViagemVolley(mActivity, latLng, new DelegateEncomendaAsyncResponse() {
                            @Override
                            public void processFinish(boolean finish, String resposta) {

                                System.out.println("Sucesso");

                                viagemTextView.setText("Finalizar Viagem");


                                if(painelIniciarViagem != null){

                                    painelIniciarViagem.setVisibility(View.GONE);
                                }

                                if(painelFinalizarViagem != null){

                                    painelFinalizarViagem.setVisibility(View.VISIBLE);
                                }

                                PainelFragment.showProgress(false);
                            }

                            @Override
                            public void processCanceled(boolean cancel) {

                                System.out.println("Error");
                                PainelFragment.showProgress(false);
                            }
                        });
                    }
                });

        builder.setNegativeButton("NÃO",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PainelFragment.showProgress(false);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

}
