package com.example.wytings.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public class TimesGraph extends GridGraph {

    private float X_LEFT_TEXT_OFFSET = 12;
    private float X_RIGHT_TEXT_OFFSET = 12;
    private float Y_BOTTOM_TEXT_OFFSET = 20;
    private Paint dashPaint = new Paint();
    public DashPathEffect dashPathEffect = new DashPathEffect(new float[]{10, 5}, 1);
    private int averageColor = Color.parseColor("#F5D000");
    private int blackDetailLine = Color.parseColor("#222020");
    private int[] redGradient = new int[]{0xFFf44542, 0xfff45f26, 0xFFf9c197};
    private int[] greenGradient = new int[]{0xFF29b32e, 0xFF5db329, 0xFFaedb5e};
    private int[] grayGradient = new int[]{0x99abadb1, 0x99dfe0e4};
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private float RATE_OFFSET = 0.005f;
    private float Y_DEFAULT_DIVISION = 7;
    private float DEFAULT_PILLAR_RATE = 0.7f;
    private int pointNumber = 330;
    private float yInterval;
    private float upperBottom;
    private float xPointInterval;
    private float rateUpperY;
    private float rateLowerY;

    private float touchX;
    private boolean showDetails;
    private boolean isMovable = true;
    private int dayNumber;

    private float minPrice;
    private float maxPrice;
    private float maxVolume;
    private boolean isBottomVisible = true;
    private float previousClosePrice = 160.2f;
    private List<TimesModel> timesModels;
    private List<String> horizontalTitles;
    private List<PrivateModel> verticalTitles = new ArrayList<>();
    private boolean isCenter;
    private OnMoveListener onMoveListener;
    private boolean activeLongPress;
    private MotionEvent lastMotionEvent;
    private boolean isScrolling;
    private GestureDetector gestureDetector;

    public TimesGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimesGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimesGraph(Context context) {
        super(context);
    }

    public void setBottomVisible(boolean isVisible) {
        this.isBottomVisible = isVisible;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    @Override
    void init() {
        setIsTextInside(false);
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
                if (Math.abs(distanceX) < 1) {
                    isScrolling = false;
                } else {
                    isScrolling = true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    public void setActiveLongPress(boolean activeLongPress) {
        this.activeLongPress = activeLongPress;
        if (lastMotionEvent != null) {
            lastMotionEvent.setAction(MotionEvent.ACTION_MOVE);
            onTouchEvent(lastMotionEvent);
        }
    }

    public boolean getActiveLongPress() {
        return this.activeLongPress;
    }

    public void setTimesModels(List<TimesModel> timesModels) {
        this.timesModels = timesModels;
    }

    public void setMovable(boolean enable) {
        isMovable = enable;
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
        overrideLongClickListener(onMoveListener != null);
    }

    private void overrideLongClickListener(boolean isOverride) {
        if (!isOverride)
            return;
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!getActiveLongPress() && !isScrolling) {
                    setActiveLongPress(true);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (timesModels == null || !isMovable) {
            return super.onTouchEvent(event);
        }
        gestureDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                touchX = event.getRawX();
                if (touchX < left || touchX > left + timesModels.size() * xPointInterval) {
                    return false;
                }
                if (activeLongPress) {
                    showDetails = true;
                }
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                showDetails = false;
                activeLongPress = false;
                isScrolling = false;
                postInvalidate();
                break;

            default:
                break;
        }
        lastMotionEvent = event;
        return super.onTouchEvent(event);
    }

    private void drawDetails(Canvas canvas) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!isMovable || onMoveListener == null) {
            return;
        }
        if (showDetails && touchX > left && touchX < right) {
            // 绘制点击线条及详情区域
            paint.reset();
            paint.setColor(blackDetailLine);

            canvas.drawLine(touchX, 2, touchX, bottom, paint);

            TimesModel model;
            try {
                //获取数据
                model = timesModels.get((int) ((touchX - left) / xPointInterval));
            } catch (Exception e) {
                Log.e("wytings", "exception occurs when getting data");
                return;
            }
            float endWhiteY = getUpperYDistance(model.getLastPrice());
            canvas.drawLine(left, endWhiteY, right, endWhiteY, paint);
            canvas.drawCircle(touchX, endWhiteY, 5, paint);

            String price = "" + model.getLastPrice();
            String ratio = decimalFormat.format(model.getChangeRatio()) + "%";
            drawTextArea(price, left, endWhiteY, Paint.Align.RIGHT, false, paint, canvas);
            drawTextArea(ratio, right, endWhiteY, Paint.Align.RIGHT, false, paint, canvas);
            drawTextArea(model.getTime(), touchX, bottom + Y_BOTTOM_TEXT_OFFSET, Paint.Align.CENTER, true, paint, canvas);
            this.onMoveListener.onMove(model);
        } else {
            this.onMoveListener.onMove(null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!checkBasicData()) {
            return;
        }
        drawVerticalAxisWithText(canvas);
        drawGradientGraph(canvas);
        drawHorizontalAxisWithText(canvas);
        drawTradeVolume(canvas);
        drawAverageLine(canvas);
        drawDashLine(canvas);
        drawDetails(canvas);
    }

    private void drawHorizontalAxisWithText(Canvas canvas) {
        if (horizontalTitles == null || horizontalTitles.isEmpty()) {
            return;
        }

        rect.setEmpty();
        paint.reset();
        paint.setColor(TEXT_COLOR_AXIS);
        paint.setTextSize(TEXT_SIZE_AXIS);

        float xInterval;
        if (isCenter) {
            xInterval = (right - left) / (horizontalTitles.size());
            for (int i = 0; i < horizontalTitles.size(); i++) {
                String text = horizontalTitles.get(i);
                float xDistance = (float) (left + (0.5 + i) * xInterval);
                drawTextBelowPoint(text, xDistance, bottom + Y_BOTTOM_TEXT_OFFSET, Paint.Align.CENTER, paint, canvas);
            }
            return;
        }

        String extraText = horizontalTitles.get(horizontalTitles.size() - 1);
        paint.getTextBounds(extraText, 0, extraText.length(), rect);
        xInterval = (right - rect.width() - left) / (horizontalTitles.size() - 1);

        drawTextBelowPoint(extraText, right, bottom + Y_BOTTOM_TEXT_OFFSET, Paint.Align.RIGHT, paint, canvas);
        for (int i = 0; i < horizontalTitles.size() - 1; i++) {
            String text = horizontalTitles.get(i);
            float xDistance = left + i * xInterval;
            drawTextBelowPoint(text, xDistance, bottom + Y_BOTTOM_TEXT_OFFSET, Paint.Align.LEFT, paint, canvas);
        }
    }

    private void drawVerticalAxisWithText(Canvas canvas) {
        loadVerticalTitles(verticalTitles, maxPrice, minPrice);
        if (verticalTitles.isEmpty()) {
            return;
        }
        paint.reset();
        paint.setTextSize(TEXT_SIZE_AXIS);
        if (isTextInside()) {
            for (int i = 0; i < verticalTitles.size(); i++) {
                float y = top + yInterval * i;
                paint.setColor(LINE_COLOR_AXIS);
                canvas.drawLine(left, y, right, y, paint);
                paint.setColor(TEXT_COLOR_AXIS);
                if (i == 0) {
                    PrivateModel privateModel = verticalTitles.get(i);
                    drawTextBelowPoint(privateModel.price, left + X_LEFT_TEXT_OFFSET, y + X_LEFT_TEXT_OFFSET, Paint.Align.LEFT, paint, canvas);
                    drawTextBelowPoint(privateModel.ratio, right - X_LEFT_TEXT_OFFSET, y + X_LEFT_TEXT_OFFSET, Paint.Align.RIGHT, paint, canvas);
                }
                if (i == verticalTitles.size() - 1) {
                    PrivateModel privateModel = verticalTitles.get(i);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(privateModel.price, left + X_LEFT_TEXT_OFFSET, y - X_LEFT_TEXT_OFFSET, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(privateModel.ratio, right - X_LEFT_TEXT_OFFSET, y - X_LEFT_TEXT_OFFSET, paint);
                }
            }
            return;
        }

        for (int i = 0; i < verticalTitles.size(); i++) {
            float y = top + yInterval * i;
            paint.setColor(TEXT_COLOR_AXIS);
            canvas.drawLine(left, y, right, y, paint);
            PrivateModel privateModel = verticalTitles.get(i);
            if (i < verticalTitles.size() - 1) {
                drawTextBelowPoint(privateModel.price, left - X_LEFT_TEXT_OFFSET, y, Paint.Align.RIGHT, paint, canvas);
                drawTextBelowPoint(privateModel.ratio, right + X_RIGHT_TEXT_OFFSET, y, Paint.Align.LEFT, paint, canvas);
            } else {
                drawTextBelowPoint(GraphUtils.getValueInfo(maxVolume, false), left - X_LEFT_TEXT_OFFSET, y, Paint.Align.RIGHT, paint, canvas);
                drawTextBelowPoint(privateModel.ratio, right + X_RIGHT_TEXT_OFFSET, y, Paint.Align.LEFT, paint, canvas);
            }
        }

        canvas.drawLine(left, bottom, right, bottom, paint);
        String text = GraphUtils.getValueInfo(maxVolume, true) + "股";
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(text, left - X_LEFT_TEXT_OFFSET, bottom, paint);
    }

    private void loadVerticalTitles(List<PrivateModel> titles, float max, float min) {
        titles.clear();
        float inter = (max - min) / 5;
        for (int i = 0; i <= 5; i++) {
            float price = max - inter * i;
            String priceStr = decimalFormat.format(price);
            String ratioStr = decimalFormat.format((price - previousClosePrice) / previousClosePrice * 100) + "%";
            titles.add(new PrivateModel(priceStr, ratioStr));
        }
    }

    private void drawDashLine(Canvas canvas) {
        path.reset();
        dashPaint.reset();
        dashPaint.setColor(blackDetailLine);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(dashPathEffect);
        float yDistance = getUpperYDistance(previousClosePrice);
        path.moveTo(left, yDistance);
        path.lineTo(right, yDistance);
        dashPaint.setPathEffect(dashPathEffect);
        canvas.drawPath(path, dashPaint);
    }

    private void drawGradientGraph(Canvas canvas) {

        paint.reset();
        path.reset();
        paint.setStyle(Paint.Style.STROKE);

        float xDistance = 0;
        float yDistance;
        for (int i = 0; i < timesModels.size(); i++) {
            xDistance = left + xPointInterval * i;
            yDistance = getUpperYDistance(timesModels.get(i).getLastPrice());
            if (i == 0) {
                path.moveTo(xDistance, yDistance);
            } else {
                path.lineTo(xDistance, yDistance);
            }

        }

        int[] gradient;
        boolean isGreenUpRedDown = true;
        if (timesModels.size() < 510) {
            if (timesModels.get(timesModels.size() - 1).getChangeRatio() > 0) {
                gradient = redGradient;
                paint.setColor(redGradient[0]);
                if (isGreenUpRedDown) {
                    gradient = greenGradient;
                    paint.setColor(greenGradient[0]);
                }
            } else if (timesModels.get(timesModels.size() - 1).getChangeRatio() < 0) {
                gradient = greenGradient;
                paint.setColor(greenGradient[0]);
                if (isGreenUpRedDown) {
                    gradient = redGradient;
                    paint.setColor(redGradient[0]);
                }
            } else {
                gradient = grayGradient;
                paint.setColor(grayGradient[0]);
            }
        } else {
            float beginPrice = timesModels.get(0).getLastPrice();
            float endPrice = timesModels.get(timesModels.size() - 1).getLastPrice();
            if (beginPrice < endPrice) {
                gradient = redGradient;
                paint.setColor(redGradient[0]);
                if (isGreenUpRedDown) {
                    gradient = greenGradient;
                    paint.setColor(greenGradient[0]);
                }
            } else if (beginPrice > endPrice) {
                gradient = greenGradient;
                paint.setColor(greenGradient[0]);
                if (isGreenUpRedDown) {
                    gradient = redGradient;
                    paint.setColor(redGradient[0]);
                }
            } else {
                gradient = grayGradient;
                paint.setColor(grayGradient[0]);
            }

        }

        paint.setStrokeWidth(3);
        canvas.drawPath(path, paint); //draw line

        path.lineTo(xDistance + xPointInterval, bottom);
        path.lineTo(left, bottom);
        path.close();

        paint.setAlpha(180);
        paint.setShader(new LinearGradient(left, getUpperYDistance(maxPrice), left, bottom, gradient, null, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

    }

    private void drawTradeVolume(Canvas canvas) {
        if (!isBottomVisible) {
            return;
        }
        paint.reset();
        paint.setColor(Color.WHITE);
        float xDistance;
        TimesModel model;
        for (int i = 0; i < timesModels.size(); i++) {
            xDistance = left + xPointInterval * i;
            model = timesModels.get(i);
            canvas.drawRect(xDistance, bottom - model.getTradeVolume() * rateLowerY, xDistance + xPointInterval * DEFAULT_PILLAR_RATE, bottom, paint); // 成交量
        }
    }

    private void drawAverageLine(Canvas canvas) {
        paint.reset();
        path.reset();
        paint.setColor(averageColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        float xDistance = left;
        float yDistance = getUpperYDistance(timesModels.get(0).getAveragePrice());
        path.moveTo(xDistance, yDistance);

        for (int i = 1; i < timesModels.size(); i++) {
            xDistance = left + xPointInterval * i;
            yDistance = getUpperYDistance(timesModels.get(i).getAveragePrice());
            path.lineTo(xDistance, yDistance);
        }
        canvas.drawPath(path, paint);

    }

    private boolean checkBasicData() {
        if (timesModels == null || timesModels.isEmpty()) {
            maxPrice = minPrice = -1;
            return false;
        }

        if (dayNumber == 5) {
            pointNumber = pointNumber * 5;
        }

        minPrice = timesModels.get(0).getLastPrice();
        maxPrice = timesModels.get(0).getLastPrice();
        maxVolume = timesModels.get(0).getTradeVolume();
        for (TimesModel model : timesModels) {
            if (model.getLastPrice() != 0 && model.getLastPrice() < minPrice) {
                minPrice = model.getLastPrice();
            }
            if (model.getAveragePrice() < minPrice) {
                minPrice = model.getAveragePrice();
            }
            if (model.getLastPrice() > maxPrice) {
                maxPrice = model.getLastPrice();
            }
            if (model.getTradeVolume() > maxVolume) {
                maxVolume = model.getTradeVolume();
            }
        }

        maxPrice = maxPrice < previousClosePrice ? previousClosePrice : maxPrice;
        minPrice = minPrice > previousClosePrice ? previousClosePrice : minPrice;
        if (Float.compare(maxPrice, minPrice) == 0) {
            float tempMax = maxPrice;
            maxPrice = tempMax * (1 + 0.1f);
            minPrice = tempMax * (1 - 0.1f);
        }

        yInterval = (bottom - top) / Y_DEFAULT_DIVISION;
        upperBottom = top + yInterval * (Y_DEFAULT_DIVISION - 2);
        xPointInterval = (right - left) / pointNumber;
        rateUpperY = (upperBottom - top) / (maxPrice - minPrice);
        rateLowerY = (bottom - upperBottom) / maxVolume;

        if (previousClosePrice == 0) {
            previousClosePrice = (maxPrice + minPrice) / 2;
        }

        horizontalTitles = new ArrayList<>();
        if (timesModels.size() < 400) {
            horizontalTitles.add("09:30");
            horizontalTitles.add("12:00/13:00");
            horizontalTitles.add("16:00");
            isCenter = false;
        } else {
            String temp = null;
            for (TimesModel model : timesModels) {
                String date = model.getDate();
                if (date.equals(temp)) {
                    continue;
                } else {
                    horizontalTitles.add(date.substring(5));
                    temp = date;
                }
            }
            isCenter = true;
        }
        resetLayout(decimalFormat.format(maxPrice));
        return true;
    }

    private float getUpperYDistance(float value) {
        if (value == 0) {
            value = minPrice;
        }
        return top + (maxPrice - value) * rateUpperY;
    }

    private class PrivateModel {
        String price;
        String ratio;

        public PrivateModel(String price, String ratio) {
            this.price = price;
            this.ratio = ratio;
        }
    }
}
