package br.com.a3rtecnologia.baixamobile.iniciar_viagem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao.IniciarViagemReceiver;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao.SincronizaIniciarViagemTimerTask;
import br.com.a3rtecnologia.baixamobile.menu.MenuDrawerActivity;
import br.com.a3rtecnologia.baixamobile.menu.PainelFragment;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class IniciarViagemDialog {

    private SessionManager sessionManager;
    private StatusBusiness statusBusiness;
    private IniciarViagemBusiness iniciarViagemBusiness;
    private EncomendaBusiness encomendaBusiness;

    private IniciarViagem iniciarViagem;




    public IniciarViagemDialog(final Activity mActivity, final TextView viagemTextView) {

        this.sessionManager = new SessionManager(mActivity);
        this.statusBusiness = new StatusBusiness(mActivity);
        this.iniciarViagemBusiness = new IniciarViagemBusiness(mActivity);
        this.encomendaBusiness = new EncomendaBusiness(mActivity);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Iniciar Viagem");
        builder.setMessage("Deseja iniciar sua viagem?");
        builder.setCancelable(false);
        builder.setPositiveButton("SIM",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PainelFragment.showProgress(true);

                        /**
                         * SALVAR DATA INICIO
                         */
                        statusBusiness.startJornadaTrabalho();



                        /**
                         * LOCALIZACAO
                         */
                        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mActivity, TabItemMapaFragment.map);
                        timerTaskLocation.startTimer();
                        LatLng latLng = timerTaskLocation.getMyLatLng();
                        timerTaskLocation.stoptimertask();



                        /**
                         * MODO OFFLINE
                         */
                        String idStr = sessionManager.getValue("id");
                        iniciarViagem = new IniciarViagem();
                        String dataInicio = statusBusiness.getDataInicioViagem();

                        iniciarViagem.setDataIteracao(dataInicio);
                        iniciarViagem.setLatitude(latLng.latitude);
                        iniciarViagem.setLongitude(latLng.longitude);
                        iniciarViagem.setIdMotorista(idStr);
                        iniciarViagem.setFgSincronizado(EnumStatusEnvio.NAO_SINCRONIZADO.getKey());

                        iniciarViagemBusiness.salvar(iniciarViagem);



                        /**
                         * MODO ONLINE (SINCRONIZAR)
                         */
//                        SincronizaIniciarViagemTimerTask task = new SincronizaIniciarViagemTimerTask(mActivity);
                        Intent iniciarViagemIntent = new Intent(mActivity, IniciarViagemReceiver.class);
                        iniciarViagemIntent.putExtra("OPERACAO", "START");
                        mActivity.sendBroadcast(iniciarViagemIntent);


                        /**
                         * UPDATE BOTAO INICIAR VIAGEM
                         */
                        PainelFragment.showProgress(false);
                        MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
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
