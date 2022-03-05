package com.Example.textart.calligrapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class ScorpionMyCrop extends Activity implements OnClickListener {
    String ImagePath;
    ImageButton btn_left;
    ImageButton btn_right;
    ImageButton btnback;
    ImageButton btnnext;
    Bitmap cropImage;
    CropImageView cropview;
    int fl_height;
    int fl_width;
    int h;
    private File mFileTemp;
    DisplayMetrics om;
    Uri selected;
    Uri selectedImageUri;
    int w;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_my_crop);

        if (ScorpionNetwork.isDataConnectionAvailable(getBaseContext())) {

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.banner);
            Banner(relativeLayout, ScorpionMyCrop.this);

        }

        findViewById();
        this.om = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.om);
        this.h = this.om.heightPixels;
        this.w = this.om.widthPixels;
        this.fl_height = 1100;
        this.fl_width = 710;
        this.fl_height = (this.h * this.fl_height) / 1280;
        this.fl_width = (this.w * this.fl_width) / 720;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            this.mFileTemp = new File(Environment.getExternalStorageDirectory(), ScorpionUtils.TEMP_FILE_NAME);
        } else {
            this.mFileTemp = new File(getFilesDir(), ScorpionUtils.TEMP_FILE_NAME);
        }
        if (ScorpionUtils.selectedImageUri != null) {
            this.selectedImageUri = ScorpionUtils.selectedImageUri;
            try {
                this.cropImage = BitmapCompression.decodeFile(new File(getPath(this.selectedImageUri)), this.fl_height, this.fl_width);
                this.cropImage = BitmapCompression.adjustImageOrientationUri(getApplicationContext(), this.selectedImageUri, this.cropImage);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                this.cropImage = BitmapCompression.decodeFile(this.mFileTemp, this.fl_height, this.fl_width);
                this.cropImage = BitmapCompression.adjustImageOrientation(this.mFileTemp, this.cropImage);
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        }
        this.cropview.setImageBitmap(this.cropImage);
        this.btnback.setOnClickListener(this);
        this.btnnext.setOnClickListener(this);
        this.btn_left.setOnClickListener(this);
        this.btn_right.setOnClickListener(this);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }

    private void findViewById() {
        this.btnback = (ImageButton) findViewById(R.id.btnback);
        this.btnnext = (ImageButton) findViewById(R.id.btn_next);
        this.btn_right = (ImageButton) findViewById(R.id.btn_right);
        this.btn_left = (ImageButton) findViewById(R.id.btn_left);
        this.cropview = (CropImageView) findViewById(R.id.cropImageView);
        this.cropview.setFixedAspectRatio(true);
        this.cropview.setAspectRatio(10, 10);
    }

    public void onBackPressed()
    
    {

        Intent newIntent = new Intent(this, ScorpionTextActivity.class);
        newIntent.addFlags(335544320);
        startActivity(newIntent);
        finish();
    }

    public void onClick(View arg0)
    {
        switch (arg0.getId()) {
            case R.id.btnback /*2131427429*/:
                onBackPressed();

                return;
            case R.id.btn_left /*2131427436*/:
                this.cropview.setImageBitmap(rotateImage(this.cropview.getBitmap(), -90.0f));
                return;
            case R.id.btn_next /*2131427437*/:
                ScorpionUtils.bits = this.cropview.getCroppedImage();
                setResult(RESULT_OK);
                finish();

                return;
            case R.id.btn_right /*2131427438*/:
                this.cropview.setImageBitmap(rotateImage(this.cropview.getBitmap(), 90.0f));

                return;
            default:
                return;
        }
    }

    public Bitmap rotateImage(Bitmap src, float degree)
    {
        Matrix matrix = new Matrix();
        matrix.preRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    protected void onDestroy()
    {
        getWindow().clearFlags(128);
        super.onDestroy();
    }

    public void Banner(final RelativeLayout Ad_Layout, final Context context) {

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.ads_bnr));
        AdRequest adre = new AdRequest.Builder().build();
        mAdView.loadAd(adre);
        Ad_Layout.addView(mAdView);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                Ad_Layout.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // TODO Auto-generated method stub
                Ad_Layout.setVisibility(View.GONE);


            }
        });
    }





}
