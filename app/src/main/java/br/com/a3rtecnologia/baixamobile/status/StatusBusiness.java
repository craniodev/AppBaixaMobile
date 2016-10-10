package br.com.a3rtecnologia.baixamobile.status;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.entrega.Recebedor;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;
import br.com.a3rtecnologia.baixamobile.util.DateUtil;

/**
 * Created by maclemon on 27/08/16.
 */
public class StatusBusiness {

    private Dao<Status, Integer> statusDao;
    private Context mContext;



    public StatusBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }



    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            statusDao = new CustomDao<Status, Integer>(helper.getConnectionSource(), Status.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }




    public void salvar(Status status){

        try {

            delete();
            statusDao.create(status);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(){

//        try {

            deleteAll();
            //statusDao.deleteById(1);

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * DELETE ALL
     *
     */
    public void deleteAll(){

        try {

            List<Status> statusList = statusDao.queryForAll();
            statusDao.delete(statusList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(Status status){

        try {

            statusDao.update(status);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    /**
     * VERIFICA SE VIAGEM INICIADA
     *
     * @return
     */
    public Status getStatus(){

        List<Status> result = null;

        try {

            result = statusDao.queryBuilder()
                    .where()
                    .eq("viagemIniciada", true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Status statusRetorno = null;
        if(result != null && result.size() > 0) {

            statusRetorno = result.get(result.size() -1);
        }

        return statusRetorno;
    }





    /**
     * RECUPERA DATA DE INICIO VIAGEM
     *
     * @return
     */
    public String getDataInicioViagem(){

        List<Status> result = null;

        try {

            result = statusDao.queryBuilder()
                    .where()
                    .eq("viagemIniciada", true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Status statusRetorno = null;
        if(result != null && result.size() > 0) {

            statusRetorno = result.get(result.size() -1);
        }

        return statusRetorno.getDataInicioViagem();
    }





    /**
     * INICIAR JORNADA DE TRABALHO
     */
    public void startJornadaTrabalho(){

        Status status = new Status();
        status.setId(1);
        status.setViagemIniciada(true);

        String data = DateUtil.getDataAtual();
        status.setDataInicioViagem(data);

        salvar(status);
    }




    /**
     * VERIFICA VIAGEM INICIADA
     * JORNADA DE TRABALHO JÁ INICIADA
     */
    public void startJornadaTrabalho(String dataIniciada){

        Status status = new Status();
        status.setId(1);
        status.setViagemIniciada(true);

        String data = DateUtil.getDataAtual();
        status.setDataInicioViagem(data);

        salvar(status);
    }





    /**
     * ENCERRAR JORNADA DE TRABALHO
     */
    public void stopJornadaTrabalho(){

        delete();
    }




    /**
     * ADICIONAR ENCOMENDA CORRENTE
     */
    public void addEncomendaCorrente(long idEncomenda){

        Status status = getStatus();

//        Status status = new Status();
        status.setId(1);
        status.setViagemIniciada(true);
        status.setIdEncomendaCorrente(idEncomenda);

        salvar(status);
    }




    /**
     * ADICIONAR ENCOMENDA CORRENTE
     */
    public void removeEncomendaCorrente(){

        Status status = getStatus();

//        Status status = new Status();
        status.setId(1);
        status.setViagemIniciada(true);
        status.setIdEncomendaCorrente(0);

        salvar(status);
    }




    /**
     * ADICIONAR ENCOMENDA CORRENTE
     */
    public boolean existeEncomendaEmAndamento(){

        if(getStatus() != null){

            Long idEncomendaCorrente = getStatus().getIdEncomendaCorrente();

            if(idEncomendaCorrente != null && idEncomendaCorrente > 0){

                return true;
            }
        }

        return false;
    }




    /**
     * GET ID ENCOMENDA CORRENTE
     */
    public long getIdEncomendaCorrente(){

        Status statusRetorno = null;

        if(getStatus() != null){

            statusRetorno = getStatus();

            return statusRetorno.getIdEncomendaCorrente();
        }

        return 0;
    }




    /**
     * VERIFICA SE JA FOI DADO O START NA JORNADA DE TRABALHO
     *
     * @return
     */
    public boolean verificarViagemIniciada(){

        List<Status> statusList = null;

        try {
            statusList = statusDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Status statusRetorno = null;
        if(statusList != null && statusList.size() > 0) {

            statusRetorno = statusList.get(statusList.size() -1);
            return statusRetorno.isViagemIniciada();
        }

        return false;
    }





    /**
     *
     *
     * @return
     */
    public boolean isNull(){

        List<Status> statusList = null;

        try {
            statusList = statusDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(statusList != null) {

            return false;
        }

        return true;
    }

}
