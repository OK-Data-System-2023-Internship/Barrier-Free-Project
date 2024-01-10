package com.example.testproject;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.abs;

public class FingerPaintView extends View {
    private Path path = new Path();
    private Bitmap drawingBitmap;
    private Canvas drawingCanvas;
    private Paint drawingPaint = new Paint(Paint.DITHER_FLAG);
    private float penX = 0f;
    private float penY = 0f;
    private Paint pen = buildDefaultPen();
    private boolean empty = true;
    private OnWriteEventListener eventListener;
    private final long WRITE_TIME = 700;

    private final Handler timerHandler = new Handler();
    public boolean isEmpty () {
        return empty;
    }

    public FingerPaintView(Context context, AttributeSet attrs) {super(context, attrs);}

    private Runnable logRunnable = new Runnable() {
        @Override
        public void run() {
            Log.v("FingerPaintView",  WRITE_TIME + "밀리초 경과");
            eventListener.onWriteFinish();
        }
    };

    public void setCustomEventListener(OnWriteEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawingBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawingCanvas = new Canvas(drawingBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(drawingBitmap, 0f, 0f, drawingPaint);
        canvas.drawPath(path, pen);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) return false;
        empty = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchStart(event.getX(), event.getY());
                invalidate();
                timerHandler.removeCallbacks(logRunnable);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp();
                performClick();
                invalidate();
                timerHandler.postDelayed(logRunnable, WRITE_TIME);
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void clear() {
        path.reset();
        drawingBitmap = Bitmap.createBitmap(drawingBitmap.getWidth(), drawingBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        drawingCanvas = new Canvas(drawingBitmap);
        empty = true;
        invalidate();
    }

    public Bitmap exportToBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        draw(canvas);
        return bitmap;
    }

    public Bitmap exportToBitmap(int width, int height) {
        Bitmap rawBitmap = exportToBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(rawBitmap, width, height, false);
        rawBitmap.recycle();
        return scaledBitmap;
    }

    private void onTouchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        penX = x;
        penY = y;
    }

    private void onTouchMove(float x, float y) {
        float dx = abs(x - penX);
        float dy = abs(y - penY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(penX, penY, (x + penX) / 2, (y + penY) / 2);
            penX = x;
            penY = y;
        }
    }

    private void onTouchUp() {
        path.lineTo(penX, penY);
        drawingCanvas.drawPath(path, pen);
        path.reset();
    }

    private static final float TOUCH_TOLERANCE = 4f;
    private static final float PEN_SIZE = 48f;

    private static Paint buildDefaultPen() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(PEN_SIZE);
        return paint;
    }
}


