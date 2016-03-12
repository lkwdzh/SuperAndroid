package com.example.wytings.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * A ScrollView which can scroll to (0,0) when pull down or up.
 *
 * @author markmjw
 * @date 2014-04-30
 */
public class ElasticScrollView2 extends ScrollView {
    private static final int MSG_REST_POSITION = 0x01;
    private static final int MSG_________ = 0x02;

    /**
     * The max scroll height.
     */
    private static final int MAX_SCROLL_HEIGHT = 400;
    /**
     * Damping, the smaller the greater the resistance
     */
    private static final float SCROLL_RATIO = 0.4f;

    private View mChildRootView;

    private float mTouchY;
    private boolean mTouchStop = false;

    private int mScrollY = 0;
    private int mScrollDy = 0;

    @SuppressWarnings("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MSG_REST_POSITION == msg.what) {
                if (mScrollY != 0 && mTouchStop) {
                    mScrollY -= mScrollDy;

                    if ((mScrollDy < 0 && mScrollY > 0) || (mScrollDy > 0 && mScrollY < 0)) {
                        mScrollY = 0;
                    }

                    mChildRootView.scrollTo(0, mScrollY);
                    // continue scroll after 20ms
                    sendEmptyMessageDelayed(MSG_REST_POSITION, 20);
                }
            } else {
                Log.i("wytings", "receive message");
                if (lastY > 0) {
                    int yDistance = (int) (lastY - 10);
                    lastY -= 10;
                    //mChildRootView.layout(left, top + yDistance, right, bottom + yDistance);
                    magnifyView(lastY / displayHeight + 1);
                    sendEmptyMessageDelayed(MSG_________, 10);

                }
            }
        }
    };

    public ElasticScrollView2(Context context) {
        super(context);

        init();
    }

    public ElasticScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ElasticScrollView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private TranslateAnimation animation;

    private int displayHeight;

    private void init() {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        displayHeight = metric.heightPixels;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            // when finished inflating from layout xml, get the first child view
            mChildRootView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("wytings", "onInterceptTouchEvent = " + ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchY = ev.getY();
        }
        _doTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i("wytings", "onTouchEvent = " + ev);
        if (null != mChildRootView) {
            _doTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private int left, top, right, bottom;
    private float lastY;

    private void _doTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                mScrollY = mChildRootView.getScrollY();
                //if (mScrollY != 0) {
            {
                mTouchStop = true;
                mScrollDy = (int) (mScrollY / 10.0f);
                mHandler.sendEmptyMessage(MSG_________);
            }

//                animation = new TranslateAnimation(0, 0, lastY, 0);
//                animation.setDuration(200);
//                mChildRootView.startAnimation(animation);
//                mChildRootView.layout(left, top, right, bottom);
//                if (listener != null) {
//                    listener.onAction(1,true);
//                }
//                lastY = 0;
            break;

            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int deltaY = (int) (-mTouchY + nowY);
                //mTouchY = nowY;
                boolean flag;
                if (flag = isNeedMove()) {
                    //Log.i("wytings", "flag=" + flag);
                    int offset = mChildRootView.getScrollY();
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        int yDistance = (int) (deltaY * SCROLL_RATIO);
                        //mChildRootView.scrollBy(0,yDistance);
                        if (bottom == 0) {
                            left = mChildRootView.getLeft();
                            top = mChildRootView.getTop();
                            right = mChildRootView.getRight();
                            bottom = mChildRootView.getBottom();
                        }
                        Log.i("wytings", "left=" + left + "->" + "top=" + top + "->" + "right=" + right + "->" + "bottom=" + bottom);
                        mChildRootView.layout(left, top + yDistance, right, bottom + yDistance);
                        lastY = yDistance;
                        mTouchStop = false;
                        magnifyView((-mTouchY + nowY) / displayHeight + 1);
                    }
                }
                break;

            default:
                break;
        }
    }


    private void doTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                mScrollY = mChildRootView.getScrollY();
                if (mScrollY != 0) {
                    mTouchStop = true;
                    mScrollDy = (int) (mScrollY / 10.0f);
                    mHandler.sendEmptyMessage(MSG_REST_POSITION);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int deltaY = (int) (mTouchY - nowY);
                mTouchY = nowY;
                boolean flag;
                if (flag = isNeedMove()) {
                    Log.i("wytings", "flag=" + flag);
                    int offset = mChildRootView.getScrollY();
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mChildRootView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
                        mTouchStop = false;
                    }
                }
                break;

            default:
                break;
        }
    }

    private int imageLeft, imageTop, imageRight, imageBottom;
    private ImageView imageView;
    private int imageHeight, imageWidth;
    private BitmapDrawable imageDrawable;

    private void magnifyView(float ratio) {
        Log.i("wytings", "ratio = " + ratio);
        if (listener != null) {
            listener.onAction(ratio, false);
        }

    }

    private boolean isNeedMove() {
        int viewHeight = mChildRootView.getMeasuredHeight();
        int scrollHeight = getHeight();
        int offset = viewHeight - scrollHeight;
        int scrollY = getScrollY();
        Log.i("wytings", "scrollY=" + scrollY + "->" + "viewHeight=" + viewHeight + "->" + "scrollHeight=" + scrollHeight + "->" + "offset=" + offset);
        //return scrollY == 0 || scrollY >= offset;
        return scrollY == 0;
    }

    private OnElasticListener listener;

    public void setOnElasticListener(OnElasticListener listener) {
        this.listener = listener;
    }

    public interface OnElasticListener {
        void onAction(float ratio, boolean isLast);
    }

}