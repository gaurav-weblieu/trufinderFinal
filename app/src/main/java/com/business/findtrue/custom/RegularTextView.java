package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class RegularTextView extends androidx.appcompat.widget.AppCompatTextView {

    Typeface regularTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Regular.ttf");

    public RegularTextView(Context context) {
        super(context);
        setFont();
    }

    public RegularTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public RegularTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Bold.ttf");
        setTypeface(regularTypeface); //function used to set font

    }
}

