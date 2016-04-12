package com.example.wytings.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public class CandleGraph extends GridGraph {

    private static int BLANK_LINE_COLOR = Color.parseColor("#222020");
    private static int MA5_COLOR = Color.parseColor("#fa7f2f");
    private static int MA10_COLOR = Color.parseColor("#b44d78");
    private static int MA20_COLOR = Color.parseColor("#46b7c4");

    private static float X_LEFT_TEXT_OFFSET = 12;
    private static float Y_BOTTOM_TEXT_OFFSET = 20;
    private float Y_DEFAULT_DIVISION = 7;
    private float DEFAULT_PILLAR_RATE = 0.7f;
    private static final int DEFAULT_CANDLE_MIN_COUNT = 30;
    private static final int DEFAULT_CANDLE_MAX_COUNT = 200;

    private List<KLineModel> originalDataList = new ArrayList<>();
    private List<KLineModel> currentDataList;
    private float yInterval;
    private float upperBottom;
    private float xPointInterval;
    private float rateUpperY;
    private int startIndex, endIndex;

    private float touchX;
    private boolean showDetails;
    private boolean isScrollable;
    private OnMoveListener onMoveListener;
    private boolean activeLongPress;
    private MotionEvent lastMotionEvent;
    private boolean isScrolling;
    private float lastValue = 0;
    private ActionType flingFlag = null;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private float minPrice;
    private float maxPrice;
    private float maxVolume;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private Scroller scroller;

    public CandleGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CandleGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandleGraph(Context context) {
        super(context);
    }

    public void setOriginalDataList(List<KLineModel> originalDataList) {
        resetCandleGraph();
        this.originalDataList = originalDataList;
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
        overrideLongClickListener(onMoveListener != null);
    }

    public void setActiveLongPress(boolean activeLongPress) {
        this.activeLongPress = activeLongPress;
        lastMotionEvent.setAction(MotionEvent.ACTION_MOVE);
        onTouchEvent(lastMotionEvent);
    }

    private void overrideLongClickListener(boolean isOverride) {
        if (!isOverride)
            return;
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isScrolling) {
                    setActiveLongPress(true);
                }
                return true;
            }
        });
    }

    @Override
    void init() {
        startIndex = 20;
        endIndex = 30;
        setIsTextInside(false);
        setIsRightEmpty(true);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                zoomGraph(detector.getScaleFactor());
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e1 == null || e2 == null) {
                    return false;
                }
                if (Math.abs(distanceX) < 1) {
                    isScrolling = false;
                } else {
                    isScrolling = true;
                    if (!activeLongPress) {
                        scrollGraph(e1.getRawX(), e2.getRawX());
                    }
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("wytings", "velocityX: " + velocityX + "velocityY: " + velocityY);
                flingFlag = velocityX < 0 ? ActionType.LEFT : ActionType.RIGHT;
                scroller.fling(0, 0, (int) Math.abs(velocityX), 0, 0, Integer.MAX_VALUE, 0, 0);
                return false;
            }
        });
        scroller = new Scroller(getContext());
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            Log.i("wytings", "velocityX:" + scroller.getStartX() + ", flingFlag:" + flingFlag);
            if (flingFlag == ActionType.LEFT) {
                resetScrollCurrentData(ActionType.LEFT);
            } else if (flingFlag == ActionType.RIGHT) {
                resetScrollCurrentData(ActionType.RIGHT);
            }
            postInvalidate();
        }
    }

    private void zoomGraph(float rate) {
        Log.i("wytings", "zoomGraph input rate: " + rate);
        if (lastValue == 0) {
            lastValue = rate;
        }
        float tempValue = Math.abs(rate - lastValue);
        if (tempValue > 0.1) {
            if (rate < lastValue) {
                //zoom in
                resetZoomData(ActionType.IN);
                lastValue = 0;
            } else {
                // zoom out
                resetZoomData(ActionType.OUT);
                lastValue = 0;
            }
        }
    }

    private void resetZoomData(ActionType zoomType) {
        if (zoomType == ActionType.IN) {
            if (currentDataList.size() < DEFAULT_CANDLE_MAX_COUNT && startIndex > 0 && endIndex < originalDataList.size() - 1) {
                Log.i("wytings", "zoom in");
                startIndex--;
                endIndex++;
                currentDataList.add(0, originalDataList.get(startIndex));
                currentDataList.add(originalDataList.get(endIndex));
            }
        } else if (zoomType == ActionType.OUT) {
            if (currentDataList.size() > DEFAULT_CANDLE_MIN_COUNT && endIndex > startIndex) {
                currentDataList.remove(0);
                currentDataList.remove(currentDataList.size() - 1);
                startIndex++;
                endIndex--;
            }
        }
    }


    private void scrollGraph(float startX, float endX) {
        if (lastValue == 0) {
            lastValue = Math.abs(startX - endX);
        }
        float currentDist = Math.abs(startX - endX);

        if (currentDist - lastValue > 20) {
            if (startX > endX) {
                Log.i("wytings", "left");
                resetScrollCurrentData(ActionType.LEFT);
                lastValue = 0;
            } else {
                Log.i("wytings", "right");
                resetScrollCurrentData(ActionType.RIGHT);
                lastValue = 0;
            }
        }
    }

    private void resetScrollCurrentData(ActionType wipeType) {
        if (wipeType == ActionType.LEFT) {
            if (endIndex < originalDataList.size() - 1) {
                endIndex++;
                currentDataList.add(currentDataList.size(), originalDataList.get(endIndex));
                currentDataList.remove(0);
                startIndex++;
            }
        } else if (wipeType == ActionType.RIGHT) {
            if (startIndex > 0) {
                startIndex--;
                currentDataList.add(0, originalDataList.get(startIndex));
                currentDataList.remove(currentDataList.size() - 1);
                endIndex--;
            }
        }
    }

    public void setScrollable(boolean scrollable) {
        this.isScrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lastMotionEvent = event;
        if (currentDataList == null) {
            return super.onTouchEvent(event);
        }
        if (event.getPointerCount() < 2 && onMoveListener != null && !showDetails && isScrollable) {
            gestureDetector.onTouchEvent(event);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (onMoveListener != null) {
                    if (event.getPointerCount() < 2) {
                        touchX = event.getRawX();
                        if (touchX < left || touchX > left + currentDataList.size() * xPointInterval) {
                            return false;
                        }
                        if (activeLongPress) {
                            showDetails = true;
                        }
                    } else if (isScrollable && !showDetails) {
                        scaleGestureDetector.onTouchEvent(event);
                    }
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                showDetails = false;
                isScrolling = false;
                activeLongPress = false;
                lastMotionEvent = null;
                lastValue = 0;
                postInvalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!checkBasicData()) {
            return;
        }

        drawHorizontalAxisWithText(canvas);
        drawVerticalAxisWithText(canvas);
        drawTradeVolume(canvas);
        drawCandleGraph(canvas);
        drawAverageLine(canvas);
        drawDetails(canvas);
    }

    private void drawHorizontalAxisWithText(Canvas canvas) {

        rect.setEmpty();
        paint.reset();
        paint.setColor(TEXT_COLOR_AXIS);
        paint.setTextSize(TEXT_SIZE_AXIS);

        List<PrivateModel> privateModels = new ArrayList<>();
        for (int i = 0; i < currentDataList.size(); i++) {
            if (currentDataList.get(i).isHead()) {
                privateModels.add(new PrivateModel(i, currentDataList.get(i).getDate()));
            }
        }

        int defaultNumber = 5;
        int extra = privateModels.size() / defaultNumber;
        while (extra >= 1) {
            int i = 0;
            while (i < privateModels.size()) {
                privateModels.remove(i++);
            }
            extra = privateModels.size() / defaultNumber;
        }

        for (PrivateModel model : privateModels) {
            String text = model.date;
            float xDistance = left + model.index * xPointInterval;
            drawTextBelowPoint(text, xDistance, bottom + Y_BOTTOM_TEXT_OFFSET, Paint.Align.CENTER, paint, canvas);
        }
    }

    private void drawDetails(Canvas canvas) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (onMoveListener == null) {
            return;
        }
        KLineModel kModel = null;
        if (showDetails) {
            // 绘制点击线条及详情区域
            paint.reset();
            paint.setColor(BLANK_LINE_COLOR);
            paint.setAlpha(100);
            int fixedInt = (int) ((touchX - left) / xPointInterval);
            try {
                //获取数据
                kModel = currentDataList.get(fixedInt);
            } catch (Exception e) {
                Log.e("wytings", "exception occurs when getting data");
                return;
            }
            touchX = left + fixedInt * xPointInterval + xPointInterval * DEFAULT_PILLAR_RATE / 2;
            canvas.drawLine(touchX, 2, touchX, bottom, paint);
            drawTextArea(kModel.getTime(), touchX, bottom + Y_BOTTOM_TEXT_OFFSET, Paint.Align.CENTER, true, paint, canvas);
            this.onMoveListener.onMove(kModel);
        } else {
            this.onMoveListener.onMove(null);
        }
    }

    private void drawVerticalAxisWithText(Canvas canvas) {
        paint.reset();
        paint.setTextSize(TEXT_SIZE_AXIS);
        paint.setColor(TEXT_COLOR_AXIS);
        List<Float> floats = loadVerticalTitles();

        for (int i = 0; i < floats.size(); i++) {
            float y = top + yInterval * i;
            canvas.drawLine(left, y, right, y, paint);
            String text1 = decimalFormat.format(floats.get(i));
            if (i < floats.size() - 1) {
                drawTextBelowPoint(text1, left - X_LEFT_TEXT_OFFSET, y, Paint.Align.RIGHT, paint, canvas);
            } else {
                drawTextBelowPoint(decimalFormat.format(maxVolume), left - X_LEFT_TEXT_OFFSET, y, Paint.Align.RIGHT, paint, canvas);
            }
        }
        canvas.drawLine(left, bottom, right, bottom, paint);
        String text = decimalFormat.format(maxVolume) + "股";
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(text, left - X_LEFT_TEXT_OFFSET, bottom, paint);
    }

    private KLineModel fixKLineModel(int i) {
        KLineModel kline = currentDataList.get(i);
        int j = i - 1;
        if (i == 0) {
            j = i + 1;
        }
        if (j >= currentDataList.size()) {
            return kline;
        }

        if (kline.getHighest() <= 0) {
            kline.setHighest(currentDataList.get(j).getHighest());
        }
        if (kline.getLowest() <= 0) {
            kline.setLowest(currentDataList.get(j).getLowest());
        }
        if (kline.getOpen() <= 0) {
            kline.setOpen(currentDataList.get(j).getOpen());
        }
        if (kline.getClose() <= 0) {
            kline.setClose(currentDataList.get(j).getClose());
        }
        return kline;
    }

    private void drawCandleGraph(Canvas canvas) {
        for (int i = 0; i < currentDataList.size(); i++) {
            float xDistance = left + xPointInterval * i;
            drawSingleCandle(xDistance, fixKLineModel(i), canvas);
        }
    }

    private void drawSingleCandle(float xDistance, KLineModel model, Canvas canvas) {
        float lineX = xDistance + (xPointInterval * DEFAULT_PILLAR_RATE) / 2;
        float lineTop = upperBottom - (model.getHighest() - minPrice) * rateUpperY;
        float lineBottom = upperBottom - (model.getLowest() - minPrice) * rateUpperY;
        float candleOpen = upperBottom - (model.getOpen() - minPrice) * rateUpperY;
        float candleClose = upperBottom - (model.getClose() - minPrice) * rateUpperY;

        paint.setColor(GraphUtils.getColor(Float.compare(model.getClose(), model.getOpen()) > 0));

        if (candleOpen > candleClose) {
            canvas.drawLine(lineX, lineBottom, lineX, lineTop, paint);
            canvas.drawRect(xDistance, candleClose, xDistance + (xPointInterval * DEFAULT_PILLAR_RATE), candleOpen, paint);
        } else {
            canvas.drawLine(lineX, lineBottom, lineX, lineTop, paint);
            canvas.drawRect(xDistance, candleOpen, xDistance + (xPointInterval * DEFAULT_PILLAR_RATE), candleClose, paint);
        }

    }

    private void drawTradeVolume(Canvas canvas) {
        float rateLowerY = (bottom - upperBottom) / maxVolume;
        paint.reset();
        float xDistance = 0;
        KLineModel model;
        for (int i = 0; i < currentDataList.size(); i++) {
            xDistance = left + xPointInterval * i;
            model = currentDataList.get(i);
            paint.setColor(GraphUtils.getColor(Float.compare(model.getClose(), model.getOpen()) > 0));
            canvas.drawRect(xDistance, bottom - model.getTradeVolume() * rateLowerY, xDistance + xPointInterval * DEFAULT_PILLAR_RATE, bottom, paint); // 成交量
        }
    }

    private List<Float> fixMAList(List<Float> MAList) {
        List<Float> result = new ArrayList<>();
        for (Float f : MAList) {
            if (f <= 0) {
                result.add(minPrice);
            } else {
                result.add(f);
            }
        }
        return result;
    }

    private void drawAverageLine(Canvas canvas) {
        paint.reset();
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        float xDistance;
        float yDistance, yDistance1, yDistance2;
        List<Float> MAList;
        Path path1 = new Path();
        Path path2 = new Path();
        for (int i = 0; i < currentDataList.size(); i++) {
            MAList = fixMAList(currentDataList.get(i).getMAValues());
            xDistance = left + xPointInterval * i;
            yDistance = top + (maxPrice - MAList.get(0)) * rateUpperY;
            yDistance1 = top + (maxPrice - MAList.get(1)) * rateUpperY;
            yDistance2 = top + (maxPrice - MAList.get(2)) * rateUpperY;
            if (i == 0) {
                path.moveTo(xDistance, yDistance);
                path1.moveTo(xDistance, yDistance1);
                path2.moveTo(xDistance, yDistance2);
            } else {
                path.lineTo(xDistance, yDistance);
                path1.lineTo(xDistance, yDistance1);
                path2.lineTo(xDistance, yDistance2);
            }
        }
        paint.setColor(MA5_COLOR);
        canvas.drawPath(path, paint);

        paint.setColor(MA10_COLOR);
        canvas.drawPath(path1, paint);

        paint.setColor(MA20_COLOR);
        canvas.drawPath(path2, paint);
    }

    private List<Float> loadVerticalTitles() {
        List<Float> floats = new ArrayList<>();
        float inter = (maxPrice - minPrice) / 5;
        for (int i = 0; i <= 5; i++) {
            floats.add(maxPrice - inter * i);
        }
        return floats;
    }

    private List<KLineModel> copyKLine(List<KLineModel> data, int start, int end) {
        List<KLineModel> result = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            result.add(data.get(i));
        }
        return result;
    }

    public void resetCandleGraph() {
        if (originalDataList != null) {
            originalDataList.clear();
        }
        if (currentDataList != null) {
            currentDataList.clear();
        }
        maxPrice = 0;
        minPrice = 0;
        maxVolume = 0;
        startIndex = 0;
        endIndex = 0;
        yInterval = 0;
        upperBottom = 0;
        xPointInterval = 0;
        rateUpperY = 0;

    }

    @SuppressWarnings("unchecked")
    private boolean checkBasicData() {
        if (originalDataList == null || originalDataList.isEmpty()) {
            maxPrice = minPrice = -1;
            return false;
        }

        if (currentDataList == null || currentDataList.isEmpty()) {
            if (originalDataList.size() < 120) {
                currentDataList = copyKLine(originalDataList, 0, originalDataList.size() - 1);
                startIndex = 0;
                endIndex = originalDataList.size() - 1;
            } else {
                currentDataList = copyKLine(originalDataList, originalDataList.size() - 120 + 1, originalDataList.size() - 1);
                startIndex = originalDataList.size() - 120 + 1;
                endIndex = originalDataList.size() - 1;
            }
        }

        minPrice = currentDataList.get(0).getLowest();
        maxPrice = currentDataList.get(0).getHighest();
        maxVolume = currentDataList.get(0).getTradeVolume();
        for (KLineModel model : currentDataList) {
            if (model.getLowest() != 0 && model.getLowest() < minPrice) {
                minPrice = model.getLowest();
            }
            if (model.getHighest() > maxPrice) {
                maxPrice = model.getHighest();
            }
            if (model.getTradeVolume() > maxVolume) {
                maxVolume = model.getTradeVolume();
            }
        }

        maxPrice = maxPrice * 1.05f;
        minPrice = minPrice * 0.95f;
        yInterval = (bottom - top) / Y_DEFAULT_DIVISION;
        upperBottom = top + yInterval * (Y_DEFAULT_DIVISION - 2);
        float x_count = DEFAULT_CANDLE_MIN_COUNT > currentDataList.size() ? DEFAULT_CANDLE_MIN_COUNT : currentDataList.size();
        xPointInterval = (right - left) / x_count;
        rateUpperY = (upperBottom - top) / (maxPrice - minPrice);
        resetLayout(decimalFormat.format(maxPrice));
        return true;
    }

    private class PrivateModel {
        String date;
        int index;

        public PrivateModel(int index, String date) {
            this.date = date;
            this.index = index;
        }
    }

    enum ActionType {
        LEFT, RIGHT, IN, OUT
    }

}
