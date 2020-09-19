package com.marsool.firetool.countrycodes;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.marsool.firetool.R;
import com.marsool.firetool.ui.Gutils;

import java.util.ArrayList;

public class CountrySelect extends LinearLayout {

    public CountrySelect(Context context, final CountryHandler handler, int popup_padding, ArrayList<CountryCode> codes) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        popup_padding = Gutils.dpToPx(popup_padding, context);
        setPaddingRelative(popup_padding, 0, popup_padding, popup_padding);

        ScrollView sv = new ScrollView(context);

        final LinearLayout all = new LinearLayout(context);
        all.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout searchResults = new LinearLayout(context);
        searchResults.setOrientation(LinearLayout.VERTICAL);

        sv.addView(all);

        loadCountryCodes(codes, all, context, popup_padding, handler);

        EditText search = new EditText(context);
        search.setHint("Search...");
        search.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.empty, null));

        int finalPopup_padding = popup_padding;
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 2) {
                    new Thread() {
                        public void run() {
                            ArrayList<CountryCode> found = new ArrayList<>();
                            for(CountryCode cc:codes) {
                                if(cc.getName().toLowerCase().indexOf(charSequence.toString().toLowerCase()) == 0) {
                                    found.add(cc);
                                }
                            }
                            new Handler(Looper.getMainLooper()).post(()-> {
                                loadCountryCodes(found, searchResults, context, finalPopup_padding, handler);
                                if(sv.getChildAt(0) == all) {
                                    sv.removeView(all);
                                    sv.addView(searchResults);
                                }
                            });
                        }
                    }.start();
                }else {
                    if(sv.getChildAt(0) == searchResults) {
                        sv.removeView(searchResults);
                        sv.addView(all);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addView(search);
        addView(sv);
    }

    private void loadCountryCodes(ArrayList<CountryCode> codes, LinearLayout root, Context ctx, int popup_padding, CountryHandler handler) {
        root.removeAllViews();
        for (final CountryCode code : codes) {
            LinearLayout count = new LinearLayout(ctx);
            count.setClickable(true);
            count.setPaddingRelative(20, 20, 20, 20);
            count.setGravity(Gravity.CENTER);
            count.setOrientation(LinearLayout.HORIZONTAL);
            final ImageView img = new ImageView(ctx);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);


            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.width = (int) (1.5 * popup_padding);
            lp.height = (int) (1.5 * popup_padding);
            img.setLayoutParams(lp);

            TextView view = new TextView(ctx);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            view.setPadding(30, 0, 0, 0);
            view.setText(code.getName());

            TextView codeV = new TextView(ctx);
            codeV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            codeV.setText(code.getDialPrefix());
            codeV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            count.addView(img);
            count.addView(view);
            count.addView(codeV, llp);

            root.addView(count);

            new Thread() {
                public void run() {
                    Bitmap bmp = code.getFlag();
                    new Handler(Looper.getMainLooper()).post(() -> img.setImageBitmap(bmp));
                }
            }.start();
            count.setOnClickListener(v -> handler.handle(code));
        }
    }
}
