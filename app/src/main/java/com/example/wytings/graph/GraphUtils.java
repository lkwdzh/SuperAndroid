package com.example.wytings.graph;

import android.content.Context;
import android.graphics.Color;

import com.example.wytings.utils.MyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public class GraphUtils {
    private static final String TIMES_ONE = "times_one";
    private static final String TIMES_FIVE = "times_five";
    private static final String KLINE_DAY = "kline_day";
    private static final String KLINE_WEEK = "kline_week";
    private static final String KLINE_MONTH = "kline_month";

    private static final int RED = Color.parseColor("#F54642");
    private static final int GREEN = Color.parseColor("#29B32E");

    public static int getColor(boolean isRed) {
        if (isRed) {
            return RED;
        } else {
            return GREEN;
        }
    }

    public static String getValueInfo(double value, boolean returnUnit) {
        String unit;
        String result;
        if (Math.abs(value) >= 1000000000000L) {
            result = String.format("%.2f", value / 1000000000000L);
            unit = "万亿";
        } else if (Math.abs(value) >= 100000000) {
            result = String.format("%.2f", value / 100000000);
            unit = "亿";
        } else if (Math.abs(value) >= 10000) {
            result = String.format("%.2f", value / 10000);
            unit = "万";
        } else {
            result = String.format("%.2f", value);
            unit = "";
        }
        if (returnUnit) {
            return unit;
        } else {
            return result;
        }
    }

    public static List<TimesModel> loadTimesGraphData(Context context, GraphType type) {
        String fileName = null;
        switch (type) {
            case TIMELINE_ONE:
                fileName = TIMES_ONE;
                break;
            case TIMELINE_FIVE:
                fileName = TIMES_FIVE;
                break;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(StreamToString(context.getAssets().open(fileName)), new TypeToken<List<TimesModel>>() {
            }.getType());
        } catch (Exception e) {
            MyLog.e(e);
        }
        return null;
    }

    public static List<KLineModel> loadKLineGraphData(Context context, GraphType type) {
        String fileName = null;
        switch (type) {
            case KLINE_DAY:
                fileName = KLINE_DAY;
                break;
            case KLINE_WEEK:
                fileName = KLINE_WEEK;
                break;
            case KLINE_MONTH:
                fileName = KLINE_MONTH;
                break;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(StreamToString(context.getAssets().open(fileName)),
                    new TypeToken<List<KLineModel>>() {
                    }.getType());
        } catch (Exception e) {
            MyLog.e(e);
        }
        return null;
    }

    private static String StreamToString(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
