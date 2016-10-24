package br.com.a3rtecnologia.baixamobile.iniciar_entrega;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.a3rtecnologia.baixamobile.EnumInicioEntregaEnvio;
import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao.IniciarEntregaReceiver;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao.SincronizaInicioEntregaTimerTask;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_lista.NavegacaoGPSDialog;
import br.com.a3rtecnologia.baixamobile.tab_lista.TabItemListaFragment;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 01/08/16.
 */
public class ListaItemDetalheDialog extends Activity{

    private Activity mActivity;
    private EncomendaBusiness encomendaBusiness;
    private SessionManager sessionManager;
    private StatusBusiness statusBusiness;




    public ListaItemDetalheDialog(final Activity mActivity, final Encomenda encomenda){

        this.mActivity = mActivity;
        this.encomendaBusiness = new EncomendaBusiness(mActivity);
        this.sessionManager = new SessionManager(mActivity);
        this.statusBusiness = new StatusBusiness(mActivity);

        LayoutInflater li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = li.inflate(R.layout.alertdialog_item_detalhe, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Detalhe da Entrega");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setNegativeButton("FECHAR",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

        builder.setPositiveButton("INICIAR ENTREGA",
                new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    /**
                     * STATUS EM ROTA(MOBILE) - SAIU PARA ENTREGA(API)
                     */
                    encomendaBusiness.atualizarStatusEncomendaEmRota(encomenda);
                    Status status = statusBusiness.getStatus();
                    status.setIdEncomendaCorrente(encomenda.getIdEncomenda());
                    statusBusiness.salvar(status);

                    /**
                     * ADICIONADO
                     */
                    encomenda.setDataInicioEntrega(DateUtil.getDataAtual());
                    encomenda.setFlagEnviado(EnumStatusEnvio.NAO_SINCRONIZADO.getKey());

                    //novo status
                    encomenda.setFlagInicioEntrega(EnumInicioEntregaEnvio.SIM.getKey());

                    encomenda.setIdStatus(EnumEncomendaStatus.SAIU_ENTREGA.getKey());
                    encomenda.setDescStatus(EnumEncomendaStatus.SAIU_ENTREGA.getValue());
                    encomendaBusiness.update(encomenda);
                    iniciarEntrega(encomenda);

                    /**
                     * ADICIONA ENCOMENDA CORRENTE
                     */
                    statusBusiness.addEncomendaCorrente(encomenda.getIdEncomenda());
                    NavegacaoGPSDialog navegacaoGPSDialog = new NavegacaoGPSDialog(mActivity, encomenda);

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

        setarValoresCampos(view, encomenda);
    }








    /**
     * ADICIONADO
     *
     * @param encomendaEntregue
     */
    private void iniciarEntrega(Encomenda encomendaEntregue){

//        SincronizaInicioEntregaTimerTask sincronizaInicioEntregaTimerTask = new SincronizaInicioEntregaTimerTask(mActivity);

        Intent iniciarEntregaIntent = new Intent(mActivity, IniciarEntregaReceiver.class);
        iniciarEntregaIntent.putExtra("OPERACAO", "START");
        mActivity.sendBroadcast(iniciarEntregaIntent);
    }



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