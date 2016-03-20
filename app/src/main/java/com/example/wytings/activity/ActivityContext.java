package com.example.wytings.activity;

import android.view.View;
import android.widget.EditText;

import com.example.wytings.utils.ContextUtils;
import com.example.wytings.utils.MyToast;

/**
 * Created by Rex on 2016/3/20.
 * https://github.com/wytings
 */
public class ActivityContext extends BaseActivity {
    @Override
    protected void initialize() {
        setOnButtonClickListener("Check NetWork available", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextUtils.isNetworkAvailable(ActivityContext.this)) {
                    MyToast.show(getActivity(), "network is available");
                } else {
                    MyToast.show(getActivity(), "network is not available");
                }
            }
        });

        setOnButtonClickListener("Check NetWork type", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextUtils.isMobileAvailable(ActivityContext.this)) {
                    MyToast.show(getActivity(), "wifi is active now");
                } else if (ContextUtils.isWifiAvailable(ActivityContext.this)) {
                    MyToast.show(getActivity(), "mobile network is active now");
                }
            }
        });

        setOnButtonClickListener("Hide KeyBoard", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextUtils.hideKeybord(getActivity());
            }
        });

        EditText editText = new EditText(getActivity());
        setExtraContent(editText);
    }
}
