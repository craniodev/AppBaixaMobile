package br.com.a3rtecnologia.baixamobile.controle_timertask;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;

/**
 * Created by maclemon on 13/10/16.
 */

@DatabaseTable(tableName = "controleTimerTask", daoClass = CustomDao.class)
public class ControleTimerTask implements Serializable{

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private Integer idOperacao;

    @DatabaseField
    private Integer flagIniciado;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(Integer idOperacao) {
        this.idOperacao = idOperacao;
    }

    public Integer getFlagIniciado() {
        return flagIniciado;
    }

    public void setFlagIniciado(Integer flagIniciado) {
        this.flagIniciado = flagIniciado;
    }
}
