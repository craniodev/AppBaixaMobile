package br.com.a3rtecnologia.baixamobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.content.FileProvider;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by maclemon on 29/08/16.
 */
public class ImagemUtil {



    public static void showPhoto(Activity mActivity, Uri photoUri){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        mActivity.startActivity(intent);
    }




    public static Bitmap getbitpam(String path){

        Bitmap imgthumBitmap=null;

        try {

            final int THUMBNAIL_WIDTH = 800;
            final int THUMBNAIL_HEIGHT = 600;

            FileInputStream fis = new FileInputStream(path);
            imgthumBitmap = BitmapFactory.decodeStream(fis);

            imgthumBitmap = Bitmap.createScaledBitmap(imgthumBitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, false);

            ByteArrayOutputStream bytearroutstream = new ByteArrayOutputStream();
            imgthumBitmap.compress(Bitmap.CompressFormat.JPEG, 100,bytearroutstream);

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        return imgthumBitmap;
    }


    public static File createImageFile(Activity mActivity, String prefix) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "baixa_mobile_" + prefix + "_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        return image;
    }

    public static Uri dispatchTakePictureIntent(Activity mActivity, String prefix, int REQUEST) {

        Uri photoURI = null;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {

                photoFile = createImageFile(mActivity, prefix);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {

                photoURI = FileProvider.getUriForFile(mActivity, "br.com.a3rtecnologia.baixamobile.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);





                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                else {
                    List<ResolveInfo> resInfoList = mActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        mActivity.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                }





                    mActivity.startActivityForResult(intent, REQUEST);
            }
        }

        return photoURI;
    }




    public static String getEncoded64ImageStringFromBitmap(Context mContext, Bitmap bitmap) {

        String imgString = "";

        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteFormat = stream.toByteArray();

            // get the base 64 string
            imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        }catch (OutOfMemoryError e){

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();

            // get the base 64 string
            imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        }

        return imgString;
    }




    public static Uri getUri(Activity mActivity, File photoFile){

        Uri photoURI = FileProvider.getUriForFile(mActivity, "br.com.a3rtecnologia.baixamobile.fileprovider", photoFile);

        return photoURI;
    }

}
