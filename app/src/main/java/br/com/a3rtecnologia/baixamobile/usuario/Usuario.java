package br.com.a3rtecnologia.baixamobile.usuario;

import br.com.a3rtecnologia.baixamobile.util.GenericEntity;

/**
 * Created by maclemon on 27/07/16.
 */
public class Usuario {

    private int idContratante;
    private int Id;
    private String Login;
    private String Nome;
    private String cpf;
    private String cnh;
    private String fone1;
    private String fone2;
    private String email;
    private String password;

    private String Resposta;


    public int getIdContratante() {
        return idContratante;
    }

    public void setIdContratante(int idContratante) {
        this.idContratante = idContratante;
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResposta() {
        return Resposta;
    }

    public void setResposta(String resposta) {
        Resposta = resposta;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }
}
