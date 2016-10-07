package br.com.a3rtecnologia.baixamobile.orm;

import android.content.Context;

import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
import br.com.a3rtecnologia.baixamobile.entrega.RecebedorBusiness;
import br.com.a3rtecnologia.baixamobile.iniciar_viagem.IniciarViagemBusiness;
import br.com.a3rtecnologia.baixamobile.ocorrencia.OcorrenciaBusiness;
import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;

/**
 * Created by maclemon on 26/09/16.
 */
public class TableUtil {

    private EncomendaBusiness encomendaBusiness;
    private RecebedorBusiness recebedorBusiness;
    private OcorrenciaBusiness ocorrenciaBusiness;
    private StatusBusiness statusBusiness;
    private IniciarViagemBusiness iniciarViagemBusiness;




    public TableUtil(Context mContext){

        encomendaBusiness = new EncomendaBusiness(mContext);
        recebedorBusiness = new RecebedorBusiness(mContext);
        ocorrenciaBusiness = new OcorrenciaBusiness(mContext);

        statusBusiness = new StatusBusiness(mContext);
        iniciarViagemBusiness = new IniciarViagemBusiness(mContext);
    }



    public void clearAllTables(){

        encomendaBusiness.deleteAll();
        recebedorBusiness.deleteAll();
        ocorrenciaBusiness.deleteAll();
        statusBusiness.deleteAll();
        iniciarViagemBusiness.deleteAll();
    }

}
