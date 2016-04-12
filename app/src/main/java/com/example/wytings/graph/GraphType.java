package com.example.wytings.graph;

import java.io.Serializable;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public enum GraphType implements Serializable {
    TIMELINE_ONE(10), TIMELINE_FIVE(11), KLINE_DAY(20), KLINE_WEEK(21), KLINE_MONTH(22);

    private final int index;

    GraphType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
