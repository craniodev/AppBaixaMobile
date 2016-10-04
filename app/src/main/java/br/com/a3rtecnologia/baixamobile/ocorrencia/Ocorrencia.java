package br.com.a3rtecnologia.baixamobile.ocorrencia;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.tipo_ocorrencia.TipoOcorrencia;

/**
 * Created by maclemon on 27/08/16.
 */
@DatabaseTable(tableName = "ocorrencia", daoClass = CustomDao.class)
public class Ocorrencia {

    @DatabaseField(generatedId = true, columnName = "idOcorrencia")
    private int Id;

    @DatabaseField(foreign = true, columnName = "idTipoOcorrencia", foreignAutoRefresh=true)
    private TipoOcorrencia tipoOcorrencia;

    @DatabaseField
    private String Descricao;

    @DatabaseField
    private String fotoOcorrenciaBase64;

    @DatabaseField
    private String fotoOcorrenciaPath;

    @DatabaseField
    private int FgExigeFoto;

    @DatabaseField
    private String DataFinalizacao;



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public TipoOcorrencia getTipoOcorrencia() {
        return tipoOcorrencia;
    }

    public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getFotoOcorrenciaBase64() {
        return fotoOcorrenciaBase64;
    }

    public void setFotoOcorrenciaBase64(String fotoOcorrenciaBase64) {
        this.fotoOcorrenciaBase64 = fotoOcorrenciaBase64;
    }

    public int getFgExigeFoto() {
        return FgExigeFoto;
    }

    public void setFgExigeFoto(int fgExigeFoto) {
        FgExigeFoto = fgExigeFoto;
    }

    public String getFotoOcorrenciaPath() {
        return fotoOcorrenciaPath;
    }

    public void setFotoOcorrenciaPath(String fotoOcorrenciaPath) {
        this.fotoOcorrenciaPath = fotoOcorrenciaPath;
    }

    public String getDataFinalizacao() {
        return DataFinalizacao;
    }

    public void setDataFinalizacao(String dataFinalizacao) {
        DataFinalizacao = dataFinalizacao;
    }
}
