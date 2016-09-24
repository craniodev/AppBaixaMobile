package br.com.a3rtecnologia.baixamobile.recuperar;

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

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.dialogs.StatusDialog;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.DelegateAsyncResponse;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.KeyboardUtil;

public class RecuperarActivity extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progress;

    private Usuario usuario;
    private AutoCompleteTextView recovery_login_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mContext = this;

        createLoading(mContext);

        recovery_login_id = (AutoCompleteTextView) findViewById(R.id.recovery_email_id);

        Button recovery_send_button = (Button) findViewById(R.id.recovery_send_button);
        recovery_send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String login = String.valueOf(recovery_login_id.getText());

                usuario = new Usuario();
                usuario.setLogin(login);

                if(TextUtils.isEmpty(usuario.getLogin())){

                    recovery_login_id.setError("Campo obrigatório");

                }else {

                    recuperaServer();

                    KeyboardUtil.hideKeyboard(mContext, recovery_login_id);
                    recovery_login_id.setText("");
                }
            }
        });
    }



    private void recuperaServer(){

        showProgress(true);

        if (InternetStatus.isNetworkAvailable(mContext)) {

//            String email = String.valueOf(recovery_login_id.getText());
//            usuario.setEmail(email);

            new RecuperarVolley(mContext, usuario, new DelegateAsyncResponse() {

                @Override
                public void processFinish(boolean success) {

                    showProgress(false);

//                    KeyboardUtil.hideKeyboard(mContext, recovery_login_id);
//
//                    recovery_login_id.setText("");
                }

                @Override
                public void processCanceled(boolean status) {

                    showProgress(false);
                }
            });

        } else {

            showProgress(false);
            StatusDialog dialog = new StatusDialog((Activity) mContext, "Recuperação de senha", "Sem conexão com internet", false);
        }
    }



    private void createLoading(Context mContext){

        progress = new ProgressDialog(mContext);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Por favor, aguarde");
    }



    private void showProgress(boolean isShow){

        if(progress != null && isShow && !progress.isShowing()){

            progress.show();

        }else if(progress != null && !isShow && progress.isShowing()){

            progress.hide();
        }
    }
}
