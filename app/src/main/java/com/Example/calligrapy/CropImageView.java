package com.Example.calligrapy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.edmodo.cropper.cropwindow.CropOverlayView;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.edmodo.cropper.util.ImageViewUtil;

public class CropImageView extends FrameLayout {
    public static final int DEFAULT_ASPECT_RATIO_X = 1;
    public static final int DEFAULT_ASPECT_RATIO_Y = 1;
    public static final boolean DEFAULT_FIXED_ASPECT_RATIO = false;
    public static final int DEFAULT_GUIDELINES = 1;
    private static final int DEFAULT_IMAGE_RESOURCE = 0;
    private static final String DEGREES_ROTATED = "DEGREES_ROTATED";
    private static final Rect EMPTY_RECT = new Rect();
    private int mAspectRatioX = DEFAULT_GUIDELINES;
    private int mAspectRatioY = DEFAULT_GUIDELINES;
    private Bitmap mBitmap;
    private CropOverlayView mCropOverlayView;
    private int mDegreesRotated = DEFAULT_IMAGE_RESOURCE;
    private boolean mFixAspectRatio = DEFAULT_FIXED_ASPECT_RATIO;
    private int mGuidelines = DEFAULT_GUIDELINES;
    private int mImageResource = DEFAULT_IMAGE_RESOURCE;
    private ImageView mImageView;
    private int mLayoutHeight;
    private int mLayoutWidth;

    public CropImageView(Context context) {
        super(context);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropImageView, DEFAULT_IMAGE_RESOURCE, DEFAULT_IMAGE_RESOURCE);
        try {
            this.mGuidelines = ta.getInteger(R.styleable.CropImageView_guidelines, DEFAULT_GUIDELINES);
            this.mFixAspectRatio = ta.getBoolean(R.styleable.CropImageView_fixAspectRatio, DEFAULT_FIXED_ASPECT_RATIO);
            this.mAspectRatioX = ta.getInteger(R.styleable.CropImageView_aspectRatioX, DEFAULT_GUIDELINES);
            this.mAspectRatioY = ta.getInteger(R.styleable.CropImageView_aspectRatioY, DEFAULT_GUIDELINES);
            this.mImageResource = ta.getResourceId(R.styleable.CropImageView_imageResource, DEFAULT_IMAGE_RESOURCE);
            init(context);
        } finally {
            ta.recycle();
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt(DEGREES_ROTATED, this.mDegreesRotated);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (this.mBitmap != null) {
                this.mDegreesRotated = bundle.getInt(DEGREES_ROTATED);
                int tempDegrees = this.mDegreesRotated;
                rotateImage(this.mDegreesRotated);
                this.mDegreesRotated = tempDegrees;
            }
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.mBitmap != null) {
            this.mCropOverlayView.setBitmapRect(ImageViewUtil.getBitmapRectCenterInside(this.mBitmap, this));
            return;
        }
        this.mCropOverlayView.setBitmapRect(EMPTY_RECT);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (this.mBitmap != null) {
            int desiredWidth;
            int desiredHeight;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (heightSize == 0) {
                heightSize = this.mBitmap.getHeight();
            }
            double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
            double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;
            if (widthSize < this.mBitmap.getWidth()) {
                viewToBitmapWidthRatio = ((double) widthSize) / ((double) this.mBitmap.getWidth());
            }
            if (heightSize < this.mBitmap.getHeight()) {
                viewToBitmapHeightRatio = ((double) heightSize) / ((double) this.mBitmap.getHeight());
            }
            if (viewToBitmapWidthRatio == Double.POSITIVE_INFINITY && viewToBitmapHeightRatio == Double.POSITIVE_INFINITY) {
                desiredWidth = this.mBitmap.getWidth();
                desiredHeight = this.mBitmap.getHeight();
            } else if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                desiredWidth = widthSize;
                desiredHeight = (int) (((double) this.mBitmap.getHeight()) * viewToBitmapWidthRatio);
            } else {
                desiredHeight = heightSize;
                desiredWidth = (int) (((double) this.mBitmap.getWidth()) * viewToBitmapHeightRatio);
            }
            int width = getOnMeasureSpec(widthMode, widthSize, desiredWidth);
            int height = getOnMeasureSpec(heightMode, heightSize, desiredHeight);
            this.mLayoutWidth = width;
            this.mLayoutHeight = height;
            this.mCropOverlayView.setBitmapRect(ImageViewUtil.getBitmapRectCenterInside(this.mBitmap.getWidth(), this.mBitmap.getHeight(), this.mLayoutWidth, this.mLayoutHeight));
            setMeasuredDimension(this.mLayoutWidth, this.mLayoutHeight);
            return;
        }
        this.mCropOverlayView.setBitmapRect(EMPTY_RECT);
        setMeasuredDimension(widthSize, heightSize);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.mLayoutWidth > 0 && this.mLayoutHeight > 0) {
            ViewGroup.LayoutParams origparams = getLayoutParams();
            origparams.width = this.mLayoutWidth;
            origparams.height = this.mLayoutHeight;
            setLayoutParams(origparams);
        }
    }

    public int getImageResource() {
        return this.mImageResource;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mImageView.setImageBitmap(this.mBitmap);
        if (this.mCropOverlayView != null) {
            this.mCropOverlayView.resetCropOverlayView();
        }
    }

    public void setImageBitmap(Bitmap bitmap, ExifInterface exif) {
        if (bitmap != null) {
            if (exif == null) {
                setImageBitmap(bitmap);
                return;
            }
            Matrix matrix = new Matrix();
            int rotate = -1;
            switch (exif.getAttributeInt("Orientation", DEFAULT_GUIDELINES)) {
                case 3 /*3*/:
                    rotate = 180;
                    break;
                case 6/*6*/:
                    rotate = 90;
                    break;
                case 8 /*8*/:
                    rotate = 270;
                    break;
            }
            if (rotate == -1) {
                setImageBitmap(bitmap);
                return;
            }
            matrix.postRotate((float) rotate);
            setImageBitmap(Bitmap.createBitmap(bitmap, DEFAULT_IMAGE_RESOURCE, DEFAULT_IMAGE_RESOURCE, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
            bitmap.recycle();
        }
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
        }
    }

    public Bitmap getCroppedImage() {
        Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(this.mBitmap, this.mImageView);
        float scaleFactorWidth = ((float) this.mBitmap.getWidth()) / ((float) displayedImageRect.width());
        float scaleFactorHeight = ((float) this.mBitmap.getHeight()) / ((float) displayedImageRect.height());
        float cropWindowX = Edge.LEFT.getCoordinate() - ((float) displayedImageRect.left);
        float cropWindowY = Edge.TOP.getCoordinate() - ((float) displayedImageRect.top);
        Bitmap re =  Bitmap.createBitmap(this.mBitmap, (int) (cropWindowX * scaleFactorWidth), (int) (cropWindowY * scaleFactorHeight), (int) (Edge.getWidth() * scaleFactorWidth), (int) (Edge.getHeight() * scaleFactorHeight));
        return re;

    }

    public RectF getActualCropRect() {
        Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(this.mBitmap, this.mImageView);
        float scaleFactorWidth = ((float) this.mBitmap.getWidth()) / ((float) displayedImageRect.width());
        float scaleFactorHeight = ((float) this.mBitmap.getHeight()) / ((float) displayedImageRect.height());
        float displayedCropLeft = Edge.LEFT.getCoordinate() - ((float) displayedImageRect.left);
        float displayedCropTop = Edge.TOP.getCoordinate() - ((float) displayedImageRect.top);
        float actualCropLeft = displayedCropLeft * scaleFactorWidth;
        float actualCropTop = displayedCropTop * scaleFactorHeight;
        return new RectF(Math.max(0.0f, actualCropLeft), Math.max(0.0f, actualCropTop), Math.min((float) this.mBitmap.getWidth(), actualCropLeft + (Edge.getWidth() * scaleFactorWidth)), Math.min((float) this.mBitmap.getHeight(), actualCropTop + (Edge.getHeight() * scaleFactorHeight)));
    }

    public void setFixedAspectRatio(boolean fixAspectRatio) {
        this.mCropOverlayView.setFixedAspectRatio(fixAspectRatio);
    }

    public void setGuidelines(int guidelines) {
        this.mCropOverlayView.setGuidelines(guidelines);
    }

    public void setAspectRatio(int aspectRatioX, int aspectRatioY) {
        this.mAspectRatioX = aspectRatioX;
        this.mCropOverlayView.setAspectRatioX(this.mAspectRatioX);
        this.mAspectRatioY = aspectRatioY;
        this.mCropOverlayView.setAspectRatioY(this.mAspectRatioY);
    }

    public void rotateImage(int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degrees);
        this.mBitmap = Bitmap.createBitmap(this.mBitmap, DEFAULT_IMAGE_RESOURCE, DEFAULT_IMAGE_RESOURCE, this.mBitmap.getWidth(), this.mBitmap.getHeight(), matrix, true);
        setImageBitmap(this.mBitmap);
        this.mDegreesRotated += degrees;
        this.mDegreesRotated %= 360;
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.crop_image_view, this, true);
        this.mImageView = (ImageView) v.findViewById(R.id.ImageView_image);
        setImageResource(this.mImageResource);
        this.mCropOverlayView = (CropOverlayView) v.findViewById(R.id.CropOverlayView);
        this.mCropOverlayView.setInitialAttributeValues(this.mGuidelines, this.mFixAspectRatio, this.mAspectRatioX, this.mAspectRatioY);
    }

    private static int getOnMeasureSpec(int measureSpecMode, int measureSpecSize, int desiredSize) {
        if (measureSpecMode == 1073741824) {
            return measureSpecSize;
        }
        if (measureSpecMode == Integer.MIN_VALUE) {
            return Math.min(desiredSize, measureSpecSize);
        }
        return desiredSize;
    }

    public Bitmap getBitmap()
    {
        return ((BitmapDrawable) this.mImageView.getDrawable()).getBitmap();
    }
}
