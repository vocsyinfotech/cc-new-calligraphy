package com.Example.textart.calligrapy.GoogleAds;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.Example.textart.calligrapy.R;

public class ExitScreen extends AppCompatActivity {

    ImageView txt_rate, txt_no, txt_yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_screen);

        txt_rate = findViewById(R.id.txt_rate);
        txt_yes = findViewById(R.id.txt_yes);
        txt_no = findViewById(R.id.txt_no);

        txt_rate.setOnClickListener(v -> rate());
        txt_yes.setOnClickListener(v -> yes());
        txt_no.setOnClickListener(v -> no());
        GoogleAds.getInstance().addNativeView(ExitScreen.this,findViewById(R.id.nativeLay));

    }

    private void rate() {
      AppUtil.shareApp(this);
    }

    private void yes() {
        finishAffinity();
    }

    private void no() {
      finish();
    }
}
