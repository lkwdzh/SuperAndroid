package com.example.wytings.activity;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wytings.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/4/3.
 * https://github.com/wytings
 */
public class ActivityMaterialDesign extends BaseActivity {
    @Override
    protected void initialize() {
        setExtraContent(R.layout.activity_material_design);
        initTextInput();
        initFloatingActionButton();
        initTabLayout();
    }

    private void initTabLayout() {
        final List<TextView> textViewList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.GRAY);
            textView.setText("This is No." + i + " content.");
            textViewList.add(textView);
        }
        ViewPager viewPager = getViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return textViewList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "PageTitle-" + (position + 1);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(textViewList.get(position));
                return textViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        TabLayout tabLayout = getViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.addTab(tabLayout.newTab(),true);

    }

    private void initFloatingActionButton() {
        FloatingActionButton floatingActionButton = getViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "successfully", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initTextInput() {
        final TextInputLayout textInputLayout = getViewById(R.id.texInputLayout);
        if (textInputLayout.getEditText() == null) {
            return;
        }
        getViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("rex".equals(textInputLayout.getEditText().getText().toString())) {
                    Snackbar.make(textInputLayout, "successfully", Snackbar.LENGTH_SHORT).show();
                } else {
                    textInputLayout.setError("error input!");
                    Snackbar.make(textInputLayout, "please input: rex", Snackbar.LENGTH_SHORT).setAction("Clear", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textInputLayout.getEditText().setText(null);
                            textInputLayout.setError(null);
                        }
                    }).show();
                }
            }
        });
    }
}
