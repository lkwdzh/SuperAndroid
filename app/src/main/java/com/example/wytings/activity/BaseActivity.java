package com.example.wytings.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wytings.R;
import com.example.wytings.widget.WaitDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public abstract class BaseActivity extends AppCompatActivity {

    private WaitDialog waitDialog;
    private Toast toast;

    @Bind(R.id.buttonsContainer)
    LinearLayout buttonsContainer;

    @Bind(R.id.contentContainer)
    LinearLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseInit();
        initialize();
    }

    public void setExtraContent(int layoutResID) {
        contentContainer.addView(View.inflate(this, layoutResID, null));
    }

    public void setExtraContent(View view) {
        contentContainer.addView(view);
    }

    public void setOnButtonClickListener(String title, View.OnClickListener listener) {
        Button button = new Button(this);
        button.setText(title);
        button.setOnClickListener(listener);
        buttonsContainer.addView(button);
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    @SuppressLint("all")
    private void baseInit() {
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        waitDialog = new WaitDialog(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(this.getClass().getSimpleName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (this instanceof MainActivity)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void showToast(Object msg) {
        toast.setText(msg + "");
        toast.show();
    }

    public void showWaitingDialog(boolean isShow) {
        if (isShow) {
            waitDialog.show();
        } else {
            waitDialog.dismiss();
        }

    }

    public <T extends View> T findMyViewById(int id) {
        return ButterKnife.findById(contentContainer, id);
    }

    protected abstract void initialize();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
