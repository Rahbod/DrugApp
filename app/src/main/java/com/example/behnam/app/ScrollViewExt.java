package com.example.behnam.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by Rahbod on 18-5-27.
 */
public class ScrollViewExt extends ScrollView {

    private Runnable scrollerTask;
    private int initialPosition;
    private int newCheck = 100;
    public View view;
    Animation animationDown;
    Animation animationUp;
    int pageHeight = getBottom();

    public ScrollViewExt(Context context) {
        super(context);
    }

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(View inView, Animation inAnimationDown, Animation inAnimationUp) {
        this.animationUp = inAnimationUp;
        this.animationDown = inAnimationDown;
        this.view = inView;

        scrollerTask = new Runnable() {
            public void run() {
                int newPosition = getScrollY();
                if (getScrollY() == 0 || getScrollY() == pageHeight || initialPosition - newPosition == 0) {//has stopped
                    view.startAnimation(animationUp);
                    menuHide = false;
                } else {
                    initialPosition = getScrollY();
                    ScrollViewExt.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void startScrollerTask() {
        initialPosition = getScrollY();
        ScrollViewExt.this.postDelayed(scrollerTask, newCheck);
    }

    public boolean menuHide = false;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (!menuHide && getScrollY() != 0 && getScrollY() != pageHeight && initialPosition != getScrollY()) {
            view.startAnimation(animationDown);
            menuHide = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                startScrollerTask();
                break;
        }
        return super.onTouchEvent(ev);
    }
}