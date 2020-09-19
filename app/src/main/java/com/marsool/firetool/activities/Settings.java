package com.marsool.firetool.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.marsool.firetool.Contact;
import com.marsool.firetool.GlobalActionBarService;
import com.marsool.firetool.R;
import com.marsool.firetool.SharedPrefManager;
import com.marsool.firetool.networking.ApiCall;
import com.marsool.firetool.networking.Handler;
import com.marsool.firetool.networking.HttpResponse;
import com.marsool.firetool.ui.Loading;
import com.marsool.firetool.ui.alerts.Alert;
import com.marsool.firetool.ui.alerts.AlertType;
import com.marsool.firetool.ui.alerts.ButtonType;


public class Settings extends Activity {
    static private final String sharedPrefName = "firetoolapp";

    public static Boolean a;
    public static Boolean b;
    public static Boolean c;
    public static Boolean d;
    public static boolean st = true;

    private SharedPrefManager spm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(sharedPrefName, 0); // 0 - for private mode
        spm = SharedPrefManager.getInstance(this);
        TextView contact = findViewById(R.id.contact);
        contact.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Contact.class);
            startActivity(intent);
        });
        contact = findViewById(R.id.contact);
        contact.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Contact.class);
            startActivity(intent);
        });
        TextView out = findViewById(R.id.logout);
        out.setOnClickListener(e -> {
            Alert confirm = new Alert(Settings.this, AlertType.CONFIRM);
            confirm.setTitle("Logout");
            confirm.setMessage("Are you sure you wanna log out?");
            showAlert(confirm, res-> {
                if (res == ButtonType.YES) {
                    Loading loading = new Loading(Settings.this);
                    loading.setTitle("Logout");
                    loading.setMessage("Logging you out...");
                    loading.show(findViewById(R.id.root));
                    ApiCall logout = new ApiCall(getString(R.string.api_base) + "logout",
                            new Handler() {
                                @Override
                                public void handleResponse(HttpResponse text) {
                                    loading.hide();
                                    SharedPrefManager.logout();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void handleError(Exception x) {
                                    //STORE PENDING LOGOUT
                                    runOnUiThread(()-> {
                                        loading.hide();
                                        SharedPrefManager.getInstance(Settings.this).storePendingLogout();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        Bundle b = new Bundle();
                                        b.putBoolean("skip_connect",true);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    });
                                }
                            });
                    logout.addHeader("Authorization", "Bearer " + spm.getToken());
                    logout.execute();
                }
            });
        });
        final Switch start = findViewById(R.id.start);
        start.setClickable(GlobalActionBarService.on);
        start.setChecked(st);
        if (!GlobalActionBarService.on) {
            start.setChecked(false);
        } else {
            start.setClickable(true);
            start.setOnClickListener(v -> st = !st);
        }

        final Switch update = (Switch) findViewById(R.id.switch1);
        update.setOnClickListener(v -> {
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences(sharedPrefName, 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref1.edit();
            editor.putBoolean("a", update.isChecked());
            editor.commit();

        });
        final Switch skip = (Switch) findViewById(R.id.switch2);
        skip.setOnClickListener(v -> {
            SharedPreferences pref14 = getApplicationContext().getSharedPreferences(sharedPrefName, 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref14.edit();
            editor.putBoolean("b", skip.isChecked());
            editor.commit();
            b = pref14.getBoolean("b", true);
        });
        Switch skip2 = (Switch) findViewById(R.id.switch3);
        skip2.setClickable(false);
        skip2.setChecked(false);
        final Switch write = (Switch) findViewById(R.id.switch4);

        final Switch send = (Switch) findViewById(R.id.switch5);
        send.setOnClickListener(v -> {
            SharedPreferences pref13 = getApplicationContext().getSharedPreferences(sharedPrefName, 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref13.edit();
            editor.putBoolean("d", send.isChecked());
            editor.commit();
            d = pref13.getBoolean("d", true);
        });
        write.setOnClickListener(v -> {
            SharedPreferences pref12 = getApplicationContext().getSharedPreferences(sharedPrefName, 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref12.edit();
            editor.putBoolean("c", write.isChecked());
            if (!write.isChecked()) {
                editor.putBoolean("d", false);
                send.setClickable(false);
                send.setChecked(false);
            } else {
                send.setClickable(true);

            }
            editor.commit();
            c = pref12.getBoolean("c", true);
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