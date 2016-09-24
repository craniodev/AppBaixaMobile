package br.com.a3rtecnologia.baixamobile.tab_mapa;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by maclemon on 19/08/16.
 */
public class AddMarker implements Runnable {

    private GoogleMap map;
    private MarkerOptions options;

    public AddMarker(GoogleMap map, MarkerOptions options) {
        this.map = map;
        this.options = options;
    }

    @Override
    public void run() {
        map.addMarker(options);
    }

}

