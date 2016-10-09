package br.com.a3rtecnologia.baixamobile.tipo_recebedor;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;

/**
 * Created by maclemon on 03/09/16.
 */
public class TipoRecebedorBusiness {

    private Dao<TipoRecebedor, Integer> tipoRecebedorDao;
    private Context mContext;



    public TipoRecebedorBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }


    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            tipoRecebedorDao = new CustomDao<TipoRecebedor, Integer>(helper.getConnectionSource(), TipoRecebedor.class);

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

            List<TipoRecebedor> list = tipoRecebedorDao.queryForAll();
            tipoRecebedorDao.delete(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    /**
     * SALVAR OCORRENCIA
     *
     * @param tipoRecebedor
     */
    public void salvarOrUpdate(TipoRecebedor tipoRecebedor){

        try {

            tipoRecebedorDao.createOrUpdate(tipoRecebedor);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * ATUALIZAR LISTA_ENCOMENDA
     *
     * @param tipoRecebedor
     */
    public void atualizar(TipoRecebedor tipoRecebedor){

        try {

            tipoRecebedorDao.update(tipoRecebedor);

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

        List<TipoRecebedor> lista = buscarTodos();

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
    public List<TipoRecebedor> buscarTodos(){

        List<TipoRecebedor> tipoRecebedorList = null;

        try {
            tipoRecebedorList = tipoRecebedorDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipoRecebedorList;
    }




    /**
     * BUSCAR POR NOME
     *
     * @return
     */
    public TipoRecebedor buscarPorNome(String nome){

        List<TipoRecebedor> tipoRecebedorList = null;

        try {

            tipoRecebedorList = tipoRecebedorDao.queryBuilder()
                    .where()
                    .eq("Descricao", nome)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TipoRecebedor tipoRecebedor = null;
        if(tipoRecebedorList != null && tipoRecebedorList.size() > 0){

            tipoRecebedor = tipoRecebedorList.get(0);
        }

        return tipoRecebedor;
    }




    /**
     * BUSCAR POR ID
     *
     * @return
     */
    public TipoRecebedor buscarPorId(int id){

        List<TipoRecebedor> tipoRecebedorList = null;

        try {

            tipoRecebedorList = tipoRecebedorDao.queryBuilder()
                    .where()
                    .eq("IdTipoRecebedor", id)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TipoRecebedor tipoRecebedor = null;
        if(tipoRecebedorList != null && tipoRecebedorList.size() > 0){

            tipoRecebedor = tipoRecebedorList.get(0);
        }

        return tipoRecebedor;
    }

}
