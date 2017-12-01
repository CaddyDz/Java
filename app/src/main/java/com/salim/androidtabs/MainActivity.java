package com.salim.androidtabs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class MainActivity extends Activity {

    ActionBar.Tab tab1, tab2, tab3;

    // Fragments that will load when the tabs are clicked
    Fragment fragment1 = new TabFragment1();
    Fragment fragment2 = new TabFragment2();
    Fragment fragment3 = new TabFragment3();

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();

        // Set the current navigation mode to tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Add titles to tabs
        tab1 = actionBar.newTab().setText("Tab1");
        tab2 = actionBar.newTab().setText("Tab2");
        tab3 = actionBar.newTab().setText("Tab3");

        // Set tab listeners which provide callbacks for tab events
        // A callback informs another class when an action occurs
        tab1.setTabListener(new TabListener(fragment1));
        tab2.setTabListener(new TabListener(fragment2));
        tab3.setTabListener(new TabListener(fragment3));

        // Adds tabs to the actionBar
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
    }
}
