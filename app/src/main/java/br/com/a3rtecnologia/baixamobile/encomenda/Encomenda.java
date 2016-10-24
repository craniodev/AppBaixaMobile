package br.com.a3rtecnologia.baixamobile.encomenda;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.a3rtecnologia.baixamobile.entrega.Recebedor;
import br.com.a3rtecnologia.baixamobile.ocorrencia.Ocorrencia;
import br.com.a3rtecnologia.baixamobile.orm.CustomDao;
import br.com.a3rtecnologia.baixamobile.tipo_recebedor.TipoRecebedor;

/**
 * Created by maclemon on 01/08/16.
 */

@DatabaseTable(tableName = "encomenda", daoClass = CustomDao.class)
public class Encomenda implements Serializable{

    @DatabaseField(generatedId = true)
    private Integer id;

    /** SOMENTE VISUAL **/
    @DatabaseField
    private Integer idOrdem;

    @DatabaseField(foreign = true, columnName = "idOcorrencia", foreignAutoRefresh=true)
    private Ocorrencia ocorrencia;

    @DatabaseField(foreign = true, columnName = "idRecebedor", foreignAutoRefresh=true)
    private Recebedor recebedor;

    @DatabaseField
    private String dataInicioEntrega;

    @DatabaseField
    private String dataBaixa;

    @DatabaseField
    private String location;

    @DatabaseField
    private Double latitude;

    @DatabaseField
    private Double longitude;

    @DatabaseField
    private Long IdDocumentoOperacional;

    @DatabaseField
    private String NrRomaneio;

    @DatabaseField
    private Long IdEncomenda;

    @DatabaseField
    private String NmDestinatario;

    @DatabaseField
    private String Endereco;

    @DatabaseField
    private String NrEndereco;

    @DatabaseField
    private String Bairro;

    @DatabaseField
    private String Cidade;

    @DatabaseField
    private String UF;

    @DatabaseField
    private String Cep;

    @DatabaseField
    private String NrNota;

    @DatabaseField
    private String NrPedido;

    @DatabaseField
    private Long IdStatus;

    @DatabaseField
    private String DescStatus;

    @DatabaseField
    private Integer flagEnviado;

    @DatabaseField
    private Boolean flagTratado;

    @DatabaseField
    private int flagInicioEntrega;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recebedor getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(Recebedor recebedor) {
        this.recebedor = recebedor;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public Long getIdDocumentoOperacional() {
        return IdDocumentoOperacional;
    }

    public void setIdDocumentoOperacional(Long idDocumentoOperacional) {
        IdDocumentoOperacional = idDocumentoOperacional;
    }

    public String getNrRomaneio() {
        return NrRomaneio;
    }

    public void setNrRomaneio(String nrRomaneio) {
        NrRomaneio = nrRomaneio;
    }

    public Long getIdEncomenda() {
        return IdEncomenda;
    }

    public void setIdEncomenda(Long idEncomenda) {
        IdEncomenda = idEncomenda;
    }

    public String getNmDestinatario() {
        return NmDestinatario;
    }

    public void setNmDestinatario(String nmDestinatario) {
        NmDestinatario = nmDestinatario;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getNrEndereco() {
        return NrEndereco;
    }

    public void setNrEndereco(String nrEndereco) {
        NrEndereco = nrEndereco;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
    }

    public String getCep() {
        return Cep;
    }

    public void setCep(String cep) {
        Cep = cep;
    }

    public String getNrNota() {
        return NrNota;
    }

    public void setNrNota(String nrNota) {
        NrNota = nrNota;
    }

    public String getNrPedido() {
        return NrPedido;
    }

    public void setNrPedido(String nrPedido) {
        NrPedido = nrPedido;
    }

    public Long getIdStatus() {
        return IdStatus;
    }

    public void setIdStatus(Long idStatus) {
        IdStatus = idStatus;
    }

    public String getDescStatus() {
        return DescStatus;
    }

    public void setDescStatus(String descStatus) {
        DescStatus = descStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getFlagEnviado() {
        return flagEnviado;
    }

    public void setFlagEnviado(int flagEnviado) {
        this.flagEnviado = flagEnviado;
    }

    public int getIdOrdem() {
        return idOrdem;
    }

    public void setIdOrdem(int idOrdem) {
        this.idOrdem = idOrdem;
    }

    public String getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(String dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public String getDataInicioEntrega() {
        return dataInicioEntrega;
    }

    public void setDataInicioEntrega(String dataInicioEntrega) {
        this.dataInicioEntrega = dataInicioEntrega;
    }

    public Boolean getFlagTratado() {
        return flagTratado;
    }

    public void setFlagTratado(Boolean flagTratado) {
        this.flagTratado = flagTratado;
    }

    public int getFlagInicioEntrega() {
        return flagInicioEntrega;
    }

    public void setFlagInicioEntrega(int flagInicioEntrega) {
        this.flagInicioEntrega = flagInicioEntrega;
    }
}
