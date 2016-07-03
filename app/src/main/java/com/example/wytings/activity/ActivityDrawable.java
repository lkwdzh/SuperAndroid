package com.example.wytings.activity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wytings.R;

/**
 * Created by Rex on 2016/3/13.
 * https://github.com/wytings
 */
public class ActivityDrawable extends BaseActivity {
    @Override
    protected void initialize() {
        setExtraContent(R.layout.activity_drawable);
        initListView();
    }

    private void initListView() {
        ListView listView = getViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"item 1", "item 2", "item 3"}));
    }
}
