package com.example.wytings.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wytings.utils.ContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class MainActivity extends ListActivity {

    private List<String> activityClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityClasses = ContextUtils.getActivities(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, getSimpleNames(activityClasses));
        setListAdapter(arrayAdapter);
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


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            Class clazz = Class.forName(activityClasses.get(position));
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final int EXIT = Menu.FIRST + 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, EXIT, 0, "Exit App");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == EXIT) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
