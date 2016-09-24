package br.com.a3rtecnologia.baixamobile.ocorrencia;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumOcorrencia {

    AUSENTE(4, "Destinatário Ausente", 0);



    private long id;
    private String descricao;
    private int fgExigeFoto;

    private EnumOcorrencia(long id, String descricao, int fgExigeFoto){

        this.id = id;
        this.descricao = descricao;
        this.fgExigeFoto = fgExigeFoto;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getFgExigeFoto() {
        return fgExigeFoto;
    }

    public void setFgExigeFoto(int fgExigeFoto) {
        this.fgExigeFoto = fgExigeFoto;
    }
}
