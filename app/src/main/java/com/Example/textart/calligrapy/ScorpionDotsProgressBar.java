package com.Example.textart.calligrapy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class ScorpionDotsProgressBar extends View {
    private int heightSize;
    private int mDotCount = 6;
    private Handler mHandler = new Handler();
    private int mIndex = 0;
    private Paint mPaint = new Paint(1);
    private Paint mPaintFill = new Paint(1);
    private float mRadius;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            ScorpionDotsProgressBar.this.mIndex = ScorpionDotsProgressBar.this.mIndex + ScorpionDotsProgressBar.this.step;
            if (ScorpionDotsProgressBar.this.mIndex < 0) {
                ScorpionDotsProgressBar.this.mIndex = 1;
                ScorpionDotsProgressBar.this.step = 1;
            } else if (ScorpionDotsProgressBar.this.mIndex > ScorpionDotsProgressBar.this.mDotCount - 1) {
                if (ScorpionDotsProgressBar.this.mDotCount - 2 >= 0) {
                    ScorpionDotsProgressBar.this.mIndex = ScorpionDotsProgressBar.this.mDotCount - 2;
                    ScorpionDotsProgressBar.this.step = -1;
                } else {
                    ScorpionDotsProgressBar.this.mIndex = 0;
                    ScorpionDotsProgressBar.this.step = 1;
                }
            }
            ScorpionDotsProgressBar.this.invalidate();
            ScorpionDotsProgressBar.this.mHandler.postDelayed(ScorpionDotsProgressBar.this.mRunnable, 300);
        }
    };
    private int margin = 7;
    private int step = 1;
    private int widthSize;

    public ScorpionDotsProgressBar(Context context) {
        super(context);
        init(context);
    }

    public ScorpionDotsProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScorpionDotsProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mRadius = 8.0f;
        this.mPaintFill.setStyle(Style.FILL);
        this.mPaintFill.setColor(Color.parseColor("#000000"));
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(1728053247);
        start();
    }

    public void setDotsCount(int count) {
        this.mDotCount = count;
    }

    public void start() {
        this.mIndex = -1;
        this.mHandler.removeCallbacks(this.mRunnable);
        this.mHandler.post(this.mRunnable);
    }

    public void stop() {
        this.mHandler.removeCallbacks(this.mRunnable);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthSize = MeasureSpec.getSize(widthMeasureSpec);
        this.heightSize = ((((int) this.mRadius) * 2) + getPaddingBottom()) + getPaddingTop();
        setMeasuredDimension(this.widthSize, this.heightSize);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float dX = ((((float) this.widthSize) - ((((float) this.mDotCount) * this.mRadius) * 2.0f)) - ((float) ((this.mDotCount - 1) * this.margin))) / 2.0f;
        float dY = (float) (this.heightSize / 2);
        for (int i = 0; i < this.mDotCount; i++) {
            if (i == this.mIndex) {
                canvas.drawCircle(dX, dY, this.mRadius, this.mPaintFill);
            } else {
                canvas.drawCircle(dX, dY, this.mRadius, this.mPaint);
            }
            dX += (this.mRadius * 2.0f) + ((float) this.margin);
        }
    }
}
