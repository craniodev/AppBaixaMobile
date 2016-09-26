package br.com.a3rtecnologia.baixamobile.orm;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.Recebedor;
import br.com.a3rtecnologia.baixamobile.entrega.RecebedorBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagem;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemBusiness;
import br.com.a3rtecnologia.baixamobile.ocorrencia.Ocorrencia;
import br.com.a3rtecnologia.baixamobile.sincronizacao.Atualizacao;
import br.com.a3rtecnologia.baixamobile.status.Status;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;

/**
 * Created by maclemon on 26/09/16.
 */
public class TableUtil {

    private EncomendaBusiness encomendaBusiness;
//    private Dao<Atualizacao, Integer> mAtualizacaoDao = null;
    private RecebedorBusiness recebedorBusiness;
//    private Dao<TipoRecebedor, Integer> mTipoRecebedorDao = null;
//    private Dao<TipoDocumento, Integer> mTipoDocumentoDao = null;
//    private Dao<TipoOcorrencia, Integer> mTipoOcorrenciaDao = null;
    private Dao<Ocorrencia, Integer> mOcorrenciaDao = null;
    private StatusBusiness statusBusiness;
    private IniciarViagemBusiness iniciarViagemBusiness;



    public TableUtil(Context mContext){

        encomendaBusiness = new EncomendaBusiness(mContext);
        recebedorBusiness = new RecebedorBusiness(mContext);
        statusBusiness = new StatusBusiness(mContext);
        iniciarViagemBusiness = new IniciarViagemBusiness(mContext);
    }


    public void clearAllTables(){

        encomendaBusiness.deleteAll();
        recebedorBusiness.deleteAll();
        statusBusiness.deleteAll();
        iniciarViagemBusiness.deleteAll();
    }


}
