package ir.rahbod.sinadrug.app;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager{

    private boolean enableSwipe;

    public MyViewPager(Context context){
        super(context);
        init();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        enableSwipe = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return enableSwipe && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return enableSwipe && super.onTouchEvent(ev);
    }

    public void setEnableSwipe(boolean enableSwipe){
        this.enableSwipe = enableSwipe;
    }
}
