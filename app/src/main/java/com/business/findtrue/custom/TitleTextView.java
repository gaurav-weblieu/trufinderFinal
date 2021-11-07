package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TitleTextView extends androidx.appcompat.widget.AppCompatTextView {

    Typeface mediumTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-SemiBold.ttf");

    public TitleTextView(Context context) {
        super(context);
        setFont();
    }

    public TitleTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public TitleTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {
        setTypeface(mediumTypeface); //function used to set font
    }
}
