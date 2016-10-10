package br.com.a3rtecnologia.baixamobile.status;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;

/**
 * Created by maclemon on 04/09/16.
 */
@DatabaseTable(tableName = "status", daoClass = CustomDao.class)
public class Status {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private boolean viagemIniciada;

    @DatabaseField
    private long idEncomendaCorrente;

    @DatabaseField
    private String dataInicioViagem;

    @DatabaseField
    private String dataFimViagem;

//    @DatabaseField(defaultValue = "0")
//    private Integer flagEnviado;



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isViagemIniciada() {
        return viagemIniciada;
    }

    public void setViagemIniciada(boolean viagemIniciada) {
        this.viagemIniciada = viagemIniciada;
    }

    public long getIdEncomendaCorrente() {
        return idEncomendaCorrente;
    }

    public void setIdEncomendaCorrente(long idEncomendaCorrente) {
        this.idEncomendaCorrente = idEncomendaCorrente;
    }

    public String getDataInicioViagem() {
        return dataInicioViagem;
    }

    public void setDataInicioViagem(String dataInicioViagem) {
        this.dataInicioViagem = dataInicioViagem;
    }

    public String getDataFimViagem() {
        return dataFimViagem;
    }

    public void setDataFimViagem(String dataFimViagem) {
        this.dataFimViagem = dataFimViagem;
    }

//    public Integer getFlagEnviado() {
//        return flagEnviado;
//    }
//
//    public void setFlagEnviado(Integer flagEnviado) {
//        this.flagEnviado = flagEnviado;
//    }
}
