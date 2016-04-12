package com.example.wytings.graph;

import android.content.Context;
import android.graphics.Color;

import com.example.wytings.utils.MyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.FileReader;
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

    public static void saveJson(Context context, Object list, String fileName) {
        try {
            Gson gson = new Gson();
            String gString = gson.toJson(list);
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(gString.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            MyLog.e(e);
        }
    }

    public static int getColor(boolean isRed) {
        if (isRed) {
            return RED;
        } else {
            return GREEN;
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
            List<TimesModel> result = gson.fromJson(new FileReader());
            return result;
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
            List<KLineModel> result = gson.fromJson(new FileReader(context.getAssets().openNonAssetFd(fileName).getFileDescriptor()),
                    new TypeToken<List<KLineModel>>() {
                    }.getType());
            return result;
        } catch (Exception e) {
            MyLog.e(e);
        }
        return null;
    }
}
