package br.com.a3rtecnologia.baixamobile.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import br.com.a3rtecnologia.baixamobile.R;

/**
 * Created by maclemon on 28/08/16.
 */
public class ActivityUtil {




    public static void toolbar(Toolbar toolbar, String title, AppCompatActivity mActivity){

//        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        toolbar.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimaryDark));
    }



    public static void actionBarColor(AppCompatActivity mActivity){

        Window window = mActivity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(mActivity.getResources().getColor(R.color.topo));
        }
    }

}
