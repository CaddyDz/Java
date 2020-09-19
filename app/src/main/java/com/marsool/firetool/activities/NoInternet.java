package com.marsool.firetool.activities;

import android.content.Intent;
import android.os.Bundle;

import com.marsool.firetool.R;
import com.marsool.firetool.networking.ConnectivityCheck;
import com.marsool.firetool.ui.Loading;
import com.marsool.firetool.ui.alerts.Alert;
import com.marsool.firetool.ui.alerts.AlertType;

public class NoInternet extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);
        Settings.st = false;

        findViewById(R.id.reconnect).setOnClickListener(e -> {
            Loading recon = new Loading(this);
            recon.setTitle("Reconnect");
            recon.setMessage("Trying to reconnect");
            recon.show(findViewById(R.id.root));

            ConnectivityCheck check = new ConnectivityCheck(this, p -> {
            },
                    () -> {
                        Intent intent = new Intent(NoInternet.this, MainActivity.class);
                        startActivity(intent);
                    },
                    () -> {
                        recon.hide();
                        Alert failed = new Alert(NoInternet.this, AlertType.INFORMATION);
                        failed.setTitle("Reconnect");
                        failed.setMessage("You're not connected!");
                        showAlert(failed);
                    });
            check.execute();
        });
    }

    public void onBackPressed() {
        finish();
        startActivity(getIntent());
        super.onBackPressed();
    }


}
