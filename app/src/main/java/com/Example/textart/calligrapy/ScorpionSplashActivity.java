package com.Example.textart.calligrapy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ScorpionSplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    Handler hl;
    Runnable rbl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(128);



        try {

        } catch (Exception e) {
        }
        this.hl = new Handler();
        this.rbl = new Runnable() {
            public void run() {
                ScorpionSplashActivity.this.startActivity(new Intent(ScorpionSplashActivity.this, ScorpionMainActivity.class));
                try {


                } catch (Exception e) {
                }
                ScorpionSplashActivity.this.finish();




            }
        };
        this.hl.postDelayed(this.rbl, (long) SPLASH_TIME_OUT);
    }

    protected void onStop() {
        getWindow().clearFlags(128);
        super.onStop();
    }

    public void onBackPressed() {
        try {
            this.hl.removeCallbacks(this.rbl);
        } catch (Exception e) {
        }
        finish();
        System.exit(0);
    }

    protected void onDestroy() {
        System.gc();
        getWindow().clearFlags(128);
        super.onDestroy();
    }



}
