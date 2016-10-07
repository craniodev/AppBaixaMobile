package br.com.a3rtecnologia.baixamobile.cadastro;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import br.com.a3rtecnologia.baixamobile.MaskBaixa;
import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.DelegateAsyncResponse;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;

public class CadastroActivity extends AppCompatActivity {

    private Context mContext;
    private View focusView;
    private ProgressDialog progress;

    private AutoCompleteTextView nomeEditText;
    private EditText cpfEditText;
    private EditText cnhEditText;
    private EditText telefoneEditText;
    private EditText celularEditText;

    private AutoCompleteTextView emailEditText;
    private Usuario usuario;

    private boolean cancel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mContext = this;
        cancel = false;
        focusView = null;

        nomeEditText = (AutoCompleteTextView) findViewById(R.id.register_complete_name_id);
        cpfEditText = (EditText) findViewById(R.id.register_cpf_id);
        cnhEditText = (EditText) findViewById(R.id.register_cnh_id);
        telefoneEditText = (EditText) findViewById(R.id.register_phone1_id);
        celularEditText = (EditText) findViewById(R.id.register_phone2_id);
        emailEditText = (AutoCompleteTextView) findViewById(R.id.register_email_id);

        MaskBaixa maskCPF = new MaskBaixa("###.###.###-##", cpfEditText);
        cpfEditText.addTextChangedListener(maskCPF);

        MaskBaixa maskCNH = new MaskBaixa("###########", cnhEditText);
        cnhEditText.addTextChangedListener(maskCNH);

        MaskBaixa maskTEL = new MaskBaixa("(##)####-####", telefoneEditText);
        telefoneEditText.addTextChangedListener(maskTEL);

        MaskBaixa maskCEL = new MaskBaixa("(##)#####-####", celularEditText);
        celularEditText.addTextChangedListener(maskCEL);

        createLoading(mContext);

        Button botaoRegistrar = (Button) findViewById(R.id.register_button);
        botaoRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                cancel = false;
                focusView = null;

                getValuesForm();
                validationForm();

                if (cancel) {

                    focusView.requestFocus();

                } else {

                    cadastroServer(usuario);
                }
            }
        });
    }



    private void cadastroServer(Usuario usuario){

        showProgress(true);

        if(InternetStatus.isNetworkAvailable(mContext)){

            new CadastroVolley(mContext, usuario, new DelegateAsyncResponse() {

                @Override
                public void processFinish(boolean success) {

                    showProgress(false);
                }

                @Override
                public void processCanceled(boolean status) {

                    showProgress(false);
                }

            });

        }else{

            showProgress(false);
            StatusDialog dialog = new StatusDialog((Activity)mContext, "Cadastro", "Sem conexão com internet", false);
        }
    }



    private void validationForm(){

        if (TextUtils.isEmpty(usuario.getNome().trim())) {

            nomeEditText.setError("Campo obrigatório");
            focusView = nomeEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario.getCpf().trim())) {

            cpfEditText.setError("Campo obrigatório");
            focusView = cpfEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario.getCnh().trim())) {

            cnhEditText.setError("Campo obrigatório");
            focusView = cnhEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario.getFone1().trim())) {

            telefoneEditText.setError("Campo obrigatório");
            focusView = telefoneEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario.getFone2().trim())) {

            celularEditText.setError("Campo obrigatório");
            focusView = celularEditText;
            cancel = true;
        }

        if (!usuario.getEmail().contains("@")) {

            emailEditText.setError("Email com formato inválido");
            focusView = emailEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario.getEmail().trim())) {

            emailEditText.setError("Campo obrigatório");
            focusView = emailEditText;
            cancel = true;
        }
    }



    private void getValuesForm(){

        nomeEditText.setError(null);
        cpfEditText.setError(null);
        cnhEditText.setError(null);
        telefoneEditText.setError(null);
        celularEditText.setError(null);
        emailEditText.setError(null);

        String nome = String.valueOf(nomeEditText.getText());
        String cpf = String.valueOf(cpfEditText.getText());
        String cnh = String.valueOf(cnhEditText.getText());
        String fone1 = String.valueOf(telefoneEditText.getText());
        String fone2 = String.valueOf(celularEditText.getText());
        String email = String.valueOf(emailEditText.getText());

        usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(removeCharacters(cpf));
        usuario.setCnh(cnh);
        usuario.setFone1(removeCharacters(fone1));
        usuario.setFone2(removeCharacters(fone2));
        usuario.setEmail(email);
    }



    private void clearAllCampos(){

        nomeEditText.setText("");
        cpfEditText.setText("");
        cnhEditText.setText("");
        telefoneEditText.setText("");
        celularEditText.setText("");
        emailEditText.setText("");
    }



    private String removeCharacters(String value){

        String value1 = value.replace("-", "");
        String value2 = value1.replace(".", "");
        String value3 = value2.replace("(", "");
        String valueFinal = value3.replace(")", "");

        return valueFinal;
    }



    private void createLoading(Context mContext){

        progress = new ProgressDialog(mContext);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Por favor, aguarde.");
    }



    private void showProgress(boolean isShow){

        if(progress != null && isShow && !progress.isShowing()){

            progress.show();

        }else if(progress != null && !isShow && progress.isShowing()){

            progress.hide();
        }
    }

}
