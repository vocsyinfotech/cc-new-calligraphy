package com.Example.textart.calligrapy.GoogleAds;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Example.textart.calligrapy.R;
import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class InternetNotAvailableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_not_available);

        ImageView gifView = findViewById(R.id.gifView);
            Glide.with(this).asGif().load(R.drawable.no_interner_error_gif1).into(gifView);

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                // TODO: Handle the connection...
                if (isConnected) {
                    if (AdsUtils.nativeId.length()<1) {
                        AdsUtils.getInstance(InternetNotAvailableActivity.this);
                        new GetSmartAdmob().execute();
                    } else {
                        Toast.makeText(InternetNotAvailableActivity.this, "Back to online.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        int randonPos = new Random().nextInt(10000 - 1000 + 1) + 1000;

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.exit_app_bt).setOnClickListener(V -> startActivity(new Intent(InternetNotAvailableActivity.this, ExitScreen.class)));
        findViewById(R.id.retry_bt).setOnClickListener(V -> {
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(InternetNotAvailableActivity.this, "Internet not found.", Toast.LENGTH_SHORT).show();
                }
            }, randonPos);
        });
    }


    private class GetSmartAdmob extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();
            String url = getString(R.string.Ad_id);

            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
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
                            Log.e("TAG", "doInBackground: " + e.getMessage());
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
            Toast.makeText(InternetNotAvailableActivity.this, "Back to online.", Toast.LENGTH_SHORT).show();
            if (success) {
                if (BaseApplication.appOpenManager==null) {
                    BaseApplication.appOpenManager = new AppOpenManager(BaseApplication.getInstance());
                }
            } else {
                AdsUtils.adsOnOff = "0";
                AdsUtils.backAdsOnOff = "0";
            }
            finish();
            super.onPostExecute(success);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}