package br.com.a3rtecnologia.baixamobile.encomenda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega.ListaItemDetalheDialog;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega.ListaItemDetalheEncerrarDialog;
import br.com.a3rtecnologia.baixamobile.util.OnItemClickListener;

/**
 * Created by maclemon on 11/06/16.
 */
public class EncomendaAdapter extends RecyclerView.Adapter<EncomendaViewHolder> {


    private List<Encomenda> encomendaList;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private StatusBusiness statusBusiness;
    private EncomendaBusiness encomendaBusiness;
    private long MENU_LIST;



    public EncomendaAdapter(List<Encomenda> encomendaList, Activity mActivity, ProgressDialog mProgressDialog, long MENU_LIST){

        this.MENU_LIST = MENU_LIST;
        this.encomendaList = encomendaList;
        this.mActivity = mActivity;
        this.mProgressDialog = mProgressDialog;
        this.statusBusiness = new StatusBusiness(mActivity);
        this.encomendaBusiness = new EncomendaBusiness(mActivity);
    }







    @Override
    public EncomendaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_encomenda, viewGroup, false);

        EncomendaViewHolder encomendaViewHolder = new EncomendaViewHolder(view);

        return encomendaViewHolder;
    }



    @Override
    public void onBindViewHolder(EncomendaViewHolder encomendaViewHolder, int position) {

        String id = String.valueOf(encomendaList.get(position).getIdOrdem());
        encomendaViewHolder.getIdCount().setText(id);

        Long idEncomendaCorrente = statusBusiness.getIdEncomendaCorrente();
        if(idEncomendaCorrente != null && idEncomendaCorrente == getItemId(position)){

            encomendaViewHolder.getIdCount().setBackgroundResource(R.drawable.shape_circular_border_green);

        }else if(encomendaList.get(position).getFlagTratado() != null && encomendaList.get(position).getFlagTratado()){

            encomendaViewHolder.getIdCount().setBackgroundResource(R.drawable.shape_circular_border_blue);

        }else{

            encomendaViewHolder.getIdCount().setBackgroundResource(R.drawable.shape_circular_border_orange);
        }


        encomendaViewHolder.getNmDestinatarioTextView().setText(encomendaList.get(position).getNmDestinatario());

        StringBuilder stringBuilder = new StringBuilder();
        validateAndAppend(encomendaList.get(position).getEndereco(), ", ", stringBuilder);
        validateAndAppend(encomendaList.get(position).getNrEndereco(), ", ", stringBuilder);
        validateAndAppend(encomendaList.get(position).getBairro(), " - ", stringBuilder);
        validateAndAppend(encomendaList.get(position).getCidade(), " - ",stringBuilder);
        validateAndAppend(encomendaList.get(position).getUF(), " - ",stringBuilder);
        validateAndAppend(encomendaList.get(position).getCep(), "",stringBuilder);

        encomendaViewHolder.getEnderecoTextView().setText(stringBuilder.toString());

        encomendaViewHolder.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                acaoEventClickListaItem(position);
            }
        });

    }




    private void acaoEventClickListaItem(int position){

        /**
         * TELA LISTAGEM DE ENCOMENDAS
         */
        boolean viagemIniciada = statusBusiness.verificarViagemIniciada();
        if(viagemIniciada && MENU_LIST == EnumEncomendaStatus.EM_ROTA.getKey()){

            boolean existe = statusBusiness.existeEncomendaEmAndamento();

            if(!existe){

                /**
                 * ABRIR DIALOG PARA INICIAR UMA ENTREGA
                 */
                ListaItemDetalheDialog dialog = new ListaItemDetalheDialog(mActivity, encomendaList.get(position));

            }else {

                Status status = statusBusiness.getStatus();
                if(status != null){

                    if(status.getIdEncomendaCorrente() == encomendaList.get(position).getIdEncomenda()){

                        /**
                         * ABRIR DIALOG PARA ENCERRAR ENCOMENDA
                         */
                        ListaItemDetalheEncerrarDialog dialog = new ListaItemDetalheEncerrarDialog(mActivity, encomendaList.get(position));

                    }else{

                        /**
                         * TENTATIVA DE ABRIR UMA NOVA ENCOMENDA
                         *
                         * NOTIFICAR QUE JA EXISTE UMA ENCOMENDA EM ANDAMENTO
                         */
                        StatusDialog dialog = new StatusDialog(mActivity, "Status Encomenda", "Existe uma encomenda em andamento.", false);
                    }

                }else {

                    /**
                     * INICIAR JORNADA DE TRABALHO
                     */
                    StatusDialog dialog = new StatusDialog(mActivity, "Iniciar Viagem", "Você ainda não iniciou a viagem.", false);
                }
            }



        }else{

            /**
             * TELA LISTAGEM OCORRENCIA
             */
            if(MENU_LIST == EnumEncomendaStatus.OCORRENCIA.getKey()){

                StatusDialog dialog = new StatusDialog(mActivity, "Ocorrência", "A encomenda já foi finalizada.", false);

                /**
                 * TELA LISTAGEM ENTREGUES
                 */
            }else if(MENU_LIST == EnumEncomendaStatus.ENTREGUE.getKey()){

                StatusDialog dialog = new StatusDialog(mActivity, "Entregue", "A encomenda já foi finalizada.", false);

                /**
                 * NOTIFICACAO DE QUE NAO FOI INICIADO JORNADA DE TRABALHO
                 */
            }else{

                StatusDialog dialog = new StatusDialog(mActivity, "Iniciar Viagem", "Você ainda não iniciou a viagem.", false);
            }
        }
    }





    private void validateAndAppend(String value, String separator, StringBuilder stringBuilder){

        if(value != null && !value.equals("")){

            stringBuilder.append(value);
            stringBuilder.append(separator);
        }
    }



    @Override
    public int getItemCount() {

        return encomendaList.size();
    }


    @Override
    public long getItemId(int position) {

        return encomendaList.get(position).getIdEncomenda();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
