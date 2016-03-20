package com.example.wytings.elastic;

import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Rex on 2016/3/20.
 * https://github.com/wytings
 */
public class ElasticListView extends Elastic {
    private boolean edgeFlag;

    public ElasticListView(ListView v, Direction direction, Type type) {
        super(v, direction, type);
        v.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0 || visibleItemCount + firstVisibleItem == totalItemCount) {
                    edgeFlag = true;
                } else {
                    edgeFlag = false;
                }
            }
        });
    }

    @Override
    boolean isOnEdge(MotionEvent event) {
        return edgeFlag;
    }
}
