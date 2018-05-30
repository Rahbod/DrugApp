package com.example.behnam.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ScrollView;

/**
 * Created by betterzw on 16-1-27.
 */
public class ScrollViewExt extends ScrollView {

    private Runnable scrollerTask;
    private int initialPosition;
    private int newCheck = 100;
    public View view;
    Animation animation;

    public ScrollViewExt(Context context) {
        super(context);

        init(view, animation);
    }

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(view, animation);
    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(view, animation);
    }

    public void init(final View view, final Animation animation) {
        scrollerTask = new Runnable() {
            public void run() {
                int newPosition = getScrollY();
                if (initialPosition - newPosition == 0) {//has stopped
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            view.setVisibility(VISIBLE);
//                            Toast.makeText(getContext(), "mm", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    view.setAnimation(animation);
//                    if(onScrollStoppedListener!=null){
//
//                        onScrollStoppedListener.onScrollStopped();
//                    }

                    Log.e("TAG", "rock========stop");
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


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Log.d("TAG", "rock========l=>" + l + ":t=>" + t
//                + ":oldl=>" + oldl + ":oldt=>" + oldt);
//        if (t > oldt) {
//            Log.e("TAG", "rock========up");
//        } else if (t < oldt) {
//            Log.e("TAG", "rock========down");
//        }
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
