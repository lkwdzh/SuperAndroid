package com.example.wytings.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Rex on 2016/3/13.
 * https://github.com/wytings
 */
public class MyToast {
    public static void show(Context context, Object msg) {
        Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
    }
}
