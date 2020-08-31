package de.tommy13.sugar.page_history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import de.tommy13.sugar.R;

/**
 * Created by tommy on 28.03.2017.
 * View to draw the history of the nutrients.
 */

public class HistoryView extends View {

    private static final float strokeThickness    = 3f;
    private static final float textThickness      = 2f;
    private static final float smallLineThickness = 1f;
    private static final int   alpha = 70;

    private HistoryDataSet dataSet;
    private boolean[]      useLabel;
    private Path           dataGraph;
    private Paint          dataGraphPaint;
    private Paint          smallLinePaint;
    private Paint          textPaintBelow;
    private Paint          textPaintLeft;

    private Paint redPaint, yellowPaint, greenPaint;


    private float baseline;
    private float textline;
    private float topline;
    private float leftline;
    private float rightline;
    private float partWidth;
    private float partWidthHalf;
    private float textGap;

    private String maxValue = "";


    public HistoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dataSet = new HistoryDataSet("");
        dataGraph = new Path();

        dataGraphPaint = new Paint();
        dataGraphPaint.setAntiAlias(true);
        dataGraphPaint.setColor(ContextCompat.getColor(getContext(), R.color.history_graph));
        dataGraphPaint.setStyle(Paint.Style.STROKE);
        dataGraphPaint.setStrokeJoin(Paint.Join.ROUND);
        dataGraphPaint.setStrokeWidth(strokeThickness);

        smallLinePaint = new Paint();
        smallLinePaint.setAntiAlias(true);
        smallLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.history_graph));
        smallLinePaint.setStyle(Paint.Style.STROKE);
        smallLinePaint.setStrokeJoin(Paint.Join.ROUND);
        smallLinePaint.setStrokeWidth(smallLineThickness);

        textPaintBelow = new Paint();
        textPaintBelow.setAntiAlias(true);
        textPaintBelow.setColor(ContextCompat.getColor(getContext(), R.color.history_graph));
        textPaintBelow.setStrokeWidth(textThickness);
        textPaintBelow.setTextAlign(Paint.Align.CENTER);

        textPaintLeft = new Paint();
        textPaintLeft.setAntiAlias(true);
        textPaintLeft.setColor(ContextCompat.getColor(getContext(), R.color.history_graph));
        textPaintLeft.setStrokeWidth(textThickness);
        textPaintLeft.setTextAlign(Paint.Align.LEFT);

        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_bad));
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setAlpha(alpha);
        redPaint.setStrokeJoin(Paint.Join.ROUND);
        redPaint.setStrokeWidth(smallLineThickness);

        yellowPaint = new Paint();
        yellowPaint.setAntiAlias(true);
        yellowPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_attention));
        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setAlpha(alpha);
        yellowPaint.setStrokeJoin(Paint.Join.ROUND);
        yellowPaint.setStrokeWidth(smallLineThickness);

        greenPaint = new Paint();
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(ContextCompat.getColor(getContext(), R.color.progressbar_good));
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setAlpha(alpha);
        greenPaint.setStrokeJoin(Paint.Join.ROUND);
        greenPaint.setStrokeWidth(smallLineThickness);
    }



    public void setData(HistoryDataSet dataSet) {
        this.dataSet = dataSet;
        dataGraph.reset();
        invalidate();

        float width = getWidth();
        float height = getHeight();
        topline    = 0.15f * height;
        baseline   = 0.85f * height;
        float realHeight = baseline - topline;

        // set the right text size
        float textSize = 0.09f * height;
        textPaintBelow.setTextSize(textSize);
        textPaintLeft.setTextSize(textSize);
        textGap    = 0.01f * width;
        textline    = baseline - textPaintBelow.ascent();

        leftline   = 0.0f * width;
        rightline  = 1.0f * width;
        float realWidth = rightline - leftline;

        maxValue  = dataSet.getMaxValue() + " " + dataSet.getUnit();

        if (dataSet.getSize() > 0) {
            // show only 10 labels
            useLabel = new boolean[dataSet.getSize()];
            int dist = (int) Math.ceil(dataSet.getSize() / 10.0);
            for (int i=0; i<dataSet.getSize(); i++) {
                useLabel[i] = ((i % dist) == 0);
            }


            partWidth = realWidth / dataSet.getSize();
            partWidthHalf = partWidth / 2.0f;

            float currentX = leftline + partWidthHalf;
            float currentY = baseline - dataSet.getValue(0) * realHeight / dataSet.getMaxValue();
            dataGraph.moveTo(currentX, currentY);

            for (int i=1; i<dataSet.getSize(); i++) {
                float nextX = leftline + i * partWidth + partWidthHalf;
                float nextY = baseline - dataSet.getValue(i) * realHeight / dataSet.getMaxValue();
                dataGraph.quadTo(currentX, currentY, nextX, nextY);
                currentX = nextX;
                currentY = nextY;
            }
        }
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setData(dataSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataSet.getSize() > 0) {

            for (int i=0; i<dataSet.getSize(); i++) {
                Paint          paint;
                Classification classification = dataSet.getClassification(i);

                if (classification == Classification.BAD) {
                    paint = redPaint;
                } else if (classification == Classification.ATTENTION) {
                    paint = yellowPaint;
                } else {
                    paint = greenPaint;
                }
                canvas.drawRect(i * partWidth + leftline, topline, (i + 1) * partWidth + leftline, baseline, paint);
            }

            for (int i=0; i<dataSet.getSize(); i++) {
                if (useLabel[i]) {
                    canvas.drawText(
                            dataSet.getLabel(i),
                            i * partWidth + partWidthHalf + leftline,
                            textline + textGap,
                            textPaintBelow);
                }
            }

            canvas.drawPath(dataGraph, dataGraphPaint);

            canvas.drawLine(leftline, topline, rightline, topline, smallLinePaint);
            canvas.drawLine(leftline, topline, leftline, baseline, smallLinePaint);
            canvas.drawLine(rightline, baseline, leftline, baseline, smallLinePaint);
            canvas.drawLine(rightline, baseline, rightline, topline, smallLinePaint);
            canvas.drawText(maxValue, leftline, topline - textGap, textPaintLeft);

        }
    }
}
