package com.example.wytings.activity;

import android.view.View;

import com.example.wytings.widget.WaitDialog;


/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class ActivityDialog extends BaseActivity {

    @Override
    protected void initialize() {
        setOnButtonClickListener("wait dialog", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitDialog();
            }
        });
    }

    private void showWaitDialog() {
        WaitDialog waitDialog = new WaitDialog(this);
        waitDialog.show();
    }

}
