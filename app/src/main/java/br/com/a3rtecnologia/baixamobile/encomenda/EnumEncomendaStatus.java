package br.com.a3rtecnologia.baixamobile.encomenda;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumEncomendaStatus {

    EM_ROTA(5, "Em Rota"),
    LIBERADO(6, "Liberado"),
    ENTREGUE(7, "Entregue"),
    OCORRENCIA(8, "Ocorrência");



    private long key;
    private String value;

    private EnumEncomendaStatus(long key, String value){

        this.key = key;
        this.value = value;
    }



    public long getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
