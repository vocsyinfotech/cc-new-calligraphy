package com.Example.calligrapy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;


public class ScorpionMainActivity extends AppCompatActivity {
    LinearLayout btn_img;
    LinearLayout btn_start;

    private boolean isStart = false;

    private int mRequestCode = 0;
    private String mPermission = "";
    private Bitmap bitmap = null;
    private File photoFile = null;

    // Permission handler...
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    afterPermission(); // create method in your activity
                } else {
                    Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
                }
            });

    public void startPermission(Activity activity, String permission, int mRequestCode) {
        this.mRequestCode = mRequestCode;
        this.mPermission = permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                afterPermission();
            } else if (activity.shouldShowRequestPermissionRationale(permission)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission");
                builder.setMessage("You need to grant the " + permission + " permission for use our application");
                builder.setCancelable(false);
                builder.setPositiveButton("I UNDERSTAND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
                builder.show();
            } else {
                requestPermissionLauncher.launch(permission);
            }
        }
    }

    private void afterPermission() {
        switch (mRequestCode) {
            case 101:
                pickFromGallery(501);
                break;
            case 102:
                startPermission(ScorpionMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 103);
                break;
            case 103:
                chooseFromCamera(502);
                break;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);

        GoogleAds.getInstance().addNativeView(this, findViewById(R.id.nativeLay));

        this.btn_start = (LinearLayout) findViewById(R.id.btn_start);
        this.btn_start.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                isStart = true;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ContextCompat.checkSelfPermission(ScorpionMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        startNextActivity();
//                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivity(intent);
//                    } else {
//                        permissionListener.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    }
//                }

                new ItemChoiceDialog(ScorpionMainActivity.this, (action -> {
                    switch (action) {
                        case "gallery":
                            startPermission(ScorpionMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
                            break;
                        case "camera":
                            startPermission(ScorpionMainActivity.this, Manifest.permission.CAMERA, 102);
                            break;
                    }
                })).show();

//                chooseFromCamera(502);
            }
        });

        this.btn_img = (LinearLayout) findViewById(R.id.btn_img);
        this.btn_img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionMainActivity.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {

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

        startActivity(new Intent(ScorpionMainActivity.this, GetStatrt2.class));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 501:
                    if (data != null) {
                        try {
                            ScorpionUtils.bits = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                            startNextActivity();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 502:
                    if (photoFile != null) {
                        ScorpionUtils.bits = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        startNextActivity();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickFromGallery(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, "Select Picture from"), requestCode);
    }

    private void chooseFromCamera(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        long timeStamp = Calendar.getInstance().getTimeInMillis();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

}
