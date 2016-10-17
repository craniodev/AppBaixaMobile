package br.com.a3rtecnologia.baixamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import br.com.a3rtecnologia.baixamobile.entrega_sincronizacao.EntregaReceiver;
import br.com.a3rtecnologia.baixamobile.iniciar_entrega_sincronizacao.IniciarEntregaReceiver;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem_sincronizacao.IniciarViagemReceiver;
import br.com.a3rtecnologia.baixamobile.ocorrencia_sincronizacao.OcorrenciaReceiver;

/**
 * Created by maclemon on 16/10/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(final Context mContext, final Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if( (wifi.isAvailable() && wifi.isConnectedOrConnecting()) || (mobile.isAvailable() && mobile.isConnectedOrConnecting())){

            AsyncCheckInternet ping = new AsyncCheckInternet(mContext, new AsyncCheckInternet.OnCallBack() {
                @Override
                public void onBack(Boolean isOnline) {

                    if(isOnline) {

                        Toast.makeText(mContext, "CHANGE CONNECTION - ONLINE", Toast.LENGTH_LONG).show();

                        Intent iniciarViagemIntent = new Intent(mContext, IniciarViagemReceiver.class);
                        iniciarViagemIntent.putExtra("OPERACAO", "START");
                        mContext.sendBroadcast(iniciarViagemIntent);

                        Intent iniciarEntregaIntent = new Intent(mContext, IniciarEntregaReceiver.class);
                        iniciarEntregaIntent.putExtra("OPERACAO", "START");
                        mContext.sendBroadcast(iniciarEntregaIntent);

                        Intent ocorrenciaIntent = new Intent(mContext, OcorrenciaReceiver.class);
                        ocorrenciaIntent.putExtra("OPERACAO", "START");
                        mContext.sendBroadcast(ocorrenciaIntent);

                        Intent entregaIntent = new Intent(mContext, EntregaReceiver.class);
                        entregaIntent.putExtra("OPERACAO", "START");
                        mContext.sendBroadcast(entregaIntent);

                    }else{

                        Toast.makeText(mContext, "CHANGE CONNECTION - OFFLINE", Toast.LENGTH_LONG).show();
                    }
                }
            });
            ping.execute();

            Log.d("Network Available ", "Flag No 1");
        }
    }


}
