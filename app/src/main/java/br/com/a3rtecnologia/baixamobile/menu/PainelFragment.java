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
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab.TabViewPagerAdapter;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaAsyncTask;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

/**
 * Created by maclemon on 30/07/16.
 */
public class PainelFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public Button painelIniciarViagem;
    public Button painelFinalizarViagem;
    public TextView painelMensagem;

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

        return fragment_tab;
    }



    @Override
    public void onStart() {
        super.onStart();

        /**
         * REGRA PARA BOTOES OU MENSAGEM - INICIAR/FINALIZAR VIAGEM
         */
        MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
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