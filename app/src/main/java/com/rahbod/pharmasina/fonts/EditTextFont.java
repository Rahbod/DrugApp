package com.rahbod.pharmasina.fonts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditTextFont extends android.support.v7.widget.AppCompatEditText {
    public EditTextFont(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans(FaNum)_Light.ttf");
        this.setTypeface(face);
    }

    public EditTextFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans(FaNum)_Light.ttf");
        this.setTypeface(face);
    }

    public EditTextFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans(FaNum)_Light.ttf");
        this.setTypeface(face);
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

}
