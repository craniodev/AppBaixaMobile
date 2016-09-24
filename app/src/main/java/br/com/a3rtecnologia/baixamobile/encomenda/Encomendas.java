package br.com.a3rtecnologia.baixamobile.encomenda;

import com.j256.ormlite.stmt.query.In;

import java.util.List;

/**
 * Created by maclemon on 01/08/16.
 */
public class Encomendas {

    private List<Encomenda> encomendas;

    private Integer idMotorista;

    private Long IdEncomenda;

    private String Resposta;

    private String StatusRetorno;



    public Long getIdEncomenda() {
        return IdEncomenda;
    }

    public void setIdEncomenda(Long idEncomenda) {
        IdEncomenda = idEncomenda;
    }

    public List<Encomenda> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
    }

    public String getResposta() {
        return Resposta;
    }

    public void setResposta(String resposta) {
        Resposta = resposta;
    }

    public String getStatusRetorno() {
        return StatusRetorno;
    }

    public void setStatusRetorno(String statusRetorno) {
        StatusRetorno = statusRetorno;
    }

    public Integer getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(Integer idMotorista) {
        this.idMotorista = idMotorista;
    }
}
