package com.zendalona.mathmantra.ui.learn.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Surface extends View {

    private static final float MAX_SCALE = 5.0f;
    private static final float MIN_SCALE = 0.1f;

    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private Matrix transformMatrix = new Matrix();
    private Matrix inverseMatrix = new Matrix();

    private float[] touchPoint = new float[2];
    private float lastTouchX, lastTouchY;
    private int activePointerId = -1;

    private ScaleGestureDetector scaleDetector;
    private boolean isScaling = false;

    public Surface(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSurface();
    }

    private void initSurface() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        path = new Path();
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.concat(transformMatrix);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                activePointerId = event.getPointerId(0);
                lastTouchX = event.getX();
                lastTouchY = event.getY();

                transformMatrix.invert(inverseMatrix);
                touchPoint[0] = lastTouchX;
                touchPoint[1] = lastTouchY;
                inverseMatrix.mapPoints(touchPoint);

                path.moveTo(touchPoint[0], touchPoint[1]);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (!isScaling) {
                    int pointerIndex = event.findPointerIndex(activePointerId);
                    float x = event.getX(pointerIndex);
                    float y = event.getY(pointerIndex);

                    float dx = x - lastTouchX;
                    float dy = y - lastTouchY;

                    transformMatrix.invert(inverseMatrix);
                    touchPoint[0] = x;
                    touchPoint[1] = y;
                    inverseMatrix.mapPoints(touchPoint);

                    path.lineTo(touchPoint[0], touchPoint[1]);

                    transformMatrix.postTranslate(dx, dy);
                    lastTouchX = x;
                    lastTouchY = y;
                }
                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP: {
                bitmapCanvas.drawPath(path, paint);
                path.reset();
                activePointerId = -1;
                invalidate();
                break;
            }

            case MotionEvent.ACTION_CANCEL:
                activePointerId = -1;
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == activePointerId) {
                    final int newIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = event.getX(newIndex);
                    lastTouchY = event.getY(newIndex);
                    activePointerId = event.getPointerId(newIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isScaling = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float currentScale = getCurrentScale();

            float newScale = currentScale * scaleFactor;
            if (newScale > MAX_SCALE) scaleFactor = MAX_SCALE / currentScale;
            else if (newScale < MIN_SCALE) scaleFactor = MIN_SCALE / currentScale;

            transformMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            invalidate();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
        }
    }

    private float getCurrentScale() {
        float[] values = new float[9];
        transformMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    public void clearSurface() {
        bitmapCanvas.drawColor(Color.WHITE);
        path.reset();
        invalidate();
    }

    public Bitmap getBitmap() {
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas resultCanvas = new Canvas(result);
        draw(resultCanvas);
        return result;
    }
}
