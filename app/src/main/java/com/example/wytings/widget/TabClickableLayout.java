package com.example.wytings.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rex on 4/12/2016.
 */
public class TabClickableLayout extends TabLayout {

    private boolean isEnable = true;

    public TabClickableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabClickableLayout(Context context) {
        super(context);
    }

    public TabClickableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnable || super.onInterceptTouchEvent(ev);
    }

    public void setTabEnable(boolean enable) {
        isEnable = enable;
    }
}
