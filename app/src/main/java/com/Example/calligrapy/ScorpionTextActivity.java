package com.Example.calligrapy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

//import com.gc.materialdesign.widgets.ProgressDialog;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.loader.MediaFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

public class ScorpionTextActivity extends AppCompatActivity implements OnClickListener {

    String TAG = "ScorpionTextActivity";
    public static final int RESULT_FROM_CAMERA = 99;
    public static final int RESULT_FROM_GALLERY = 98;
    String applicationName;
    String[] bg_name;
    ImageView btn_back;
    ImageView btn_bg;
    ImageView btn_bg_clr;
    ImageView btn_gallary;
    ImageView btn_gradiant;
    ImageView btn_more;
    ImageView btn_save;
    ImageView btn_symbole;
    ImageView btn_symbole_clr;
    ImageView btn_text;
    Drawable drawable;
    FrameLayout frm;
    HorizontalListView hlvSimpleList1;
    boolean isUpHoneycomb = false;
    File mFileShareTemp;
    private File mGalleryFolder;
    Uri mImageUri;
    String path;
    ProgressDialog pdsave;
    PopupWindow pwindow;
    Uri screenshotUri;
    Uri selectedImageUri;
    StickerView sticker_view;
    Bitmap bm;
    private Dialog dialog;
    private int requestCode;
    private File photoFile;

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

    public void checkPermission(String permission, int requestCode) {
        this.requestCode = requestCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                handleEvent(requestCode);
            } else if (this.shouldShowRequestPermissionRationale(permission)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                permissionListener.launch(permission);
            }
        }
    }

    private void handleEvent(int i) {

        switch (i) {
            case 101:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = createImageFile();
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, ScorpionTextActivity.RESULT_FROM_CAMERA);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Intent it_cam = new Intent("android.media.action.IMAGE_CAPTURE");
//
//                Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", ScorpionUtils.mFileTemp);
//                ScorpionUtils.selectedImageUri = uri;
//                Log.e(TAG, "handleEvent:  ScorpionUtils.selectedImageUri: " + ScorpionUtils.selectedImageUri);
//                it_cam.putExtra("output", uri);
//                ScorpionTextActivity.this.startActivityForResult(it_cam, ScorpionTextActivity.RESULT_FROM_CAMERA);
                dialog.dismiss();
                break;
            case 102:
                ScorpionTextActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), ScorpionTextActivity.RESULT_FROM_GALLERY);
                dialog.dismiss();
                break;
            case 301:
                try {
                    sticker_view.createBitmap();
                    ScorpionTextActivity.this.saveShareImage();
                    Uri screenshotUri1 = FileProvider.getUriForFile(ScorpionTextActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(ScorpionTextActivity.this.mImageUri.getPath()));
                    //Uri screenshotUri1 = Uri.fromFile(new File(ScorpionTextActivity.this.mImageUri.getPath()));
                    Intent intent = new Intent("android.intent.action.ATTACH_DATA");
                    intent.setDataAndType(screenshotUri1, "image/*");
                    intent.putExtra("png", "image/*");
                    ScorpionTextActivity.this.startActivity(Intent.createChooser(intent, "Set as..."));
                    pwindow.dismiss();
                } catch (Exception e2) {
                    Log.e("TAG", "handleEvent: " + e2.getMessage());
                }
                break;
        }


    }

    private final ActivityResultLauncher<String> permissionListener = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            handleEvent(requestCode);
        } else {
            Toast.makeText(this, "You Need Grant The Permission", Toast.LENGTH_SHORT).show();
        }
    });

    private class MySaveImage extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

//            ScorpionTextActivity.this.pdsave = new ProgressDialog(ScorpionTextActivity.this, "Saving...", ScorpionTextActivity.this.getResources().getColor(R.color.Dialog_title_bg_color));
            pdsave = new ProgressDialog(ScorpionTextActivity.this);
            pdsave.setCancelable(false);
            pdsave.show();
            try {
                ScorpionTextActivity.this.sticker_view.createBitmap();
            } catch (Exception e) {
            }
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            Log.e("TAG", "doInBackground: save ");
            ScorpionTextActivity.this.saveImage();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ScorpionTextActivity.this.pdsave.dismiss();
            Toast.makeText(ScorpionTextActivity.this.getApplicationContext(), "Image Saved in " + ScorpionTextActivity.this.applicationName, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScorpionTextActivity.this, ScorpionSave_Images_ShowActivity.class));
            galleryAddPic();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = mGalleryFolder;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private class shareTask extends AsyncTask<Void, Void, Bitmap> {
        private shareTask() {
        }

        protected void onPreExecute() {
            try {
                ScorpionTextActivity.this.sticker_view.createBitmap();
            } catch (Exception e) {
            }
        }

        protected Bitmap doInBackground(Void... arg0) {
            ScorpionTextActivity.this.saveShareImage();
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            screenshotUri = FileProvider.getUriForFile(ScorpionTextActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(ScorpionTextActivity.this.mImageUri.getPath()));
            sharingIntent.setType("image/png");
            sharingIntent.putExtra("android.intent.extra.STREAM", ScorpionTextActivity.this.screenshotUri);
            ScorpionTextActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share Image"));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_text);
        findById();

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.nativeLay));

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);

        if ("mounted".equals(Environment.getExternalStorageState())) {
            ScorpionUtils.mFileTemp = new File(Environment.getExternalStorageDirectory(), ScorpionUtils.TEMP_FILE_NAME);
        } else {
            ScorpionUtils.mFileTemp = new File(getFilesDir(), ScorpionUtils.TEMP_FILE_NAME);
        }
        if (VERSION.SDK_INT >= 11) {
            this.isUpHoneycomb = true;
        }
        this.applicationName = getResources().getString(R.string.app_name);
        this.mGalleryFolder = createFolders();
        if (ScorpionUtils.bits!=null) {
            this.sticker_view.setImageBitmap(getResizedBitmapp(ScorpionUtils.bits, 700));
        }
        this.sticker_view.setBackgroundColor(-1);
        this.sticker_view.setLooked(false);
        this.btn_text.setOnClickListener(this);
        this.btn_bg.setOnClickListener(this);
        this.btn_bg_clr.setOnClickListener(this);
        this.btn_save.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.btn_more.setOnClickListener(this);
        this.btn_symbole.setOnClickListener(this);
        this.btn_symbole_clr.setOnClickListener(this);
        this.btn_gradiant.setOnClickListener(this);
        this.btn_gallary.setOnClickListener(this);
    }

    private void findById() {
        this.btn_back = (ImageView) findViewById(R.id.btn_back);
        this.btn_more = (ImageView) findViewById(R.id.btn_more);

        this.btn_text = (ImageView) findViewById(R.id.btn_text);
        this.btn_bg = (ImageView) findViewById(R.id.btn_bg);
        this.btn_bg_clr = (ImageView) findViewById(R.id.btn_bg_clr);
        this.btn_save = (ImageView) findViewById(R.id.btn_save);
        this.btn_gradiant = (ImageView) findViewById(R.id.btn_gradiant);
        this.btn_symbole = (ImageView) findViewById(R.id.btn_symbole);
        this.btn_gallary = (ImageView) findViewById(R.id.btn_gallary);
        this.btn_symbole_clr = (ImageView) findViewById(R.id.btn_symbole_clr);

        this.sticker_view = (StickerView) findViewById(R.id.sticker_view);
        this.frm = (FrameLayout) findViewById(R.id.frm);
        this.hlvSimpleList1 = (HorizontalListView) findViewById(R.id.hlvSimpleList1);
    }

    private String[] getImage(String folderName) throws IOException {
        return getAssets().list(folderName);
    }

    @SuppressLint({"NewApi"})
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.btn_back /*2131427456*/:

                hlvSimpleList1.setVisibility(View.INVISIBLE);
                startActivity(new Intent(ScorpionTextActivity.this, ScorpionMainActivity.class));


                return;

            case R.id.btn_more /*2131427458*/:

                hlvSimpleList1.setVisibility(View.INVISIBLE);
                openPopupMenu(getApplicationContext(), btn_more);


                return;

            case R.id.btn_gallary /*2131427459*/:

                hlvSimpleList1.setVisibility(View.INVISIBLE);
                selectImage();


                return;

            case R.id.btn_text /*2131427460*/:

                hlvSimpleList1.setVisibility(View.INVISIBLE);
                startActivityForResult(new Intent(ScorpionTextActivity.this, ScorpionEditTextActivity.class), 100);


                return;

            case R.id.btn_bg /*2131427461*/:

                try {
                    bg_name = getImage("bg");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                hlvSimpleList1.setVisibility(View.VISIBLE);
                hlvSimpleList1.setAdapter(new ScorpionStickerAdapter(ScorpionTextActivity.this, new ArrayList(Arrays.asList(bg_name)), "bg"));
                hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                        try {
                            ScorpionTextActivity.this.sticker_view.setImageBitmap(BitmapFactory.decodeStream(ScorpionTextActivity.this.getAssets().open("bg/" + ScorpionTextActivity.this.bg_name[arg2])));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


                return;
            case R.id.btn_gradiant /*2131427462*/:

                hlvSimpleList1.setVisibility(View.VISIBLE);
                try {
                    bg_name = getImage("gradient");
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                hlvSimpleList1.setAdapter(new ScorpionStickerAdapter(ScorpionTextActivity.this, new ArrayList(Arrays.asList(bg_name)), "gradient"));
                hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                        try {

                            ScorpionTextActivity.this.sticker_view.setImageBitmap(BitmapFactory.decodeStream(ScorpionTextActivity.this.getAssets().open("gradient/" + ScorpionTextActivity.this.bg_name[arg2])));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


                return;
            case R.id.btn_bg_clr /*2131427463*/:

                hlvSimpleList1.setVisibility(View.VISIBLE);

                hlvSimpleList1.setAdapter(new ScorpionCustomeColorAdapter(ScorpionTextActivity.this, new ArrayList(Arrays.asList(ScorpionUtils.color))));
                hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {

                        ScorpionTextActivity.this.sticker_view.setImageBitmap(null);
                        ScorpionTextActivity.this.sticker_view.setBackgroundColor(Color.parseColor(ScorpionUtils.color[arg2]));
                    }
                });


                return;
            case R.id.btn_symbole /*2131427464*/:

                hlvSimpleList1.setVisibility(View.GONE);
                startActivityForResult(new Intent(ScorpionTextActivity.this, ScorpionStickerGridActivity.class), MediaFile.FILE_TYPE_DTS);


                return;
            case R.id.btn_symbole_clr /*2131427465*/:

                hlvSimpleList1.setVisibility(View.VISIBLE);
                hlvSimpleList1.setAdapter(new ScorpionCustomeColorAdapter(ScorpionTextActivity.this, new ArrayList(Arrays.asList(ScorpionUtils.color))));
                hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                        try {
                            ScorpionTextActivity.this.drawable = ScorpionTextActivity.this.sticker_view.getCurruntDrawablr();
                            ScorpionTextActivity.this.drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(ScorpionUtils.color[arg2]), Mode.SRC_IN));
                            ScorpionTextActivity.this.sticker_view.replace(ScorpionTextActivity.this.drawable);
                        } catch (Exception e) {
                        }
                    }
                });


                return;
            case R.id.btn_save /*2131427466*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionTextActivity.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        hlvSimpleList1.setVisibility(View.INVISIBLE);
                        if (isUpHoneycomb) {
                            new MySaveImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                            return;
                        } else {
                            new MySaveImage().execute(new Void[0]);
                            return;
                        }
                    }
                });

            default:
                return;
        }
    }

    @SuppressLint("ResourceType")
    private void selectImage() {
        dialog = new Dialog(this, 16973841);
        View v = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.select_img_dialog_bg, null);
        Animation left_anim = AnimationUtils.loadAnimation(this, R.anim.left_anim);
        Animation right_anim = AnimationUtils.loadAnimation(this, R.anim.right_anim);
        ImageView Gal = (ImageView) v.findViewById(R.id.PopGallery);
        ImageView Cam = (ImageView) v.findViewById(R.id.PopCamera);
        Gal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 102);
            }
        });
        Cam.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                checkPermission(Manifest.permission.CAMERA, 101);
            }
        });
        Gal.setAnimation(right_anim);
        Cam.setAnimation(left_anim);
        dialog.setContentView(v);
        dialog.show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case R.styleable.AutofitTextView_precision /*1*/:
                if (grantResults.length > 0 && grantResults[0] == 0) {
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void openPopupMenu(Context context, ImageView btn) {
        this.pwindow = new PopupWindow(this);
        this.pwindow.setBackgroundDrawable(new BitmapDrawable());
        View contentView = getLayoutInflater().inflate(R.layout.pop_window, null);
        this.pwindow.setContentView(contentView);
        LinearLayout llsetas = (LinearLayout) contentView.findViewById(R.id.llsetas);
        LinearLayout llrateas = (LinearLayout) contentView.findViewById(R.id.llrateus);
        LinearLayout llsave = (LinearLayout) contentView.findViewById(R.id.llsave);
        LinearLayout llsharelink = (LinearLayout) contentView.findViewById(R.id.llsharelink);
        ((LinearLayout) contentView.findViewById(R.id.llshareimg)).setOnClickListener(new OnClickListener() {
            @SuppressLint({"NewApi"})
            public void onClick(View arg0) {

                if (ScorpionTextActivity.this.isUpHoneycomb) {
                    new shareTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                } else {
                    new shareTask().execute(new Void[0]);
                }
                ScorpionTextActivity.this.pwindow.dismiss();


            }
        });
        llsetas.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                checkPermission(Manifest.permission.SET_WALLPAPER, 301);

            }

        });
        llrateas.setOnClickListener(new OnClickListener() {
            @SuppressLint({"NewApi"})
            public void onClick(View arg0) {

                ScorpionTextActivity.this.pwindow.dismiss();
                try {
                    try {
                        ScorpionTextActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + ScorpionTextActivity.this.getApplicationContext().getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        ScorpionTextActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + ScorpionTextActivity.this.getApplicationContext().getPackageName())));
                    }
                } catch (Exception e2) {
                }


            }
        });
        llsave.setOnClickListener(new OnClickListener() {
            @SuppressLint({"NewApi"})
            public void onClick(View arg0) {

                if (ScorpionTextActivity.this.isUpHoneycomb) {
                    new MySaveImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                } else {
                    new MySaveImage().execute(new Void[0]);
                }
                ScorpionTextActivity.this.pwindow.dismiss();


            }
        });
        llsharelink.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                try {
                    Intent i = new Intent("android.intent.action.SEND");
                    i.setType(StringBody.CONTENT_TYPE);
                    i.putExtra("android.intent.extra.SUBJECT", ScorpionTextActivity.this.getResources().getString(R.string.app_name));
                    i.putExtra("android.intent.extra.TEXT", ScorpionTextActivity.this.getResources().getString(R.string.sharemsg) + "https://play.google.com/store/apps/details?id=" + ScorpionTextActivity.this.getApplicationContext().getPackageName() + " \n\n");
                    ScorpionTextActivity.this.startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception e) {
                }


            }
        });
        this.pwindow.setFocusable(true);
        this.pwindow.setWindowLayoutMode(-2, -2);
        this.pwindow.setOutsideTouchable(true);
        this.pwindow.showAsDropDown(btn, 0, 20);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 100) {
            ScorpionDialogDrawable dd = new ScorpionDialogDrawable();
            dd.setFontStyle(getApplicationContext(), ScorpionUtils.typeface);
            dd.setTextData(ScorpionUtils.text);
            dd.setTextColor(ScorpionUtils.clr);
            this.sticker_view.addSticker(dd);
        } else if (requestCode == RESULT_FROM_CAMERA) {
            ScorpionUtils.selectedImageUri = Uri.fromFile(photoFile);
            startActivityForResult(new Intent(this, ScorpionMyCrop.class), MediaFile.FILE_TYPE_MP2PS);
        } else if (requestCode == RESULT_FROM_GALLERY) {
            this.selectedImageUri = data.getData();
            ScorpionUtils.selectedImageUri = this.selectedImageUri;
            startActivityForResult(new Intent(this, ScorpionMyCrop.class), MediaFile.FILE_TYPE_MP2PS);
        } else if (requestCode == MediaFile.FILE_TYPE_MP2PS) {
            this.sticker_view.setImageBitmap(getResizedBitmapp(ScorpionUtils.bits, 700));
        } else if (requestCode == MediaFile.FILE_TYPE_DTS) {
            try {
                this.drawable = new BitmapDrawable(BitmapFactory.decodeStream(getAssets().open(ScorpionUtils.SelectedTattooName)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.sticker_view.addSticker(this.drawable);
        }
    }

    public Bitmap getResizedBitmapp(Bitmap image, int maxSize) {
        int width;
        int height;
        float bitmapRatio = ((float) image.getWidth()) / ((float) image.getHeight());
        if (bitmapRatio > 1.0f) {
            width = maxSize;
            height = (int) (((float) width) / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (((float) height) * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Bitmap getFrameBitmap() {
        this.frm.setDrawingCacheEnabled(true);
        this.frm.refreshDrawableState();
        this.frm.buildDrawingCache();
        bm = Bitmap.createBitmap(this.frm.getWidth(), this.frm.getHeight(), Config.RGB_565);
        bm = Bitmap.createBitmap(this.frm.getDrawingCache());
        this.frm.setDrawingCacheEnabled(false);
        return bm;
    }

    private Boolean saveShareImage() {
        this.mFileShareTemp = new File(Environment.getExternalStorageDirectory(), ScorpionUtils.TEMP_FILE_NAME);
        if (this.mFileShareTemp.exists()) {
            this.mFileShareTemp.delete();
        }
        try {
            Bitmap selectedImage = getFrameBitmap();
            this.mImageUri = Uri.parse("file://" + this.mFileShareTemp.getPath());
            FileOutputStream fos = new FileOutputStream(this.mFileShareTemp);
            selectedImage.compress(CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();
            return Boolean.valueOf(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    private File createFolders() {
//        File baseDir;
//        if (VERSION.SDK_INT < 8) {
//            baseDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
////            baseDir = Environment.getExternalStorageDirectory();
//        } else {
//            baseDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//        }
//        if (baseDir == null) {
//            return getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//        }
        File aviaryFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), this.applicationName);
        return (aviaryFolder.exists() || aviaryFolder.mkdirs()) ? aviaryFolder : Environment.getExternalStorageDirectory();
    }

    private void saveImage() {
        Exception e;
        File file = null;
        if (this.mGalleryFolder != null && this.mGalleryFolder.exists()) {
            file = new File(this.mGalleryFolder, "cameff_" + System.currentTimeMillis() + ".png");
            this.path = file.getPath();
        }
        try {

            this.mImageUri = Uri.parse("file://" + file.getPath());
            FileOutputStream fos = new FileOutputStream(file);
            try {
                getFrameBitmap().compress(CompressFormat.PNG, 50, fos);
                fos.flush();
                fos.close();
                try {
                    MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, new String[]{"image/png"}, null);
                } catch (Exception e2) {

                    Toast.makeText(this, "E2 - " + e2.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e3) {
                Toast.makeText(this, "e3 - " + e3.getMessage(), Toast.LENGTH_SHORT).show();
                e = e3;
                FileOutputStream fileOutputStream = fos;
                e.printStackTrace();
            }
        } catch (Exception e4) {
            Toast.makeText(this, "e4 - " + e4.getMessage(), Toast.LENGTH_SHORT).show();
            e = e4;
            e.printStackTrace();
        }
    }

    public void onBackPressed() {

        startActivity(new Intent(ScorpionTextActivity.this, ScorpionMainActivity.class));

    }

}
