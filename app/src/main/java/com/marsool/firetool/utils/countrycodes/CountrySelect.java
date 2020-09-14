package com.marsool.firetool.utils.countrycodes;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blongho.country_data.World;
import com.marsool.firetool.utils.assets.AssetLoader;

import java.util.ArrayList;

public class CountrySelect extends ScrollView {
    public CountrySelect(Context context, final CountryHandler handler, int popup_padding) {
        super(context);
        final LinearLayout ll = new LinearLayout(context);
        ll.setPaddingRelative(popup_padding, popup_padding, popup_padding, popup_padding);
        ll.setOrientation(LinearLayout.VERTICAL);
        addView(ll);
        ArrayList<CountryCode> codes = AssetLoader.loadCountryCodes(context);

        for (final CountryCode code : codes) {
            LinearLayout count = new LinearLayout(context);
            count.setClickable(true);
            count.setPaddingRelative(20, 20, 20, 20);
            count.setGravity(Gravity.LEFT);
            count.setOrientation(LinearLayout.HORIZONTAL);
            final ImageView img = new ImageView(context);
            img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            RelativeLayout imageLayout = new RelativeLayout(context);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.height = 50;
            lp.width = 50;
            imageLayout.addView(img, lp);

            TextView view = new TextView(context);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            view.setPadding(30, 0, 0, 0);
            view.setText(code.getName());

            TextView codeV = new TextView(context);
            codeV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            codeV.setText(code.getDialPrefix());
            codeV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            count.addView(imageLayout);
            count.addView(view);
            count.addView(codeV, llp);

            ll.addView(count);

            final int flag = World.getFlagOf(code.getISOCode());
            img.setImageResource(flag);

            count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.handle(code);
                }
            });
        }
    }
}
