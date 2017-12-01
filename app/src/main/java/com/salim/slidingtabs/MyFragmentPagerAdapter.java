package com.salim.slidingtabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    // Holds tab titles
    private String tabTitles[] = new String[] {"Frog #1", "Frog #2", "Frog #3"};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    // Return the correct Fragment based on index
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TabFragment1();
        } else if (position == 1) {
            return new TabFragment2();
        } else if (position == 2) {
            return new TabFragment3();
        }
        return null;
    }



    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the tab title to SlidingTabLayout
        return tabTitles[position];
    }
}
