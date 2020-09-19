package com.marsool.firetool.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Loading {
    private Popup popup;
    private TextView msg;
    public Loading(Context ctx) {
        popup = new Popup(ctx, 300);
        LinearLayout logLay = new LinearLayout(ctx);
        logLay.setOrientation(LinearLayout.VERTICAL);
        logLay.setGravity(Gravity.CENTER);

        msg = new TextView(ctx);
        msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        msg.setPadding(0, Gutils.dpToPx(20, ctx), 0, Gutils.dpToPx(20, ctx));
        msg.setTextColor(Color.BLACK);

        LinearLayout buttons = new LinearLayout(ctx);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setGravity(Gravity.CENTER);

        ProgressBar pb = new ProgressBar(ctx);
        pb.setLayoutParams(new FrameLayout.LayoutParams(Gutils.dpToPx(60, ctx),Gutils.dpToPx(60, ctx)));
        buttons.addView(pb);

        logLay.addView(msg);
        logLay.addView(buttons);
        popup.loadContent(logLay);
    }

    public void setTitle(String title) {
        popup.loadContent(title);
    }

    public void setMessage(String message) {
        msg.setText(message);
    }

    public void show(FrameLayout root) {
        root.addView(popup);
        popup.show();
    }

    public Popup getPopup() {
        return popup;
    }

    public void hide() {
        popup.hide();
    }
}
