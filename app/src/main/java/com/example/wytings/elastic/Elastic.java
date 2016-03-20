package com.example.wytings.elastic;

import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Rex on 2016/3/20.
 * https://github.com/wytings
 */
public abstract class Elastic implements View.OnTouchListener {

    public enum Direction {
        VERTICAL, HORIZONTAL
    }

    public enum Type {
        LAYOUT, CONTENT
    }

    private ValueAnimator valueAnimator = new ValueAnimator();
    private Direction scrollDirection;
    private Type scrollType;
    private View view;
    private int moveDistance;

    public Elastic(View v, Direction direction, Type type) {
        if (v == null) {
            return;
        }
        scrollDirection = direction == null ? Direction.VERTICAL : direction;
        scrollType = type == null ? Type.LAYOUT : type;
        view = v;
        initView();
    }

    public View getView() {
        return view;
    }

    private void initView() {
        view.setClickable(true);
        view.setOnTouchListener(this);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedValue() instanceof Integer) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    if (scrollDirection == Direction.HORIZONTAL) {
                        if (scrollType == Type.LAYOUT) {
                            view.setTranslationX(value);
                        } else if (scrollType == Type.CONTENT) {
                            view.scrollTo(-value, 0);
                        }
                    } else if (scrollDirection == Direction.VERTICAL) {
                        if (scrollType == Type.LAYOUT) {
                            view.setTranslationY(value);
                        } else if (scrollType == Type.CONTENT) {
                            view.scrollTo(0, -value);
                        }
                    }
                }
            }
        });
    }

    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (valueAnimator.isRunning()) {
                    return false;
                }
                if (event.getHistorySize() > 0 && isOnEdge(event)) {
                    if (scrollDirection == Direction.HORIZONTAL) {
                        float deltaX = event.getX() - event.getHistoricalX(0);
                        moveDistance += deltaX;
                        if (scrollType == Type.LAYOUT) {
                            view.setTranslationX(moveDistance);
                        } else if (scrollType == Type.CONTENT) {
                            view.scrollTo(-moveDistance, 0);
                        }
                    } else if (scrollDirection == Direction.VERTICAL) {
                        float deltaY = event.getY() - event.getHistoricalY(0);
                        moveDistance += deltaY;
                        if (scrollType == Type.LAYOUT) {
                            view.setTranslationY(moveDistance);
                        } else if (scrollType == Type.CONTENT) {
                            view.scrollTo(0, -moveDistance);
                        }
                    }
                }
            }
            return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                valueAnimator.setIntValues(moveDistance, 0);
                valueAnimator.start();
                moveDistance = 0;
            }
            break;
        }
        return false;
    }

    // you need to tell the listener when the view is in the edge.
    abstract boolean isOnEdge(MotionEvent event);
}
