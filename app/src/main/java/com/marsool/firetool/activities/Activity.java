package com.marsool.firetool.activities;

import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.marsool.firetool.R;
import com.marsool.firetool.ui.Popup;
import com.marsool.firetool.ui.alerts.Alert;
import com.marsool.firetool.ui.alerts.AlertResultHandler;

public class Activity extends AppCompatActivity {
    private FrameLayout root;
    private Popup openPopup;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        root = findViewById(R.id.root);
    }

    public Popup getOpenPopup() {
        return openPopup;
    }

    protected void showPopup(Popup popup) {
        openPopup = popup;
        try {
            root.addView(popup);
        } catch (IllegalStateException x) {
            //IGNORE
        }
        popup.show();
    }

    protected void showAlert(Alert alert, AlertResultHandler handler) {
        alert.setHandler(handler);
        showPopup(alert.getPopup());
    }

    protected void showAlert(Alert alert) {
        showAlert(alert, null);
    }

    public void onBackPressed() {
        Popup openPopup = getOpenPopup();
        if (openPopup != null && openPopup.isOpen()) {
            openPopup.hide();
        }
    }
}
