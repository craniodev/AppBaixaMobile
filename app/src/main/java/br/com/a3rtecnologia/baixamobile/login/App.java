package br.com.a3rtecnologia.baixamobile.login;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by maclemon on 30/08/16.
 */
public class App extends Application{


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
