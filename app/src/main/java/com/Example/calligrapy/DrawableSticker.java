package com.Example.calligrapy;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class DrawableSticker extends Sticker {
    protected static final String TAG = "DrawableSticker";
    private Drawable mDrawable;
    private Rect mRealBounds;

    public DrawableSticker(Drawable drawable) {
        this.mDrawable = drawable;
        this.mMatrix = new Matrix();
        this.mRealBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.concat(this.mMatrix);
        this.mDrawable.setBounds(this.mRealBounds);
        this.mDrawable.draw(canvas);
        canvas.restore();
    }

    public int getWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    public void release() {
        super.release();
        if (this.mDrawable != null) {
            this.mDrawable = null;
        }
    }
}
