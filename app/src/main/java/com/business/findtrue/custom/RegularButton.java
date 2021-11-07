package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.business.findtrue.R;

public class RegularButton extends androidx.appcompat.widget.AppCompatButton {
    Typeface mediumTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Regular.ttf");
    public RegularButton(Context context) {
        super(context);
        setFont(context);
    }

    public RegularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public RegularButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }



    private void setFont(Context context) {
        //Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"font/montserrat_bold.ttf");
        setTypeface(mediumTypeface); //function used to set font
        setTextColor(ContextCompat.getColor(context, R.color.button_text_color));
        //setPadding(0,0,15,0);
        //setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

        //setBackgroundTintList(ContextCompat.getColor(context,R.color.colorPrimary));
    }

}
