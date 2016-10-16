package br.com.a3rtecnologia.baixamobile.controle_timertask;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumOperacao {

    OP_INICIAR_VIAGEM(1),
    OP_INICIAR_ENTREGA(2),
    OP_FINALIZAR_ENTREGA(3);



    private Integer value;

    private EnumOperacao(Integer value){

        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
