package com.Example.calligrapy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import vocsy.ads.ExitScreen;

public class GetStartActivity extends AppCompatActivity {
    private Button get_start;
    private LogoView image;
    private TextView title;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        SystemConfiguration.setStatusBarColor(this, R.color.white);

        get_start = findViewById(R.id.get_start);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);

        get_start.setOnClickListener(view -> {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, image, "image");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(image, "image")
            );

            Intent intent = new Intent(GetStartActivity.this, EnterAppActivity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetStartActivity.this, ExitScreen.class));
    }
}