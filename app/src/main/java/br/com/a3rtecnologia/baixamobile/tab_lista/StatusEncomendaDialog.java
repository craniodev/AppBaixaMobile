package br.com.a3rtecnologia.baixamobile.tab_lista;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.EntregaAcitivty;
import br.com.a3rtecnologia.baixamobile.entrega.IniciarEntregaVolley;
import br.com.a3rtecnologia.baixamobile.ocorrencia.OcorrenciaActivity;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class StatusEncomendaDialog extends Activity{

    private EncomendaBusiness encomendaBusiness;
    private Activity mActivity;
    private SessionManager sessionManager;
    private StatusBusiness statusBusiness;



    public StatusEncomendaDialog(final Activity mActivity){

        this.mActivity = mActivity;
        encomendaBusiness = new EncomendaBusiness(mActivity);
        sessionManager = new SessionManager(mActivity);
        statusBusiness = new StatusBusiness(mActivity);

        LayoutInflater li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = li.inflate(R.layout.alertdialog_finalizar_viagem, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Status da Entrega");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setNegativeButton("OCORRÊNCIA",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        abrirTelaOcorrencias();
                    }
                });

        builder.setPositiveButton("ENTREGUE",
                new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

//                    String idEncomenda = sessionManager.getValue("CORRENTE");
//                    Long id = Long.parseLong(idEncomenda);

                    Status status = statusBusiness.getStatus();


                    Encomenda encomendaCorrente = encomendaBusiness.buscarEncomendaCorrente(status.getIdEncomendaCorrente());

                    /**
                     * REMOVIDO
                     */
//                    iniciarEntrega(encomendaCorrente);

                    abrirTelaEntrega();
                }
            });




        final AlertDialog alert = builder.create();
        alert.show();


        /**
         * retrieve findViewById
         */
//        TextView dialog_finalizar_viagem_msg = (TextView) view.findViewById(R.id.dialog_finalizar_viagem_msg);
//        dialog_finalizar_viagem_msg.setText(isNull(encomenda.getNrRomaneio()));


    }


    /**
     * REMOVIDO
     */
//    private void iniciarEntrega(Encomenda encomendaEntregue){
//
//        LatLng latLng = null;
//
//        IniciarEntregaVolley iniciarEntregaVolley = new IniciarEntregaVolley(mActivity, latLng, encomendaEntregue, new DelegateEncomendaAsyncResponse() {
//
//            @Override
//            public void processFinish(boolean finish, String resposta) {
//
//                System.out.println(resposta);
//
////                finalizarViagemx();
//            }
//
//            @Override
//            public void processCanceled(boolean cancel) {
//
//                System.out.println("ERRO INICIAR ENTREGA");
//            }
//        });
//    }







//    public void finalizarViagemX(){
//
//        FinalizarViagemVolley finalizarViagemVolley = new FinalizarViagemVolley(mActivity, new DelegateEncomendaAsyncResponse() {
//            @Override
//            public void processFinish(boolean finish, String resposta) {
//
//                System.out.println(resposta);
//            }
//
//            @Override
//            public void processCanceled(boolean cancel) {
//
//                System.out.println("ERRO FINALIZAR VIAGEM");
//            }
//        });
//    }
















    private void abrirTelaEntrega(){

//        Intent intent = new Intent(mActivity, EntregaActivity.class);
//        mActivity.startActivity(intent);


        Intent intent = new Intent(mActivity, EntregaAcitivty.class);
        mActivity.startActivity(intent);


    }

    private void abrirTelaOcorrencias(){

        Intent intent = new Intent(mActivity, OcorrenciaActivity.class);
        mActivity.startActivity(intent);
    }


}
