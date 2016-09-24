package br.com.a3rtecnologia.baixamobile.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.a3rtecnologia.baixamobile.R;

/**
 * Created by maclemon on 01/08/16.
 */
public class ContaFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *Inflate fragment_tab and setup Views.
         */
        View x = inflater.inflate(R.layout.fragment_account, null);


        return x;
    }
}
