package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RegularEditText extends androidx.appcompat.widget.AppCompatEditText {
    Typeface regularTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Regular.ttf");
    public RegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(regularTypeface);
        }
    }
}
