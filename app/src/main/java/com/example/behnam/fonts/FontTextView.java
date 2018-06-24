package com.example.behnam.fonts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.behnam.app.ActivityReminderStep2;

public class FontTextView extends android.support.v7.widget.AppCompatTextView {
    public FontTextView(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(face);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(face);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
}
