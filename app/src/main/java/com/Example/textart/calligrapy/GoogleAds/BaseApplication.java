package com.Example.textart.calligrapy.GoogleAds;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class BaseApplication extends MultiDexApplication {

    public static final String TAG = "BaseApplication";
    public static BaseApplication instance;
    public static InterstitialAd mInterstitialAd;
    public static InterstitialAd mInterstitialAdBack;
    public static AppOpenManager appOpenManager;

    public static synchronized BaseApplication getInstance() {
        BaseApplication application;
        synchronized (BaseApplication.class) {
            application = instance;
        }
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

    }
}
