package br.com.a3rtecnologia.baixamobile.sincronizacao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;

/**
 * Created by maclemon on 26/08/16.
 */
@DatabaseTable(tableName = "atualizacao", daoClass = CustomDao.class)
public class Atualizacao implements Serializable{



    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String dataUltimaAtualizacao;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(String dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}
