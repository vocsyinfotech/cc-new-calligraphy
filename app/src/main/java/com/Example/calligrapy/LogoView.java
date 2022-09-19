package com.Example.calligrapy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;


public class LogoView extends ImageView {

    private static final String TAG = "LogoView";
    private float radius = 16.0f;
    private Path path = new Path();
    private RectF rect;

    public LogoView(Context context) {
        super(context);
        init(context, null);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        try {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LogoView, 0, 0);
            radius = typedArray.getInteger(R.styleable.LogoView_radius, 16);
            typedArray.recycle();
            invalidate();
            requestLayout();
        } catch (Exception e) {
            Log.e(TAG, "init: " + e.getMessage());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
