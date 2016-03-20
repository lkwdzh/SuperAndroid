package com.example.wytings.elastic;

import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Rex on 2016/3/20.
 * https://github.com/wytings
 */
public class ElasticHelper {

    public static void setUp(View view) {
        if (view instanceof ScrollView) {
            new ElasticScrollView((ScrollView) view, Elastic.Direction.VERTICAL, Elastic.Type.LAYOUT);
        } else if (view instanceof ListView) {
            new ElasticListView((ListView) view, Elastic.Direction.VERTICAL, Elastic.Type.LAYOUT);
        } else {
            new ElasticCommon(view, Elastic.Direction.VERTICAL, Elastic.Type.LAYOUT);
        }
    }

    public static void setUp(View view, Elastic.Direction direction, Elastic.Type type) {
        if (view instanceof ScrollView) {
            new ElasticScrollView((ScrollView) view, direction, type);
        } else if (view instanceof ListView) {
            new ElasticListView((ListView) view, direction, type);
        } else {
            new ElasticCommon(view, direction, type);
        }
    }

}
