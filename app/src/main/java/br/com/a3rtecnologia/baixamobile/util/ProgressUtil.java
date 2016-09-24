//package br.com.a3rtecnologia.baixamobile.util;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//
///**
// * Created by maclemon on 12/08/16.
// */
//public class ProgressUtil {
//
//    private Context mContext;
//    private ProgressDialog progressDialog;
//
//    public ProgressUtil(Context mContext, ProgressDialog progressDialog){
//
//        this.mContext = mContext;
//        this.progressDialog = progressDialog;
//
//        this.progressDialog = new ProgressDialog(mContext);
//        this.progressDialog.setCanceledOnTouchOutside(false);
//        this.progressDialog.setMessage("Por favor, aguarde");
//    }
//
//
//
//    private void createLoading(Context mContext){
//
//
//    }
//
//    public void showProgress(boolean isShow){
//
//        if(this.progressDialog != null && isShow){
//
//            this.progressDialog.show();
//
//        }else if(this.progressDialog != null && !isShow){
//
//            this.progressDialog.hide();
//        }
//    }
//
//}
