package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.DelegateEntregaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.menu.MenuDrawerActivity;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.OcorrenciaReceiver;
import br.com.a3rtecnologia.baixamobile.ocorrencia_tratada_sincronizacao.AtualizaEncomendaPendenteTimerTask;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrenciaAdapter;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrenciaBusiness;
import br.com.a3rtecnologia.baixamobile.util.ActivityUtil;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;
import br.com.a3rtecnologia.baixamobile.util.ImagemUtil;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

public class OcorrenciaActivity extends AppCompatActivity {

    private Uri photoURI;
    private int REQUEST_TAKE_PHOTO_OCORRENCIA = 1;
    private String diretorioCompleto;

    private ImageView ocorrencia_foto;

    private Activity mActivity;
    private Ocorrencia ocorrencia;

    private EncomendaBusiness encomendaBusiness;
    private TipoOcorrenciaBusiness tipoOcorrenciaBusiness;
    private StatusBusiness statusBusiness;
    private SessionManager sessionManager;
    private OcorrenciaBusiness ocorrenciaBusiness;

    private ProgressDialog progress;

    private Context mContext;

    private Spinner ocorrencia_tipo_ocorrencia;
    private TipoOcorrenciaAdapter adapter_tipo_ocorrencia;
    private String finalizarViagemOcorrencia;
    private Map<String, String> camposPendentes;

    private LinearLayout grupo_foto_ocorrencia;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencia);

        grupo_foto_ocorrencia = (LinearLayout) findViewById(R.id.grupo_foto_ocorrencia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActivityUtil.toolbar(toolbar, "Ocorrênca", this);

        mActivity = this;
        mContext = this;
        ocorrencia = new Ocorrencia();
        sessionManager = new SessionManager(mContext);
        camposPendentes = new HashMap<String, String>();

        finalizarViagemOcorrencia = sessionManager.getFinalizarViagemOcorrenciaForcado();
        if(finalizarViagemOcorrencia.equalsIgnoreCase("1")){
            grupo_foto_ocorrencia.setVisibility(View.GONE);
        }

        encomendaBusiness = new EncomendaBusiness(mActivity);
        tipoOcorrenciaBusiness = new TipoOcorrenciaBusiness(mContext);
        sessionManager = new SessionManager(mContext);
        statusBusiness = new StatusBusiness(mContext);
        ocorrenciaBusiness = new OcorrenciaBusiness(mContext);

        createLoading(mActivity);

        createComboOcorrencias();

        botaoFlutuante();

        fotoOcorrencia();
    }



//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState) {
//
//        getDadosFormulario();
//
//        savedInstanceState.putInt("STATE_ID_TIPO_OCORRENCIA", ocorrencia.getTipoOcorrencia().getId());
//
//        savedInstanceState.putString("STATE_FOTO_OCORRENCIA_PATH", ocorrencia.getFotoOcorrenciaPath());
//        savedInstanceState.putString("STATE_FOTO_OCORRENCIA_BASE64", ocorrencia.getFotoOcorrenciaBase64());
//
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//
//
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        mActivity = this;
//        mContext = this;
//        ocorrencia = new Ocorrencia();
//        sessionManager = new SessionManager(mContext);
//        camposPendentes = new HashMap<String, String>();
//
//        finalizarViagemOcorrencia = sessionManager.getFinalizarViagemOcorrenciaForcado();
//
//        encomendaBusiness = new EncomendaBusiness(mActivity);
//        tipoOcorrenciaBusiness = new TipoOcorrenciaBusiness(mContext);
//        sessionManager = new SessionManager(mContext);
//
//        createLoading(mActivity);
//
//        createComboOcorrencias();
//
//        botaoFlutuante();
//
//        fotoOcorrencia();
//
//
//        /**
//         * TIPO RECEBEDOR
//         */
//        int idTipoOcorrencia = savedInstanceState.getInt("STATE_ID_TIPO_OCORRENCIA");
//        TipoOcorrencia tipoOcorrencia = tipoOcorrenciaBusiness.buscarPorId(idTipoOcorrencia);
//
//        ocorrencia.setTipoOcorrencia(tipoOcorrencia);
//        int idPositonTipoRecebedor = adapter_tipo_ocorrencia.getItemIndexById(idTipoOcorrencia);
//        ocorrencia_tipo_ocorrencia.setSelection(idPositonTipoRecebedor);
//
//
//        /**
//         * FOTO OCORRENCIA
//         */
//        String fotoOcorrenciaPath = savedInstanceState.getString("STATE_FOTO_OCORRENCIA_PATH");
//        String fotoOcorrenciaBase64 = savedInstanceState.getString("STATE_FOTO_OCORRENCIA_BASE64");
//        if(fotoOcorrenciaPath != null){
//
//            Uri uri = ImagemUtil.getUri(this, new File(fotoOcorrenciaPath));
//            Bitmap bm = getBitmap(uri);
//
//            ocorrencia_foto = (ImageView) findViewById(R.id.ocorrencia_foto);
//
//            ocorrencia_foto.setVisibility(View.VISIBLE);
//            ocorrencia_foto.setImageBitmap(bm);
//
//            ocorrencia.setFotoOcorrenciaBase64(fotoOcorrenciaBase64);
//            ocorrencia.setFotoOcorrenciaPath(fotoOcorrenciaPath);
//
//            visualizarFotoOcorrencia();
//        }
//
//    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO_OCORRENCIA && resultCode == RESULT_OK) {

            Bitmap bm = getBitmap(photoURI);

            ocorrencia_foto.setImageBitmap(bm);

            String fotoOcorrenciaBase64 = ImagemUtil.getEncoded64ImageStringFromBitmap(mContext, bm);
            ocorrencia.setFotoOcorrenciaBase64(fotoOcorrenciaBase64);
            ocorrencia.setFotoOcorrenciaPath(diretorioCompleto);
        }
    }



    private Bitmap getBitmap(Uri uri){

        String name = uri.getPath().replace("/my_images/", "");
        diretorioCompleto = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/br.com.a3rtecnologia.baixamobile/files/Pictures/" + name;
        Bitmap bm = ImagemUtil.getbitpam(diretorioCompleto);

        return bm;
    }



    /**
     * CREATE TIPO RECEBEDOR
     */
    private void createComboOcorrencias(){

        List<TipoOcorrencia> tipoOcorrenciaList = tipoOcorrenciaBusiness.buscarTodos();

        ocorrencia_tipo_ocorrencia = (Spinner) findViewById(R.id.ocorrencia_tipo_ocorrencia);

        adapter_tipo_ocorrencia = new TipoOcorrenciaAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, tipoOcorrenciaList);

        adapter_tipo_ocorrencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ocorrencia_tipo_ocorrencia.setAdapter(adapter_tipo_ocorrencia);
    }



    private void botaoFlutuante(){

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean result = validateForm();

                if(result){

                    enviarDadosServidor();
                }
            }
        });
    }



    private boolean validateForm() {

        camposPendentes.clear();

        TipoOcorrencia tipoOcorrencia = (TipoOcorrencia) ocorrencia_tipo_ocorrencia.getSelectedItem();
        ocorrencia.setTipoOcorrencia(tipoOcorrencia);

        StringBuilder sb = new StringBuilder();
        if(tipoOcorrencia != null) {

            if (tipoOcorrencia.getId() == 0) {

                camposPendentes.put("TIPO_OCORRENCIA", "Combo tipo ocorrência");
                sb.append(camposPendentes.get("TIPO_OCORRENCIA")+"\n");
            }


            String isFinalizarForcado = sessionManager.getFinalizarViagemOcorrenciaForcado();

            /**
             * QUANDO NAO FOR FORCADO, VALIDAR FOTO
             */
            if(!isFinalizarForcado.equalsIgnoreCase("1")) {

                if(tipoOcorrencia.getFgExigeFoto() == 1){

                    /**
                     * VALIDACAO ARQUIVOS
                     */
                    if (ocorrencia.getFotoOcorrenciaPath() == null) {

                        camposPendentes.put("FOTO_OCORRENCIA", "Foto da ocorrência");
                        sb.append(camposPendentes.get("FOTO_OCORRENCIA") + "\n");
                    }
                }
            }

        }

        if(camposPendentes.size() > 0){

            StatusDialog dialog = new StatusDialog(this, "Formulário Incompleto", "Preencha os campos obrigatórios\n\n"+sb.toString(), false);

            return false;
        }

        return true;
    }


    /**
     * USAR PARA GRAVAR ESTADO
     * @return
     */
//    private void getDadosFormulario(){
//
//        /**
//         * REVER - PEGAR COMBO SELECIONADO
//         */
//        /**
//         * GET TIPO OCORRENCIA
//         */
//        ocorrencia_tipo_ocorrencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//
//                TipoOcorrencia tipoOcorrencia = adapter_tipo_ocorrencia.getItem(position);
//
//                ocorrencia.setTipoOcorrencia(tipoOcorrencia);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//
//    }



    private void enviarDadosServidor(){

        /** 1 - progress **/
        showProgress(true);

        /** 4 - verifica se é um finalizar viagem forcado **/
        if(finalizarViagemOcorrencia.equalsIgnoreCase("1")){

            sessionManager.setFinalizarViagemOcorrenciaForcado("");

            /**
             * FORCAR FINALIZAR TODAS ENCOMENDAS PENDENTES, COMO OCORRENCIA
             */
            finalizarViagemOcorrenciaForcado(ocorrencia);

        }else {

            /** 2 - recupera id encomenda corrente **/
            long id = statusBusiness.getIdEncomendaCorrente();

            /** 2.1 - recupera objeto encomenda corrente **/
            final Encomenda encomendaCorrente = encomendaBusiness.buscarEncomendaCorrente(id);

            /** 3 - recupera localizacao atual **/
            MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mContext, TabItemMapaFragment.map);
            timerTaskLocation.startTimer();

            /** 3.1 - monta objeto latlng **/
            LatLng latLng = timerTaskLocation.getMyLatLng();
            timerTaskLocation.stoptimertask();

            if(latLng != null){

                encomendaCorrente.setLatitude(latLng.latitude);
                encomendaCorrente.setLongitude(latLng.longitude);
            }

            /** 5 - data atual da baixa **/
            encomendaCorrente.setDataBaixa(DateUtil.getDataAtual());
            encomendaBusiness.update(encomendaCorrente);


            /** 6 - remove COR do circulo se for uma encomenda TRATADA **/
            encomendaCorrente.setFlagTratado(false);

            /** 7 - atualiza STATUS da encomenda para OCORRENCIA **/
            encomendaBusiness.atualizarStatusEncomendaOcorrencia(encomendaCorrente);

            /** 8 - DESMARCA encomenda como CORRENTE **/
            statusBusiness.removeEncomendaCorrente();
//                    TabItemMapaFragment.isRemoveMarkerMap = true;

            /** 9 - atualiza botao iniciar/finalizar viagem **/
            MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

            Toast.makeText(mContext, "API - START - ENCOMENDA PENDENTE - SUCESSO", Toast.LENGTH_LONG).show();

            /** 10 - ativa VERIFICADOR de encomendas TRATADAS **/
            AtualizaEncomendaPendenteTimerTask atualizaEncomendaPendenteTimerTask = new AtualizaEncomendaPendenteTimerTask(mContext);
            atualizaEncomendaPendenteTimerTask.startTimer();

            /** 11 - adiciona recebedor - SOMENTE TRATADAS **/


            /** 12 - marca como NAO SINCRONIZADO **/
            encomendaCorrente.setFlagEnviado(EnumStatusEnvio.NAO_SINCRONIZADO.getKey());

            ocorrenciaBusiness.salvarOcorrencia(ocorrencia);
            encomendaCorrente.setOcorrencia(ocorrencia);

            /** 13 - atualiza encomenda - FINAL **/
            encomendaBusiness.update(encomendaCorrente);

            /**
             * ATIVAR SINCRONISMO
             */
            Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
            ocorrenciaIntent.putExtra("OPERACAO", "START");
            getApplicationContext().sendBroadcast(ocorrenciaIntent);





//            ControleTimerTaskBusiness controleTimerTaskBusiness = new ControleTimerTaskBusiness(mContext);
//            Integer total = controleTimerTaskBusiness.verificarFinalizarEntrega();
//
//            if(total == null || total == 0){
//
//                ControleTimerTask controleTimerTask = new ControleTimerTask();
//                controleTimerTask.setIdOperacao(EnumOperacao.OP_FINALIZAR_ENTREGA.getValue());
//                controleTimerTaskBusiness.salvar(controleTimerTask);
//
//                SincronizaEncomendaPendenteTimerTask sincronizaEncomendaPendenteTimerTask = new SincronizaEncomendaPendenteTimerTask(mContext);
//            }

            finish();
        }
    }



    /**
     * FORCAR FINALIZAR TODAS ENCOMENDAS PENDENTES, COMO OCORRENCIA
     */
    private void finalizarViagemOcorrenciaForcado(Ocorrencia ocorrencia){

        FinalizarViagemOcorrenciaVolley finalizarViagemOcorrenciaVolley = new FinalizarViagemOcorrenciaVolley(mActivity, ocorrencia, new DelegateEntregaAsyncResponse() {
            @Override
            public void processFinish(boolean finish, String resposta) {

                System.out.print(resposta);
                showProgress(false);

                statusBusiness.stopJornadaTrabalho();

                MenuDrawerActivity.exibirBotaoIniciarFinalizarViagem(statusBusiness, encomendaBusiness);

                if(AtualizaEncomendaPendenteTimerTask.timer != null){

                    AtualizaEncomendaPendenteTimerTask.timer.cancel();
                }

                finish();
            }

            @Override
            public void processCanceled(boolean cancel) {

                System.out.print("ERRO FINALIZA VIAGEM OCORRENCIA - FORCADO");

                showProgress(false);

                finish();
            }
        });
    }



    private void fotoOcorrencia(){

        ImageView ocorrencia_foto_icon = (ImageView) findViewById(R.id.ocorrencia_foto_icon);
        ocorrencia_foto_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ocorrencia_foto = (ImageView) findViewById(R.id.ocorrencia_foto);
                ocorrencia_foto.setVisibility(View.VISIBLE);

                photoURI = ImagemUtil.dispatchTakePictureIntent(mActivity, "ocorrencia", REQUEST_TAKE_PHOTO_OCORRENCIA);

                visualizarFotoOcorrencia();
            }
        });
    }



    private void visualizarFotoOcorrencia(){

        ocorrencia_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagemUtil.showPhoto(mActivity, Uri.parse("file:" + ocorrencia.getFotoOcorrenciaPath()));
            }
        });
    }



    private void createLoading(Context mContext){

        progress = new ProgressDialog(mContext);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Enviando");
    }

    private void showProgress(boolean isShow){

        if(progress != null && isShow && !progress.isShowing()){

            progress.show();

        }else if(progress != null && !isShow && progress.isShowing()){

            progress.hide();
        }
    }

}
