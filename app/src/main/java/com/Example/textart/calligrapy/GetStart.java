package com.Example.textart.calligrapy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Example.textart.calligrapy.GoogleAds.ExitScreen;
import com.Example.textart.calligrapy.GoogleAds.GoogleAds;
import com.Example.textart.calligrapy.GoogleAds.InternetNotAvailableActivity;
import com.Example.textart.calligrapy.GoogleAds.RandomAdListener;
import com.Example.textart.calligrapy.GoogleAds.RandomBackAdListener;
import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;

public class GetStart extends AppCompatActivity {
LinearLayout getstart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        GoogleAds.getInstance().addNativeView(this, findViewById(R.id.nativeLay));
        Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                // TODO: Handle the connection...
                if (!isConnected) {
                    Toast.makeText(GetStart.this, "Internet Connection lost.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GetStart.this, InternetNotAvailableActivity.class));
                }
            }
        });

        getstart =findViewById(R.id.getstart);

        getstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStart.this, new RandomAdListener() {
                @Override
                public void onClick() {
                    startActivity(new Intent(GetStart.this,GetStatrt2.class));
                }
            });
            }
        });
    }
    @Override
    public void onBackPressed() {
        GoogleAds.getInstance().showBackCounterInterstitialAd(this, new RandomBackAdListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(GetStart.this, ExitScreen.class));
            }
        });
    }
}