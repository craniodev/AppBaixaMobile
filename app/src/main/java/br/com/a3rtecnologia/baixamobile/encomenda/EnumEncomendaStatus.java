package br.com.a3rtecnologia.baixamobile.encomenda;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.sincronizacao.Atualizacao;
import br.com.a3rtecnologia.baixamobile.usuario.Usuario;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumEncomendaStatus {

    EM_ROTA(5, "Em Rota"),
    LIBERADO(6, "Liberado"),

//    PENDENTE_SINCRONIZAR(-1, "Pendente Sincronizar"),

    ENTREGUE(7, "Entregue"),
    OCORRENCIA(8, "Ocorrência");



    private long key;
    private String value;

    private EnumEncomendaStatus(long key, String value){

        this.key = key;
        this.value = value;
    }



    public long getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
