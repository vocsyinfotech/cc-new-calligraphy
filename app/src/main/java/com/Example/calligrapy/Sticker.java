package com.Example.calligrapy;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public abstract class Sticker {
    protected static final String TAG = "Sticker";
    protected boolean mIsFlipped;
    protected Matrix mMatrix;
    private float[] mMatrixValues = new float[9];

    public abstract void draw(Canvas canvas);

    public abstract int getHeight();

    public abstract int getWidth();

    public boolean isFlipped() {
        return this.mIsFlipped;
    }

    public void setFlipped(boolean flipped) {
        this.mIsFlipped = flipped;
    }

    public Matrix getMatrix() {
        return this.mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        this.mMatrix.set(matrix);
    }

    public float[] getBoundPoints() {
        if (this.mIsFlipped) {
            return new float[]{(float) getWidth(), 0.0f, 0.0f, 0.0f, (float) getWidth(), (float) getHeight(), 0.0f, (float) getHeight()};
        }
        return new float[]{0.0f, 0.0f, (float) getWidth(), 0.0f, 0.0f, (float) getHeight(), (float) getWidth(), (float) getHeight()};
    }

    public float[] getMappedBoundPoints() {
        float[] dst = new float[8];
        this.mMatrix.mapPoints(dst, getBoundPoints());
        return dst;
    }

    public float[] getMappedPoints(float[] src) {
        float[] dst = new float[src.length];
        this.mMatrix.mapPoints(dst, src);
        return dst;
    }

    public RectF getBound() {
        return new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
    }

    public RectF getMappedBound() {
        RectF dst = new RectF();
        this.mMatrix.mapRect(dst, getBound());
        return dst;
    }

    public PointF getCenterPoint() {
        return new PointF((float) (getWidth() / 2), (float) (getHeight() / 2));
    }

    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        float[] dst = getMappedPoints(new float[]{pointF.x, pointF.y});
        return new PointF(dst[0], dst[1]);
    }

    public float getCurrentScale() {
        return getMatrixScale(this.mMatrix);
    }

    public float getCurrentHeight() {
        return getMatrixScale(this.mMatrix) * ((float) getHeight());
    }

    public float getCurrentWidth() {
        return getMatrixScale(this.mMatrix) * ((float) getWidth());
    }

    private float getMatrixScale(@NonNull Matrix matrix) {
        return (float) Math.sqrt(Math.pow((double) getMatrixValue(matrix, 0), 2.0d) + Math.pow((double) getMatrixValue(matrix, 3), 2.0d));
    }

    public float getCurrentAngle() {
        return getMatrixAngle(this.mMatrix);
    }

    private float getMatrixAngle(@NonNull Matrix matrix) {
        return (float) (-(Math.atan2((double) getMatrixValue(matrix, 1), (double) getMatrixValue(matrix, 0)) * 57.29577951308232d));
    }

    private float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0, to = 9) int valueIndex) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[valueIndex];
    }

    public void release() {
    }
}
