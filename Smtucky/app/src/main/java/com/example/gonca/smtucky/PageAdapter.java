package com.example.gonca.smtucky;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ana_f on 26/12/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {
    final int Tab_num;
    private String Titles[] = new String[] { "Autocarros", "Avisos", "Favoritos" };
    private Context context;
    PageFragment tab1;
    FavoritesFragment tab2;
    WarningsFragment tab3;

    public PageAdapter(FragmentManager fm, int num) {
        super(fm);
        this.Tab_num = num;

    }


    @Override
    public int getCount() {
        //return POSITION_NONE;
        return Tab_num;
    }


    @Override
    public Fragment getItem(int position) {

        if (position==0){
            Log.d("MUAHAHAHAH ",""+position);
            tab1 = PageFragment.newInstance(1);

            return tab1;

        }
        else if(position ==1){
            Log.d("LOL ",""+position);
            tab2 = FavoritesFragment.newInstance(2);
            return tab2;
        }
        else if(position==2){
            Log.d("RI-ME",""+position);
            tab3 = WarningsFragment.newInstance(3);
            return tab3;
        }
        return PageFragment.newInstance(1);
    }

    public void refreshFragment(int position) {
        switch (position) {
            case 1:
                tab1.refreshApi();
                break;
            case 2:
                tab2.refreshApi();
                break;
            case 3:
                tab3.refreshApi();
                break;

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}