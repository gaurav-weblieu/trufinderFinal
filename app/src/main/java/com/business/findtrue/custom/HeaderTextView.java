package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class HeaderTextView extends androidx.appcompat.widget.AppCompatTextView {

    Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Bold.ttf");

    public HeaderTextView(Context context) {
        super(context);
        setFont();
    }

    public HeaderTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public HeaderTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {
        //Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"font/montserrat_bold.ttf");
        setTypeface(boldTypeface); //function used to set font

    }
}
