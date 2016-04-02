package com.example.wytings.activity;

import android.widget.ScrollView;

import com.example.wytings.R;
import com.example.wytings.utils.MyLog;
import com.example.wytings.widget.SuperScrollView;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class ActivityScroll extends BaseActivity {

    public SuperScrollView scrollView;

    @Override
    protected void initialize() {
        setExtraContent(R.layout.activity_scroll);
        scrollView = findMyViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new SuperScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(ScrollView view, SuperScrollView.ScrollState state) {
                if (state != SuperScrollView.ScrollState.SCROLLING) {
                    showToast(state);
                    MyLog.d("IDLE - " + view.getScrollX() + "," + view.getScrollY() + "," + state.toString());
                } else {
                    MyLog.d("SCROLLING - " + view.getScrollX() + "," + view.getScrollY() + "," + state.toString());
                }
            }
        });
    }
}
