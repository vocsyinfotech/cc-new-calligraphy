package com.Example.textart.calligrapy;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Example.textart.calligrapy.GoogleAds.GoogleAds;
import com.Example.textart.calligrapy.GoogleAds.RandomAdListener;
import com.Example.textart.calligrapy.GoogleAds.RandomBackAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;


public class ScorpionMainActivity extends AppCompatActivity {
    LinearLayout btn_img;
    LinearLayout btn_start;

    private boolean isStart = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);

        GoogleAds.getInstance().addNativeView(this, findViewById(R.id.nativeLay));

        this.btn_start = (LinearLayout) findViewById(R.id.btn_start);
        this.btn_start.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionMainActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {

                        isStart = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(ScorpionMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                startNextActivity();
                            } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            } else {
                                permissionListener.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }
                        }
                    }
                });

            }
        });
        this.btn_img = (LinearLayout) findViewById(R.id.btn_img);
        this.btn_img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionMainActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        isStart = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(ScorpionMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                startNextActivity();
                            } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            } else {
                                permissionListener.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }
                        }
                    }
                });

            }
        });
    }

    private void startNextActivity() {
        startActivity(new Intent(ScorpionMainActivity.this, isStart ? ScorpionTextActivity.class : ScorpionSave_Images_ShowActivity.class));
    }


    public void onBackPressed() {
        GoogleAds.getInstance().showBackCounterInterstitialAd(this, new RandomBackAdListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(ScorpionMainActivity.this,GetStatrt2.class));
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Exit Application?");
//        alertDialogBuilder
//                .setMessage("Click yes to exit!")
//                .setCancelable(false)
//                .setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                moveTaskToBack(true);
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(1);
//                            }
//                        })
//
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        dialog.cancel();
//                    }
//
//
//                })
//
//
//                .setNeutralButton("More app", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Intent more = new Intent(
//                                "android.intent.action.VIEW",
//                                Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.moreapp)));
//                        try {
//                            startActivity(more);
//
//                        } catch (ActivityNotFoundException e) {
//                            Toast.makeText(
//                                    ScorpionMainActivity.this,
//                                    "you_don_t_have_google_play_installed_or_internet_connection", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                })
//        ;
//
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }


    private ActivityResultLauncher<String> permissionListener = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            startNextActivity();
        } else {
            Toast.makeText(this, "Please Grant The Permission", Toast.LENGTH_SHORT).show();
        }
    });


}
