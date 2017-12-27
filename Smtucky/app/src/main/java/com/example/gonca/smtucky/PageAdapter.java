package com.example.gonca.smtucky;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by ana_f on 26/12/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {
    final int Tab_num = 3;
    private String Titles[] = new String[] { "Autocarros", "Avisos", "Favoritos" };
    private Context context;

    public PageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return Tab_num;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            Toast.makeText(context, "WE ARE AT POSITION "+position, Toast.LENGTH_SHORT).show();
            return PageFragment.newInstance(1);

        }
        else if(position ==1){
            Toast.makeText(context, "WE ARE AT POSITION "+position, Toast.LENGTH_SHORT).show();

            return new FavoritesFragment();
        }
        else if(position==2){
            Toast.makeText(context, "WE ARE AT POSITION "+position, Toast.LENGTH_SHORT).show();
            return new WarningsFragment();
        }
        return PageFragment.newInstance(1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}