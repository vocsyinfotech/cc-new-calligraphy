package com.Example.calligrapy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ScorpionMainActivity extends AppCompatActivity {
    private int PERMISSION_REQUEST_CODE = 101;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_DARK);
        setContentView(R.layout.activity_main);

        findViewById(R.id.create_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScorpionMainActivity.this, CalligraphyMainActivity.class));
            }
        });

        findViewById(R.id.your_creation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    startActivity(new Intent(ScorpionMainActivity.this, MyCreation.class));
                } else {
                    requestPermission();
                }
            }
        });
    }

    private boolean checkPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? ContextCompat.checkSelfPermission(ScorpionMainActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED : ContextCompat.checkSelfPermission(ScorpionMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? ActivityCompat.shouldShowRequestPermissionRationale(ScorpionMainActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) : ActivityCompat.shouldShowRequestPermissionRationale(ScorpionMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(ScorpionMainActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? new String[]{android.Manifest.permission.READ_MEDIA_IMAGES} : new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(ScorpionMainActivity.this, MyCreation.class));
            }
        }
    }
}