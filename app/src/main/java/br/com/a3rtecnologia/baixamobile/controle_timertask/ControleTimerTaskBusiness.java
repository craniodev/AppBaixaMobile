package br.com.a3rtecnologia.baixamobile.controle_timertask;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.a3rtecnologia.baixamobile.EnumStatusEnvio;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EnumEncomendaStatus;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.orm.DatabaseHelper;

/**
 * Created by maclemon on 27/08/16.
 */
public class ControleTimerTaskBusiness {

    private Dao<ControleTimerTask, Integer> controleTimerTaskDao;
    private Context mContext;


    public ControleTimerTaskBusiness(Context mContext){

        this.mContext = mContext;
        getDao();
    }



    private void getDao(){

        DatabaseHelper helper  = new DatabaseHelper(mContext);

        try {

            controleTimerTaskDao = new CustomDao<ControleTimerTask, Integer>(helper.getConnectionSource(), ControleTimerTask.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("");
    }





    /**
     * SALVAR NOVA ENCOMENDA
     *
     * @param controleTimerTask
     */
    public void salvar(ControleTimerTask controleTimerTask){

        try {

            controleTimerTaskDao.create(controleTimerTask);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * atualizar NOVA ENCOMENDA
     *
     * @param controleTimerTask
     */
    public void update(ControleTimerTask controleTimerTask){

        try {

            controleTimerTaskDao.update(controleTimerTask);

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

            List<ControleTimerTask> controleTimerTaskList = controleTimerTaskDao.queryForAll();
            controleTimerTaskDao.delete(controleTimerTaskList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllFinalizarEntrega(){

        try {

            List<ControleTimerTask> controleTimerTaskList = verificarFinalizarEntregaList();
            controleTimerTaskDao.delete(controleTimerTaskList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ControleTimerTask verificarInicioViagem(int operacao){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .eq("idOperacao", operacao)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        int res = result.size();
        ControleTimerTask controleTimerTask = null;

        if(res > 0){

            controleTimerTask = result.get(res-1);
        }

        return controleTimerTask;
    }

    public ControleTimerTask verificarInicioEntrega(int operacao){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .in("idOperacao", operacao)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int res = result.size();
        ControleTimerTask controleTimerTask = null;

        if(res > 0){

            controleTimerTask = result.get(res-1);
        }

        return controleTimerTask;
    }

    public ControleTimerTask verificarFinalizarEntrega(int operacao){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .in("idOperacao", operacao)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int res = result.size();
        ControleTimerTask controleTimerTask = null;

        if(res > 0){

            controleTimerTask = result.get(res-1);
        }

        return controleTimerTask;
    }











    public Integer coutAll(){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.size();
    }

    public Integer verificarInicioViagem(){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .in("idOperacao", EnumOperacao.OP_INICIAR_VIAGEM.getValue())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.size();
    }

    public Integer verificarInicioEntrega(){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .in("idOperacao", EnumOperacao.OP_INICIAR_ENTREGA.getValue())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.size();
    }

    public Integer verificarFinalizarEntrega(){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .in("idOperacao", EnumOperacao.OP_FINALIZAR_ENTREGA.getValue())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.size();
    }

    public List<ControleTimerTask> verificarFinalizarEntregaList(){

        List<ControleTimerTask> result = null;

        try {

            result = controleTimerTaskDao.queryBuilder()
                    .where()
                    .in("idOperacao", EnumOperacao.OP_FINALIZAR_ENTREGA.getValue())
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}
