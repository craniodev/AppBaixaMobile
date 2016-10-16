package br.com.a3rtecnologia.baixamobile.menu;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.EncerrarViagemDialog;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemDialog;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendasAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaVolley;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.ocorrencia.AtualizaEncomendaPendenteTimerTask;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.OcorrenciaReceiver;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_lista.FinalizarViagemVolley;
import br.com.a3rtecnologia.baixamobile.tab_lista.StatusEncomendaDialog;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumentoVolley;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrenciaVolley;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedorVolley;
import br.com.a3rtecnologia.baixamobile.util.ActivityUtil;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

public class MenuDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private SessionManager sessionManager;

    private Toolbar toolbar;
    private RelativeLayout layout;

    private static TextView viagemTextView;

    private StatusBusiness statusBusiness;
    private EncomendaBusiness encomendaBusiness;

    private static Context mContext;
    private static Activity mActivity;
    private static ProgressDialog mProgressDialog;

    private Menu menu;
    private AtualizaEncomendaPendenteTimerTask atualizaEncomendaPendenteTimerTask;

    private static int REQUEST_CODE_ASK_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActivityUtil.toolbar(toolbar, "Painel", this);
        ActivityUtil.actionBarColor(this);
        mContext = getApplicationContext();
        mActivity = this;

        sessionManager = new SessionManager(mContext);
        statusBusiness = new StatusBusiness(mContext);
        encomendaBusiness = new EncomendaBusiness(mContext);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        permissaoWRITE_EXTERNAL_STORAGE();

        createLoading(this);

        donwloadTablesTipo();


        verificador();
    }



    private void verificador(){

        /**
         * ATIVA VERIFICADOR DE ENCOMENDAS TRATADAS
         */
//        AtualizaEncomendaPendenteTimerTask atualizaEncomendaPendenteTimerTask = new AtualizaEncomendaPendenteTimerTask(mContext);
//        atualizaEncomendaPendenteTimerTask.startTimer();

//        SincronizaEncomendaPendenteTimerTask sincronizaEncomendaPendenteTimerTask = new SincronizaEncomendaPendenteTimerTask(mContext);

        Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
        ocorrenciaIntent.putExtra("OPERACAO", "START");
        getApplicationContext().sendBroadcast(ocorrenciaIntent);
    }




    private void permissaoWRITE_EXTERNAL_STORAGE(){

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                init();

                return;
            }
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

            init();

            return;
        }

        init();
    }



    private void init(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        /**
         * NOME E EMAIL NO MENU
         */
        TextView userName = (TextView) header.findViewById(R.id.nave_header_user_name);
        userName.setText(sessionManager.getValue("nome"));

        TextView userEmail = (TextView) header.findViewById(R.id.nave_header_user_email);
        userEmail.setText(sessionManager.getValue("email"));

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new PainelFragment()).commit();
    }



    /**
     * WAZE
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int DESLIGAR_WAZE = 0;

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }else if(resultCode == DESLIGAR_WAZE){

                StatusEncomendaDialog statusEncomendaDialog = new StatusEncomendaDialog(this);
            }
        }
    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);

        MenuItem menuItem = menu.findItem(R.id.item_generic);
        MenuItemCompat.setActionView(menuItem, R.layout.menu_action_bar_layout);
        layout = (RelativeLayout) MenuItemCompat.getActionView(menuItem);

        iniciarViagemActionBar(menuItem);

        return super.onCreateOptionsMenu(menu);
    }



    /**
     * TEXT VIEW ACTION BAR
     *
     * @param menuItem
     */
    private void iniciarViagemActionBar(final MenuItem menuItem){

        viagemTextView = (TextView) layout.findViewById(R.id.menu_action_bar_label);
        viagemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viagemTextView.getText().toString().equalsIgnoreCase("FINALIZAR VIAGEM")) {

                    finalizarViagem();

                } else if (viagemTextView.getText().toString().equalsIgnoreCase("INICIAR VIAGEM")) {

                    IniciarViagemDialog dialog = new IniciarViagemDialog(mActivity, viagemTextView);
                }
            }
        });

        //UPDATE TEXT BOTAO
        exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
    }



    public static void exibirBotaoIniciarFinalizarViagem(StatusBusiness statusBusiness, EncomendaBusiness encomendaBusiness){

        boolean viagemIniciada = statusBusiness.verificarViagemIniciada();
        Status status = statusBusiness.getStatus();

        /**
         * VIAGEM JA INICIADA
         */
        if(viagemIniciada){

            if(viagemTextView != null) {
                //BOTAO INICIAR VIAGEM
                viagemTextView.setText("Finalizar Viagem");
            }

        }else if(status != null){

            /**
             * NAO EXISTE ENCOMENDAS
             */
            int total = encomendaBusiness.countEncomendasEmRotaAndLiberado();
            if (total == 0) {

                if(viagemTextView != null) {

                    //MENSAGEM DE QUE NAO TEM ENCOMENDAS NO MOMENTO
                    viagemTextView.setText("Sem Encomendas");

                    /**
                     * ATVAR BUSCA ENCOMENDAS
                     */
//                    showProgress(true);
//                    encomendaTimerTask.startTimer();
                }
                /**
                 * EXISTE ENCOMENDAS
                 */
            }else{

                if(viagemTextView != null) {

                    //BOTAO INICIAR VIAGEM
                    viagemTextView.setText("Iniciar Viagem");

//                    showProgress(false);
//                    encomendaTimerTask.stoptimertask();
                }
            }

        }else{

            /**
             * NAO EXISTE ENCOMENDAS
             */
            int total = encomendaBusiness.countEncomendasEmRotaAndLiberado();
            if (total == 0) {

                if(viagemTextView != null) {

                    //MENSAGEM DE QUE NAO TEM ENCOMENDAS NO MOMENTO
                    viagemTextView.setText("Sem Encomendas");

                    /**
                     * ATVAR BUSCA ENCOMENDAS
                     */
//                        showProgress(true);
//                        encomendaTimerTask.startTimer();
                }
                /**
                 * EXISTE ENCOMENDAS
                 */
            }else{

                if(viagemTextView != null) {

                    //BOTAO INICIAR VIAGEM
                    viagemTextView.setText("Iniciar Viagem");

//                        showProgress(false);
//                        encomendaTimerTask.stoptimertask();

                }
            }
        }
    }




    private void finalizarViagem(){

//        createLoading(mContext, "Finalizando Viagem");
//        showProgress(true);

        /**
         * AGUARDAR RESPOSTA
         *
         * PODE RECEBER LISTA DE ENCOMENDAS PENDENTES PARA ENCERRAR AINDA
         *
         */
        FinalizarViagemVolley finalizarViagemVolley = new FinalizarViagemVolley(mActivity, new DelegateEncomendaAsyncResponse() {
            @Override
            public void processFinish(boolean finish, String resposta) {

                showProgress(false);

                /**
                 * SE NAO TEM ENCOMENDAS PENDENTES
                 * SUCESSO
                 */
                if(resposta.equalsIgnoreCase("SUCESSO")) {

//                                    painelIniciarViagem.setVisibility(View.VISIBLE);
//                                    painelFinalizarViagem.setVisibility(View.GONE);

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
                    EncerrarViagemDialog dialog = new EncerrarViagemDialog(mActivity, "Finalizar Viagem", resposta);
                }
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.println("ERRO FINALIZAR VIAGEM");
                showProgress(false);
            }
        });
    }






    private void donwloadTablesTipo(){

        TipoOcorrenciaVolley tipoOcorrenciaVolley = new TipoOcorrenciaVolley(mContext);
        TipoDocumentoVolley tipoDocumentoVolley = new TipoDocumentoVolley(mContext);
        TipoRecebedorVolley tipoRecebedorVolley = new TipoRecebedorVolley(mContext);
    }

//    private void downloadEncomendas(){
//
//        String primeiroLogin = sessionManager.isPrimeiroLogin();
//        if(primeiroLogin.equalsIgnoreCase("1")) {
//
////            sessionManager.setPrimeiroLogin("");
//
//            /**
//             * LIMPAR BASE PARA GARANTIR
//             */
//            encomendaBusiness.deleteAll();
//
//            buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_EM_ROTA.getValue());
//            buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_ENTREGUE.getValue());
//            buscarAPIOnline(EnumAPI.ID_TIPO_ENCOMENDA_PENDENTE.getValue());
//
//
//        }
//    }


    private void buscarAPIOnline(String statusEncomenda){

        new EncomendaVolley(mContext, statusEncomenda, 0, new DelegateEncomendasAsyncResponse() {

            @Override
            public void processFinish(boolean finish, Encomendas encomendas) {

                System.out.println(finish);
//                updateAdapter(encomendas.getEncomendas());

//                PainelFragment.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);
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





    /**
     * BARRA DE BUSCA
     *
     * @param item
     * @return
     */
//    private void search(Menu menu){
//
//        MenuItem searchItem = menu.findItem(R.id.search_icon);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                List<Cart> listaFilter = new ArrayList<Cart>();
//
//                for (Cart item : cartList){
//
//                    String value = item.getTitle().toUpperCase();
//                    String queryValue = query.toUpperCase();
//
//                    if(value.contains(queryValue)){
//
//                        listaFilter.add(item);
//                    }
//                }
//
//                updateAdapter(listaFilter);
//
//                searchView.clearFocus();
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_panel) {

            setTitle("Painel");

            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.containerView,new PainelFragment()).commit();

        } else if (id == R.id.nav_pending) {

            setTitle("Pendentes");

            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.containerView,new EntregaPendenteFragment()).commit();


        } else if (id == R.id.nav_finished) {

            setTitle("Realizadas");

            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.containerView,new EntregaRealizadaFragment()).commit();

        } else if (id == R.id.nav_account) {

            setTitle("Minha Conta");

            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.containerView,new ContaFragment()).commit();

        } else if (id == R.id.nav_exit) {

            SairDialog sairDialog = new SairDialog(this, atualizaEncomendaPendenteTimerTask);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void setTitle(String title){

        if(toolbar != null){

            toolbar.setTitle(title);
        }
    }

    private static void createLoading(Context mContext){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Carregando encomendas");
    }

    public static void showProgress(boolean isShow){

        if(mProgressDialog != null && isShow && !mProgressDialog.isShowing()){

            mProgressDialog.show();

        }else if(mProgressDialog != null && !isShow && mProgressDialog.isShowing()){

            mProgressDialog.hide();
        }
    }
}
