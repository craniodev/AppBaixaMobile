//package br.com.a3rtecnologia.baixamobile.entrega;
//
//import android.app.Activity;
//import android.content.Context;
//
//import com.google.android.gms.maps.model.LatLng;
//
//import java.util.List;
//
//import br.com.a3rtecnologia.baixamobile.encomenda.Encomenda;
//import br.com.a3rtecnologia.baixamobile.encomenda.EncomendaBusiness;
//import br.com.a3rtecnologia.baixamobile.status.StatusBusiness;
//import br.com.a3rtecnologia.baixamobile.util.InternetStatus;
//
///**
// * Created by maclemon on 03/10/16.
// */
//
//public class SincronizarEncomenda {
//
//    private EncomendaBusiness encomendaBusiness;
//    private StatusBusiness statusBusiness;
//    private RecebedorBusiness recebedorBusiness;
//
//    private Context mContext;
//    private Activity mActivity;
//
//
//    public SincronizarEncomenda(Context mContext, Activity mActivity){
//
//        this.mContext = mContext;
//        this.mActivity = mActivity;
//
//        this.encomendaBusiness = new EncomendaBusiness(mContext);
//        this.statusBusiness = new StatusBusiness(mContext);
//        this.recebedorBusiness = new RecebedorBusiness(mContext);
//
//    }
//
//
//    private void sincronizar(){
//
//        List<Encomenda> encomendaList = encomendaBusiness.buscarOcorrenciasNaoSincronizadas();
//
//
//        if(InternetStatus.isNetworkAvailable(mContext)) {
//
//
//            for (Encomenda encomenda : encomendaList){
//
//                encomenda.getLatitude();
//                encomenda.getLongitude();
//
//                LatLng latLng = new LatLng(encomenda.getLatitude(), encomenda.getLongitude());
//
//                BaixaEntregueVolley baixaEntregueVolley = new BaixaEntregueVolley(mContext, encomenda, recebedor, latLng, new DelegateEntregaAsyncResponse() {
//
//                    @Override
//                    public void processFinish(boolean finish, String resposta) {
//
//                    }
//
//                    @Override
//                    public void processCanceled(boolean cancel) {
//
//                    }
//                };
//            }
//
//
//
//            BaixaEntregueVolley baixaEntregueVolley = new BaixaEntregueVolley(mContext, encomendaCorrente, recebedor, latLng, new DelegateEntregaAsyncResponse() {
//
//                @Override
//                public void processFinish(boolean finish, String resposta) {
//
//                    showProgress(false);
//
//
//                    /**
//                     * REMOVE FLAG COLOR
//                     */
//                    encomendaCorrente.setFlagTratado(false);
//
//                    /**
//                     * ATUALIZA STATUS ENCOMENDA
//                     */
//                    encomendaBusiness.encomendaEntregue(encomendaCorrente);
//
//                    /**
//                     * REMOVE ENCOMENDA CORRENTE
//                     */
//                    statusBusiness.removeEncomendaCorrente();
////                TabItemMapaFragment.isRemoveMarkerMap = true;
//
//                    /**
//                     * REMOVE MARKER
//                     */
////                TabItemMapaFragment.marker.remove();
////                TabItemMapaFragment.map.clear();
//
//
//                    /**
//                     * ENVIADO COM SUCESSO
//                     *
//                     * DELETE DO MODO OFFLINE
//                     */
//                    recebedorBusiness.getRecebedorList();
//                    recebedorBusiness.delete(recebedor);
//
//                    finish();
//                }
//
//                @Override
//                public void processCanceled(boolean cancel) {
//
//                    System.out.println("ERRO - BAIXA ENCOMENDA");
//
//                    showProgress(false);
////                    finish();
//                }
//            });
//
//        }else{
//
//            /**
//             * MODO OFFLINE
//             *
//             * SALVAR RECEBEDOR LOCAL
//             */
//            recebedor.setDataFinalizacao(encomendaCorrente.getDataBaixa());
//            recebedorBusiness.salvarRecebedor(recebedor);
//            recebedorBusiness.getRecebedorList();
//
//
//            /**
//             * REMOVE FLAG COLOR
//             */
//            encomendaCorrente.setFlagTratado(false);
//
//            /**
//             * ATUALIZA STATUS ENCOMENDA
//             */
//            encomendaBusiness.encomendaEntregue(encomendaCorrente);
//
//            /**
//             * REMOVE ENCOMENDA CORRENTE
//             */
//            statusBusiness.removeEncomendaCorrente();
//
//
//            /**
//             * START TIMER TASK
//             *
//             *
//             */
//
//
//
//
//            /**
//             * FINALIZAR
//             */
////            finish();
//
//
//        }
//
//    }
//
//}
