package com.zendalona.mathmantra.ui.learn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.zendalona.mathmantra.R;

public class NumberLineView extends View {

    private int currentPosition;
    private int numberRangeStart;
    private int numberRangeEnd;
    private final String MASCOT_EMOJI = "\uD83E\uDDCD\u200D♂\uFE0F";

    private Paint linePaint;
    private Paint numberPaint;
    private Paint mascotPaint;

    private float gap;

    public NumberLineView(Context context) {
        super(context);
        init();
    }

    public NumberLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue));
        linePaint.setStrokeWidth(12f);

        numberPaint = new Paint();
        numberPaint.setColor(ContextCompat.getColor(getContext(), R.color.lightBlue));
        numberPaint.setTextSize(40f);

        mascotPaint = new Paint();
        mascotPaint.setTextSize(200f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNumberLine(canvas);
        drawMascot(canvas);
    }

    private void drawNumberLine(Canvas canvas) {
        float startX = getWidth() * 0.01f;
        float endX = getWidth() * 0.98f;
        float centerY = getHeight() / 2f;

        canvas.drawLine(0 , centerY, getWidth(), centerY, linePaint);

        gap = (endX - startX) / (numberRangeEnd - numberRangeStart);

        // Draw numbers on the number line
        Log.d("Drawing number line range : ", numberRangeStart + " to " + numberRangeEnd);
        for (int number = numberRangeStart; number <= numberRangeEnd; number++) {
            float x = startX + (number - numberRangeStart) * gap;
            canvas.drawText(String.valueOf(number), x, centerY + 50f, numberPaint);
        }
    }

    private void drawMascot(Canvas canvas) {
        float centerY = getHeight() / 2f;
        float mascotPosition = (currentPosition - numberRangeStart - 0.4f) * gap;
        if (mascotPosition < 0) mascotPosition = 0;
        if (mascotPosition > getWidth()) mascotPosition = getWidth();
        canvas.drawText(MASCOT_EMOJI, mascotPosition , centerY - 50f, mascotPaint);
    }

    public void updateNumberLine(int start, int end, int position) {
        this.numberRangeStart = start;
        this.numberRangeEnd = end;
        this.currentPosition = position;
        invalidate();
    }

    public int moveLeft() {
        currentPosition--;
        invalidate();
        return currentPosition;
    }

    public int moveRight() {
        currentPosition++;
        invalidate();
        return currentPosition;
    }

}
