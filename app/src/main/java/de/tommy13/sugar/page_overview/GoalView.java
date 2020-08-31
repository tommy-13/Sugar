package de.tommy13.sugar.page_overview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import de.tommy13.sugar.R;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 28.03.2017.
 * View to draw the history of the nutrients.
 */

public class GoalView extends View {

    private final float MAX_PROGRESS_FACTOR = 1.2f;
    private final float GOAL_LINE_THICKNESS = 4f;
    private final float BORDER_THICKNESS    = 6f;


    private float goal, currentValue;
    private float goalLine, progressRight;
    private Paint goalLinePaint, backPaint, borderPaint;
    private Paint currentForeground;
    private Paint redPaint, yellowPaint, greenPaint;

    private float width, height;
    private float baseline, topline;
    private float leftline, rightline;
    private float realWidth;


    public GoalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_border));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(BORDER_THICKNESS);
        borderPaint.setStrokeJoin(Paint.Join.ROUND);

        backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_back));
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setStrokeJoin(Paint.Join.ROUND);

        goalLinePaint = new Paint();
        goalLinePaint.setAntiAlias(true);
        goalLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_goal));
        goalLinePaint.setStrokeWidth(GOAL_LINE_THICKNESS);
        goalLinePaint.setStrokeJoin(Paint.Join.ROUND);

        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_bad));
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setStrokeJoin(Paint.Join.ROUND);

        yellowPaint = new Paint();
        yellowPaint.setAntiAlias(true);
        yellowPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_attention));
        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setStrokeJoin(Paint.Join.ROUND);

        greenPaint = new Paint();
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_good));
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setStrokeJoin(Paint.Join.ROUND);
    }



    public void setData(float goal, float currentValue) {
        this.goal         = goal;
        this.currentValue = currentValue;
        invalidate();

        width      = getWidth();
        height     = getHeight();
        topline    = 0.0f * height;
        baseline   = 1.0f * height;
        leftline   = 0.0f * width;
        rightline  = 1.0f * width;
        realWidth  = rightline - leftline;

        if (currentValue < MAX_PROGRESS_FACTOR * goal) {
            if (currentValue < goal * AppPreferences.YELLOW_PERCENTAGE / 100) {
                currentForeground = greenPaint;
            } else if (currentValue < goal) {
                currentForeground = yellowPaint;
            } else {
                currentForeground = redPaint;
            }
            goalLine      = leftline + realWidth / MAX_PROGRESS_FACTOR;
            progressRight = leftline + (currentValue * realWidth) / (MAX_PROGRESS_FACTOR * goal);
        } else {
            currentForeground = redPaint;
            goalLine          = leftline + realWidth * goal / currentValue;
            progressRight     = leftline + realWidth;
        }

    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setData(goal, currentValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // background progressbar
        canvas.drawRect(leftline, topline, realWidth, baseline, backPaint);

        // foreground progressbar
        canvas.drawRect(leftline, topline, progressRight, baseline, currentForeground);

        // goal line
        canvas.drawLine(goalLine, topline, goalLine, baseline, goalLinePaint);

        // border progressbar
        canvas.drawRect(leftline, topline, realWidth, baseline, borderPaint);
    }

}
