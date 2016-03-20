package com.example.wytings.elastic;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rex on 2016/3/20.
 * https://github.com/wytings
 */
public class ElasticCommon extends Elastic {
    public ElasticCommon(View v, Direction direction, Type type) {
        super(v, direction, type);
    }

    @Override
    boolean isOnEdge(MotionEvent event) {
        return true;
    }
}
