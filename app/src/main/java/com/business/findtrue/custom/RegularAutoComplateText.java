package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.business.findtrue.R;

public class RegularAutoComplateText extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    Typeface regularTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Regular.ttf");
    public RegularAutoComplateText(Context context) {
        super(context);
        initFont(context);
    }

    public RegularAutoComplateText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    public RegularAutoComplateText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont(context);
    }

    private void initFont(Context context) {
        setTypeface(regularTypeface); //function used to set font
        setTextColor(ContextCompat.getColor(context, R.color.black));
        setPadding(30,30,15,15);
    }
}
