package com.zendalona.mathmantra.ui.learn.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.CanvasKt;

import java.util.ArrayList;

public class Surface extends View {

    private Paint paint;
    private Path path;
    private ArrayList<Path> pathList = new ArrayList<>();
    private ArrayList<Paint> paintList = new ArrayList<>();
    private Bitmap bitmap;
    private Canvas canvas;



    public Surface(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSurface();
    }

    private void initSurface() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        path = new Path();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i<pathList.size(); i+=1) {
            canvas.drawPath(pathList.get(i), paintList.get(i));
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(x, y);
                pathList.add(path);

                Paint tempPaint = new Paint(paint);
                paintList.add(tempPaint);
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    public void clearSurface() {
        pathList.clear();
        paintList.clear();
        path.reset();
        invalidate();
    }

    public Bitmap getBitmap() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}
