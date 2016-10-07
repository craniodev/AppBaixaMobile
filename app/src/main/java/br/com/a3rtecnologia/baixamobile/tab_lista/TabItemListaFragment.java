package br.com.a3rtecnologia.baixamobile.tab_lista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendasAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaAdapter;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaVolley;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.menu.MenuDrawerActivity;
import br.com.a3rtecnologia.baixamobile.menu.PainelFragment;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;
import br.com.a3rtecnologia.baixamobile.util.DelegateResponse;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 31/07/16.
 */
public class TabItemListaFragment extends Fragment {

    private SwipyRefreshLayout mSwipyRefreshLayout;
    private static RecyclerView recyclerView;

    private static List<Encomenda> encomendaList;

    private static Context mContext;
    private ProgressDialog progress;

    private static EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;

    static EncomendaAdapter encomendaAdapter;
    private SessionManager sessionManager;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment_panel_encomenda = inflater.inflate(R.layout.fragment_panel_list, container, false);

        mContext = getContext();
        encomendaBusiness = new EncomendaBusiness(mContext);
        statusBusiness = new StatusBusiness(mContext);
        sessionManager = new SessionManager(mContext);

        createLoading(mContext);

        recyclerView = (RecyclerView) fragment_panel_encomenda.findViewById(R.id.recycler_view_encomenda);

        createReciclerView();

        mSwipyRefreshLayout = (SwipyRefreshLayout) fragment_panel_encomenda.findViewById(R.id.swipeRefreshLayout);
        swipyRefresh();

        return fragment_panel_encomenda;
    }



    @Override
    public void onStart() {
        super.onStart();

        System.out.println("");

        String primeiroLogin = sessionManager.isPrimeiroLogin();
        if(primeiroLogin.equalsIgnoreCase("")) {

            connectionServer();

        }else {

            downloadEncomendas();
        }

        updateAdapter();
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




    public static void updateAdapter(){

        /**
         * GARANTIR ATUALIZACAO
         */
        encomendaList = encomendaBusiness.buscarEntregasEmRota();
        encomendaAdapter = new EncomendaAdapter(encomendaList, (Activity)mContext, null, EnumEncomendaStatus.EM_ROTA.getKey());
        encomendaAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(encomendaAdapter);
    }

    private void updateAdapter(List<Encomenda> encomendaList){

        this.encomendaList = encomendaList;

        encomendaAdapter = new EncomendaAdapter(encomendaList, getActivity(), null, EnumEncomendaStatus.EM_ROTA.getKey());
        encomendaAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(encomendaAdapter);
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



    private void connectionServer() {

        showProgress(true);

        int total = encomendaBusiness.count();
        int pendentesSincronizar = encomendaBusiness.countNaoSincronizadas();
        boolean result = false;

        if (total == 0 && pendentesSincronizar == 0) {

            result = true;
        }

        if (InternetStatus.isNetworkAvailable(mContext) && result) {

            /**
             * LIMPAR BASE PARA GARANTIR
             */
            encomendaBusiness.deleteAll();

            buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_EM_ROTA.getValue());

            showProgress(false);

        } else {

            List<Encomenda> encomendas = null;

            encomendas = encomendaBusiness.buscarEntregasEmRota();
            int encomendasEmRota = encomendas.size();


            if (encomendas != null) {

                /**
                 * SE NAO TEM ENCOMENDAS EM ROTA(LOCAL)
                 * LIMPA A BASE(LOCAL) E BUSCA MAIS NA WEB
                 *
                 *
                 * REGRA PARA ULTIMA ENCOMENDA
                 *
                 */
                if (encomendasEmRota == 0 && pendentesSincronizar == 0) {

                    /**
                     * rever, deletar somente as EM ROTA
                     */
                    encomendaBusiness.deleteAll();

                    /**
                     * buscar somente as deletadas, EM ROTA
                     */
                    buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_EM_ROTA.getValue());


                    showProgress(false);

                } else {

                    /**
                     * EXISTE ENCOMENDAS EM ROTA(LOCAL)
                     *
                     */
                    updateAdapter(encomendas);

                    showProgress(false);
                }


            } else {

                /***
                 * TODAS ENCOMENDAS ENVIADAS
                 * MAS PENDENTES DE SINCRONISMO
                 *
                 */
                encomendas = encomendaBusiness.buscarEntregasEmRota();
                updateAdapter(encomendas);
            }
        }
    }



    private void buscarAPIOnline(String statusEncomenda){

        new EncomendaVolley(getContext(), statusEncomenda, 0, new DelegateEncomendasAsyncResponse() {

            @Override
            public void processFinish(boolean finish, Encomendas encomendas) {

                System.out.println(finish);

                updateAdapter(encomendas.getEncomendas());

                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                showProgress(false);
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println(cancel);
                showProgress(false);
            }
        });
    }



    private void buscarAPIOnlineDownload(String statusEncomenda){

        new EncomendaVolley(getContext(), statusEncomenda, 1, new DelegateEncomendasAsyncResponse() {

            @Override
            public void processFinish(boolean finish, Encomendas encomendas) {

                System.out.println(finish);

                updateAdapter(encomendas.getEncomendas());

                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                showProgress(false);
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println(cancel);
                showProgress(false);
            }
        });
    }




    private void buscarAPIOnlineOthers(String statusEncomenda){

        new EncomendaVolley(getContext(), statusEncomenda, 1, new DelegateEncomendasAsyncResponse() {

            @Override
            public void processFinish(boolean finish, Encomendas encomendas) {

                System.out.println(finish);

                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                showProgress(false);
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println(cancel);
                showProgress(false);
            }
        });
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
        progress.setMessage("Carregando lista");
    }

    private void showProgress(boolean isShow){

        if(progress != null && isShow && !progress.isShowing()){

            progress.show();

        }else if(progress != null && !isShow && progress.isShowing()){

            progress.hide();
        }
    }



    private void downloadEncomendas(){

        String primeiroLogin = sessionManager.isPrimeiroLogin();
        if(primeiroLogin.equalsIgnoreCase("1")) {

            sessionManager.setPrimeiroLogin("");

            /**
             * LIMPAR BASE PARA GARANTIR
             */
            encomendaBusiness.deleteAll();

            buscarAPIOnlineDownload(EnumAPI.ID_TIPO_ENCOMENDA_EM_ROTA.getValue());
            buscarAPIOnlineOthers(EnumAPI.ID_TIPO_ENCOMENDA_ENTREGUE.getValue());
            buscarAPIOnlineOthers(EnumAPI.ID_TIPO_ENCOMENDA_PENDENTE.getValue());
        }
    }

}
