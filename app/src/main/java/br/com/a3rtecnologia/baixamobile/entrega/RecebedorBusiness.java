package br.com.a3rtecnologia.baixamobile.entrega;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.api.EnumAPI;
import br.com.a3rtecnologia.baixamobile.encomenda.DelegateEncomendaAsyncResponse;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomendas;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.util.EnumHttpError;
import br.com.a3rtecnologia.baixamobile.util.GsonRequest;
import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
import br.com.a3rtecnologia.baixamobile.util.SessionManager;
import br.com.a3rtecnologia.baixamobile.util.VolleySingleton;
import br.com.a3rtecnologia.baixamobile.util.VolleyTimeout;

/**
 * Created by maclemon on 27/08/16.
 */
public class RecebedorBusiness {

    private Dao<Recebedor, Integer> recebedorDao;
    private Context mContext;



    public RecebedorBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }



    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            recebedorDao = new CustomDao<Recebedor, Integer>(helper.getConnectionSource(), Recebedor.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }





    public void atualizarRecebedor(Recebedor recebedor){

        try {

            recebedorDao.update(recebedor);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * DELETE ALL
     *
     */
    public void deleteAll(){

        try {

            List<Recebedor> recebedorList = recebedorDao.queryForAll();
            recebedorDao.delete(recebedorList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void salvarRecebedor(Recebedor recebedor){


    }

    public void salvarAssinaturaDigital(Recebedor recebedor){


    }

    public void salvarFotoComprovante(Recebedor recebedor){


    }


}
