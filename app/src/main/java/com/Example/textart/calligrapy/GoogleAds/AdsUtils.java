package com.Example.textart.calligrapy.GoogleAds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AdsUtils {
    /**
     * always remember to remove this test id's, after complete app.
     */
    public static AdsUtils instance;
    public static String bannerId = "";
    public static String bannerIdOptional = "";
    public static String nativeId = "";
    public static String nativeIdOptional = "";
    public static String interstitialId = "";
    public static String interstitialIdOptional = "";
    public static String backInterstitialId = "";
    public static String openAds = "";
    // 0 = off
    // 1 = on
    public static String adsOnOff = "1";
    public static String backAdsOnOff = "1";
    public static int adsMin = 0;
    public static int adsMax = 5;
    public static int backAdsMin = 0;
    public static int backAdsMax = 5;
    public static int adsRandomSize = 0;
    public static int backAdsRandomSize = 0;
    public static int adsCounter = 0;
    public static int backAdsCounter = 0;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private static Activity activity;

    public AdsUtils(Activity activity) {
        this.activity = activity;
    }


    public static void generateRandomAdsRange() {
        adsRandomSize = randomInRange(adsMin, adsMax);//new Random().nextInt((adsMax - adsMin) + 1) + adsMin;
        adsCounter = 0;
        editor.putString("adsInterRange", String.valueOf(adsRandomSize));
        editor.apply();
    }

    public static void generateBackRandomAdsRange() {
        backAdsRandomSize = randomInRange(backAdsMin, backAdsMax);// new Random().nextInt((backAdsMax - backAdsMin) + 1) + backAdsMin;
        backAdsCounter = 0;
        editor.putString("backAdsInterRange", String.valueOf(backAdsRandomSize));
        editor.apply();
    }

    public static int randomInRange(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }


    @SuppressLint("CommitPrefEdits")
    public static synchronized AdsUtils getInstance(Activity activity) {


        sharedPreferences = activity.getSharedPreferences("adsPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        if (instance == null) {

            instance = new AdsUtils(activity);
            editor.putString("adsInterRange", "0");
            editor.putString("backAdsInterRange", "0");
            editor.apply();

        }

        return instance;

    }
}
