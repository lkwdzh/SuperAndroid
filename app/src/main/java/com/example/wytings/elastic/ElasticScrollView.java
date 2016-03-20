package com.example.wytings.elastic;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.example.wytings.utils.MyLog;

/**
 * Created by Rex on 2016/3/20.
 * https://github.com/wytings
 */
public class ElasticScrollView extends Elastic {

    private View childView;

    public ElasticScrollView(ScrollView v, Direction direction, Type type) {
        super(v, direction, type);
        v.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        childView = v.getChildAt(0);
    }

    @Override
    boolean isOnEdge(MotionEvent event) {
        boolean directionFlag = false;
        if (event.getHistorySize() > 0) {
            directionFlag = event.getY() - event.getHistoricalY(0) > 0;
        }
        MyLog.d(" --------->directionFlag = " + directionFlag + "," + (event.getY() - event.getHistoricalY(0)));
        if (getView().getScrollY() == 0 && directionFlag) {
            MyLog.d(" --------->getView().getScrollY() = " + getView().getScrollY());
            return true;
        }
        if (childView != null && childView.getMeasuredHeight() <= getView().getScrollY() + getView().getHeight() && !directionFlag) {
            MyLog.d(" --------->getMeasuredHeight() = " + childView.getMeasuredHeight() + ",getScrollY()=" + getView().getScrollY() + ",getHeight()=" + getView().getHeight());
            return true;
        }
        return false;
    }
}
