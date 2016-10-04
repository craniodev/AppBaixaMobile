package br.com.a3rtecnologia.baixamobile.encomenda;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;

/**
 * Created by maclemon on 27/08/16.
 */
public class EncomendaBusiness {

    private Dao<Encomenda, Integer> encomendaDao;
    private Context mContext;


    public EncomendaBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }


    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            encomendaDao = new CustomDao<Encomenda, Integer>(helper.getConnectionSource(), Encomenda.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }



    /**
     * SALVAR NOVA ENCOMENDA
     *
     * @param encomenda
     */
    public void salvar(Encomenda encomenda){

        try {

            encomendaDao.create(encomenda);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * atualizar NOVA ENCOMENDA
     *
     * @param encomenda
     */
    public void update(Encomenda encomenda){

        try {

            encomendaDao.update(encomenda);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * ATUALIZAR LISTA_ENCOMENDA
     *
     * @param encomenda
     */
    public void atualizarEncomenda(Encomenda encomenda){

        try {

            encomendaDao.update(encomenda);

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

            List<Encomenda> encomendaList = encomendaDao.queryForAll();
            encomendaDao.delete(encomendaList);

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

        List<Encomenda> lista = buscarTodos();

        if(lista != null){

            return lista.size();

        }else{

            return 0;
        }
    }




    /**
     * COUNT
     *
     * @return
     */
    public int countOcorrenciasNaoSincronizadas(){

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

        List<Encomenda> lista = buscarOcorrenciasNaoSincronizadas();

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
    public List<Encomenda> buscarTodos(){

        List<Encomenda> encomendas = null;

        try {
            encomendas = encomendaDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return encomendas;
    }







    /**
     * BUSCAR EM ROTA
     *
     * @return
     */
    public List<Encomenda> getEncomendasByIds(List<Long> listIdEncomenda){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .in("IdEncomenda", listIdEncomenda)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * BUSCAR EM ROTA
     *
     * @return
     */
    public List<Encomenda> getEncomendasByIdSincronizado(List<Long> listIdEncomenda){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .in("IdEncomenda", listIdEncomenda)
                    .and()
                    .eq("flagEnviado", EnumStatusEnvio.SINCRONIZADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * BUSCAR ENTREGUE
     *
     * @return
     */
    public List<Encomenda> buscarEntregasFinalizadas(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("IdStatus", EnumEncomendaStatus.ENTREGUE.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * BUSCAR ENTREGUE NAO SINCRONIZADAS
     *
     * @return
     */
    public List<Encomenda> buscarOcorrenciasNaoSincronizadas(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("IdStatus", EnumEncomendaStatus.OCORRENCIA.getKey())
                    .and()
                    .eq("flagEnviado", EnumStatusEnvio.NAO_SINCRONIZADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * BUSCAR PENDENTES
     *
     * @return
     */
    public List<Encomenda> buscarEntregasOcorrencia(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("IdStatus", EnumEncomendaStatus.OCORRENCIA.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }




    /**
     * BUSCAR PENDENTES NAO SINCRONIZADAS
     *
     * @return
     */
    public List<Encomenda> buscarPendentesNaoSincronizadas(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("IdStatus", EnumEncomendaStatus.OCORRENCIA.getKey())
                    .and()
                    .eq("flagEnviado", EnumStatusEnvio.NAO_SINCRONIZADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * BUSCAR PENDENTES NAO SINCRONIZADAS
     *
     * @return
     */
    public List<Encomenda> buscarRealizadasNaoSincronizadas(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("IdStatus", EnumEncomendaStatus.ENTREGUE.getKey())
                    .and()
                    .eq("flagEnviado", EnumStatusEnvio.NAO_SINCRONIZADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * BUSCAR EM ROTA
     *
     * @return
     */
    public List<Encomenda> buscarEntregasEmRota(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
//                    .eq("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey())
                    .in("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey(), EnumEncomendaStatus.LIBERADO.getKey())
//                    .and()
//                    .eq("IdStatus", EnumEncomendaStatus.LIBERADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * COUNT EM ROTA
     *
     * @return
     */
    public int countEncomendasEmRotaAndLiberado(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    //.in("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey(), EnumEncomendaStatus.LIBERADO.getKey())
                    .eq("IdStatus", EnumEncomendaStatus.LIBERADO.getKey())
                    .or()
                    .eq("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result != null ? result.size() : 0;
    }





    /**
     * BUSCAR EM ROTA
     *
     * @return
     */
    public List<Encomenda> buscarEntregasEmRotaLimit10(){

        List<Encomenda> list = null;

        QueryBuilder<Encomenda, Integer> builder = encomendaDao.queryBuilder();

        try {

            builder.where().in("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey(), EnumEncomendaStatus.LIBERADO.getKey());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        builder.limit(10);
//        builder.orderBy("columnName", true);  // true for ascending, false for descending

        try {

            list = encomendaDao.query(builder.prepare());  // returns list of ten items

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }





    /**
     * BUSCAR ULTIMA ENTREGUE
     *
     * @return
     */
    public List<Encomenda> buscarNaoEnviadas(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("flagEnviado", 0)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }




    /**
     * BUSCAR POR NOME
     *
     * @return
     */
    public Encomenda buscarPorNome(String nome){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("NmDestinatario", nome)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        Encomenda encomenda = null;
        if(result != null && result.size() > 0){

            encomenda = result.get(0);
        }

        return encomenda;
    }





//    /**
//     * BUSCAR ULTIMA ENTREGUE
//     *
//     * @return
//     */
//    public Encomenda buscarEncomendaCorrente(){
//
//        List<Encomenda> result = null;
//
//        try {
//
//            result = encomendaDao.queryBuilder()
//                    .where()
//                    .eq("flagEncomendaCorrente", 1)
//                    .query();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        Encomenda encomenda = null;
//        if(result != null && result.size() > 0){
//
//            encomenda = result.get(result.size() - 1);
//        }
//
//        return encomenda;
//    }





    /**
     * BUSCAR ULTIMA ENTREGUE
     *
     * @return
     */
    public Encomenda buscarEncomendaCorrente(Long idEncomenda){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("IdEncomenda", idEncomenda)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Encomenda encomenda = null;
        if(result != null && result.size() > 0){

            encomenda = result.get(result.size() - 1);
        }

        return encomenda;
    }





    /**
     * ENCOMENDA CORRENTE
     *
     * @return
     */
//    public Encomenda atualizarEncomendaCorrente(Encomenda encomenda){
//
//        encomenda.setFlagEncomendaCorrente(1);
//
//        atualizarEncomenda(encomenda);
//
//        return encomenda;
//    }





    /**
     * EM ROTA
     *
     * @return
     */
    public Encomenda atualizarStatusEncomendaEmRota(Encomenda encomenda){

        encomenda.setIdStatus(EnumEncomendaStatus.EM_ROTA.getKey());
        encomenda.setDescStatus(EnumEncomendaStatus.EM_ROTA.getValue());

        atualizarEncomenda(encomenda);

        return encomenda;
    }




    /**
     * EM ROTA
     *
     * @return
     */
    public Encomenda atualizarStatusEncomendaOcorrencia(Encomenda encomenda){

        encomenda.setIdStatus(EnumEncomendaStatus.OCORRENCIA.getKey());
        encomenda.setDescStatus(EnumEncomendaStatus.OCORRENCIA.getValue());

        atualizarEncomenda(encomenda);

        return encomenda;
    }





    /**
     * ENTREGUE
     *
     * @return
     */
    public Encomenda encomendaEntregue(Encomenda encomenda){

//        Encomenda encomenda = buscarEncomendaCorrente();

        encomenda.setIdStatus(EnumEncomendaStatus.ENTREGUE.getKey());
        encomenda.setDescStatus(EnumEncomendaStatus.ENTREGUE.getValue());
//        encomenda.setFlagEncomendaCorrente(0);

        atualizarEncomenda(encomenda);

        return encomenda;
    }


}
