package br.com.a3rtecnologia.baixamobile.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.a3rtecnologia.baixamobile.controle_timertask.ControleTimerTask;
import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.entrega.Recebedor;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagem;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;
import br.com.a3rtecnologia.baixamobile.ocorrencia.Ocorrencia;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "ormlite.db";
    private static final int DATABASE_VERSION = 68;

    private Dao<Encomenda, Integer> mEncomendaDao = null;
    private Dao<Recebedor, Integer> mRecebedorDao = null;
    private Dao<TipoRecebedor, Integer> mTipoRecebedorDao = null;
    private Dao<TipoDocumento, Integer> mTipoDocumentoDao = null;
    private Dao<TipoOcorrencia, Integer> mTipoOcorrenciaDao = null;
    private Dao<Ocorrencia, Integer> mOcorrenciaDao = null;
    private Dao<Status, Integer> mStatusDao = null;
    private Dao<IniciarViagem, Integer> mIniciarViagemDao = null;
    private Dao<ControleTimerTask, Integer> mControleTimerTaskDao = null;

    private RuntimeExceptionDao<Encomenda, ?> m;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Encomenda.class);
            TableUtils.createTable(connectionSource, Recebedor.class);
            TableUtils.createTable(connectionSource, TipoRecebedor.class);
            TableUtils.createTable(connectionSource, TipoDocumento.class);
            TableUtils.createTable(connectionSource, TipoOcorrencia.class);
            TableUtils.createTable(connectionSource, Ocorrencia.class);
            TableUtils.createTable(connectionSource, Status.class);
            TableUtils.createTable(connectionSource, IniciarViagem.class);
            TableUtils.createTable(connectionSource, ControleTimerTask.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Encomenda.class, true);
            TableUtils.dropTable(connectionSource, Recebedor.class, true);
            TableUtils.dropTable(connectionSource, TipoRecebedor.class, true);
            TableUtils.dropTable(connectionSource, TipoDocumento.class, true);
            TableUtils.dropTable(connectionSource, TipoOcorrencia.class, true);
            TableUtils.dropTable(connectionSource, Ocorrencia.class, true);
            TableUtils.dropTable(connectionSource, Status.class, true);
            TableUtils.dropTable(connectionSource, IniciarViagem.class, true);
            TableUtils.dropTable(connectionSource, ControleTimerTask.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    /* Encomenda */
    public Dao<Encomenda, Integer> getEncomendaDao() throws SQLException {

        if (mEncomendaDao == null) {
            mEncomendaDao = getDao(Encomenda.class);
        }

        m = getRuntimeExceptionDao(Encomenda.class);

        return mEncomendaDao;
    }

    /* Recebedor */
    public Dao<Recebedor, Integer> getRecebedorDao() throws SQLException {

        if (mRecebedorDao == null) {
            mRecebedorDao = getDao(Recebedor.class);
        }

        return mRecebedorDao;
    }


    /* TipoDocumento */
    public Dao<TipoDocumento, Integer> getTipoDocumentoDao() throws SQLException {

        if (mTipoDocumentoDao == null) {
            mTipoDocumentoDao = getDao(TipoDocumento.class);
        }

        return mTipoDocumentoDao;
    }

    /* TipoDocumento */
    public Dao<TipoRecebedor, Integer> getTipoRecebedorDao() throws SQLException {

        if (mTipoRecebedorDao == null) {
            mTipoRecebedorDao = getDao(TipoRecebedor.class);
        }

        return mTipoRecebedorDao;
    }

    /* TipoOcorrencia */
    public Dao<TipoOcorrencia, Integer> getTipoOcorrenciaDao() throws SQLException {

        if (mTipoOcorrenciaDao == null) {
            mTipoOcorrenciaDao = getDao(TipoOcorrencia.class);
        }

        return mTipoOcorrenciaDao;
    }

    /* Ocorrencia */
    public Dao<Ocorrencia, Integer> getOcorrenciaDao() throws SQLException {

        if (mOcorrenciaDao == null) {
            mOcorrenciaDao = getDao(Ocorrencia.class);
        }

        return mOcorrenciaDao;
    }

    /* Status */
    public Dao<Status, Integer> getStatusDao() throws SQLException {

        if (mStatusDao == null) {
            mStatusDao = getDao(Status.class);
        }

        return mStatusDao;
    }

    /* IniciarViagem */
    public Dao<IniciarViagem, Integer> getIniciarViagemDao() throws SQLException {

        if (mIniciarViagemDao == null) {
            mIniciarViagemDao = getDao(IniciarViagem.class);
        }

        return mIniciarViagemDao;
    }

    /* ControleTimerTask */
    public Dao<ControleTimerTask, Integer> getControleTimerTaskDao() throws SQLException {

        if (mControleTimerTaskDao == null) {
            mControleTimerTaskDao = getDao(ControleTimerTask.class);
        }

        return mControleTimerTaskDao;
    }



    @Override
    public void close() {
        mEncomendaDao = null;
        mRecebedorDao = null;
        mTipoRecebedorDao = null;
        mTipoDocumentoDao = null;
        mTipoOcorrenciaDao = null;
        mOcorrenciaDao = null;
        mIniciarViagemDao = null;
        mControleTimerTaskDao = null;

        super.close();
    }



    private static DatabaseHelper sDatabaseHelper;

//    public static DatabaseHelper getInstance(Context context) {
//        if (sDatabaseHelper == null) {
//            sDatabaseHelper = new DatabaseHelper(context.getApplicationContext());
//        }
//
//        return sDatabaseHelper;
//    }



    public static DatabaseHelper getInstance() {
        return sDatabaseHelper;
    }
}