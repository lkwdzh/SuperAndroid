package com.example.wytings.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wytings.utils.ContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class MainActivity extends BaseActivity {

    private List<String> activityClasses;

    @Override
    protected void initialize() {
        activityClasses = ContextUtils.getActivities(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, getSimpleNames(activityClasses));
        ListView listView = new ListView(this);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClassName(getBaseActivity(), activityClasses.get(position));
                startActivity(intent);

            }
        });
        setExtraContent(listView);
    }

    private List<String> getSimpleNames(List<String> names) {
        List<String> result = new ArrayList<>();
        for (String name : names) {
            result.add(name.substring(name.lastIndexOf(".") + 1));
        }
        for (int i = 0; i < result.size(); i++) {
            if (getClass().getSimpleName().equals(result.get(i))) {
                result.remove(i);
                activityClasses.remove(i);
                break;
            }
        }
        return result;
    }

    private static final int EXIT = Menu.FIRST + 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, EXIT, 0, "Exit App");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EXIT) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onOptionsItemSelected(item);
    }
}
