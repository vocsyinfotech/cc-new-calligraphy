package com.Example.calligrapy;

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


import java.io.File;

import vocsy.ads.GoogleAds;

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
        path = getIntent().getExtras().getString("imageID");
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
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_gallary /*2131427490*/:
//                Intent b1 = new Intent(getApplicationContext(), ScorpionMainActivity.class);
//                b1.setFlags(67141632);
//                startActivity(b1);
//                finish();

                onBackPressed();
                return;
            case R.id.btn_delete /*2131427491*/:
                path = getIntent().getExtras().getString("imageID");
                File fdelete = new File(path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        onBackPressed();
                        DeleteRecursive(fdelete);
                    }
                }
                onBackPressed();
                return;
            case R.id.btn_share /*2131427492*/:

                String appnameString = getResources().getString(R.string.app_name);
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.putExtra("android.intent.extra.SUBJECT", appnameString);
                // Uri screenshotUri = Uri.fromFile(new File(this.path));

                File file = new File(path);
                Uri screenshotUri = FileProvider.getUriForFile(ScorpionImage_Activity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                sharingIntent.setType("image/jpg");
                sharingIntent.putExtra("android.intent.extra.STREAM", screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share Image"));

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
