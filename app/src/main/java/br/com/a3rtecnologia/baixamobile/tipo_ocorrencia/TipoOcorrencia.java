package br.com.a3rtecnologia.baixamobile.tipo_ocorrencia;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;

/**
 * Created by maclemon on 27/08/16.
 */
@DatabaseTable(tableName = "tipoOcorrencia", daoClass = CustomDao.class)
public class TipoOcorrencia {

    /** ID INDEX MOBILE **/
    @DatabaseField(generatedId = true)
    private int IdMobile;

    /** ID WEB **/
    @DatabaseField
    private int Id;

    @DatabaseField
    private String Descricao;

    @DatabaseField
    private int FgExigeFoto;



    public int getIdMobile() {
        return IdMobile;
    }

    public void setIdMobile(int idMobile) {
        IdMobile = idMobile;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public int getFgExigeFoto() {
        return FgExigeFoto;
    }

    public void setFgExigeFoto(int fgExigeFoto) {
        FgExigeFoto = fgExigeFoto;
    }
}
