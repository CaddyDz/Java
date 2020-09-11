package com.marsool.firetool;

import android.os.Bundle;
import android.text.NoCopySpan;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternet extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);
        Settings.st=false;
    }
    public void onBackPressed() {
        finish();
        startActivity(getIntent());
        super.onBackPressed();
    }


}
