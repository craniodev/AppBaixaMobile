package br.com.a3rtecnologia.baixamobile.entrega;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.MaskBaixa;
import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.assinatura.AssinaturaDigital;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tab_mapa.MyLocationTimerTask;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumentoAdapter;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumentoBusiness;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrenciaBusiness;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedorBusiness;
import br.com.a3rtecnologia.baixamobile.util.ActivityUtil;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedorAdapter;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;
import br.com.a3rtecnologia.baixamobile.util.ImagemUtil;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;

public class EntregaAcitivty extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO_COMPROVANTE = 1;
    static final int REQUEST_ASSINATURA_DIGITAL = 2;
    static final int REQUEST_RETORNO_ASSINATURA_DIGITAL = 3;



    private ImageView entrega_comprovante_foto;
    private ImageView entrega_assinatura_digital_foto;

    private Uri photoURI;
    private Uri assinaturaURI;
    private String diretorioCompleto;

    private Recebedor recebedor;

    private RecebedorBusiness recebedorBusiness;
    private EncomendaBusiness encomendaBusiness;
    private TipoDocumentoBusiness tipoDocumentoBusiness;
    private TipoOcorrenciaBusiness tipoOcorrenciaBusiness;
    private TipoRecebedorBusiness tipoRecebedorBusiness;
    private StatusBusiness statusBusiness;

    private SessionManager sessionManager;

    private Activity mActivity;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    private AutoCompleteTextView entrega_nome_recebedor;
//    private MaskedEditText entrega_nr_documento;
//    private MaskedEditText entrega_cnh;
//    private MaskedEditText entrega_cpf;
//    private MaskedEditText entrega_rg;
    private EditText entrega_nr_documento;
    private EditText entrega_cnh;
    private EditText entrega_cpf;
    private EditText entrega_rg;
    private Spinner entrega_tipo_recebedor;
    private Spinner entrega_tipo_documento;

    private TipoRecebedorAdapter adapter_tipo_recebedor;
    private TipoDocumentoAdapter adapter_tipo_documento;

    private Map<String, View> controleTipoDocumento;
    private Map<String, String> camposPendentes;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);

        mActivity = this;
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActivityUtil.toolbar(toolbar, "Finalizar Entrega", this);
        ActivityUtil.actionBarColor(this);

        botaoFlutuanteEnviar();

        recebedor = new Recebedor();

        recebedorBusiness = new RecebedorBusiness(mContext);
        encomendaBusiness = new EncomendaBusiness(mContext);
        tipoDocumentoBusiness = new TipoDocumentoBusiness(mContext);
        tipoOcorrenciaBusiness = new TipoOcorrenciaBusiness(mContext);
        tipoRecebedorBusiness = new TipoRecebedorBusiness(mContext);
        statusBusiness = new StatusBusiness(mContext);

        sessionManager = new SessionManager(mContext);

        controleTipoDocumento = new HashMap<String, View>();
        camposPendentes = new HashMap<String, String>();

        assinaturaDigital();
        fotoComprovante();

        createComboRecebedor();
        createComboDocumento();

        createNomeRecebedor();
        createNumeroDocumentoRecebedor();






        getDadosFormulario();
    }




    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        getDadosFormulario();

        savedInstanceState.putInt("STATE_ID_TIPO_RECEBEDOR", recebedor.getTipoRecebedor().getId());
        savedInstanceState.putString("STATE_NOME_RECEBEDOR", recebedor.getNome());

        savedInstanceState.putInt("STATE_ID_TIPO_DOCUMENTO", recebedor.getTipoDocumento().getId());
        savedInstanceState.putString("STATE_NUM_DOCUMENTO", recebedor.getNrDocumento());

        savedInstanceState.putString("STATE_FOTO_ASSINATURA_PATH", recebedor.getFotoAssinaturaDigitalPath());
        savedInstanceState.putString("STATE_FOTO_ASSINATURA_BASE64", recebedor.getFotoAssinaturaDigitalBase64());

        savedInstanceState.putString("STATE_FOTO_COMPROVANTE_PATH", recebedor.getFotoComprovantePath());
        savedInstanceState.putString("STATE_FOTO_COMPROVANTE_BASE64", recebedor.getFotoComprovanteBase64());

        super.onSaveInstanceState(savedInstanceState);
    }




    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        botaoFlutuanteEnviar();

        recebedor = new Recebedor();

        recebedorBusiness = new RecebedorBusiness(mContext);
        encomendaBusiness = new EncomendaBusiness(mContext);
        tipoDocumentoBusiness = new TipoDocumentoBusiness(mContext);
        tipoOcorrenciaBusiness = new TipoOcorrenciaBusiness(mContext);
        tipoRecebedorBusiness = new TipoRecebedorBusiness(mContext);

        sessionManager = new SessionManager(mContext);

        controleTipoDocumento = new HashMap<String, View>();

        assinaturaDigital();
        fotoComprovante();

        createNomeRecebedor();
        createComboRecebedor();
        createNumeroDocumentoRecebedor();
        createComboDocumento();

        getDadosFormulario();


        /**
         * TIPO RECEBEDOR
         */
        int idTipoRecebedor = savedInstanceState.getInt("STATE_ID_TIPO_RECEBEDOR");
        TipoRecebedor tipoRecebedor = tipoRecebedorBusiness.buscarPorId(idTipoRecebedor);

        recebedor.setTipoRecebedor(tipoRecebedor);
        int idPositonTipoRecebedor = adapter_tipo_recebedor.getItemIndexById(idTipoRecebedor);
        entrega_tipo_recebedor.setSelection(idPositonTipoRecebedor);

        /**
         * NOME
         */
        String nome = savedInstanceState.getString("STATE_NOME_RECEBEDOR");
        recebedor.setNome(nome);

        /**
         * TIPO DOCUMENTO
         */
        int idTipoDocumento = savedInstanceState.getInt("STATE_ID_TIPO_DOCUMENTO");
        TipoDocumento tipoDocumento = tipoDocumentoBusiness.buscarPorId(idTipoDocumento);

        recebedor.setTipoDocumento(tipoDocumento);
        int idPositonTipoDocumento = adapter_tipo_documento.getItemIndexById(idTipoDocumento);
        entrega_tipo_documento.setSelection(idPositonTipoDocumento);

        /**
         * NUMERO
         */
        String numDocumento = savedInstanceState.getString("STATE_NUM_DOCUMENTO");
        recebedor.setNrDocumento(numDocumento);

        /**
         * ASSINATURA
         */
        String fotoAssinaturaPath = savedInstanceState.getString("STATE_FOTO_ASSINATURA_PATH");
        String fotoAssinaturaBase64 = savedInstanceState.getString("STATE_FOTO_ASSINATURA_BASE64");
        if(fotoAssinaturaPath != null){

            Uri uri = ImagemUtil.getUri(this, new File(fotoAssinaturaPath));
            Bitmap bm = getBitmap(uri);

            entrega_assinatura_digital_foto = (ImageView) findViewById(R.id.entrega_assinatura_digital_foto);

            entrega_assinatura_digital_foto.setVisibility(View.VISIBLE);
            entrega_assinatura_digital_foto.setImageBitmap(bm);

            recebedor.setFotoAssinaturaDigitalBase64(fotoAssinaturaBase64);
            recebedor.setFotoAssinaturaDigitalPath(fotoAssinaturaPath);

            visualizarAssinaturaDigital();
        }

        /**
         * COMPROVANTE
         */
        String fotoComprovantePath = savedInstanceState.getString("STATE_FOTO_COMPROVANTE_PATH");
        String fotoComprovanteBase64 = savedInstanceState.getString("STATE_FOTO_COMPROVANTE_BASE64");
        if(fotoComprovantePath != null){

            Uri uri = ImagemUtil.getUri(this, new File(fotoComprovantePath));
            Bitmap bm = getBitmap(uri);

            entrega_comprovante_foto = (ImageView) findViewById(R.id.entrega_comprovante_foto);

            entrega_comprovante_foto.setVisibility(View.VISIBLE);
            entrega_comprovante_foto.setImageBitmap(bm);

            recebedor.setFotoComprovanteBase64(fotoComprovanteBase64);
            recebedor.setFotoComprovantePath(fotoComprovantePath);

            visualizarFotoComprovante();
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }








    private void botaoFlutuanteEnviar(){

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




    private void getDadosFormulario(){

        /**
         * GET NOME RECEBEDOR
         */
        String nomeRecebedor = String.valueOf(entrega_nome_recebedor.getText());
        recebedor.setNome(nomeRecebedor);



        /**
         * GET TIPO RECEBEDOR
         */
        entrega_tipo_recebedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                TipoRecebedor tipoRecebedorRecuperado = adapter_tipo_recebedor.getItem(position);

                recebedor.setTipoRecebedor(tipoRecebedorRecuperado);


                if(tipoRecebedorRecuperado.getId() == 4) {
                    Long idEncomenda = statusBusiness.getIdEncomendaCorrente();
                    if (idEncomenda != null) {

                        Encomenda encomenda = encomendaBusiness.buscarEncomendaCorrente(idEncomenda);

                        entrega_nome_recebedor = (AutoCompleteTextView) findViewById(R.id.entrega_nome_recebedor);
                        entrega_nome_recebedor.setText(encomenda.getNmDestinatario());
                        entrega_nome_recebedor.setEnabled(false);
                    }
                }else{

                    entrega_nome_recebedor = (AutoCompleteTextView) findViewById(R.id.entrega_nome_recebedor);
                    entrega_nome_recebedor.setText("");
                    entrega_nome_recebedor.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        entrega_tipo_recebedor.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<TipoRecebedor>() {
//
//            @Override public void onItemSelected(MaterialSpinner view, int position, long id, TipoRecebedor item) {
////                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
//
//                TipoRecebedor tipoRecebedorRecuperado = adapter_tipo_recebedor.getItem(position);
//
//                recebedor.setTipoRecebedor(tipoRecebedorRecuperado);
//            }
//        });



        /**
         * GET TIPO DOCUMENTO
         */
        entrega_tipo_documento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                TipoDocumento tipoDocumentoRecuperado = adapter_tipo_documento.getItem(position);

                recebedor.setTipoDocumento(tipoDocumentoRecuperado);

                regraCampoDocumento(tipoDocumentoRecuperado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        /**
         * GET NUMERO DOCUMENTO
         */
//        MaskedEditText tipoDocumento = (MaskedEditText) controleTipoDocumento.get("TIPO_DOCUMENTO");
//        if(tipoDocumento != null){
//
//            String numeroDocumento = String.valueOf(tipoDocumento.getText());
//            recebedor.setNrDocumento(numeroDocumento);
//        }
        EditText tipoDocumento = (EditText) controleTipoDocumento.get("TIPO_DOCUMENTO");
        if(tipoDocumento != null){

            String numeroDocumento = String.valueOf(tipoDocumento.getText());
            recebedor.setNrDocumento(numeroDocumento);
        }

    }



    private void regraCampoDocumento(TipoDocumento tipoDocumentoRecuperado){

        //CNH
        if(tipoDocumentoRecuperado.getId() == 3){

            entrega_cnh.setVisibility(View.VISIBLE);
            entrega_cpf.setVisibility(View.GONE);
            entrega_nr_documento.setVisibility(View.GONE);
            entrega_rg.setVisibility(View.GONE);

            controleTipoDocumento.put("TIPO_DOCUMENTO", entrega_cnh);

        //CPF
        }else if(tipoDocumentoRecuperado.getId() == 1){

            entrega_cnh.setVisibility(View.GONE);
            entrega_cpf.setVisibility(View.VISIBLE);
            entrega_nr_documento.setVisibility(View.GONE);
            entrega_rg.setVisibility(View.GONE);

            controleTipoDocumento.put("TIPO_DOCUMENTO", entrega_cpf);

        //OUTROS
        }else if(tipoDocumentoRecuperado.getId() == 4){

            entrega_cnh.setVisibility(View.GONE);
            entrega_cpf.setVisibility(View.GONE);
            entrega_nr_documento.setVisibility(View.VISIBLE);
            entrega_rg.setVisibility(View.GONE);

            controleTipoDocumento.put("TIPO_DOCUMENTO", entrega_nr_documento);

        //RG
        }else if(tipoDocumentoRecuperado.getId() == 2){

            entrega_cnh.setVisibility(View.GONE);
            entrega_cpf.setVisibility(View.GONE);
            entrega_nr_documento.setVisibility(View.GONE);
            entrega_rg.setVisibility(View.VISIBLE);

            controleTipoDocumento.put("TIPO_DOCUMENTO", entrega_rg);
        }
    }



    /**
     * CREATE NOME RECEBEDOR
     */
    private String createNomeRecebedor(){

        entrega_nome_recebedor = (AutoCompleteTextView) findViewById(R.id.entrega_nome_recebedor);
        String nomeRecebedor = String.valueOf(entrega_nome_recebedor.getText());

        return nomeRecebedor;
    }



    /**
     * CREATE TIPO RECEBEDOR
     */
    private void createComboRecebedor(){

        List<TipoRecebedor> recebedorList = tipoRecebedorBusiness.buscarTodos();

        entrega_tipo_recebedor = (Spinner) findViewById(R.id.entrega_tipo_recebedor);

        adapter_tipo_recebedor = new TipoRecebedorAdapter(mContext, android.R.layout.simple_spinner_item, recebedorList);
        adapter_tipo_recebedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entrega_tipo_recebedor.setAdapter(adapter_tipo_recebedor);

//        entrega_tipo_recebedor.setFocusable(true);
        entrega_tipo_recebedor.setFocusableInTouchMode(true);
    }



    /**
     * CREATE NUMERO DOCUMENTO
     */
    private void createNumeroDocumentoRecebedor(){

//        entrega_nr_documento = (MaskedEditText) findViewById(R.id.entrega_nr_documento);
//        entrega_cnh = (MaskedEditText) findViewById(R.id.entrega_cnh);
//        entrega_cpf = (MaskedEditText) findViewById(R.id.entrega_cpf);
//        entrega_rg = (MaskedEditText) findViewById(R.id.entrega_rg);
        entrega_nr_documento = (EditText) findViewById(R.id.entrega_nr_documento);
        entrega_cnh = (EditText) findViewById(R.id.entrega_cnh);
        entrega_cpf = (EditText) findViewById(R.id.entrega_cpf);
        entrega_rg = (EditText) findViewById(R.id.entrega_rg);



        MaskBaixa maskOUTRO = new MaskBaixa("####################", entrega_nr_documento);
        entrega_nr_documento.addTextChangedListener(maskOUTRO);

        MaskBaixa maskCNH = new MaskBaixa("###########", entrega_cnh);
        entrega_cnh.addTextChangedListener(maskCNH);

        MaskBaixa maskCPF = new MaskBaixa("###.###.###-##", entrega_cpf);
        entrega_cpf.addTextChangedListener(maskCPF);

        MaskBaixa maskRG = new MaskBaixa("##.###.###-##", entrega_rg);
        entrega_rg.addTextChangedListener(maskRG);
    }



    /**
     * TIPO DOCUMENTO
     */
    private void createComboDocumento(){

        List<TipoDocumento> tipoDocumentoList = tipoDocumentoBusiness.buscarTodos();

        entrega_tipo_documento = (Spinner) findViewById(R.id.entrega_tipo_documento);
        adapter_tipo_documento = new TipoDocumentoAdapter(mContext, android.R.layout.simple_spinner_item, tipoDocumentoList);

        // Specify the layout to use when the list of choices appears
        adapter_tipo_documento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        entrega_tipo_documento.setAdapter(adapter_tipo_documento);


//        entrega_tipo_recebedor.setFocusable(true);
//        entrega_tipo_recebedor.setFocusableInTouchMode(true);
    }



    /**
     * VALIDACAO FORMULARIO
     *
     * @return
     */
    private boolean validateForm(){

        camposPendentes.clear();

//        MaskedEditText tipoDocumento = (MaskedEditText) controleTipoDocumento.get("TIPO_DOCUMENTO");
//
//        String nome = String.valueOf(entrega_nome_recebedor.getText());
//
//        String numero = "";
//        if(tipoDocumento != null){
//
//            numero = String.valueOf(tipoDocumento.getText());
//        }
        EditText tipoDocumento = (EditText) controleTipoDocumento.get("TIPO_DOCUMENTO");

        String nome = String.valueOf(entrega_nome_recebedor.getText());

        String numero = "";
        if(tipoDocumento != null){

            numero = String.valueOf(tipoDocumento.getText());
        }

        boolean nomeValido = false;
        boolean numeroValido = false;

        /**
         * VALIDACAO NOME
         */
        if(!TextUtils.isEmpty(nome) && isNameValid(nome)){

            nomeValido = true;

        }else{

            camposPendentes.put("NOME", "Campo nome");

            entrega_nome_recebedor.setError("campo obrigatório");
            //entrega_nome_recebedor.requestFocus();
        }


        /**
         * VALIDACAO COMBO DOCUMENTO
         */
        int idSelectDocument = entrega_tipo_documento.getSelectedItemPosition();

        if(idSelectDocument == 0){

            camposPendentes.put("COMBO_DOCUMENTO", "Combo documento");

//            StatusDialog dialog = new StatusDialog(this, "Formulário Incompleto", "Por favor, preencha todos os campos.", false);
//            return false;
        }



        /**
         * VALIDACAO NUMERO DOCUMENTO
         */
        if(!TextUtils.isEmpty(numero) && !TextUtils.isEmpty(numero.trim())){

            numeroValido = true;

        }else{

            camposPendentes.put("NUM_DOCUMENTO", "Campo número do documento");

            entrega_nr_documento.setError("campo obrigatório");
            entrega_nr_documento.requestFocus();
        }


//        if(isNumeroValid(numero)) {
//
//            camposPendentes.put("NUM_DOCUMENTO", "Número do documento muito curto");
//            entrega_nr_documento.setError("número do documento muito curto");
//            entrega_nr_documento.requestFocus();
//
//        }


        /**
         * VALIDACAO COMBO RECEBEDOR
         */
        int idSelectRecebedor = entrega_tipo_recebedor.getSelectedItemPosition();

//        if(idSelectDocument == 0){
//
//            camposPendentes.put("COMBO_DOCUMENTO", "Combo tipo de documento");
//
////            StatusDialog dialog = new StatusDialog(this, "Formulário Incompleto", "Por favor, preencha todos os campos.", false);
////            return false;
//        }


        if(idSelectRecebedor == 0){

            camposPendentes.put("COMBO_RECEBEDOR", "Combo recebedor");
        }


        /**
         * VALIDACAO ARQUIVOS
         */
//        if(recebedor.getFotoAssinaturaDigitalPath() == null){
//
//            camposPendentes.put("ASSINATURA", "Assinatura digital");
//        }

        if(recebedor.getFotoComprovantePath() == null){

            camposPendentes.put("COMPROVANTE", "Foto do comprovante");
        }



        StringBuilder sb = new StringBuilder();
        if(camposPendentes.get("COMBO_RECEBEDOR") != null){

            sb.append(camposPendentes.get("COMBO_RECEBEDOR")+ " \n");
        }

        if(camposPendentes.get("NOME") != null){

            sb.append(camposPendentes.get("NOME")+ " \n");
        }

        if(camposPendentes.get("COMBO_DOCUMENTO") != null){

            sb.append(camposPendentes.get("COMBO_DOCUMENTO")+ " \n");
        }

        if(camposPendentes.get("NUM_DOCUMENTO") != null){

            sb.append(camposPendentes.get("NUM_DOCUMENTO")+ " \n");
        }

        if(camposPendentes.get("ASSINATURA") != null){

            sb.append(camposPendentes.get("ASSINATURA")+ " \n");
        }

        if(camposPendentes.get("COMPROVANTE") != null){

            sb.append(camposPendentes.get("COMPROVANTE")+ " \n");
        }

        //MOSTRAR DIALOG - CAMPOS PENDENTES
        if(camposPendentes.size() > 0){

            StatusDialog dialog = new StatusDialog(this, "Formulário Incompleto", "Preencha os campos obrigatórios\n\n" +sb.toString(), false);

            return false;
        }

        return true;
    }

    private boolean isNameValid(String name) {

        if(name != null){

            return true;
        }

        return false;
    }

    private boolean isNumeroValid(String numero) {

        if(numero != null && numero.length() > 9){

            return true;
        }

        return false;
    }




    /**
     * ENVIAR SERVIDOR
     */
    private void enviarDadosServidor(){

        createLoading(mActivity, "Enviando");
        showProgress(true);

        getDadosFormulario();

        long id = statusBusiness.getIdEncomendaCorrente();
        final Encomenda encomendaCorrente = encomendaBusiness.buscarEncomendaCorrente(id);

        //RECUPERA MINHA LOCALIZACAO ATUAL
        MyLocationTimerTask timerTaskLocation = new MyLocationTimerTask(mContext, TabItemMapaFragment.map);
        timerTaskLocation.startTimer();

        LatLng latLng = timerTaskLocation.getMyLatLng();
        timerTaskLocation.stoptimertask();

        /**
         * ATUALIZAR DATA BAIXA
         */
        encomendaCorrente.setDataBaixa(DateUtil.getDataAtual());
        encomendaBusiness.update(encomendaCorrente);



        if(InternetStatus.isNetworkAvailable(mContext)) {

            BaixaEntregueVolley baixaEntregueVolley = new BaixaEntregueVolley(getApplicationContext(), encomendaCorrente, recebedor, latLng, new DelegateEntregaAsyncResponse() {

                @Override
                public void processFinish(boolean finish, String resposta) {

                    showProgress(false);


                    /**
                     * REMOVE FLAG COLOR
                     */
                    encomendaCorrente.setFlagTratado(false);

                    /**
                     * ATUALIZA STATUS ENCOMENDA
                     */
                    encomendaBusiness.encomendaEntregue(encomendaCorrente);

                    /**
                     * REMOVE ENCOMENDA CORRENTE
                     */
                    statusBusiness.removeEncomendaCorrente();
//                TabItemMapaFragment.isRemoveMarkerMap = true;

                    /**
                     * REMOVE MARKER
                     */
//                TabItemMapaFragment.marker.remove();
//                TabItemMapaFragment.map.clear();


                    /**
                     * ENVIADO COM SUCESSO
                     *
                     * DELETE DO MODO OFFLINE
                     */
                    recebedorBusiness.getRecebedorList();
                    recebedorBusiness.delete(recebedor);

                    finish();
                }

                @Override
                public void processCanceled(boolean cancel) {

                    System.out.println("ERRO - BAIXA ENCOMENDA");

                    showProgress(false);
                    finish();
                }
            });

        }else{

            /**
             * MODO OFFLINE
             *
             * SALVAR RECEBEDOR LOCAL
             */
            recebedor.setDataFinalizacao(encomendaCorrente.getDataBaixa());
            recebedorBusiness.salvarRecebedor(recebedor);
//            recebedorBusiness.getRecebedorList();


            /**
             * REMOVE FLAG COLOR
             */
            encomendaCorrente.setFlagTratado(false);

            /**
             * ATUALIZA STATUS ENCOMENDA
             */
            encomendaBusiness.encomendaEntregue(encomendaCorrente);

            /**
             * REMOVE ENCOMENDA CORRENTE
             */
            statusBusiness.removeEncomendaCorrente();


            /**
             * START TIMER TASK
             *
             *
             */




            /**
             * FINALIZAR
             */
            finish();


        }
    }













    private void assinaturaDigital(){

        ImageView entrega_assinatura_digital_icon = (ImageView) findViewById(R.id.entrega_assinatura_digital_icon);
        entrega_assinatura_digital_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entrega_assinatura_digital_foto = (ImageView) findViewById(R.id.entrega_assinatura_digital_foto);

                Intent intent = new Intent(getApplicationContext(), AssinaturaDigital.class);
                startActivityForResult(intent, REQUEST_RETORNO_ASSINATURA_DIGITAL);
                visualizarAssinaturaDigital();
            }
        });
    }

    private void visualizarAssinaturaDigital(){

        entrega_assinatura_digital_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagemUtil.showPhoto(mActivity, Uri.parse("file:" + recebedor.getFotoAssinaturaDigitalPath()));
            }
        });
    }



    private void fotoComprovante(){

        ImageView entrega_foto_comprovante_icon = (ImageView) findViewById(R.id.entrega_foto_comprovante_icon);
        entrega_foto_comprovante_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entrega_comprovante_foto = (ImageView) findViewById(R.id.entrega_comprovante_foto);

                photoURI = ImagemUtil.dispatchTakePictureIntent(mActivity, "comprovante", REQUEST_TAKE_PHOTO_COMPROVANTE);

                visualizarFotoComprovante();
            }
        });
    }

    private void visualizarFotoComprovante(){

        entrega_comprovante_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagemUtil.showPhoto(mActivity, Uri.parse("file:" + recebedor.getFotoComprovantePath()));
            }
        });
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO_COMPROVANTE && resultCode == RESULT_OK) {

            Bitmap bm = null;
            if(photoURI != null) {

                bm = getBitmap(photoURI);


                entrega_comprovante_foto.setVisibility(View.VISIBLE);
                entrega_comprovante_foto.setImageBitmap(bm);

                String base64FotoComprovante = ImagemUtil.getEncoded64ImageStringFromBitmap(mContext, bm);

                recebedor.setFotoComprovanteBase64(base64FotoComprovante);
                recebedor.setFotoComprovantePath(diretorioCompleto);
            }
        }

        if(requestCode == REQUEST_ASSINATURA_DIGITAL && resultCode == RESULT_OK){

            Bitmap bm = null;
            if(assinaturaURI != null) {

                bm = getBitmap(assinaturaURI);

                entrega_assinatura_digital_foto.setImageBitmap(bm);

                String base64FotoAssinaturaDigital = ImagemUtil.getEncoded64ImageStringFromBitmap(mContext, bm);
                recebedor.setFotoAssinaturaDigitalBase64(base64FotoAssinaturaDigital);
            }
        }

        if(requestCode == REQUEST_RETORNO_ASSINATURA_DIGITAL && resultCode == RESULT_OK){

            Bundle assinaturaPath = (Bundle) data.getExtras();
            File assinatura = (File) assinaturaPath.get("assinatura_name");
            diretorioCompleto = assinatura.getAbsolutePath();
            assinaturaURI = (Uri) assinaturaPath.get("assinatura_uri");

            if(assinaturaURI != null) {

                Bitmap bm = getBitmap(assinaturaURI);

                entrega_assinatura_digital_foto = (ImageView) findViewById(R.id.entrega_assinatura_digital_foto);

                entrega_assinatura_digital_foto.setVisibility(View.VISIBLE);
                entrega_assinatura_digital_foto.setImageBitmap(bm);

                String base64FotoAssinaturaDigital = ImagemUtil.getEncoded64ImageStringFromBitmap(mContext, bm);
                recebedor.setFotoAssinaturaDigitalBase64(base64FotoAssinaturaDigital);
                recebedor.setFotoAssinaturaDigitalPath(diretorioCompleto);
            }
        }
    }





    private Bitmap getBitmap(Uri uri){
        Bitmap bm = null;

        if(uri != null){

            String name = uri.getPath().replace("/my_images/", "");
            diretorioCompleto = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/br.com.a3rtecnologia.baixamobile/files/Pictures/" + name;
            bm = ImagemUtil.getbitpam(diretorioCompleto);

        }else{


        }

        return bm;
    }





    private void createLoading(Context mContext, String message){

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    public void showProgress(boolean isShow){

        if(mProgressDialog != null && isShow && !mProgressDialog.isShowing()){

            mProgressDialog.show();

        }else if(mProgressDialog != null && !isShow && mProgressDialog.isShowing()){

            mProgressDialog.hide();
        }
    }

}
