package com.joshuahugh.cent2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuahugh on 06/03/15.
 */
public class FragmentManagerr extends FragmentPagerAdapter {

    private List<AbsFrag> fragments;


    public FragmentManagerr(FragmentManager fm, int time) {
        super(fm);
        this.fragments = new ArrayList<AbsFrag>();

        MainFragment mf = new MainFragment();


        Bundle bundle = new Bundle();
        bundle.putInt("time", time);

        mf.setArguments(bundle);

        fragments.add(mf);
        fragments.add(new MissedShotsFragment());

    }

    @Override
    public AbsFrag getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getName();
    }
}
