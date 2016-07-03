package com.example.wytings.activity;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.wytings.R;
import com.example.wytings.graph.CandleGraph;
import com.example.wytings.graph.GraphType;
import com.example.wytings.graph.GraphUtils;
import com.example.wytings.graph.GridGraph;
import com.example.wytings.graph.KLineModel;
import com.example.wytings.graph.TimesGraph;
import com.example.wytings.graph.TimesModel;
import com.example.wytings.utils.DataLoader;
import com.example.wytings.utils.MyLog;
import com.example.wytings.widget.TabClickableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/4/11.
 * https://github.com/wytings
 */
public class ActivityGraph extends BaseActivity {

    List<String> titles = new ArrayList<>();
    TabClickableLayout tabLayout;
    TimesGraph timesGraph;
    CandleGraph candleGraph;

    @Override
    protected void initialize() {
        setExtraContent(R.layout.activity_graph);
        tabLayout = getViewById(R.id.tabLayout);
        initGraph();
        titles.add("分时");
        titles.add("五日");
        titles.add("日K");
        titles.add("周K");
        titles.add("月K");
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MyLog.d("onTabSelected --- " + tab.getText() + " - " + tab.getPosition());
                loadGraphData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                MyLog.d("onTabUnselected --- " + tab.getText() + " - " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                MyLog.d("onTabReselected --- " + tab.getText() + " - " + tab.getPosition());
                loadGraphData(tab.getPosition());
            }
        });
        tabLayout.getTabAt(0).select();
    }

    private void initGraph() {
        timesGraph = getViewById(R.id.timesGraph);
        timesGraph.setOnMoveListener(new GridGraph.OnMoveListener() {
            @Override
            public void onMove(Object object) {
                TimesModel model = (TimesModel) object;
                MyLog.d("timesGraph ----- " + object);
                if (model == null) {
                } else {
                }
            }
        });
        candleGraph = getViewById(R.id.candleGraph);
        candleGraph.setOnMoveListener(new GridGraph.OnMoveListener() {
            @Override
            public void onMove(Object object) {
                KLineModel model = (KLineModel) object;
                MyLog.d("candleGraph ----- " + object);
                if (model == null) {
                } else {
                }
            }
        });
    }

    private void loadGraphData(final int index) {
        DataLoader.loadData(new DataLoader.DataLoaderCallback() {
            @Override
            public void onStart() {
                tabLayout.setTabEnable(false);
            }

            @Override
            public Object onCall() throws Exception {
                getGraphData(index);
                return null;
            }

            @Override
            public void onEnd(Object object) {
                updateGraph(index);
                tabLayout.setTabEnable(true);
            }
        });
    }

    private void getGraphData(int index) {
        GraphType type = GraphType.valueOf(index);
        switch (type) {
            case TIMELINE_ONE:
            case TIMELINE_FIVE:
                timesGraph.setTimesModels(GraphUtils.loadTimesGraphData(getBaseActivity(), type));
                break;
            case KLINE_DAY:
            case KLINE_WEEK:
            case KLINE_MONTH:
                candleGraph.setOriginalDataList(GraphUtils.loadKLineGraphData(getBaseActivity(), type));
                break;
        }
    }

    private void updateGraph(int index) {
        switch (GraphType.valueOf(index)) {
            case TIMELINE_ONE:
            case TIMELINE_FIVE:
                candleGraph.setVisibility(View.GONE);
                timesGraph.postInvalidate();
                timesGraph.setVisibility(View.VISIBLE);
                break;
            case KLINE_DAY:
            case KLINE_WEEK:
            case KLINE_MONTH:
                timesGraph.setVisibility(View.GONE);
                candleGraph.postInvalidate();
                candleGraph.setVisibility(View.VISIBLE);
                break;
        }
    }
}
