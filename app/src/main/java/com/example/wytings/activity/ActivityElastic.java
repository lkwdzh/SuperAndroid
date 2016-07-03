package com.example.wytings.activity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wytings.R;
import com.example.wytings.elastic.Elastic;
import com.example.wytings.elastic.ElasticHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/4/23.
 * https://github.com/wytings
 */
public class ActivityElastic extends BaseActivity {
    @Override
    protected void initialize() {
        setExtraContent(R.layout.activity_elastic);
        ListView listView = getViewById(R.id.listView);
        List<String> strings = new ArrayList<>();
        for (int i = 10; i < 30; i++) {
            strings.add("This is Item " + i);
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, strings));
        ElasticHelper.setUp(listView, Elastic.Direction.VERTICAL, Elastic.Type.LAYOUT);

    }
}
