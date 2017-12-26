package com.example.gonca.smtucky;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ana_f on 26/12/2017.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(FragmentManager f) {
        super(f);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment n = Tab.newInstance(position+1);
        return n;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==1){
            return "Favoritos";
        }
        else if(position==2){
            return "Avisos";
        }
        else{
            return "Autocarros";
        }

    }
}
