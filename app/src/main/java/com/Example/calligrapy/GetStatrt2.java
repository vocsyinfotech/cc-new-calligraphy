package com.Example.calligrapy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
;import vocsy.ads.AppUtil;
import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

public class GetStatrt2 extends AppCompatActivity {
    LinearLayout start, share_app, privacy_policy, rateapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_statrt2);
        start = findViewById(R.id.start);
        share_app = findViewById(R.id.share_app);
        privacy_policy = findViewById(R.id.privacy_policy);
        rateapp = findViewById(R.id.rateapp);

        GoogleAds.getInstance().addNativeView(this, findViewById(R.id.nativeLay));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {

                        startActivity(new Intent(GetStatrt2.this, ScorpionMainActivity.class));
                    }
                });
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {

                        AppUtil.privacyPolicy(GetStatrt2.this, getString(R.string.privacy_policy));
                    }
                });

            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {

                        AppUtil.shareApp(GetStatrt2.this);
                    }
                });

            }
        });
        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {

                        AppUtil.rateApp(GetStatrt2.this);
                    }
                });

            }
        });

    }
    @Override
    public void onBackPressed() {

                startActivity(new Intent(GetStatrt2.this,GetStart.class));

    }

}