package com.example.wytings.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class SuperScrollView extends ScrollView {

    private int lastScrollY = 0;
    private int SCROLL_EVENT = 0x300;
    private Handler handler;
    private OnScrollChangeListener scrollChangeListener;
    private View contentView;

    public SuperScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SuperScrollView(Context context) {
        super(context);
        initialize();
    }

    public SuperScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    @SuppressLint("HandlerLeak")
    private void initialize() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SCROLL_EVENT) {
                    if (contentView == null) {
                        contentView = getChildAt(0);
                    }
                    if (lastScrollY == getScrollY()) {
                        if (scrollChangeListener != null) {
                            if (getScrollY() == 0) {
                                scrollChangeListener.onScrollChanged(SuperScrollView.this, ScrollState.TOP);
                            } else if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
                                scrollChangeListener.onScrollChanged(SuperScrollView.this, ScrollState.BOTTOM);
                            } else {
                                scrollChangeListener.onScrollChanged(SuperScrollView.this, ScrollState.IDLE);
                            }
                        }
                    } else {
                        lastScrollY = getScrollY();
                        handler.sendEmptyMessageDelayed(SCROLL_EVENT, 100);
                    }
                }
            }
        };
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollChangeListener != null) {
            scrollChangeListener.onScrollChanged(this, ScrollState.SCROLLING);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendEmptyMessage(SCROLL_EVENT);
        }
        return super.onTouchEvent(ev);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        scrollChangeListener = listener;
    }

    public enum ScrollState {
        IDLE, SCROLLING, TOP, BOTTOM
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(ScrollView view, ScrollState state);
    }


}
