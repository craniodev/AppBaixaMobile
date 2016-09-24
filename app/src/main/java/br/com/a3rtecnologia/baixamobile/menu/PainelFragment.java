package br.com.a3rtecnologia.baixamobile.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.EncerrarViagemDialog;
import br.com.a3rtecnologia.baixamobile.dialogs.IniciarViagemDialog;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.ocorrencia.AtualizaEncomendaPendenteTimerTask;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab.TabViewPagerAdapter;
import br.com.a3rtecnologia.baixamobile.tab_lista.FinalizarViagemVolley;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaAsyncTask;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 30/07/16.
 */
public class PainelFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static Button painelIniciarViagem;
    public static Button painelFinalizarViagem;
    public static TextView painelMensagem;

    public static ProgressDialog mProgressDialog;
    private TabViewPagerAdapter tabViewPagerAdapter;
    private Context mContext;


    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    private SessionManager sessionManager;
    public EncomendaBusiness encomendaBusiness;
    private StatusBusiness statusBusiness;




    public void startTimer() {

        //set a new Timer
        timer = new Timer();

        showProgress(true);
        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 500, 10000); //
    }

    public void stoptimertask(View v) {

        //stop the timer, if it's not already null
        if (timer != null) {

            timer.cancel();

            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {

                        encomendaBusiness = new EncomendaBusiness(mContext);
                        TabItemMapaAsyncTask task  =new TabItemMapaAsyncTask(mContext, tabViewPagerAdapter.getTabMap(), timer, timerTask, mProgressDialog, tabViewPagerAdapter.getTabMap().getMap());

                        List<Encomenda> encomendaList = encomendaBusiness.buscarEntregasEmRotaLimit10();

                        task.execute(encomendaList);
                    }
                });
            }
        };
    }









    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *Inflate fragment_tab and setup Views.
         */
        this.mContext = getContext();
        this.sessionManager = new SessionManager(mContext);
        this.statusBusiness = new StatusBusiness(mContext);
        this.encomendaBusiness = new EncomendaBusiness(mContext);


        View fragment_tab = inflater.inflate(R.layout.fragment_tab, null);
        tabLayout = (TabLayout) fragment_tab.findViewById(R.id.tabs);
        viewPager = (ViewPager) fragment_tab.findViewById(R.id.viewpager);
        painelIniciarViagem = (Button) fragment_tab.findViewById(R.id.painelIniciarViagem);
        painelFinalizarViagem = (Button) fragment_tab.findViewById(R.id.painelFinalizarViagem);
        painelMensagem = (TextView) fragment_tab.findViewById(R.id.painelMensagem);




//        buttonIniciarViagem();
//        buttonFinalizarViagem();

        createLoading(mContext);

        /**
         *Set an Apater for the View Pager
         */
        tabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(tabViewPagerAdapter);

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {

            @Override
            public void run() {

                tabLayout.setupWithViewPager(viewPager);
            }
        });



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                System.out.println("onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {

                System.out.println("onPageSelected");

                if(viewPager.getCurrentItem() == 0){

                    System.out.println("TAB LISTA");

                }else if(viewPager.getCurrentItem() == 1){

                    System.out.println("TAB MAP");

                    startTimer();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                System.out.println("onPageScrollStateChanged");
            }
        });



//        /**
//         * REGRA PARA BOTOES OU MENSAGEM - INICIAR/FINALIZAR VIAGEM
//         */
//        exibirBotaoIniciarFinalizarViagem();

        return fragment_tab;
    }




    @Override
    public void onStart() {
        super.onStart();

        //startTimer();

        /**
         * REGRA PARA BOTOES OU MENSAGEM - INICIAR/FINALIZAR VIAGEM
         */
//        exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
        MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

    }





//    public static void exibirBotaoIniciarFinalizarViagem(StatusBusiness statusBusiness, EncomendaBusiness encomendaBusiness){
//
//        boolean viagemIniciada = statusBusiness.verificarViagemIniciada();
//        Status status = statusBusiness.getStatus();
//        //String isFinalizadoForcado = sessionManager.getFinalizarViagemOcorrenciaForcado();
//
//        /**
//         * VIAGEM JA INICIADA
//         */
//        if(viagemIniciada){
//
//            //BOTAO INICIAR VIAGEM
//            painelIniciarViagem.setVisibility(View.GONE);
//            painelFinalizarViagem.setVisibility(View.VISIBLE);
//            painelMensagem.setVisibility(View.GONE);
//
//        }else if(status != null){
//
//            /**
//             * NAO EXISTE ENCOMENDAS
//             */
//            int total = encomendaBusiness.countEncomendasEmRotaAndLiberado();
//            if (total == 0) {
//
//                //MENSAGEM DE QUE NAO TEM ENCOMENDAS NO MOMENTO
//                painelIniciarViagem.setVisibility(View.GONE);
//                painelFinalizarViagem.setVisibility(View.GONE);
//                painelMensagem.setVisibility(View.VISIBLE);
//
//            /**
//             * EXISTE ENCOMENDAS
//             */
//            }else{
//
//                //BOTAO INICIAR VIAGEM
//                painelIniciarViagem.setVisibility(View.VISIBLE);
//                painelFinalizarViagem.setVisibility(View.GONE);
//                painelMensagem.setVisibility(View.GONE);
//            }
//
//        }else{
//
//            /**
//             * NAO EXISTE ENCOMENDAS
//             */
//            int total = encomendaBusiness.countEncomendasEmRotaAndLiberado();
//            if (total == 0) {
//
//                //MENSAGEM DE QUE NAO TEM ENCOMENDAS NO MOMENTO
//                painelIniciarViagem.setVisibility(View.GONE);
//                painelFinalizarViagem.setVisibility(View.GONE);
//                painelMensagem.setVisibility(View.VISIBLE);
//
//                /**
//                 * EXISTE ENCOMENDAS
//                 */
//            }else{
//
//                //BOTAO INICIAR VIAGEM
//                painelIniciarViagem.setVisibility(View.VISIBLE);
//                painelFinalizarViagem.setVisibility(View.GONE);
//                painelMensagem.setVisibility(View.GONE);
//            }
//        }
//    }




    /**
     * INICIAR JORNADA DE TRABALHO
     *
     * TODAS ENCOMENDAS INICARAM COM STATUS DE - EM ROTA
     */
    private void buttonIniciarViagem(){

//        painelIniciarViagem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

//                createLoading(mContext, "Iniciando Viagem");
//                showProgress(true);
//
//                IniciarViagemDialog dialog = new IniciarViagemDialog(getActivity(), "Iniciar Viagem", "Deseja iniciar sua viagem?", painelIniciarViagem, painelFinalizarViagem, false);
//            }
//        });
    }

    private void buttonFinalizarViagem(){

//        painelFinalizarViagem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

                createLoading(mContext, "Finalizando Viagem");
                showProgress(true);

                /**
                 * AGUARDAR RESPOSTA
                 *
                 * PODE RECEBER LISTA DE ENCOMENDAS PENDENTES PARA ENCERRAR AINDA
                 *
                 */
                FinalizarViagemVolley finalizarViagemVolley = new FinalizarViagemVolley(getActivity(), new DelegateEncomendaAsyncResponse() {
                    @Override
                    public void processFinish(boolean finish, String resposta) {

                        showProgress(false);

                        /**
                         * SE NAO TEM ENCOMENDAS PENDENTES
                         * SUCESSO
                         */
                        if(resposta.equalsIgnoreCase("SUCESSO")) {

                            painelIniciarViagem.setVisibility(View.VISIBLE);
                            painelFinalizarViagem.setVisibility(View.GONE);

                            Status status = statusBusiness.getStatus();
                            if (status != null) {

                                /**
                                 * REMOVIDO
                                 */
//                                status.setViagemIniciada(false);
//                                statusBusiness.atualizar(status);

                                /**
                                 * ADICIONADO
                                 */
                                statusBusiness.stopJornadaTrabalho();
//                                PainelFragment.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
                                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                                if(AtualizaEncomendaPendenteTimerTask.timer != null){

                                    AtualizaEncomendaPendenteTimerTask.timer.cancel();
                                }
                            }

                        }else{

                            /**
                             * TEM ENCOMENDAS PENDENTES - ENCERRAR FORCADO
                             */

//                            StatusDialog dialog = new StatusDialog(getActivity(), "Finalizar Viagem", resposta, false);
                            EncerrarViagemDialog dialog = new EncerrarViagemDialog(getActivity(), "Finalizar Viagem", resposta);
                        }
                    }

                    @Override
                    public void processCanceled(boolean cancel) {

                        System.out.println("ERRO FINALIZAR VIAGEM");
                        showProgress(false);
                    }
                });
//            }
//        });
    }










    private void createLoading(Context mContext){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Carregando encomendas");
    }

    private void createLoading(Context mContext, String message){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    public static void showProgress(boolean isShow){

        if(mProgressDialog != null && isShow && !mProgressDialog.isShowing()){

            mProgressDialog.show();

        }else if(mProgressDialog != null && !isShow && mProgressDialog.isShowing()){

            mProgressDialog.hide();
        }
    }

}