package com.Example.calligrapy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import vocsy.ads.AppUtil;


public class EnterAppActivity extends AppCompatActivity {

    private LinearLayout get_started, share_app, privacy_policy, rate_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_app);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        SystemConfiguration.setStatusBarColor(this, R.color.white);
        get_started = findViewById(R.id.get_started);
        share_app = findViewById(R.id.share_app);
        privacy_policy = findViewById(R.id.privacy_policy);
        rate_app = findViewById(R.id.rate_app);

        get_started.setOnClickListener(view -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(get_started, "lay_1"),
                    Pair.create(privacy_policy, "lay_2")
            );

            Intent intent = new Intent(EnterAppActivity.this, ScorpionMainActivity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        });

        privacy_policy.setOnClickListener(v -> AppUtil.privacyPolicy(EnterAppActivity.this, getString(R.string.privacy_policy)));

        share_app.setOnClickListener(v -> AppUtil.shareApp(EnterAppActivity.this));

        rate_app.setOnClickListener(v -> AppUtil.rateApp(EnterAppActivity.this));

    }

}