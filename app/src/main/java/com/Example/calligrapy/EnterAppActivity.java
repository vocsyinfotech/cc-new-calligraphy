package com.Example.calligrapy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import vocsy.ads.AppUtil;
import vocsy.ads.ExitScreen;
import vocsy.ads.GoogleAds;


public class EnterAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_DARK);
        setContentView(R.layout.activity_enter_app);

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.nativeLay));

        findViewById(R.id.btnGetStarted).setOnClickListener(view -> startActivity(new Intent(EnterAppActivity.this, ScorpionMainActivity.class)));

        findViewById(R.id.btnRateApp).setOnClickListener(view -> AppUtil.rateApp(EnterAppActivity.this));

        findViewById(R.id.btnPrivacyPolicy).setOnClickListener(view -> AppUtil.privacyPolicy(EnterAppActivity.this, getString(R.string.privacy_policy)));

        findViewById(R.id.btnShareApp).setOnClickListener(view -> AppUtil.shareApp(EnterAppActivity.this));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EnterAppActivity.this, ExitScreen.class));
    }
}