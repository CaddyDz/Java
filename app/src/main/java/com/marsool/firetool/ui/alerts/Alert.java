package com.marsool.firetool.ui.alerts;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marsool.firetool.ui.Gutils;
import com.marsool.firetool.ui.Popup;

public class Alert {
    private Popup popup;
    private TextView msg;
    private AlertResultHandler handler;

    public Alert(Context ctx, AlertType type) {
        popup = new Popup(ctx, 350);

        LinearLayout logLay = new LinearLayout(ctx);
        logLay.setOrientation(LinearLayout.VERTICAL);
        logLay.setGravity(Gravity.CENTER);

        msg = new TextView(ctx);
        msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        msg.setPadding(0, Gutils.dpToPx(30, ctx), 0, Gutils.dpToPx(30, ctx));
        msg.setTextColor(Color.BLACK);
        LinearLayout buttons = new LinearLayout(ctx);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setGravity(Gravity.CENTER);

        for (int i = 0; i < type.getButtons().size(); i++) {
            ButtonType bt = type.getButtons().get(i);
            AlertButton button = new AlertButton(ctx, bt);
            if (i == 0) {
                button.setLayoutParams(new FrameLayout.LayoutParams(Gutils.dpToPx(100, ctx), FrameLayout.LayoutParams.WRAP_CONTENT));
            } else {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Gutils.dpToPx(100, ctx), FrameLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = Gutils.dpToPx(30, ctx);
                button.setLayoutParams(params);
            }
            buttons.addView(button);
            button.setOnClickListener(e -> {
                popup.hide();
                if (handler != null)
                    handler.handle(bt);
            });
        }

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

    public Popup getPopup() {
        return popup;
    }

    public void setHandler(AlertResultHandler handler) {
        this.handler = handler;
    }

    public void hide() {
        popup.hide();
    }

    public boolean isShown() {
        return popup.isOpen();
    }
}
