package com.rahbod.pharmasina.fonts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FontTextViewBold extends android.support.v7.widget.AppCompatTextView {
    public FontTextViewBold(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IranSans/ttf/IRANSansWeb_Bold.ttf");
        this.setTypeface(face);
    }

    public FontTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IranSans/ttf/IRANSansWeb_Bold.ttf");
        this.setTypeface(face);
    }

    public FontTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IranSans/ttf/IRANSansWeb_Bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
