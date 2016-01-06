package com.betomaluje.android.allytest.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by betomaluje on 1/6/16.
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;

    public FragmentViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
