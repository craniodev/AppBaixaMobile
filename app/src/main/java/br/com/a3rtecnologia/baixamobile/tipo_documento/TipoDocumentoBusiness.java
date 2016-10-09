package br.com.a3rtecnologia.baixamobile.tipo_documento;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;

/**
 * Created by maclemon on 03/09/16.
 */
public class TipoDocumentoBusiness {

    private Dao<TipoDocumento, Integer> tipoDocumentoDao;
    private Context mContext;



    public TipoDocumentoBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }


    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            tipoDocumentoDao = new CustomDao<TipoDocumento, Integer>(helper.getConnectionSource(), TipoDocumento.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }




    /**
     * DELETE ALL
     *
     */
    public void deleteAll(){

        try {

            List<TipoDocumento> list = tipoDocumentoDao.queryForAll();
            tipoDocumentoDao.delete(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * SALVAR OCORRENCIA
     *
     * @param tipoDocumento
     */
    public void salvarOrUpdate(TipoDocumento tipoDocumento){

        try {

            tipoDocumentoDao.createOrUpdate(tipoDocumento);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * ATUALIZAR LISTA_ENCOMENDA
     *
     * @param tipoDocumento
     */
    public void atualizar(TipoDocumento tipoDocumento){

        try {

            tipoDocumentoDao.update(tipoDocumento);

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

        List<TipoDocumento> lista = buscarTodos();

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
    public List<TipoDocumento> buscarTodos(){

        List<TipoDocumento> tipoDocumentoList = null;

        try {
            tipoDocumentoList = tipoDocumentoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipoDocumentoList;
    }




    /**
     * BUSCAR POR NOME
     *
     * @return
     */
    public TipoDocumento buscarPorNome(String nome){

        List<TipoDocumento> tipoDocumentoList = null;

        try {

            tipoDocumentoList = tipoDocumentoDao.queryBuilder()
                    .where()
                    .eq("Descricao", nome)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TipoDocumento tipoDocumento = null;
        if(tipoDocumentoList != null && tipoDocumentoList.size() > 0){

            tipoDocumento = tipoDocumentoList.get(0);
        }

        return tipoDocumento;
    }




    /**
     * BUSCAR POR NOME
     *
     * @return
     */
    public TipoDocumento buscarPorId(int id){

        List<TipoDocumento> tipoDocumentoList = null;

        try {

            tipoDocumentoList = tipoDocumentoDao.queryBuilder()
                    .where()
                    .eq("idTipoDocumento", id)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TipoDocumento tipoDocumento = null;
        if(tipoDocumentoList != null && tipoDocumentoList.size() > 0){

            tipoDocumento = tipoDocumentoList.get(0);
        }

        return tipoDocumento;
    }

}
