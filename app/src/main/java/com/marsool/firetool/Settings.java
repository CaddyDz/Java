package com.marsool.firetool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.marsool.firetool.networking.ApiCall;
import com.marsool.firetool.networking.Handler;
import com.marsool.firetool.networking.HttpResponse;


public class Settings extends AppCompatActivity {
    public static Boolean a;
    public static Boolean b;
    public static Boolean c;
    public static Boolean d;
    Button contact, out;
    public static boolean st = true;

    private String token;
    private SharedPrefManager spm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        spm = SharedPrefManager.getInstance(this);
        TextView contact = findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Contact.class);
                startActivity(intent);
            }
        });
        contact = findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Contact.class);
                startActivity(intent);
            }
        });
        TextView out = findViewById(R.id.logout);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCall logout = new ApiCall(getString(R.string.api_base) + "logout",
                        new Handler() {
                            @Override
                            public void handleResponse(HttpResponse text) {
                                SharedPrefManager.logout();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void handleError(Exception x) {

                            }
                        });
                logout.addHeader("Authorization","Bearer " + spm.getToken());
                logout.execute();
            }
        });
        final Switch start = findViewById(R.id.start);
        start.setClickable(GlobalActionBarService.on);
        start.setChecked(st);
        if (!GlobalActionBarService.on) {
            start.setChecked(false);
        } else {
            start.setClickable(true);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    st = !st;
                }
            });
        }

        final Switch update = (Switch) findViewById(R.id.switch1);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("a", update.isChecked());
                editor.commit();

            }
        });
        final Switch skip = (Switch) findViewById(R.id.switch2);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("b", skip.isChecked());
                editor.commit();
                b = pref.getBoolean("b", true);
            }
        });
        Switch skip2 = (Switch) findViewById(R.id.switch3);
        skip2.setClickable(false);
        skip2.setChecked(false);
        final Switch write = (Switch) findViewById(R.id.switch4);

        final Switch send = (Switch) findViewById(R.id.switch5);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("d", send.isChecked());
                editor.commit();
                d = pref.getBoolean("d", true);
            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("c", write.isChecked());
                if (!write.isChecked()) {
                    editor.putBoolean("d", false);
                    send.setClickable(false);
                    send.setChecked(false);
                } else {
                    send.setClickable(true);

                }
                editor.commit();
                c = pref.getBoolean("c", true);
            }
        });
        a = pref.getBoolean("a", true);
        b = pref.getBoolean("b", true);
        c = pref.getBoolean("c", true);
        d = pref.getBoolean("d", true);
        update.setChecked(a);
        skip.setChecked(b);
        write.setChecked(c);
        send.setChecked(d);
    }
}