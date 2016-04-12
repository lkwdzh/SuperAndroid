package com.example.wytings.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.wytings.R;
import com.example.wytings.graph.CandleGraph;
import com.example.wytings.graph.GraphType;
import com.example.wytings.graph.GraphUtils;
import com.example.wytings.graph.TimesGraph;
import com.example.wytings.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/4/11.
 * https://github.com/wytings
 */
public class ActivityGraph extends BaseActivity {

    List<View> graphViews = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void initialize() {
        setExtraContent(R.layout.activity_graph);
        viewPager = findMyViewById(R.id.viewPager);
        tabLayout = findMyViewById(R.id.tabLayout);

        titles.add("分时");
        titles.add("五日");
        titles.add("日K");
        titles.add("周K");
        titles.add("月K");

        initGraph();
        initUI();

//        DataLoader.loadData(new DataLoader.DataLoaderCallback() {
//            @Override
//            public Object onCall() throws Exception {
//                initGraph();
//                return null;
//            }
//
//            @Override
//            public void onEnd(Object object) {
//                initUI();
//            }
//        });
    }

    private void initUI() {
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

            @Override
            public int getCount() {
                return graphViews.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(graphViews.get(position));
                return graphViews.get(position);
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

        tabLayout.setupWithViewPager(viewPager);
    }

    private void initGraph() {
        TimesGraph timesGraphOne = new TimesGraph(this);
        timesGraphOne.setTimesModels(GraphUtils.loadTimesGraphData(getBaseActivity(), GraphType.TIMELINE_ONE));
        graphViews.add(timesGraphOne);
        TimesGraph timesGraphFive = new TimesGraph(this);
        timesGraphFive.setTimesModels(GraphUtils.loadTimesGraphData(getBaseActivity(), GraphType.TIMELINE_FIVE));
        graphViews.add(timesGraphFive);
        CandleGraph candleGraphDay = new CandleGraph(this);
        candleGraphDay.setOriginalDataList(GraphUtils.loadKLineGraphData(getBaseActivity(), GraphType.KLINE_DAY));
        graphViews.add(candleGraphDay);
        CandleGraph candleGraphWeek = new CandleGraph(this);
        candleGraphWeek.setOriginalDataList(GraphUtils.loadKLineGraphData(getBaseActivity(), GraphType.KLINE_WEEK));
        graphViews.add(candleGraphWeek);
        CandleGraph candleGraphMonth = new CandleGraph(this);
        candleGraphMonth.setOriginalDataList(GraphUtils.loadKLineGraphData(getBaseActivity(), GraphType.KLINE_MONTH));
        graphViews.add(candleGraphMonth);

    }
}
