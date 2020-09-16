package com.marsool.firetool.ui.alerts;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.marsool.firetool.ui.Gutils;

public class AlertButton extends AppCompatTextView {
    public AlertButton(Context context, ButtonType type) {
        super(context);
        setLayoutParams(new FrameLayout.LayoutParams(Gutils.dpToPx(150, context), FrameLayout.LayoutParams.WRAP_CONTENT));
        int h_padding = Gutils.dpToPx(15, context);
        int v_padding = Gutils.dpToPx(10, context);
        setPadding(h_padding, v_padding, h_padding, v_padding);
        setBackground(ResourcesCompat.getDrawable(getResources(), type.getBack(), null));
        setGravity(Gravity.CENTER);
        setText(getResources().getString(type.getText()));
        setTextAlignment(Button.TEXT_ALIGNMENT_CENTER);
        setTransformationMethod(null);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        setTextColor(type.getTextColor());
    }

    public AlertButton(Context ctx) {
        this(ctx, ButtonType.CANCEL);
    }
}
