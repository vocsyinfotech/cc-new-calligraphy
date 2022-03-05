package com.Example.textart.calligrapy;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;


public class ScorpionMainActivity extends Activity {
    ImageButton btn_img;
    ImageButton btn_start;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);


        if (ScorpionNetwork.isDataConnectionAvailable(getBaseContext())) {

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.banner);

            Banner(relativeLayout, ScorpionMainActivity.this);
        }

        try {

        } catch (Exception e) {
        }

        this.btn_start = (ImageButton) findViewById(R.id.btn_start);
        this.btn_start.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {


                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER};
                String rationale = "Please provide  permission so that you can ...";
                Permissions.Options options = new Permissions.Options()
                        .setRationaleDialogTitle("Info")
                        .setSettingsDialogTitle("Warning");

                Permissions.check(ScorpionMainActivity.this, permissions, rationale, options, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        // do your task.

                        ScorpionMainActivity.this.startActivity(new Intent(ScorpionMainActivity.this, ScorpionTextActivity.class));
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        // permission denied, block the feature.
                    }
                });


            }
        });
        this.btn_img = (ImageButton) findViewById(R.id.btn_img);
        this.btn_img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {


                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER};
                String rationale = "Please provide  permission so that you can ...";
                Permissions.Options options = new Permissions.Options()
                        .setRationaleDialogTitle("Info")
                        .setSettingsDialogTitle("Warning");

                Permissions.check(ScorpionMainActivity.this, permissions, rationale, options, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        // do your task.

                        ScorpionMainActivity.this.startActivity(new Intent(ScorpionMainActivity.this, ScorpionSave_Images_ShowActivity.class));
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        // permission denied, block the feature.
                    }
                });


            }
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }


                })


                .setNeutralButton("More app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent more = new Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.moreapp)));
                        try {
                            startActivity(more);

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(
                                    ScorpionMainActivity.this,
                                    "you_don_t_have_google_play_installed_or_internet_connection", Toast.LENGTH_LONG).show();
                        }

                    }
                })
        ;


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void Banner(final RelativeLayout Ad_Layout, final Context context) {

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.LARGE_BANNER);
        mAdView.setAdUnitId(getString(R.string.ads_bnr));
        AdRequest adre = new AdRequest.Builder().build();
        mAdView.loadAd(adre);
        Ad_Layout.addView(mAdView);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                Ad_Layout.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // TODO Auto-generated method stub

                Ad_Layout.setVisibility(View.GONE);
            }
        });
    }


}
