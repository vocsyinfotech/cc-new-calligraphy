package com.Example.textart.calligrapy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.grantland.widget.BuildConfig;

class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    BitmapUtil() {
    }

    public static String saveImageToGallery(Bitmap bmp) {
        if (bmp == null) {
            Log.e(TAG, "saveImageToGallery: the bitmap is null");
            return BuildConfig.FLAVOR;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(), "Playalot");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "saveImageToGallery: the path of bmp is " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public static void notifySystemGallery(Context context, File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "notifySystemGallery: the file do not exist.");
            return;
        }
        try {
            Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
    }
}
