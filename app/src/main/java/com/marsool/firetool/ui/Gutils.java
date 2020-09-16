package com.marsool.firetool.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class Gutils {
    public static int dpToPx(float dp, Context ctx) {
        Resources r = ctx.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return (int) px;
    }
}
