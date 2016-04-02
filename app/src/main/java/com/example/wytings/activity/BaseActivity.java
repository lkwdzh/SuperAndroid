package com.example.wytings.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wytings.R;
import com.example.wytings.utils.MyLog;
import com.example.wytings.widget.WaitDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public abstract class BaseActivity extends Activity {

    private int index = 0;
    private WaitDialog waitDialog;
    private Toast toast;

    @Bind({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    List<Button> buttons;

    @Bind(R.id.content)
    LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseInit();
        initialize();
    }

    public void setExtraContent(int layoutResID) {
        content.addView(View.inflate(this, layoutResID, null));
    }

    public void setExtraContent(View view) {
        content.addView(view);
    }

    public void setOnButtonClickListener(String title, View.OnClickListener listener) {
        if (index < buttons.size()) {
            Button button = buttons.get(index);
            button.setVisibility(View.VISIBLE);
            button.setText(title);
            button.setOnClickListener(listener);
            index++;
        } else {
            MyLog.d("sorry, no more buttons");
        }
    }

    public Activity getActivity() {
        return this;
    }

    @SuppressLint("all")
    private void baseInit() {
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        waitDialog = new WaitDialog(this);
        if (getActionBar() != null) {
            getActionBar().setTitle(this.getClass().getSimpleName());
        }
    }

    public void showToast(Object msg) {
        toast.setText(msg + "");
        toast.show();
    }

    public void showWaitingDialog() {
        waitDialog.show();
    }

    public void dismissWaitingDialog() {
        waitDialog.dismiss();
    }

    public <T extends View> T findView(int id) {
        return ButterKnife.findById(content, id);
    }

    protected abstract void initialize();

}
