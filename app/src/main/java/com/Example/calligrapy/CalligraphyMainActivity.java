package com.Example.calligrapy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class CalligraphyMainActivity extends AppCompatActivity {
    private int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private int GALLERY_PERMISSION_REQUEST_CODE = 102;
    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemConfiguration.setStatusBarColor(this, R.color.appTheme, true);
        setContentView(R.layout.activity_calligraphy_main);

        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.btnCamera).setOnClickListener(view -> {
            if (checkForCameraPermission()) {
                chooseFromCamera(502);
            } else {
                requestForCameraPermission();
            }
        });

        findViewById(R.id.btnGallery).setOnClickListener(view -> {
            if (checkGalleryPermission()) {
                pickFromGallery(501);
            } else {
                requestForGalleryPermission();
            }
        });
    }

    private boolean checkGalleryPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? ContextCompat.checkSelfPermission(CalligraphyMainActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED : ContextCompat.checkSelfPermission(CalligraphyMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? ActivityCompat.shouldShowRequestPermissionRationale(CalligraphyMainActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) : ActivityCompat.shouldShowRequestPermissionRationale(CalligraphyMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(CalligraphyMainActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? new String[]{android.Manifest.permission.READ_MEDIA_IMAGES} : new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkForCameraPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? ContextCompat.checkSelfPermission(CalligraphyMainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CalligraphyMainActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED : ContextCompat.checkSelfPermission(CalligraphyMainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CalligraphyMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? ActivityCompat.shouldShowRequestPermissionRationale(CalligraphyMainActivity.this, Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(CalligraphyMainActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) : ActivityCompat.shouldShowRequestPermissionRationale(CalligraphyMainActivity.this, Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(CalligraphyMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(CalligraphyMainActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? new String[]{Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES} : new String[]{Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFromCamera(502);
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFromGallery(501);
            }
        }
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
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */);
        return image;
    }


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

    private void startNextActivity() {
        startActivity(new Intent(CalligraphyMainActivity.this, ScorpionTextActivity.class));
    }
}