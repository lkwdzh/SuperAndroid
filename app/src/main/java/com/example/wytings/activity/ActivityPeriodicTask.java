package com.example.wytings.activity;

import android.view.View;

import com.example.wytings.utils.MyLog;
import com.example.wytings.utils.PeriodicTask;

/**
 * Created by Rex on 2016/4/2.
 * https://github.com/wytings
 */
public class ActivityPeriodicTask extends BaseActivity {

    private PeriodicTask periodicTask;
    private int onceValidIndex = 0;
    private int onCallIndex = 100;

    @Override
    protected void initialize() {
        periodicTask = new PeriodicTask(this, 2, 10);
        periodicTask.setPeriodicTaskCallback(new PeriodicTask.PeriodicTaskCallback() {
            @Override
            public void onStart() {
                showWaitingDialog();
            }

            @Override
            public boolean onOnceValidCall() throws Exception {
                onceValidIndex++;
                Thread.sleep(3000);
                MyLog.d("this is once valid call - " + onceValidIndex);
                if (onceValidIndex == 3) {
                    MyLog.d("this is once valid call end ");
                    return true;
                }
                return false;
            }

            @Override
            public Object onCall() throws Exception {
                Thread.sleep(3000);
                MyLog.d("this is onCall ");
                return onCallIndex++;
            }

            @Override
            public void onEnd(Object object) {
                dismissWaitingDialog();
                MyLog.d("this is onEnd - " + object);
            }
        });

        setOnButtonClickListener("start periodic task", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodicTask.startTask();
            }
        });
        setOnButtonClickListener("stop periodic task", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodicTask.stopTask();
            }
        });
    }
}
