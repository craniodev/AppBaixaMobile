package br.com.a3rtecnologia.baixamobile.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import br.com.a3rtecnologia.baixamobile.menu.MenuDrawerActivity;

/**
 * Created by maclemon on 31/08/16.
 */
public class PermissionUtil {

    private static int REQUEST_CODE_ASK_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private static int REQUEST_CODE_ASK_PERMISSIONS_ACCESS_FINE_LOCATION = 2;
    private static int REQUEST_CODE_ASK_PERMISSIONS_INTERNET = 3;
    private static int REQUEST_CODE_ASK_PERMISSIONS_ACCESS_NETWORK_STATE = 4;
    private static int REQUEST_CODE_ASK_PERMISSIONS_ACCESS_COARSE_LOCATION = 5;







    public static void permissaoWRITE_EXTERNAL_STORAGE(Activity mAtivity) {

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mAtivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mAtivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                showMessageOKCancel("You need to allow access to Contacts",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(MenuDrawerActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//                            }
//                        });

                ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                return;
            }
            ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            return;
        }

//        insertDummyContact();
    }

//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }





    public static void permissaoACCESS_FINE_LOCATION(Activity mAtivity) {

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mAtivity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mAtivity, Manifest.permission.ACCESS_FINE_LOCATION)) {

//                showMessageOKCancel("You need to allow access to Contacts",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(MenuDrawerActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//                            }
//                        });

                ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS_ACCESS_FINE_LOCATION);

                return;
            }
            ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS_ACCESS_FINE_LOCATION);
            return;
        }

//        insertDummyContact();
    }






    public static void permissaoACCESS_COARSE_LOCATION(Activity mAtivity) {

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mAtivity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mAtivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

//                showMessageOKCancel("You need to allow access to Contacts",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(MenuDrawerActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//                            }
//                        });

                ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS_ACCESS_COARSE_LOCATION);

                return;
            }
            ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS_ACCESS_COARSE_LOCATION);
            return;
        }

//        insertDummyContact();
    }










    public static void permissaoACCESS_NETWORK_STATE(Activity mAtivity) {

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mAtivity, Manifest.permission.ACCESS_NETWORK_STATE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mAtivity, Manifest.permission.ACCESS_NETWORK_STATE)) {

//                showMessageOKCancel("You need to allow access to Contacts",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(MenuDrawerActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//                            }
//                        });

                ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_ASK_PERMISSIONS_INTERNET);

                return;
            }
            ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_ASK_PERMISSIONS_INTERNET);
            return;
        }

//        insertDummyContact();
    }




    public static void permissaoINTERNET(Activity mAtivity) {

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mAtivity, Manifest.permission.INTERNET);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mAtivity, Manifest.permission.INTERNET)) {

//                showMessageOKCancel("You need to allow access to Contacts",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(MenuDrawerActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//                            }
//                        });

                ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.INTERNET}, REQUEST_CODE_ASK_PERMISSIONS_ACCESS_NETWORK_STATE);

                return;
            }
            ActivityCompat.requestPermissions(mAtivity, new String[] {Manifest.permission.INTERNET}, REQUEST_CODE_ASK_PERMISSIONS_ACCESS_NETWORK_STATE);
            return;
        }

//        insertDummyContact();
    }

}
