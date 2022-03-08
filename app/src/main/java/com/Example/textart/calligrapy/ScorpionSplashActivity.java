package com.Example.textart.calligrapy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.Example.textart.calligrapy.GoogleAds.AdsUtils;
import com.Example.textart.calligrapy.GoogleAds.AppOpenManager;
import com.Example.textart.calligrapy.GoogleAds.BaseApplication;
import com.Example.textart.calligrapy.GoogleAds.HttpHandler;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

public class ScorpionSplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    Handler hl;
    Runnable rbl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AdsUtils.getInstance(ScorpionSplashActivity.this);
        new GetSmartAdmob().execute();
    }

    private class GetSmartAdmob extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.e("sefghsrtbh", "doInBackground: ");
            HttpHandler sh = new HttpHandler();
            String url = getString(R.string.ad_json);
            String jsonStr = sh.makeServiceCall(url);
            Log.e("TAG", "doInBackground: " + jsonStr);
            if (jsonStr != null && !jsonStr.isEmpty()) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject adsJsonObject = jsonObj.getJSONObject("ads");

                    AdsUtils.bannerId = adsJsonObject.getString("bannerid");
                    AdsUtils.bannerIdOptional = adsJsonObject.getString("bannerid1");
                    AdsUtils.nativeId = adsJsonObject.getString("nativeid");
                    AdsUtils.nativeIdOptional = adsJsonObject.getString("nativeid1");
                    AdsUtils.interstitialId = adsJsonObject.getString("interid");
                    AdsUtils.interstitialIdOptional = adsJsonObject.getString("interid1");
                    AdsUtils.backInterstitialId = adsJsonObject.getString("backinterid");
                    AdsUtils.openAds = adsJsonObject.getString("openads");
                    AdsUtils.adsOnOff = adsJsonObject.getString("inads");
                    AdsUtils.backAdsOnOff = adsJsonObject.getString("backads");
                    AdsUtils.adsMin = adsJsonObject.getInt("inmin");
                    AdsUtils.adsMax = adsJsonObject.getInt("inmax");
                    AdsUtils.backAdsMin = adsJsonObject.getInt("backmin");
                    AdsUtils.backAdsMax = adsJsonObject.getInt("backmax");

                    return true;

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    return false;

                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                BaseApplication.appOpenManager = new AppOpenManager(BaseApplication.getInstance());
            } else {
                AdsUtils.adsOnOff = "0";
                AdsUtils.backAdsOnOff = "0";
            }
            ScorpionSplashActivity.this.startActivity(new Intent(ScorpionSplashActivity.this, GetStart.class));
            finish();
            super.onPostExecute(success);
        }
    }


}
