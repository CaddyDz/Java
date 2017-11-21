package com.englishdz.salim.navdrawex;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyCompanyFragment extends Fragment {
    /*
     * Returns a new instance of this fragment for the given section number.
     */
    public static MyCompanyFragment newInstance() {
        MyCompanyFragment fragment = new MyCompanyFragment();
        return fragment;
    }

    public MyCompanyFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }
}
