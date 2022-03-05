package com.Example.textart.calligrapy;


import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;


public class ScorpionApps extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.ads_id));

    }
}
