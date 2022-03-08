package com.Example.textart.calligrapy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.Example.textart.calligrapy.GoogleAds.AppUtil;
import com.Example.textart.calligrapy.GoogleAds.ExitScreen;
import com.Example.textart.calligrapy.GoogleAds.GoogleAds;
import com.Example.textart.calligrapy.GoogleAds.RandomAdListener;
import com.Example.textart.calligrapy.GoogleAds.RandomBackAdListener;

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
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        startActivity(new Intent(GetStatrt2.this, ScorpionMainActivity.class));
                    }
                });
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        AppUtil.privacyPolicy(GetStatrt2.this, getString(R.string.privacy_policy));
                    }
                });

            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        AppUtil.shareApp(GetStatrt2.this);
                    }
                });

            }
        });
        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStatrt2.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        AppUtil.rateApp(GetStatrt2.this);
                    }
                });

            }
        });

    }
    @Override
    public void onBackPressed() {
        GoogleAds.getInstance().showBackCounterInterstitialAd(this, new RandomBackAdListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(GetStatrt2.this,GetStart.class));
            }
        });
    }

}