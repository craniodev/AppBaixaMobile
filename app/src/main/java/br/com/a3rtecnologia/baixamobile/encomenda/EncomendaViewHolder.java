package br.com.a3rtecnologia.baixamobile.encomenda;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.util.OnItemClickListener;


/**
 * Created by maclemon on 11/06/16.
 */
public class EncomendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView cardView;

    private TextView idCount;
    private TextView NmDestinatarioTextView;
    private TextView EnderecoTextView;


    private OnItemClickListener onItemClickListener;



    public EncomendaViewHolder(View view){
        super(view);

        cardView = (CardView) view.findViewById(R.id.cardview_encomenda);

        idCount = (TextView) view.findViewById(R.id.countTextView);

        NmDestinatarioTextView = (TextView) view.findViewById(R.id.NmDestinatarioTextView);
        EnderecoTextView = (TextView) view.findViewById(R.id.EnderecoTextView);
        view.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        onItemClickListener.onItemClick(view, getAdapterPosition());
    }


    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TextView getIdCount() {
        return idCount;
    }

    public void setId(TextView idCount) {
        this.idCount = idCount;
    }


    public TextView getNmDestinatarioTextView() {
        return NmDestinatarioTextView;
    }

    public void setNmDestinatarioTextView(TextView nmDestinatarioTextView) {
        NmDestinatarioTextView = nmDestinatarioTextView;
    }

    public TextView getEnderecoTextView() {
        return EnderecoTextView;
    }

    public void setEnderecoTextView(TextView enderecoTextView) {
        EnderecoTextView = enderecoTextView;
    }



}
