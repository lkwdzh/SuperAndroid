package com.example.wytings.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public abstract class GridGraph extends View {

    private static float TEXT_AREA_LEFT_AND_RIGHT = 130;
    private static float TEXT_AREA_BOTTOM = 40;
    private static float BLANK_AREA_TOP = 50;
    private boolean isTextInside = false;
    private boolean isRightEmpty = false;
    public static int BACKGROUND_COLOR = Color.parseColor("#f9f9f7");
    public float TEXT_SIZE_AXIS = 23;
    public int LINE_COLOR_AXIS = Color.parseColor("#e0e0e0");
    public int TEXT_COLOR_AXIS = Color.parseColor("#868686");

    public float left, top, right, bottom;
    public Paint paint = new Paint();
    public Path path = new Path();
    public Rect rect = new Rect();

    public GridGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        baseInit();
    }

    public GridGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        baseInit();
    }

    public GridGraph(Context context) {
        super(context);
        baseInit();
    }

    public void setIsTextInside(boolean isTextInside) {
        this.isTextInside = isTextInside;
    }

    public boolean isTextInside() {
        return this.isTextInside;
    }

    public void setIsRightEmpty(boolean isRightEmpty) {
        this.isRightEmpty = isRightEmpty;
    }

    private void baseInit() {
        this.setClickable(true);
        init();
    }

    abstract void init();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(BACKGROUND_COLOR);
        fixLayoutValues();
    }

    public void resetLayout(String maxString) {
        if (!isTextInside()) {
            rect.setEmpty();
            paint.reset();
            paint.setTextSize(TEXT_SIZE_AXIS);
            paint.getTextBounds(maxString, 0, maxString.length(), rect);
            left = rect.right + rect.left + 50;
        }
    }

    private void fixLayoutValues() {
        if (isTextInside) {
            left = 3;
            top = 3;
            right = getWidth() - 3;
            bottom = getHeight() - TEXT_AREA_BOTTOM;
        } else {
            left = TEXT_AREA_LEFT_AND_RIGHT;
            top = BLANK_AREA_TOP;
            right = getWidth() - TEXT_AREA_LEFT_AND_RIGHT;
            bottom = getHeight() - TEXT_AREA_BOTTOM;
            if (isRightEmpty) {
                right = getWidth() - 3;
            }
        }
    }

    public void drawTextBelowPoint(String text, float x, float y, Paint.Align align, Paint paint, Canvas canvas) {
        paint.setTextAlign(align);
        rect.setEmpty();
        paint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, x, y + rect.height() / 2, paint); // 坐标点在文字正上方
    }

    public void drawTextArea(String text, float x, float y, Paint.Align align, boolean isBottom, Paint paint, Canvas canvas) {
        paint.reset();
        paint.setTextSize(TEXT_SIZE_AXIS);
        paint.setTextAlign(align);
        paint.setStyle(Paint.Style.FILL);
        rect.setEmpty();
        paint.getTextBounds(text, 0, text.length(), rect);

        if (isBottom) {
            paint.setColor(0xe0222020);
            canvas.drawRect(x - rect.width() / 2, y - rect.height(), x + rect.width() / 2, y + rect.height() / 2, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(text, x, y + rect.height() / 4, paint); // 坐标点在文字正中间
        } else {
            paint.setColor(0xe0222020);
            canvas.drawRect(x - rect.width(), y - rect.height(), x, y + rect.height() / 2, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(text, x, y + rect.height() / 4, paint); // 坐标点在文字正中间
        }

        paint.reset();
    }

    public interface OnMoveListener {
        void onMove(Object object);
    }

}
