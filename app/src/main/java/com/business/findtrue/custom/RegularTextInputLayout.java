package com.business.findtrue.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputLayout;
import com.business.findtrue.R;

import java.lang.reflect.Field;

public class RegularTextInputLayout extends TextInputLayout {
    Typeface regularTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Montserrat-Regular.ttf");
    public RegularTextInputLayout(@NonNull Context context) {
        super(context);
        initFont(context);
    }

    public RegularTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    public RegularTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont(context);
    }

    private void initFont(Context context) {
        //setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE);
        setBoxBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        setErrorTextAppearance(R.style.WelcomeErrorAppearance);
        setBoxStrokeColor(ContextCompat.getColor(context, R.color.gray));
        setBoxStrokeWidthFocused(1);
        setBoxStrokeWidth(1);
        setErrorIconDrawable(null);
        setBoxCornerRadii(10f,10f,10f,10f);

        //setBoxCornerRadii(5,5,5,5);

        /*RegularTextInputEditText editText = new RegularTextInputEditText(context);
        if (editText != null) {
            editText.setTypeface(regularTypeface);
            editText.setTextColor(ContextCompat.getColor(context, R.color.black));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count<1){
                        setErrorEnabled(false);
                    }
                    setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }*/
        try {

            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) tpf.get(cth)).setTypeface(regularTypeface);
        } catch (Exception ignored) {
            // Nothing to do
        }
    }
}
