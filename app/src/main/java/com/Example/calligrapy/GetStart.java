package com.Example.calligrapy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.ExitScreen;
import vocsy.ads.GoogleAds;

public class GetStart extends AppCompatActivity {
LinearLayout getstart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        GoogleAds.getInstance().addNativeView(this, findViewById(R.id.nativeLay));

        getstart =findViewById(R.id.getstart);

        getstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStart.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {




                startActivity(new Intent(GetStart.this,GetStatrt2.class));
                }
            });
            }
        });
    }
    @Override
    public void onBackPressed() {

                startActivity(new Intent(GetStart.this, ExitScreen.class));

    }
}