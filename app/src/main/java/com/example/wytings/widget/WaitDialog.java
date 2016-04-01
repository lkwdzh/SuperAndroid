package com.example.wytings.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.wytings.R;
import com.example.wytings.utils.MyLog;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class WaitDialog extends AlertDialog {

    private Activity ownerActivity;

    public WaitDialog(Activity activity) {
        super(activity);
        ownerActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wait);
        initialize();
    }

    private void initialize() {
        this.setCanceledOnTouchOutside(false);
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                MyLog.d(" setOnKeyListener-----> " + keyCode);
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    ownerActivity.finish();
                }
                return false;
            }
        });
    }
}
