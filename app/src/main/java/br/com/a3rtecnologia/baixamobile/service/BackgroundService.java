//package br.com.a3rtecnologia.baixamobile.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.widget.Toast;
//
///**
// * Created by maclemon on 02/08/16.
// */
//public class BackgroundService extends Service {
//
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//
//        return null;
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//
//    @Override
//    public void onDestroy() {
//
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//
//        super.onDestroy();
//    }
//}