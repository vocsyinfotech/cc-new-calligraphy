package com.Example.textart.calligrapy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.loader.MediaFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScorpionTextActivity extends Activity implements OnClickListener {
    public static final int RESULT_FROM_CAMERA = 99;
    public static final int RESULT_FROM_GALLERY = 98;
    String applicationName;
    String[] bg_name;
    ImageButton btn_back;
    ImageButton btn_bg;
    ImageButton btn_bg_clr;
    ImageButton btn_gallary;
    ImageButton btn_gradiant;
    ImageButton btn_more;
    ImageButton btn_save;
    ImageButton btn_symbole;
    ImageButton btn_symbole_clr;
    ImageButton btn_text;
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


    private class MySaveImage extends AsyncTask<Void, Void, Void> {
        private MySaveImage() {
        }

        protected void onPreExecute() {
            ScorpionTextActivity.this.pdsave = new ProgressDialog(ScorpionTextActivity.this, "Saving...", ScorpionTextActivity.this.getResources().getColor(R.color.Dialog_title_bg_color));
            ScorpionTextActivity.this.pdsave.setCancelable(false);
            ScorpionTextActivity.this.pdsave.show();
            try {
                ScorpionTextActivity.this.sticker_view.createBitmap();
            } catch (Exception e) {
            }
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            ScorpionTextActivity.this.saveImage();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ScorpionTextActivity.this.pdsave.dismiss();
            Toast.makeText(ScorpionTextActivity.this.getApplicationContext(), "Image Saved in " + ScorpionTextActivity.this.applicationName, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScorpionTextActivity.this, ScorpionMainActivity.class));

            Google_Itrestial_Ads(ScorpionTextActivity.this, getString(R.string.ads_inter));


        }
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


        if (ScorpionNetwork.isDataConnectionAvailable(getBaseContext())) {

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.banner);
            Banner(relativeLayout, ScorpionTextActivity.this);


        }


        findById();
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
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
        this.btn_text = (ImageButton) findViewById(R.id.btn_text);
        this.btn_bg = (ImageButton) findViewById(R.id.btn_bg);
        this.btn_bg_clr = (ImageButton) findViewById(R.id.btn_bg_clr);
        this.btn_save = (ImageButton) findViewById(R.id.btn_save);
        this.btn_back = (ImageButton) findViewById(R.id.btn_back);
        this.btn_more = (ImageButton) findViewById(R.id.btn_more);
        this.btn_gradiant = (ImageButton) findViewById(R.id.btn_gradiant);
        this.btn_symbole = (ImageButton) findViewById(R.id.btn_symbole);
        this.btn_gallary = (ImageButton) findViewById(R.id.btn_gallary);
        this.btn_symbole_clr = (ImageButton) findViewById(R.id.btn_symbole_clr);
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
                this.hlvSimpleList1.setVisibility(View.INVISIBLE);
                startActivity(new Intent(ScorpionTextActivity.this, ScorpionMainActivity.class));
                return;
            case R.id.btn_more /*2131427458*/:
                this.hlvSimpleList1.setVisibility(View.INVISIBLE);
                openPopupMenu(getApplicationContext(), this.btn_more);
                return;
            case R.id.btn_gallary /*2131427459*/:
                this.hlvSimpleList1.setVisibility(View.INVISIBLE);
                selectImage();
                return;
            case R.id.btn_text /*2131427460*/:
                this.hlvSimpleList1.setVisibility(View.INVISIBLE);
                startActivityForResult(new Intent(this, ScorpionEditTextActivity.class), 100);
                return;
            case R.id.btn_bg /*2131427461*/:

                try {
                    this.bg_name = getImage("bg");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.hlvSimpleList1.setVisibility(View.VISIBLE);
                this.hlvSimpleList1.setAdapter(new ScorpionStickerAdapter(this, new ArrayList(Arrays.asList(this.bg_name)), "bg"));
                this.hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
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

                this.hlvSimpleList1.setVisibility(View.VISIBLE);
                try {
                    this.bg_name = getImage("gradient");
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                this.hlvSimpleList1.setAdapter(new ScorpionStickerAdapter(this, new ArrayList(Arrays.asList(this.bg_name)), "gradient"));
                this.hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
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
                this.hlvSimpleList1.setVisibility(View.VISIBLE);

                this.hlvSimpleList1.setAdapter(new ScorpionCustomeColorAdapter(this, new ArrayList(Arrays.asList(ScorpionUtils.color))));
                this.hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {

                        ScorpionTextActivity.this.sticker_view.setImageBitmap(null);
                        ScorpionTextActivity.this.sticker_view.setBackgroundColor(Color.parseColor(ScorpionUtils.color[arg2]));
                    }
                });
                return;
            case R.id.btn_symbole /*2131427464*/:
                this.hlvSimpleList1.setVisibility(View.GONE);
                startActivityForResult(new Intent(this, ScorpionStickerGridActivity.class), MediaFile.FILE_TYPE_DTS);
                return;
            case R.id.btn_symbole_clr /*2131427465*/:
                this.hlvSimpleList1.setVisibility(View.VISIBLE);
                this.hlvSimpleList1.setAdapter(new ScorpionCustomeColorAdapter(this, new ArrayList(Arrays.asList(ScorpionUtils.color))));
                this.hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
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
                this.hlvSimpleList1.setVisibility(View.INVISIBLE);
                if (this.isUpHoneycomb) {
                    new MySaveImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    return;
                } else {
                    new MySaveImage().execute(new Void[0]);
                    return;
                }
            default:
                return;
        }
    }

    private void selectImage() {
        final Dialog d = new Dialog(this, 16973841);
        View v = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.select_img_dialog_bg, null);
        Animation left_anim = AnimationUtils.loadAnimation(this, R.anim.left_anim);
        Animation right_anim = AnimationUtils.loadAnimation(this, R.anim.right_anim);
        ImageButton Gal = (ImageButton) v.findViewById(R.id.PopGallery);
        ImageButton Cam = (ImageButton) v.findViewById(R.id.PopCamera);
        Gal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ScorpionTextActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), ScorpionTextActivity.RESULT_FROM_GALLERY);
                d.dismiss();
            }
        });
        Cam.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent it_cam = new Intent("android.media.action.IMAGE_CAPTURE");
                it_cam.putExtra("output", Uri.fromFile(ScorpionUtils.mFileTemp));
                ScorpionTextActivity.this.startActivityForResult(it_cam, ScorpionTextActivity.RESULT_FROM_CAMERA);
                d.dismiss();
            }
        });
        Gal.setAnimation(right_anim);
        Cam.setAnimation(left_anim);
        d.setContentView(v);
        d.show();
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

    public void openPopupMenu(Context context, ImageButton btn) {
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
                try {
                    ScorpionTextActivity.this.sticker_view.createBitmap();
                } catch (Exception e) {
                }
                try {
                    ScorpionTextActivity.this.saveShareImage();

                    Uri screenshotUri1 = FileProvider.getUriForFile(ScorpionTextActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(ScorpionTextActivity.this.mImageUri.getPath()));

                    //Uri screenshotUri1 = Uri.fromFile(new File(ScorpionTextActivity.this.mImageUri.getPath()));
                    Intent intent = new Intent("android.intent.action.ATTACH_DATA");
                    intent.setDataAndType(screenshotUri1, "image/*");
                    intent.putExtra("png", "image/*");
                    ScorpionTextActivity.this.startActivity(Intent.createChooser(intent, "Set as..."));
                } catch (Exception e2) {
                }
                ScorpionTextActivity.this.pwindow.dismiss();
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
            ScorpionUtils.selectedImageUri = null;
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

    public void onBackPressed() {

        startActivity(new Intent(ScorpionTextActivity.this, ScorpionMainActivity.class));
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
        File baseDir;
        if (VERSION.SDK_INT < 8) {
            baseDir = Environment.getExternalStorageDirectory();
        } else {
            baseDir = Environment.getExternalStorageDirectory();
        }
        if (baseDir == null) {
            return Environment.getExternalStorageDirectory();
        }
        File aviaryFolder = new File(baseDir, this.applicationName);
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
                }
            } catch (Exception e3) {
                e = e3;
                FileOutputStream fileOutputStream = fos;
                e.printStackTrace();
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
        }
    }


    public void Banner(final RelativeLayout Ad_Layout, final Context context) {

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
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





    public void Google_Itrestial_Ads(final Context context, final String Ad_Id) {


        try {

            AdRequest adRequest = new AdRequest.Builder().build();
            final InterstitialAd interstitialAds = new InterstitialAd(context);
            interstitialAds.setAdUnitId(Ad_Id);

            interstitialAds.loadAd(adRequest);

            interstitialAds.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {

                    interstitialAds.show();

                }

                @Override
                public void onAdClosed() {


                }

                @Override
                public void onAdFailedToLoad(int errorCode) {

                }
            });
        } catch (Exception e) {

        }
    }


}
