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
     * COUNT - TODOS DIFERENTE DE NAO_SINCRONIZADO
     *
     * @return
     */
    public int count(){

        List<Encomenda> lista = buscarTodosNaoPendentesSincronismo();


        if(lista != null){

            return lista.size();

        }else{

            return 0;
        }
    }




    /**
     * OCORRENCIA
     *
     *
     * COUNT - OCORRENCIAS NAO SINCRONIZADAS
     *
     * @return
     */
    public int countOcorrenciasNaoSincronizadas(){

        List<Encomenda> lista = buscarOcorrenciasNaoSincronizadas();

        if(lista != null){

            return lista.size();

        }else{

            return 0;
        }
    }











    /**
     * ENTREGUE
     *
     *
     * COUNT - ENTREGUE NAO SINCRONIZADAS
     *
     * @return
     */
    public int countEntregueNaoSincronizadas(){

        List<Encomenda> lista = buscarEntregueNaoSincronizadas();

        if(lista != null){

            return lista.size();

        }else{

            return 0;
        }
    }







    /**
     * OCORRENCIA E ENTREGUE
     *
     *
     * COUNT - NAO SINCRONIZADAS - ENTREGUE E OCORRENCIAS
     *
     * @return
     */
    public int countNaoSincronizadas(){

        List<Encomenda> lista = naoSincronizados();

        if(lista != null){

            return lista.size();

        }else{

            return 0;
        }
    }









    /**
     * TUDO
     *
     *
     * BUSCAR TODOS - INDEPENDENTE DE STATUS
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
     * TODOS COM STATUS DA API
     *
     *
     * BUSCAR TODOS COM STATUS DIFERENTE DE NAO SINCRONIZADOS
     *
     * @return
     */
    public List<Encomenda> buscarTodosNaoPendentesSincronismo(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .in("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey(), EnumEncomendaStatus.LIBERADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }







    /**
     * EM ROTA
     *
     *
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
     * ENTREGA JA SINCRONIZADA
     *
     *
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
     * ENTREGA NAO SINCRONIZADA
     *
     *
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
     * ENTREGA NAO SINCRONIZADA
     *
     *
     * BUSCAR ENTREGUE NAO SINCRONIZADAS
     *
     * @return
     */
    public List<Encomenda> naoSincronizados(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .eq("flagEnviado", EnumStatusEnvio.NAO_SINCRONIZADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }




    /**
     * ENTREGA - NAO SINCRONIZADA
     *
     *
     * BUSCAR ENTREGUE NAO SINCRONIZADAS
     *
     * @return
     */
    public List<Encomenda> buscarEntregueNaoSincronizadas(){

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
     * OCORRENCIA
     *
     *
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
     * OCORRENCIA - NAO SINCRONIZADO
     *
     *
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
     * ENTREGUE - NAO SINCRONIZADO
     *
     *
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
     * ENCOMENDA - STATUS DA API
     *
     *
     * BUSCAR EM ROTA
     *
     * @return
     */
    public List<Encomenda> buscarEntregasEmRota(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .in("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey(), EnumEncomendaStatus.LIBERADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





    /**
     * ENCOMENDA - STATUS DA API
     *
     *
     *
     * COUNT EM ROTA
     *
     * @return
     */
    public int countEncomendasEmRotaAndLiberado(){

        List<Encomenda> result = null;

        try {

            result = encomendaDao.queryBuilder()
                    .where()
                    .in("IdStatus", EnumEncomendaStatus.EM_ROTA.getKey(), EnumEncomendaStatus.LIBERADO.getKey())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result != null ? result.size() : 0;
    }





    /**
     * MAPA - STATUS DA API
     *
     *
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
     * MAPA - ENCOMENDA POR NOME
     *
     *
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








    /**
     * ENCOMENDA - EM ANDAMENTO
     *
     *
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
     * ENCOMENDA - ATUALIZAR STATUS - EM ROTA
     *
     *
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
     * ENCOMENDA - ATUALIZAR STATUS - OCORRENCIA
     *
     *
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
     * ENCOMENDA - ATUALIZAR STATUS - ENTREGUE
     *
     *
     * ENTREGUE
     *
     * @return
     */
    public Encomenda encomendaEntregue(Encomenda encomenda){

        encomenda.setIdStatus(EnumEncomendaStatus.ENTREGUE.getKey());
        encomenda.setDescStatus(EnumEncomendaStatus.ENTREGUE.getValue());

        atualizarEncomenda(encomenda);

        return encomenda;
    }


}
