package com.Example.textart.calligrapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.Example.textart.calligrapy.GoogleAds.GoogleAds;
import com.Example.textart.calligrapy.GoogleAds.RandomAdListener;
import com.Example.textart.calligrapy.GoogleAds.RandomBackAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class ScorpionImage_Activity extends Activity implements OnClickListener {

    String applicationname;
    ImageButton back_gallery;
    Context context;
    ImageButton delete_gallery;
    String path;
    ImageButton share_gallery;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.new_calli_dslr);

        this.context = this;
        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.nativeLay));
        this.path = getIntent().getExtras().getString("imageID");
        ((ImageView) findViewById(R.id.displayimage)).setImageBitmap(BitmapFactory.decodeFile(this.path, new Options()));
        this.applicationname = getResources().getString(R.string.app_name);
        this.back_gallery = (ImageButton) findViewById(R.id.btn_back_gallary);
        this.back_gallery.setOnClickListener(this);
        this.share_gallery = (ImageButton) findViewById(R.id.btn_share);
        this.share_gallery.setOnClickListener(this);
        this.delete_gallery = (ImageButton) findViewById(R.id.btn_delete);
        this.delete_gallery.setOnClickListener(this);
    }


    public void onBackPressed() {
        GoogleAds.getInstance().showBackCounterInterstitialAd(this, new RandomBackAdListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_gallary /*2131427490*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionImage_Activity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {

                        Intent b1 = new Intent(getApplicationContext(), ScorpionMainActivity.class);
                        b1.setFlags(67141632);
                        startActivity(b1);
                        finish();
                    }
                });


                return;
            case R.id.btn_delete /*2131427491*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionImage_Activity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        String path = getIntent().getExtras().getString("imageID");
                        String folderpath = Environment.getExternalStorageDirectory().toString();
                        File fdelete = new File(path);
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                Log.e("-->", "file Deleted :" + path);
                                finish();
                                Intent in = new Intent(getApplicationContext(), ScorpionSave_Images_ShowActivity.class);
                                in.setFlags(67141632);
                                startActivity(in);

                                DeleteRecursive(fdelete);
                            } else {
                                Log.e("-->", "file not Deleted :" + path);
                            }
                        }
                    }
                });

                finish();
                return;
            case R.id.btn_share /*2131427492*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionImage_Activity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {

                        String appnameString = getResources().getString(R.string.app_name);
                        Intent sharingIntent = new Intent("android.intent.action.SEND");
                        sharingIntent.putExtra("android.intent.extra.SUBJECT", appnameString);
                        // Uri screenshotUri = Uri.fromFile(new File(this.path));

                        File file = new File(path);
                        Uri screenshotUri = FileProvider.getUriForFile(ScorpionImage_Activity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                        sharingIntent.setType("image/jpg");
                        sharingIntent.putExtra("android.intent.extra.STREAM", screenshotUri);
                        startActivity(Intent.createChooser(sharingIntent, "Share Image"));
                    }
                });

                return;
            default:
                return;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"NewApi"})
    public static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                DeleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }


}
