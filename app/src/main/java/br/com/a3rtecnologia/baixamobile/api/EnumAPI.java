package br.com.a3rtecnologia.baixamobile.api;

/**
 * Created by maclemon on 28/07/16.
 */
public enum EnumAPI {

    ID_CONTRATANTE("3"),
    ID_TIPO_ENCOMENDA_EM_ROTA("1"),
    ID_TIPO_ENCOMENDA_ENTREGUE("2"),
    ID_TIPO_ENCOMENDA_PENDENTE("3"),

    HEADER_PARAM_1("Api"),
    HEADER_PARAM_2("fernandoApi"),


    LOGIN("http://baixamobileapi.azurewebsites.net/api/Login"),
    VERIFICA_VIAGEM_INICIADA("http://baixamobileapi.azurewebsites.net/api/VerificaViagemIniciada"),
    CADASTRO("http://baixamobileapi.azurewebsites.net/api/Motorista"),
    RECUPERAR("http://baixamobileapi.azurewebsites.net/api/ResetarSenha"),

    LISTA_ENCOMENDA("http://baixamobileapi.azurewebsites.net/api/EncomendaMotorista"),//BUSCA TODAS ENCOMENDAS



    INICIA_VIAGEM("http://baixamobileapi.azurewebsites.net/api/IniciarViagem"),//INICIAR JORNADA DE TRABALHO
    FINALIZA_VIAGEM("http://baixamobileapi.azurewebsites.net/api/FinalizarViagem"),//FINALIZA JORNADA DE TRABALHO
    FINALIZA_VIAGEM_OCORRENCIA("http://baixamobileapi.azurewebsites.net/api/FinalizarViagemOcorrencia"),//FINALIZA JORNADA DE TRABALHO FORCADA



    ENTREGA("http://baixamobileapi.azurewebsites.net/api/IniciarEntrega"),//INICIAR UMA ENTREGA INDIVIDUAL

    LISTA_OCORRENCIA("http://baixamobileapi.azurewebsites.net/api/ListarOcorrencia"),//TIPO DE OCORRENCIA
    LISTA_RECEBEDOR("http://baixamobileapi.azurewebsites.net/api/ListarRecebedor"),//TIPO DE RECEBEDORE
    LISTA_DOCUMENTO("http://baixamobileapi.azurewebsites.net/api/ListarDocumento"),//TIPO DE DOCUMENTO

    BAIXA_ENTREGUE("http://baixamobileapi.azurewebsites.net/api/BaixaEntregue"),//DAR BAIXA EM UMA ENTREGA
    BAIXA_OCORRENCIA("http://baixamobileapi.azurewebsites.net/api/BaixaOcorrencia"),//DAR BAIXA EM UMA OCORRENCIA

    ATUALIZA_ENCOMENDA_PENDENTE("http://baixamobileapi.azurewebsites.net/api/AtualizaEncomendaPendente");//DAR BAIXA EM UMA OCORRENCIA







    private String value;

    private EnumAPI(String value){

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
