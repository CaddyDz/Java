package com.marsool.firetool.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.marsool.firetool.R;

public class Popup extends FrameLayout {
    static int popupPadding = -1;
    static int animDur = 300;

    private boolean open;
    private View overlay;
    private LinearLayout popup;
    private TextView title;
    private int popupSize;
    private FrameLayout content;

    public Popup(Context ctx, int popupSizeInDp) {
        super(ctx);
        setLayoutDirection(LAYOUT_DIRECTION_LTR);
        if (popupPadding == -1) {
            popupPadding = (int) getResources().getDimension(R.dimen.popup_padding);
        }
        this.popupSize = Gutils.dpToPx(popupSizeInDp,ctx);
        setId(R.id.pop_cont);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        overlay = new View(ctx);
        overlay.setId(R.id.pop_overlay);
        overlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        overlay.setAlpha(0f);
        overlay.setBackgroundColor(Color.BLACK);
        overlay.setClickable(false);

        popup = new LinearLayout(ctx);
        popup.setId(R.id.pop_root);
        popup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, popupSize));
        popup.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.popup_back, null));
        popup.setOrientation(LinearLayout.VERTICAL);
        popup.setTranslationY(getScreenSize().getHeight());
        popup.setClickable(true);

        LinearLayout top = new LinearLayout(ctx);
        top.setId(R.id.pop_top);
        top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        top.setGravity(Gravity.CENTER);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setPadding(popupPadding, popupPadding, popupPadding, popupPadding);

        title = new TextView(ctx);
        title.setId(R.id.pop_title);
        title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        title.setLetterSpacing(.1f);
        int titlePadding = Gutils.dpToPx(14,ctx);
        title.setPadding(titlePadding, titlePadding, titlePadding, titlePadding);
        title.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        title.setTextColor(Color.BLACK);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        View sep = new View(ctx);
        sep.setId(R.id.pop_sep);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Gutils.dpToPx(1,ctx));
        params.leftMargin = Gutils.dpToPx(20,ctx);
        params.rightMargin = Gutils.dpToPx(20,ctx);
        sep.setLayoutParams(params);
        sep.setBackgroundColor(Color.rgb(100, 100, 100));

        content = new FrameLayout(ctx);

        top.addView(title);
        popup.addView(top);
        popup.addView(sep);
        popup.addView(content);
        addView(overlay);
        addView(popup);
    }

    public void loadContent(View v, String tit) {
        content.removeAllViews();
        title.setText(tit);
        content.addView(v);
    }

    public void loadContent(String tit) {
        title.setText(tit);
    }

    public void loadContent(View v) {
        content.removeAllViews();
        content.addView(v);
    }

    public void show() {
        ObjectAnimator fade_animation = ObjectAnimator.ofFloat(overlay, "alpha", .5f);
        fade_animation.setDuration(animDur);
        ObjectAnimator slide_animation = ObjectAnimator.ofFloat(popup, "translationY", getScreenSize().getHeight() - popupSize);
        slide_animation.setDuration(animDur);
        open = true;
        overlay.setClickable(true);
        slide_animation.start();
        fade_animation.start();
    }

    public void hide() {
        ObjectAnimator fade_animation = ObjectAnimator.ofFloat(overlay, "alpha", 0f);
        fade_animation.setDuration(animDur);
        ObjectAnimator slide_animation = ObjectAnimator.ofFloat(popup, "translationY", getScreenSize().getHeight());
        slide_animation.setDuration(animDur);
        open = false;
        overlay.setClickable(false);
        slide_animation.start();
        fade_animation.start();
    }

    public boolean isOpen() {
        return open;
    }

    private Dimension getScreenSize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;
        return new Dimension(dpWidth, dpHeight);
    }

    static class Dimension {
        private float width;
        private float height;

        public Dimension(float width, float height) {
            this.width = width;
            this.height = height;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }
    }
}
