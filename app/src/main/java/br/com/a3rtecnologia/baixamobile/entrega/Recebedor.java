package br.com.a3rtecnologia.baixamobile.entrega;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.tipo_documento.TipoDocumento;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;

/**
 * Created by maclemon on 27/08/16.
 */
@DatabaseTable(tableName = "recebedor", daoClass = CustomDao.class)
public class Recebedor {

    @DatabaseField(generatedId = true, columnName = "idRecebedor")
    private int Id;

    @DatabaseField
    private String Nome;

    @DatabaseField(foreign = true, columnName = "idTipoRecebedor", foreignAutoRefresh=true)
    private TipoRecebedor tipoRecebedor;

    @DatabaseField(foreign = true, columnName = "idTipoDocumento", foreignAutoRefresh=true)
    private TipoDocumento tipoDocumento;

    @DatabaseField
    private String nrDocumento;

    @DatabaseField
    private String fotoAssinaturaDigitalBase64;

    @DatabaseField
    private String fotoComprovanteBase64;

    @DatabaseField
    private String fotoAssinaturaDigitalPath;

    @DatabaseField
    private String fotoComprovantePath;

    @DatabaseField
    private String DataFinalizacao;



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public TipoRecebedor getTipoRecebedor() {
        return tipoRecebedor;
    }

    public void setTipoRecebedor(TipoRecebedor tipoRecebedor) {
        this.tipoRecebedor = tipoRecebedor;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public String getFotoAssinaturaDigitalBase64() {
        return fotoAssinaturaDigitalBase64;
    }

    public void setFotoAssinaturaDigitalBase64(String fotoAssinaturaDigitalBase64) {
        this.fotoAssinaturaDigitalBase64 = fotoAssinaturaDigitalBase64;
    }

    public String getFotoComprovanteBase64() {
        return fotoComprovanteBase64;
    }

    public void setFotoComprovanteBase64(String fotoComprovanteBase64) {
        this.fotoComprovanteBase64 = fotoComprovanteBase64;
    }

    public String getFotoAssinaturaDigitalPath() {
        return fotoAssinaturaDigitalPath;
    }

    public void setFotoAssinaturaDigitalPath(String fotoAssinaturaDigitalPath) {
        this.fotoAssinaturaDigitalPath = fotoAssinaturaDigitalPath;
    }

    public String getFotoComprovantePath() {
        return fotoComprovantePath;
    }

    public void setFotoComprovantePath(String fotoComprovantePath) {
        this.fotoComprovantePath = fotoComprovantePath;
    }

    public String getDataFinalizacao() {
        return DataFinalizacao;
    }

    public void setDataFinalizacao(String dataFinalizacao) {
        DataFinalizacao = dataFinalizacao;
    }

}
