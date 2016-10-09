package br.com.a3rtecnologia.baixamobile.tipo_ocorrencia;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;

/**
 * Created by maclemon on 03/09/16.
 */
public class TipoOcorrenciaBusiness {

    private Dao<TipoOcorrencia, Integer> tipoOcorrenciaDao;
    private Context mContext;



    public TipoOcorrenciaBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }


    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            tipoOcorrenciaDao = new CustomDao<TipoOcorrencia, Integer>(helper.getConnectionSource(), TipoOcorrencia.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }




    public void delete(List<TipoOcorrencia> tipoOcorrenciaList){

        try {

            tipoOcorrenciaDao.delete(tipoOcorrenciaList);

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

            List<TipoOcorrencia> list = tipoOcorrenciaDao.queryForAll();
            tipoOcorrenciaDao.delete(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * SALVAR OCORRENCIA
     *
     * @param tipoOcorrencia
     */
    public void salvarOrUpdate(TipoOcorrencia tipoOcorrencia){

        try {


            tipoOcorrenciaDao.createOrUpdate(tipoOcorrencia);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * ATUALIZAR LISTA_ENCOMENDA
     *
     * @param tipoOcorrencia
     */
    public void atualizar(TipoOcorrencia tipoOcorrencia){

        try {

            tipoOcorrenciaDao.update(tipoOcorrencia);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







    /**
     * COUNT
     *
     * @return
     */
    public int count(){

//        Long count = null;
//        try {
//
//            count = usuarioDao.queryBuilder()
//                    .where()
//                    .eq(Usuario.FIELD_NAME_NAME, "Fernando")
//                    .countOf();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        List<TipoOcorrencia> lista = buscarTodos();

        if(lista != null){

            return lista.size();

        }else{

            return 0;
        }
    }







    /**
     * BUSCAR TODOS
     *
     * @return
     */
    public List<TipoOcorrencia> buscarTodos(){

        List<TipoOcorrencia> tipoOcorrenciaList = null;

        try {
            tipoOcorrenciaList = tipoOcorrenciaDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipoOcorrenciaList;
    }




    /**
     * BUSCAR POR NOME
     *
     * @return
     */
    public TipoOcorrencia buscarPorNome(String nome){

        List<TipoOcorrencia> tipoOcorrenciaList = null;

        try {

            tipoOcorrenciaList = tipoOcorrenciaDao.queryBuilder()
                    .where()
                    .eq("Descricao", nome)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TipoOcorrencia tipoOcorrencia = null;
        if(tipoOcorrenciaList != null && tipoOcorrenciaList.size() > 0){

            tipoOcorrencia = tipoOcorrenciaList.get(0);
        }

        return tipoOcorrencia;
    }




    /**
     * BUSCAR POR ID
     *
     * @return
     */
    public TipoOcorrencia buscarPorId(int id){

        List<TipoOcorrencia> tipoOcorrenciaList = null;

        try {

            tipoOcorrenciaList = tipoOcorrenciaDao.queryBuilder()
                    .where()
                    .eq("idTipoOcorrencia", id)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TipoOcorrencia tipoOcorrencia = null;
        if(tipoOcorrenciaList != null && tipoOcorrenciaList.size() > 0){

            tipoOcorrencia = tipoOcorrenciaList.get(0);
        }

        return tipoOcorrencia;
    }

}
