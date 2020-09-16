package com.marsool.firetool;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Contact extends AppCompatActivity {
    public static String FACEBOOK_URL = "https://www.facebook.com/FireTool-101688798315228/";
    public static String FACEBOOK_PAGE_ID = "FireTool";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        ImageButton facebook=(ImageButton) findViewById(R.id.facebook);
        ImageButton what=(ImageButton) findViewById(R.id.whatsapp);
        facebook.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(getApplicationContext());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });
        TextView f=(TextView) findViewById(R.id.f);
        f.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(getApplicationContext());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });
        what.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=+201554489687";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        TextView w=(TextView) findViewById(R.id.w);
        w.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=+201554489687";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
