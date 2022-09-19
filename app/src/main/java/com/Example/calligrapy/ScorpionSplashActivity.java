package com.Example.calligrapy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import vocsy.ads.AdsHandler;
import vocsy.ads.GetSmartAdmob;

public class ScorpionSplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    Handler hl;
    Runnable rbl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        String[] adsUrls = new String[]{
                getString(R.string.bnr_admob)// 1st Banner Ads Id
                , getString(R.string.native_admob)// 2st Native Ads Id
                , getString(R.string.int_admob)// 3st interstitial Ads Id
                , getString(R.string.app_open_admob)// 4st App-Open Ads Id
                , getString(R.string.video_admob)// 5st Rewarded Ads Id
        };

        new GetSmartAdmob(this, adsUrls, (success) -> {
            // admob init Success
        }).execute();

        AdsHandler.setAdsOn(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ScorpionSplashActivity.this, GetStartActivity.class));
                finish();
            }
        }, 1500);
    }


}
