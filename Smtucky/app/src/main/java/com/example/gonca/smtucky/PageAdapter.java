package com.example.gonca.smtucky;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}