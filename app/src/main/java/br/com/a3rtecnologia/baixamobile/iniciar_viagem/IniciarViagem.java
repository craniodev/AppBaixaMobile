package br.com.a3rtecnologia.baixamobile.iniciar_viagem;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.a3rtecnologia.baixamobile.orm.CustomDao;

/**
 * Created by maclemon on 19/09/16.
 */
@DatabaseTable(tableName = "iniciarViagem", daoClass = CustomDao.class)
public class IniciarViagem {


    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String IdMotorista;

    @DatabaseField
    private Double Latitude;

    @DatabaseField
    private Double Longitude;

    @DatabaseField
    private String DataIteracao;

    @DatabaseField
    private int fgSincronizado;



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIdMotorista() {
        return IdMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        IdMotorista = idMotorista;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getDataIteracao() {
        return DataIteracao;
    }

    public void setDataIteracao(String dataIteracao) {
        DataIteracao = dataIteracao;
    }

    public int getFgSincronizado() {
        return fgSincronizado;
    }

    public void setFgSincronizado(int fgSincronizado) {
        this.fgSincronizado = fgSincronizado;
    }
}
