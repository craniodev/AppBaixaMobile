package br.com.a3rtecnologia.baixamobile.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.a3rtecnologia.baixamobile.tab_lista.TabItemListaFragment;
import br.com.a3rtecnologia.baixamobile.tab_mapa.TabItemMapaFragment;

/**
 * Created by maclemon on 30/07/16.
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private TabItemListaFragment tabLista;
    private TabItemMapaFragment tabMap;


    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 :

                tabLista = new TabItemListaFragment();

                return tabLista;

            case 1 :

                tabMap = new TabItemMapaFragment();

                return tabMap;
        }

        return null;
    }

    @Override
    public int getCount() {

        return 2;
    }


    /**
     * This method returns the title of the tab according to the position.
     */
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0 :
                return "Lista";
            case 1 :
                return "Mapa";
        }

        return null;
    }

    public TabItemListaFragment getTabLista() {
        return tabLista;
    }

    public TabItemMapaFragment getTabMap() {
        return tabMap;
    }

    public void setTabMap(TabItemMapaFragment tabMap) {
        this.tabMap = tabMap;
    }
}
