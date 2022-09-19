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

import vocsy.ads.GoogleAds;


public class ScorpionMainActivity extends AppCompatActivity {
    private LinearLayout your_creation;
    private LinearLayout create_new;

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
        SystemConfiguration.setStatusBarColor(this, R.color.backgroundcolor);
        setContentView(R.layout.activity_main);

        GoogleAds.getInstance().addBigNativeView(this, findViewById(R.id.nativeLay));

        this.create_new = (LinearLayout) findViewById(R.id.create_new);
        this.create_new.setOnClickListener(arg0 -> {
            isStart = true;
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
        });

        this.your_creation = (LinearLayout) findViewById(R.id.your_creation);
        this.your_creation.setOnClickListener(arg0 -> GoogleAds.getInstance().showCounterInterstitialAd(ScorpionMainActivity.this, () -> {
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
        }));
    }

    private void startNextActivity() {
        startActivity(new Intent(ScorpionMainActivity.this, isStart ? ScorpionTextActivity.class : MyCreation.class));
    }


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
