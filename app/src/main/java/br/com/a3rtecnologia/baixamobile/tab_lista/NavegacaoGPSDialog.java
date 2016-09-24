package br.com.a3rtecnologia.baixamobile.tab_lista;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.IniciarEntregaVolley;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class NavegacaoGPSDialog extends Activity{

    private Activity mActivity;
    private EncomendaBusiness encomendaBusiness;
    private SessionManager sessionManager;



    public NavegacaoGPSDialog(final Activity mActivity, final Encomenda encomenda){

        this.mActivity = mActivity;
        this.encomendaBusiness = new EncomendaBusiness(mActivity);
        this.sessionManager = new SessionManager(mActivity);

        LayoutInflater li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = li.inflate(R.layout.alertdialog_navegacao_gps, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Navegação");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setNegativeButton("FECHAR",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

        builder.setPositiveButton("NAVEGAR COM WAZE",
                new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Double latitude = encomenda.getLatitude();
                    Double longitude = encomenda.getLongitude();

                    try {

                        String url = "";
                        if(latitude != null && longitude != null) {

                            url = "waze://?ll=" + latitude + "," + longitude + "&z10&navigate=yes";

                        }else{

                            url = "waze://?q="+ encomenda.getEndereco() +"&z10&navigate=yes";
                        }

                        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        mActivity.startActivityForResult(intent, 1);

                    } catch ( ActivityNotFoundException ex) {

                        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                        mActivity.startActivity(intent);
                    }



                    /**
                     * UPDATE LISTA ENCOMENDAS
                     *
                     * ALTERACAO STATUS BOLINHAS
                     */
                    TabItemListaFragment.updateAdapter();
                }
            });


        final AlertDialog alert = builder.create();
        alert.show();

//        setarValoresCampos(view, encomenda);
    }


    /**
     * ADICIONADO
     *
     * @param encomendaEntregue
     */
//    private void iniciarEntrega(Encomenda encomendaEntregue){
//
//        LatLng latLng = null;
//
//        IniciarEntregaVolley iniciarEntregaVolley = new IniciarEntregaVolley(mActivity, encomendaEntregue, new DelegateEncomendaAsyncResponse() {
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


    /**
     * REMOVIDO
     *
     *
     * @param view
     * @param encomenda
     */
//    /**
//     * PASSAR ESSA CHAMADA PARA UM TIMER TASK
//     *
//     * @param encomenda
//     */
//    public void iniciarViagem(Encomenda encomenda){
//
////        LatLng latLng = new LatLng(encomenda.getLatitude(), encomenda.getLongitude());
//        LatLng latLng = null;
//
//        IniciarViagemVolley iniciarViagemVolley = new IniciarViagemVolley(mActivity, latLng, new DelegateEncomendaAsyncResponse() {
//            @Override
//            public void processFinish(boolean finish, String resposta) {
//
//                System.out.println(resposta);
//            }
//
//            @Override
//            public void processCanceled(boolean cancel) {
//
//                System.out.println("ERRO INICIAR VIAGEM");
//            }
//        });
//    }




    private void setarValoresCampos(View view, Encomenda encomenda){

        /**
         * retrieve findViewById
         */
        TextView dialog_item_detalhe_NrRomaneioTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_NrRomaneioTextView);
        dialog_item_detalhe_NrRomaneioTextView.setText(isNull(encomenda.getNrRomaneio()));

        TextView dialog_item_detalhe_IdEncomendaTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_IdEncomendaTextView);
        dialog_item_detalhe_IdEncomendaTextView.setText(isNull(encomenda.getIdEncomenda()));

        TextView dialog_item_detalhe_NmDestinatarioTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_NmDestinatarioTextView);
        dialog_item_detalhe_NmDestinatarioTextView.setText(isNull(encomenda.getNmDestinatario()));

        TextView dialog_item_detalhe_EnderecoTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_EnderecoTextView);
        dialog_item_detalhe_EnderecoTextView.setText(montarEnderecoCompleto(encomenda));

        TextView dialog_item_detalhe_NrNotaTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_NrNotaTextView);
        dialog_item_detalhe_NrNotaTextView.setText(isNull(encomenda.getNrNota()));

        TextView dialog_item_detalhe_NrPedidoTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_NrPedidoTextView);
        dialog_item_detalhe_NrPedidoTextView.setText(isNull(encomenda.getNrPedido()));

        TextView dialog_item_detalhe_DescStatusTextView = (TextView) view.findViewById(R.id.dialog_item_detalhe_DescStatusTextView);
        dialog_item_detalhe_DescStatusTextView.setText(isNull(encomenda.getDescStatus()));
    }



    private String montarEnderecoCompleto(Encomenda encomenda){

        StringBuilder stringBuilder = new StringBuilder();
        validateAndAppend(encomenda.getEndereco(), " - ", stringBuilder);
        validateAndAppend(encomenda.getNrEndereco(), " - ", stringBuilder);
        validateAndAppend(encomenda.getBairro(), " - ", stringBuilder);
        validateAndAppend(encomenda.getCidade(), " - ",stringBuilder);
        validateAndAppend(encomenda.getUF(), " - CEP: ",stringBuilder);
        validateAndAppend(encomenda.getCep(), "",stringBuilder);

        return stringBuilder.toString();
    }




    private void validateAndAppend(String value, String separator, StringBuilder stringBuilder){

        if(value != null && !value.equals("")){

            stringBuilder.append(value);
            stringBuilder.append(separator);
        }
    }




    private String isNull(Object value){

        if(value != null && !value.equals("")){

            return String.valueOf(value);

        }else{

            return "--";
        }
    }

}