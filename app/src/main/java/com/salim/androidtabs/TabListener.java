package com.salim.androidtabs;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

// The TabListener is a callback interface which is invoked when a tab action occurs
public class TabListener implements ActionBar.TabListener {

    // Fragment that gets passed so we can switch fragments when a tab is clicked
    Fragment fragment;

    // Receive the fragment we will place in activity_main
    public TabListener(Fragment fragment) {
        this.fragment = fragment;
    }

    // Replace the current fragment with the one passed
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragmentContainer, fragment);
    }

    // Remove the fragment when a tab is unselected
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
