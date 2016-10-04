package br.com.a3rtecnologia.baixamobile.ocorrencia;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.entrega.Recebedor;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;

/**
 * Created by maclemon on 27/08/16.
 */
public class OcorrenciaBusiness {

    private Dao<Ocorrencia, Integer> ocorrenciaDao;
    private Context mContext;



    public OcorrenciaBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }



    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            ocorrenciaDao = new CustomDao<Ocorrencia, Integer>(helper.getConnectionSource(), Ocorrencia.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }


    /**
     * UPDATE OCORRENCIA
     *
     * @param ocorrencia
     */
    public void atualizarOcorrencia(Ocorrencia ocorrencia){

        try {

            ocorrenciaDao.update(ocorrencia);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    /**
     * BUSCA TODAS OCORRENCIAS
     *
     * @return
     */
    public List<Ocorrencia> getOcorrenciaList(){

        List<Ocorrencia> result = null;

        try {

            result = ocorrenciaDao.queryForAll();

//            for (Recebedor recebedor : result){
//
//
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Recebedor recebedor = null;
//        if(result != null && result.size() > 0) {
//
//            recebedor = result.get(result.size() -1);
//        }

        return result;
    }





    /**
     * DELETE
     *
     */
    public void delete(Ocorrencia ocorrencia){

        try {

            ocorrenciaDao.delete(ocorrencia);

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

            List<Ocorrencia> ocorrenciaList = ocorrenciaDao.queryForAll();
            ocorrenciaDao.delete(ocorrenciaList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void salvarOcorrencia(Ocorrencia ocorrencia){

        try {

            ocorrenciaDao.create(ocorrencia);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
