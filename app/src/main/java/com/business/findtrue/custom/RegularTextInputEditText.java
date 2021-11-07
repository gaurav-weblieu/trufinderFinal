package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.business.findtrue.R;

public class RegularTextInputEditText extends TextInputEditText {
    Typeface regularTypeface = Typeface.createFromAsset(getContext().getAssets(),
            "font/Montserrat-Regular.ttf");
    public RegularTextInputEditText(@NonNull Context context) {
        super(context);
        initFont(context);
    }

    public RegularTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    public RegularTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont(context);
    }
    private void initFont(Context context) {
        setTypeface(regularTypeface); //function used to set font
        setTextColor(ContextCompat.getColor(context, R.color.black));
        setPadding(30,30,10,20);
    }
}
