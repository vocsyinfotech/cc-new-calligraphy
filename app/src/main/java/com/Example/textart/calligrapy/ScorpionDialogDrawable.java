package com.Example.textart.calligrapy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import com.koushikdutta.ion.loader.MediaFile;

import me.grantland.widget.BuildConfig;

public class ScorpionDialogDrawable extends Drawable {
    private Paint mBorderPaint = new Paint();
    private int mOffsetX = 0;
    private int mOffsetY = 30;
    private int mRadius = 20;
    private RectF mRectF;
    private Paint mTextPaint;
    String text = BuildConfig.FLAVOR;

    public ScorpionDialogDrawable() {
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(Color.parseColor("#bb000000"));
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(-16777216);
        this.mTextPaint.setTextSize(80.0f);
        this.mRectF = new RectF(0.0f, 0.0f, (float) (getIntrinsicWidth() - this.mOffsetX), (float) (getIntrinsicHeight() - this.mOffsetY));
    }

    public int getIntrinsicWidth() {
        return MediaFile.FILE_TYPE_DTS;
    }

    public int getIntrinsicHeight() {
        return MediaFile.FILE_TYPE_DTS;
    }

    public int getRadius() {
        return this.mRadius;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    public int getOffsetY() {
        return this.mOffsetY;
    }

    public void setOffsetY(int offsetY) {
        this.mOffsetY = offsetY;
    }

    public int getOffsetX() {
        return this.mOffsetX;
    }

    public void setOffsetX(int offsetX) {
        this.mOffsetX = offsetX;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (this.mRectF == null) {
            this.mRectF = new RectF((float) left, (float) top, (float) (right - this.mOffsetX), (float) (bottom - this.mOffsetY));
            return;
        }
        this.mRectF.left = (float) left;
        this.mRectF.top = (float) top;
        this.mRectF.right = (float) right;
        this.mRectF.bottom = (float) bottom;
    }

    public void setFontStyle(Context c, String name) {
        this.mTextPaint.setTypeface(Typeface.createFromAsset(c.getAssets(), name));
    }

    public void setTextData(String text) {
        this.text = text;
    }

    public void setTextColor(int color) {
        this.mTextPaint.setColor(color);
    }

    public void draw(Canvas canvas) {
        Path path = new Path();
        path.moveTo((this.mRectF.right - ((float) (this.mOffsetY * 2))) - ((float) this.mOffsetX), this.mRectF.bottom);
        path.lineTo((this.mRectF.right - ((float) (((this.mOffsetY * 2) + this.mRadius) / 2))) - ((float) this.mOffsetX), this.mRectF.bottom + ((float) this.mOffsetY));
        path.lineTo((this.mRectF.right - ((float) this.mRadius)) - ((float) this.mOffsetX), this.mRectF.bottom);
        float y = this.mRectF.centerY();
        float[] characterWidths = new float[this.text.length()];
        float textWidth = 0.0f;
        for (int i = 0; i < this.mTextPaint.getTextWidths(this.text, characterWidths); i++) {
            textWidth += characterWidths[i];
        }
        canvas.save();
        canvas.translate((this.mRectF.width() / 2.0f) - (textWidth / 2.0f), 0.0f);
        canvas.drawText(this.text, 0.0f, y, this.mTextPaint);
        canvas.restore();
    }

    public void setAlpha(int i) {
        this.mBorderPaint.setAlpha(i);
        this.mTextPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
