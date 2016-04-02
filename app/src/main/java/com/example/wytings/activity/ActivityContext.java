package com.example.wytings.activity;

import android.view.View;
import android.widget.EditText;

import com.example.wytings.utils.ContextUtils;

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
                    showToast("network is available");
                } else {
                    showToast("network is not available");
                }
            }
        });

        setOnButtonClickListener("Check NetWork type", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextUtils.isMobileAvailable(ActivityContext.this)) {
                    showToast("wifi is active now");
                } else if (ContextUtils.isWifiAvailable(ActivityContext.this)) {
                    showToast("mobile network is active now");
                }
            }
        });

        setOnButtonClickListener("Hide KeyBoard", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextUtils.hideKeyboard(getActivity());
            }
        });

        EditText editText = new EditText(getActivity());
        setExtraContent(editText);
    }
}
