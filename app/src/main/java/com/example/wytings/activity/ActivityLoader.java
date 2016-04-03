package com.example.wytings.activity;

import android.view.View;

import com.example.wytings.utils.DataLoader;
import com.example.wytings.utils.HttpUtils;
import com.example.wytings.utils.MyLog;

/**
 * Created by Rex on 2016/4/1.
 * https://github.com/wytings
 */
public class ActivityLoader extends BaseActivity {

    @Override
    protected void initialize() {
        setOnButtonClickListener("get data", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLoader.loadData(new DataLoader.DataLoaderCallback() {
                    @Override
                    public void onStart() {
                        showWaitingDialog(true);
                    }

                    @Override
                    public Object onCall() throws Exception {
                        Thread.sleep(3000);
                        String url = "http://www.baidu.com/";
                        return HttpUtils.getHttpSource(url);
                    }

                    @Override
                    public void onEnd(Object object) {
                        showWaitingDialog(false);
                        showToast(object);
                        MyLog.d(" onEnd ----------------- " + object);
                    }
                });
            }
        });
    }
}
