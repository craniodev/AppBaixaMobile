package br.com.a3rtecnologia.baixamobile.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaAdapter;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.ocorrencia.SincronizaEncomendaPendenteTimerTask;

/**
 * Created by maclemon on 01/08/16.
 */
public class EntregaPendenteFragment extends Fragment {

    private SwipyRefreshLayout mSwipyRefreshLayout;
    private RecyclerView recyclerView;

    //LISTA COMPARTILHADA COM MAPA
    public static List<Encomenda> encomendaList;

    private Context mContext;
    private ProgressDialog progress;

    private EncomendaBusiness encomendaBusiness;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *Inflate fragment_tab and setup Views.
         */
        View fragment_pending = inflater.inflate(R.layout.fragment_pending, null);


        mContext = getContext();
        encomendaBusiness = new EncomendaBusiness(mContext);
//        getDao();

        createLoading(mContext);

        recyclerView = (RecyclerView) fragment_pending.findViewById(R.id.recycler_view_pending);

        createReciclerView();

        connectionServer();

        mSwipyRefreshLayout = (SwipyRefreshLayout) fragment_pending.findViewById(R.id.swipeRefreshLayout);
        swipyRefresh();

        SincronizaEncomendaPendenteTimerTask sincronizaEncomendaPendenteTimerTask = new SincronizaEncomendaPendenteTimerTask(mContext);
//        sincronizaEncomendaPendenteTimerTask.startTimer();

        return fragment_pending;
    }





    private void createReciclerView(){

        recyclerView.setHasFixedSize(true);

        /**
         * LayoutManager Options
         * 1 - LinearLayoutManager
         * 2 - GridLayoutManager
         * 3 - StaggeredGridLayoutManager
         * 4 - Person
         */
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(llm);
    }




    private void updateAdapter(){

        EncomendaAdapter produtoAdapter = new EncomendaAdapter(encomendaList, getActivity(), null, EnumEncomendaStatus.OCORRENCIA.getKey());
        produtoAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(produtoAdapter);
    }

    private void updateAdapter(List<Encomenda> encomendaList){

        this.encomendaList = encomendaList;

        EncomendaAdapter produtoAdapter = new EncomendaAdapter(encomendaList, getActivity(), null, EnumEncomendaStatus.OCORRENCIA.getKey());
        produtoAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(produtoAdapter);
    }







    private void swipyRefresh(){

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if(direction == SwipyRefreshLayoutDirection.TOP){

                    refreshItems();

                }else if(direction == SwipyRefreshLayoutDirection.BOTTOM){

                    refreshComplete();//DESATIVADO
                }
            }
        });
    }




    private void connectionServer(){

        showProgress(true);


//        if(InternetStatus.isNetworkAvailable(mContext)) {
//
//            new EncomendaVolley(getContext(), new Usuario(), new DelegateEncomendasAsyncResponse() {
//
//                @Override
//                public void processFinish(boolean finish, Encomendas encomendas) {
//
//                    System.out.println(finish);
//                    updateAdapter(encomendas.getEncomendas());
//
//                    showProgress(false);
//                }
//
//                @Override
//                public void processCanceled(boolean cancel) {
//
//                    System.out.println(cancel);
//                    showProgress(false);
//                }
//
//            });
//
//
//        }else{

            List<Encomenda> encomendas = null;

//                encomendas = encomendaDao.queryForAll();
                encomendas = encomendaBusiness.buscarEntregasOcorrencia();
//        encomendas = encomendaBusiness.buscarEntregasFinalizadas();



            if(encomendas != null){

                updateAdapter(encomendas);

                showProgress(false);
            }

//        }
    }




    private void refreshItems() {

        connectionServer();

        refreshComplete();
    }




    private void refreshComplete() {

        mSwipyRefreshLayout.setRefreshing(false);
    }




    private void createLoading(Context mContext){

        progress = new ProgressDialog(mContext);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Carregando entregas pendentes");
    }

    private void showProgress(boolean isShow){

        if(progress != null && isShow && !progress.isShowing()){

            progress.show();

        }else if(progress != null && !isShow && progress.isShowing()){

            progress.hide();
        }
    }

    public void loadingShow(){

        progress = new ProgressDialog(mContext);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Carregando encomendas no mapa");

        showProgress(true);
    }

    public void loadingHide(){

        showProgress(false);
    }


}
