package com.marsool.firetool.ui.alerts;

import android.graphics.Color;

import com.marsool.firetool.R;

public enum ButtonType {
    YES(R.string.yes, R.drawable.button, Color.WHITE), CANCEL(R.string.cancel, R.drawable.cancel, Color.BLACK), OK(R.string.ok, R.drawable.ok, Color.BLACK);

    private int text;
    private int back;
    private int textColor;
    ButtonType(int text, int back, int textColor) {
        this.text = text;
        this.back = back;
        this.textColor = textColor;
    }

    public int getText() {
        return text;
    }

    public int getBack() {
        return back;
    }

    public int getTextColor() {
        return textColor;
    }
}
