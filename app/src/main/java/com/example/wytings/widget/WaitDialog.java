package com.example.wytings.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.example.wytings.R;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class WaitDialog extends AlertDialog {
    public WaitDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wait);
    }
}
