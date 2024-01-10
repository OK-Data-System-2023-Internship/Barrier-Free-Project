package com.example.testproject;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CircleView extends View {
    private List<Integer> circleColors = new ArrayList<>();
    private Paint paint;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void addCircle(int color) {
        circleColors.add(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = 15; // 원의 반지름

        for (int i = 0; i < circleColors.size(); i++) {
            paint.setColor(circleColors.get(i));
            canvas.drawCircle(centerX, centerY, radius, paint);
            centerY += 2 * radius; // 다음 원을 그리기 위해 Y 좌표 이동
        }
    }
}
