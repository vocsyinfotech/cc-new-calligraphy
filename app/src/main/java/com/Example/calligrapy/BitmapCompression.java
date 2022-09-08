package com.Example.calligrapy;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapCompression
{
    static int h;
    static int w;

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        float scale = Math.max(((float) newWidth) / ((float) sourceWidth), ((float) newHeight) / ((float) sourceHeight));
        float scaledWidth = scale * ((float) sourceWidth);
        float scaledHeight = scale * ((float) sourceHeight);
        float left = (((float) newWidth) - scaledWidth) / 2.0f;
        float top = (((float) newHeight) - scaledHeight) / 2.0f;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        new Canvas(dest).drawBitmap(source, null, targetRect, null);
        return dest;
    }

    public static Bitmap decodeFile(File f, int reqHeight, int reqWidth) {
        Bitmap bitmap = null;
        try {
            Options o = new Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int scale = 1;
            while ((o.outWidth / scale) / 2 >= reqWidth && (o.outHeight / scale) / 2 >= reqHeight) {
                scale *= 2;
            }
            Options o2 = new Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return bitmap;
    }

    public static Bitmap adjustImageOrientation(File f, Bitmap image)
    {
        try {
            int rotate;
            switch (new ExifInterface(f.getAbsolutePath()).getAttributeInt("Orientation", 0)) {
                case me.grantland.widget.R.styleable.AutofitTextView_minTextSize /*0*/:
                    rotate = 0;
                    break;
                case me.grantland.widget.R.styleable.AutofitTextView_precision /*1*/:
                    rotate = 1;
                    break;
                case 3 /*3*/:
                    rotate = 180;
                    break;
                case 6 /*6*/:
                    rotate = 90;
                    break;
                case 8 /*8*/:
                    rotate = 270;
                    break;
                default:
                    rotate = 0;
                    break;
            }
            Matrix mtx = new Matrix();
            w = image.getWidth();
            h = image.getHeight();
            if (rotate > 1) {
                mtx.preRotate((float) rotate);
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false);
            }
            return image.copy(Config.ARGB_8888, true);
        } catch (IOException e) {
            return null;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case 2/*2*/:
                matrix.setScale(-1.0f, 1.0f);
                break;
            case 3 /*3*/:
                matrix.setRotate(180.0f);
                break;
            case 4 /*4*/:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 5 /*5*/:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 6 /*6*/:
                matrix.setRotate(90.0f);
                break;
            case 7 /*7*/:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 8 /*8*/:
                matrix.setRotate(-90.0f);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri, new String[]{"orientation"}, null, null, null);
        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri, int MAX_IMAGE_DIMENSION) throws IOException {
        int rotatedWidth;
        int rotatedHeight;
        Bitmap srcBitmap;
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        Options dbo = new Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();
        int orientation = getOrientation(context, photoUri);
        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float maxRatio = Math.max(((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION), ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION));
            Options options = new Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();
        if (orientation <= 0) {
            return srcBitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float) orientation);
        return Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
    }

    public static Bitmap adjustImageOrientationUri(Context context, Uri f, Bitmap image) {
        Matrix matrix = new Matrix();
        Cursor cursor = context.getContentResolver().query(f, new String[]{"orientation"}, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int orientation = cursor.getInt(0);
            w = image.getWidth();
            h = image.getHeight();
            if (orientation > 0) {
                matrix.preRotate((float) orientation);
                image = Bitmap.createBitmap(image, 0, 0, w, h, matrix, false);
            }
        }
        return image.copy(Config.ARGB_8888, true);
    }
}
