package com.Example.textart.calligrapy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StickerView extends androidx.appcompat.widget.AppCompatImageView {
    public static final float DEFAULT_ICON_EXTRA_RADIUS = 10.0f;
    public static final float DEFAULT_ICON_RADIUS = 30.0f;
    private static final String TAG = "StickerView";
    private Paint mBorderPaint;
    private ActionMode mCurrentMode;
    private BitmapStickerIcon mDeleteIcon;
    private Matrix mDownMatrix;
    private float mDownX;
    private float mDownY;
    private BitmapStickerIcon mFlipIcon;
    private Sticker mHandlingSticker;
    private float mIconExtraRadius;
    private float mIconRadius;
    private boolean mLooked;
    private PointF mMidPoint;
    private Matrix mMoveMatrix;
    private float mOldDistance;
    private float mOldRotation;
    private OnStickerClickListener mOnStickerClickListener;
    private Matrix mSizeMatrix;
    private RectF mStickerRect;
    private List<Sticker> mStickers;
    private int mTouchSlop;
    private BitmapStickerIcon mZoomIcon;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaopo$flying$sticker$StickerView$ActionMode = new int[ActionMode.values().length];

        static {
            try {
                $SwitchMap$com$xiaopo$flying$sticker$StickerView$ActionMode[ActionMode.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$xiaopo$flying$sticker$StickerView$ActionMode[ActionMode.DRAG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$xiaopo$flying$sticker$StickerView$ActionMode[ActionMode.ZOOM_WITH_TWO_FINGER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$xiaopo$flying$sticker$StickerView$ActionMode[ActionMode.ZOOM_WITH_ICON.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private enum ActionMode {
        NONE,
        DRAG,
        ZOOM_WITH_TWO_FINGER,
        ZOOM_WITH_ICON,
        DELETE,
        FLIP_HORIZONTAL,
        CLICK
    }

    public interface OnStickerClickListener {
        void onStickerClick(Sticker sticker);
    }

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"NewApi"})
    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIconRadius = DEFAULT_ICON_RADIUS;
        this.mIconExtraRadius = DEFAULT_ICON_EXTRA_RADIUS;
        this.mOldDistance = 1.0f;
        this.mOldRotation = 0.0f;
        this.mCurrentMode = ActionMode.NONE;
        this.mStickers = new ArrayList();
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(-16777216);
        this.mBorderPaint.setAlpha(160);
        this.mSizeMatrix = new Matrix();
        this.mDownMatrix = new Matrix();
        this.mMoveMatrix = new Matrix();
        this.mStickerRect = new RectF();
        this.mDeleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(),R.drawable.closetext));
        this.mZoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.size));
        this.mFlipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.flip));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            this.mStickerRect.left = (float) left;
            this.mStickerRect.top = (float) top;
            this.mStickerRect.right = (float) right;
            this.mStickerRect.bottom = (float) bottom;
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mStickers.size(); i++) {
            Sticker sticker = (Sticker) this.mStickers.get(i);
            if (sticker != null) {
                sticker.draw(canvas);
            }
        }
        if (this.mHandlingSticker != null && !this.mLooked) {
            float[] bitmapPoints = getStickerPoints(this.mHandlingSticker);
            float x1 = bitmapPoints[0];
            float y1 = bitmapPoints[1];
            float x2 = bitmapPoints[2];
            float y2 = bitmapPoints[3];
            float x3 = bitmapPoints[4];
            float y3 = bitmapPoints[5];
            float x4 = bitmapPoints[6];
            float y4 = bitmapPoints[7];
            canvas.drawLine(x1, y1, x2, y2, this.mBorderPaint);
            canvas.drawLine(x1, y1, x3, y3, this.mBorderPaint);
            canvas.drawLine(x2, y2, x4, y4, this.mBorderPaint);
            canvas.drawLine(x4, y4, x3, y3, this.mBorderPaint);
            float rotation = calculateRotation(x3, y3, x4, y4);
            canvas.drawCircle(x1, y1, this.mIconRadius, this.mBorderPaint);
            this.mDeleteIcon.setX(x1);
            this.mDeleteIcon.setY(y1);
            this.mDeleteIcon.getMatrix().reset();
            this.mDeleteIcon.getMatrix().postRotate(rotation, (float) (this.mDeleteIcon.getWidth() / 2), (float) (this.mDeleteIcon.getHeight() / 2));
            this.mDeleteIcon.getMatrix().postTranslate(x1 - ((float) (this.mDeleteIcon.getWidth() / 2)), y1 - ((float) (this.mDeleteIcon.getHeight() / 2)));
            this.mDeleteIcon.draw(canvas);
            canvas.drawCircle(x4, y4, this.mIconRadius, this.mBorderPaint);
            this.mZoomIcon.setX(x4);
            this.mZoomIcon.setY(y4);
            this.mZoomIcon.getMatrix().reset();
            this.mZoomIcon.getMatrix().postRotate(180.0f + rotation, (float) (this.mZoomIcon.getWidth() / 2), (float) (this.mZoomIcon.getHeight() / 2));
            this.mZoomIcon.getMatrix().postTranslate(x4 - ((float) (this.mZoomIcon.getWidth() / 2)), y4 - ((float) (this.mZoomIcon.getHeight() / 2)));
            this.mZoomIcon.draw(canvas);
            canvas.drawCircle(x2, y2, this.mIconRadius, this.mBorderPaint);
            this.mFlipIcon.setX(x2);
            this.mFlipIcon.setY(y2);
            this.mFlipIcon.getMatrix().reset();
            this.mFlipIcon.getMatrix().postRotate(180.0f + rotation, (float) (this.mDeleteIcon.getWidth() / 2), (float) (this.mDeleteIcon.getHeight() / 2));
            this.mFlipIcon.getMatrix().postTranslate(x2 - ((float) (this.mFlipIcon.getWidth() / 2)), y2 - ((float) (this.mFlipIcon.getHeight() / 2)));
            this.mFlipIcon.draw(canvas);
        }
    }

    @SuppressLint({"NewApi"})
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mLooked) {
            return super.onTouchEvent(event);
        }
        switch (MotionEventCompat.getActionMasked(event)) {
            case 0/*0*/:
                this.mCurrentMode = ActionMode.DRAG;
                this.mDownX = event.getX();
                this.mDownY = event.getY();
                if (checkDeleteIconTouched(this.mIconExtraRadius)) {
                    this.mCurrentMode = ActionMode.DELETE;
                } else if (checkHorizontalFlipIconTouched(this.mIconExtraRadius)) {
                    this.mCurrentMode = ActionMode.FLIP_HORIZONTAL;
                } else if (!checkZoomIconTouched(this.mIconExtraRadius) || this.mHandlingSticker == null) {
                    this.mHandlingSticker = findHandlingSticker();
                } else {
                    this.mCurrentMode = ActionMode.ZOOM_WITH_ICON;
                    this.mMidPoint = calculateMidPoint();
                    this.mOldDistance = calculateDistance(this.mMidPoint.x, this.mMidPoint.y, this.mDownX, this.mDownY);
                    this.mOldRotation = calculateRotation(this.mMidPoint.x, this.mMidPoint.y, this.mDownX, this.mDownY);
                }
                if (this.mHandlingSticker != null) {
                    this.mDownMatrix.set(this.mHandlingSticker.getMatrix());
                }
                invalidate();
                return true;
            case 1 /*1*/:
                if (this.mCurrentMode == ActionMode.DELETE && this.mHandlingSticker != null) {
                    this.mStickers.remove(this.mHandlingSticker);
                    this.mHandlingSticker.release();
                    this.mHandlingSticker = null;
                    invalidate();
                }
                if (this.mCurrentMode == ActionMode.FLIP_HORIZONTAL && this.mHandlingSticker != null) {
                    this.mHandlingSticker.getMatrix().preScale(-1.0f, 1.0f, this.mHandlingSticker.getCenterPoint().x, this.mHandlingSticker.getCenterPoint().y);
                    this.mHandlingSticker.setFlipped(!this.mHandlingSticker.isFlipped());
                    invalidate();
                }
                if (this.mCurrentMode == ActionMode.DRAG && Math.abs(event.getX() - this.mDownX) < ((float) this.mTouchSlop) && Math.abs(event.getY() - this.mDownY) < ((float) this.mTouchSlop) && this.mHandlingSticker != null) {
                    this.mCurrentMode = ActionMode.CLICK;
                    if (this.mOnStickerClickListener != null) {
                        this.mOnStickerClickListener.onStickerClick(this.mHandlingSticker);
                    }
                }
                this.mCurrentMode = ActionMode.NONE;
                return true;
            case 2 /*2*/:
                handleCurrentMode(event);
                invalidate();
                return true;
            case 5/*5*/:
                this.mOldDistance = calculateDistance(event);
                this.mOldRotation = calculateRotation(event);
                this.mMidPoint = calculateMidPoint(event);
                if (this.mHandlingSticker == null || !isInStickerArea(this.mHandlingSticker, event.getX(1), event.getY(1)) || checkDeleteIconTouched(this.mIconExtraRadius)) {
                    return true;
                }
                this.mCurrentMode = ActionMode.ZOOM_WITH_TWO_FINGER;
                return true;
            case 6 /*6*/:
                this.mCurrentMode = ActionMode.NONE;
                return true;
            default:
                return true;
        }
    }

    private void handleCurrentMode(MotionEvent event)
    {
        float newDistance;
        float newRotation;
        switch (AnonymousClass1.$SwitchMap$com$xiaopo$flying$sticker$StickerView$ActionMode[this.mCurrentMode.ordinal()]) {
            case 2/*2*/:
                if (this.mHandlingSticker != null) {
                    this.mMoveMatrix.set(this.mDownMatrix);
                    this.mMoveMatrix.postTranslate(event.getX() - this.mDownX, event.getY() - this.mDownY);
                    this.mHandlingSticker.getMatrix().set(this.mMoveMatrix);
                    return;
                }
                return;
            case 3 /*3*/:
                if (this.mHandlingSticker != null) {
                    newDistance = calculateDistance(event);
                    newRotation = calculateRotation(event);
                    this.mMoveMatrix.set(this.mDownMatrix);
                    this.mMoveMatrix.postScale(newDistance / this.mOldDistance, newDistance / this.mOldDistance, this.mMidPoint.x, this.mMidPoint.y);
                    this.mMoveMatrix.postRotate(newRotation - this.mOldRotation, this.mMidPoint.x, this.mMidPoint.y);
                    this.mHandlingSticker.getMatrix().set(this.mMoveMatrix);
                    return;
                }
                return;
            case 4 /*4*/:
                if (this.mHandlingSticker != null) {
                    newDistance = calculateDistance(this.mMidPoint.x, this.mMidPoint.y, event.getX(), event.getY());
                    newRotation = calculateRotation(this.mMidPoint.x, this.mMidPoint.y, event.getX(), event.getY());
                    this.mMoveMatrix.set(this.mDownMatrix);
                    this.mMoveMatrix.postScale(newDistance / this.mOldDistance, newDistance / this.mOldDistance, this.mMidPoint.x, this.mMidPoint.y);
                    this.mMoveMatrix.postRotate(newRotation - this.mOldRotation, this.mMidPoint.x, this.mMidPoint.y);
                    this.mHandlingSticker.getMatrix().set(this.mMoveMatrix);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private boolean checkZoomIconTouched(float extraRadius) {
        float x = this.mZoomIcon.getX() - this.mDownX;
        float y = this.mZoomIcon.getY() - this.mDownY;
        return (x * x) + (y * y) <= (this.mIconRadius + extraRadius) * (this.mIconRadius + extraRadius);
    }

    private boolean checkDeleteIconTouched(float extraRadius) {
        float x = this.mDeleteIcon.getX() - this.mDownX;
        float y = this.mDeleteIcon.getY() - this.mDownY;
        return (x * x) + (y * y) <= (this.mIconRadius + extraRadius) * (this.mIconRadius + extraRadius);
    }

    private boolean checkHorizontalFlipIconTouched(float extraRadius) {
        float x = this.mFlipIcon.getX() - this.mDownX;
        float y = this.mFlipIcon.getY() - this.mDownY;
        return (x * x) + (y * y) <= (this.mIconRadius + extraRadius) * (this.mIconRadius + extraRadius);
    }

    private Sticker findHandlingSticker() {
        for (int i = this.mStickers.size() - 1; i >= 0; i--) {
            if (isInStickerArea((Sticker) this.mStickers.get(i), this.mDownX, this.mDownY)) {
                return (Sticker) this.mStickers.get(i);
            }
        }
        return null;
    }

    private boolean isInStickerArea(Sticker sticker, float downX, float downY) {
        return sticker.getMappedBound().contains(downX, downY);
    }

    @SuppressLint({"NewApi"})
    private PointF calculateMidPoint(MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return new PointF();
        }
        return new PointF((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
    }

    private PointF calculateMidPoint() {
        if (this.mHandlingSticker == null) {
            return new PointF();
        }
        return this.mHandlingSticker.getMappedCenterPoint();
    }

    @SuppressLint({"NewApi"})
    private float calculateRotation(MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0.0f;
        }
        return (float) Math.toDegrees(Math.atan2((double) (event.getY(0) - event.getY(1)), (double) (event.getX(0) - event.getX(1))));
    }

    private float calculateRotation(float x1, float y1, float x2, float y2) {
        return (float) Math.toDegrees(Math.atan2((double) (y1 - y2), (double) (x1 - x2)));
    }

    @SuppressLint({"NewApi"})
    private float calculateDistance(MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0.0f;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        double x = (double) (x1 - x2);
        double y = (double) (y1 - y2);
        return (float) Math.sqrt((x * x) + (y * y));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < this.mStickers.size(); i++) {
            Sticker sticker = (Sticker) this.mStickers.get(i);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    private void transformSticker(Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        float scaleFactor;
        if (this.mSizeMatrix != null) {
            this.mSizeMatrix.reset();
        }
        this.mSizeMatrix.postTranslate((float) ((getWidth() - sticker.getWidth()) / 2), (float) ((getHeight() - sticker.getHeight()) / 2));
        if (getWidth() < getHeight()) {
            scaleFactor = ((float) getWidth()) / ((float) sticker.getWidth());
        } else {
            scaleFactor = ((float) getHeight()) / ((float) sticker.getHeight());
        }
        this.mSizeMatrix.postScale(scaleFactor / 2.0f, scaleFactor / 2.0f, (float) (getWidth() / 2), (float) (getHeight() / 2));
        sticker.getMatrix().reset();
        sticker.getMatrix().set(this.mSizeMatrix);
        invalidate();
    }

    public void replace(Drawable stickerDrawable) {
        if (this.mHandlingSticker != null) {
            if (this.mHandlingSticker instanceof DrawableSticker) {
                ((DrawableSticker) this.mHandlingSticker).setDrawable(stickerDrawable);
            } else {
                Log.d(TAG, "replace: the current sticker did not support drawable");
            }
            invalidate();
        }
    }

    @SuppressLint({"NewApi"})
    public void replace(Bitmap stickerBitmap) {
        replace(new BitmapDrawable(getResources(), stickerBitmap));
    }

    public void replace(Sticker sticker, boolean needStayState) {
        if (this.mHandlingSticker != null && sticker != null) {
            if (needStayState) {
                sticker.getMatrix().set(this.mHandlingSticker.getMatrix());
                sticker.setFlipped(this.mHandlingSticker.isFlipped());
            }
            this.mStickers.set(this.mStickers.indexOf(this.mHandlingSticker), sticker);
            this.mHandlingSticker = sticker;
            invalidate();
        }
    }

    public float[] getStickerPoints(Sticker sticker) {
        if (sticker == null) {
            return new float[8];
        }
        return sticker.getMappedBoundPoints();
    }

    @SuppressLint({"NewApi"})
    public void addSticker(Bitmap stickerBitmap) {
        addSticker(new BitmapDrawable(getResources(), stickerBitmap));
    }

    public void addSticker(Drawable stickerDrawable) {
        float scaleFactor;
        Sticker drawableSticker = new DrawableSticker(stickerDrawable);
        drawableSticker.getMatrix().postTranslate((float) ((getWidth() - drawableSticker.getWidth()) / 2), (float) ((getHeight() - drawableSticker.getHeight()) / 2));
        if (getWidth() < getHeight()) {
            scaleFactor = ((float) getWidth()) / ((float) stickerDrawable.getIntrinsicWidth());
        } else {
            scaleFactor = ((float) getHeight()) / ((float) stickerDrawable.getIntrinsicWidth());
        }
        drawableSticker.getMatrix().postScale(scaleFactor / 2.0f, scaleFactor / 2.0f, (float) (getWidth() / 2), (float) (getHeight() / 2));
        this.mHandlingSticker = drawableSticker;
        this.mStickers.add(drawableSticker);
        invalidate();
    }

    public void save(File file) throws IOException {
        FileNotFoundException e;
        Throwable th;
        Bitmap bitmap = null;
        FileOutputStream outputStream = null;
        try {
            bitmap = createBitmap();
            FileOutputStream outputStream2 = new FileOutputStream(file);
            try {
                bitmap.compress(CompressFormat.PNG, 100, outputStream2);
                BitmapUtil.notifySystemGallery(getContext(), file);
                if (bitmap != null) {
                    bitmap.recycle();
                }
                if (outputStream2 != null) {
                    try {
                        outputStream2.close();
                        outputStream = outputStream2;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        outputStream = outputStream2;
                        return;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = outputStream2;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
//                throw th;
            }
        } catch (FileNotFoundException e4) {
            e = e4;
            e.printStackTrace();
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (outputStream != null)
            {
                outputStream.close();
            }
        }
    }

    public Bitmap createBitmap() {
        this.mHandlingSticker = null;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        draw(new Canvas(bitmap));
        return bitmap;
    }

    public float getIconRadius() {
        return this.mIconRadius;
    }

    public void setIconRadius(float iconRadius) {
        this.mIconRadius = iconRadius;
        invalidate();
    }

    public float getIconExtraRadius() {
        return this.mIconExtraRadius;
    }

    public void setIconExtraRadius(float iconExtraRadius) {
        this.mIconExtraRadius = iconExtraRadius;
    }

    public boolean isLooked() {
        return this.mLooked;
    }

    public void setLooked(boolean looked) {
        this.mLooked = looked;
        invalidate();
    }

    public void setOnStickerClickListener(OnStickerClickListener onStickerClickListener) {
        this.mOnStickerClickListener = onStickerClickListener;
    }

    public Drawable getCurruntDrawablr()
    {
        return ((DrawableSticker) this.mHandlingSticker).getDrawable();
    }
}
