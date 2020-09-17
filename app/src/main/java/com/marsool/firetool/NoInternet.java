package com.marsool.firetool;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.marsool.firetool.networking.ConnectivityCheck;
import com.marsool.firetool.ui.Loading;
import com.marsool.firetool.ui.alerts.Alert;
import com.marsool.firetool.ui.alerts.AlertType;

public class NoInternet extends AppCompatActivity {
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
                        failed.showAndWait(findViewById(R.id.root), r -> {
                        });
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
