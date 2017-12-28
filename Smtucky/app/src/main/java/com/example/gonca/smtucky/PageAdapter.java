package com.example.gonca.smtucky;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ana_f on 26/12/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {
    final int Tab_num;
    private String Titles[] = new String[] { "Autocarros", "Avisos", "Favoritos" };
    private Context context;
    ActionBar.Tab Tab1 = null, Tab2 = null, Tab3 = null;

    public PageAdapter(FragmentManager fm, int num) {
        super(fm);
        this.Tab_num = num;

    }

    @Override
    public int getCount() {
        return Tab_num;
    }

    @Override
    public Fragment getItem(int position) {

        if (position==0){
            Log.d("MUAHAHAHAH ",""+position);
            return PageFragment.newInstance(1);

        }
        else if(position ==1){
            Log.d("LOL ",""+position);
            return FavoritesFragment.newInstance(2);
        }
        else if(position==2){
            Log.d("RI-ME",""+position);
            return WarningsFragment.newInstance(3);
        }
        return PageFragment.newInstance(1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}