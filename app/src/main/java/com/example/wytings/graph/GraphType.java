package com.example.wytings.graph;

import java.io.Serializable;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public enum GraphType implements Serializable {
    TIMELINE_ONE(0), TIMELINE_FIVE(1), KLINE_DAY(2), KLINE_WEEK(3), KLINE_MONTH(4);

    private final int index;

    GraphType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static GraphType valueOf(int index) {
        GraphType[] enumConstants = GraphType.class.getEnumConstants();
        if (index < 0 || index > 4) {
            return TIMELINE_ONE;
        } else {
            return enumConstants[index];

        }
    }
}
