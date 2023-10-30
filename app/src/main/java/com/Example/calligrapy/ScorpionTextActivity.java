package com.Example.calligrapy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.koushikdutta.ion.loader.MediaFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import vocsy.ads.AppUtil;
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
    private int requestCode;
    private File photoFile;
    ScorpionCustomeFontAdapter adpt;
    String[] fontname;
    Typeface typeface;
    int cnt_pos = 0;

    String[] tattooName;

    private EmojiAdapter mEmojiAdapter;

    private String folder;

    private File createImageFile() throws IOException {
        long timeStamp = Calendar.getInstance().getTimeInMillis();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */);
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
                break;
            case 102:
                ScorpionTextActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), ScorpionTextActivity.RESULT_FROM_GALLERY);
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
            startActivity(new Intent(ScorpionTextActivity.this, MyCreation.class));
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
        SystemConfiguration.setStatusBarColor(this, R.color.appTheme, true);
        setContentView(R.layout.activity_text);
        findById();

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
        if (ScorpionUtils.bits != null) {
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

        initClicks();
    }

    private void initClicks() {
        findViewById(R.id.layout1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btn1).setBackgroundResource(R.drawable.ic_selected_bg);
                findViewById(R.id.btn2).setBackgroundResource(0);
                findViewById(R.id.btn3).setBackgroundResource(0);
                findViewById(R.id.btn4).setBackgroundResource(0);
                findViewById(R.id.btn5).setBackgroundResource(0);
                findViewById(R.id.btn6).setBackgroundResource(0);

                hlvSimpleList1.setVisibility(View.INVISIBLE);
//                startActivityForResult(new Intent(ScorpionTextActivity.this, ScorpionEditTextActivity.class), 100);
                showPopupForTextStyle();
            }
        });
        findViewById(R.id.layout2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btn1).setBackgroundResource(0);
                findViewById(R.id.btn2).setBackgroundResource(R.drawable.ic_selected_bg);
                findViewById(R.id.btn3).setBackgroundResource(0);
                findViewById(R.id.btn4).setBackgroundResource(0);
                findViewById(R.id.btn5).setBackgroundResource(0);
                findViewById(R.id.btn6).setBackgroundResource(0);

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
            }
        });
        findViewById(R.id.layout3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btn1).setBackgroundResource(0);
                findViewById(R.id.btn2).setBackgroundResource(0);
                findViewById(R.id.btn3).setBackgroundResource(R.drawable.ic_selected_bg);
                findViewById(R.id.btn4).setBackgroundResource(0);
                findViewById(R.id.btn5).setBackgroundResource(0);
                findViewById(R.id.btn6).setBackgroundResource(0);

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
            }
        });
        findViewById(R.id.layout4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btn1).setBackgroundResource(0);
                findViewById(R.id.btn2).setBackgroundResource(0);
                findViewById(R.id.btn3).setBackgroundResource(0);
                findViewById(R.id.btn4).setBackgroundResource(R.drawable.ic_selected_bg);
                findViewById(R.id.btn5).setBackgroundResource(0);
                findViewById(R.id.btn6).setBackgroundResource(0);

                hlvSimpleList1.setVisibility(View.VISIBLE);

                hlvSimpleList1.setAdapter(new ScorpionCustomeColorAdapter(ScorpionTextActivity.this, new ArrayList(Arrays.asList(ScorpionUtils.color))));
                hlvSimpleList1.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {

                        ScorpionTextActivity.this.sticker_view.setImageBitmap(null);
                        ScorpionTextActivity.this.sticker_view.setBackgroundColor(Color.parseColor(ScorpionUtils.color[arg2]));
                    }
                });
            }
        });
        findViewById(R.id.layout5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btn1).setBackgroundResource(0);
                findViewById(R.id.btn2).setBackgroundResource(0);
                findViewById(R.id.btn3).setBackgroundResource(0);
                findViewById(R.id.btn4).setBackgroundResource(0);
                findViewById(R.id.btn5).setBackgroundResource(R.drawable.ic_selected_bg);
                findViewById(R.id.btn6).setBackgroundResource(0);

                hlvSimpleList1.setVisibility(View.GONE);
                showEmojiDialog();
//                startActivityForResult(new Intent(ScorpionTextActivity.this, ScorpionStickerGridActivity.class), MediaFile.FILE_TYPE_DTS);

            }
        });
        findViewById(R.id.layout6).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.btn1).setBackgroundResource(0);
                findViewById(R.id.btn2).setBackgroundResource(0);
                findViewById(R.id.btn3).setBackgroundResource(0);
                findViewById(R.id.btn4).setBackgroundResource(0);
                findViewById(R.id.btn5).setBackgroundResource(0);
                findViewById(R.id.btn6).setBackgroundResource(R.drawable.ic_selected_bg);

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
            }
        });

        findViewById(R.id.btn_text).setOnClickListener(view -> findViewById(R.id.layout1).performClick());
        findViewById(R.id.btn_bg).setOnClickListener(view -> findViewById(R.id.layout2).performClick());
        findViewById(R.id.btn_gradiant).setOnClickListener(view -> findViewById(R.id.layout3).performClick());
        findViewById(R.id.btn_bg_clr).setOnClickListener(view -> findViewById(R.id.layout4).performClick());
        findViewById(R.id.btn_symbole).setOnClickListener(view -> findViewById(R.id.layout5).performClick());
        findViewById(R.id.btn_symbole_clr).setOnClickListener(view -> findViewById(R.id.layout6).performClick());
    }

    private void showPopupForTextStyle() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.text_style_bottom_sheet);

        bottomSheetDialog.findViewById(R.id.btnClose).setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.findViewById(R.id.btnDone).setOnClickListener(view -> {
            if (((EditText) bottomSheetDialog.findViewById(R.id.edtText)).getText().toString().length() > 0) {
                ScorpionUtils.text = ((EditText) bottomSheetDialog.findViewById(R.id.edtText)).getText().toString();
                ScorpionDialogDrawable dd = new ScorpionDialogDrawable();
                dd.setFontStyle(getApplicationContext(), ScorpionUtils.typeface);
                dd.setTextData(ScorpionUtils.text);
                dd.setTextColor(ScorpionUtils.clr);
                this.sticker_view.addSticker(dd);
                bottomSheetDialog.dismiss();
                return;
            }
            Toast.makeText(getApplicationContext(), "Please Enter Text!", Toast.LENGTH_LONG).show();
        });

        bottomSheetDialog.findViewById(R.id.btnColorPicker).setOnClickListener(view -> openColorDialog(bottomSheetDialog));

        this.fontname = getImagefont("fontfile");

        NonScrollListView fontgrid = bottomSheetDialog.findViewById(R.id.fontgrid);
        this.adpt = new ScorpionCustomeFontAdapter(this, new ArrayList<>(Arrays.asList(this.fontname)));
        fontgrid.setAdapter(this.adpt);

        fontgrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                if (!ScorpionUtils.arr.contains(arg2 + me.grantland.widget.BuildConfig.FLAVOR)) {
                    ScorpionUtils.typeface = "fontfile/" + ScorpionTextActivity.this.fontname[arg2];
                    ScorpionTextActivity.this.typeface = Typeface.createFromAsset(ScorpionTextActivity.this.getAssets(), "fontfile/" + ScorpionTextActivity.this.fontname[arg2]);
                    ((EditText) bottomSheetDialog.findViewById(R.id.edtText)).setTypeface(ScorpionTextActivity.this.typeface);
                } else if (Pattern.compile("([0-9])").matcher(((EditText) findViewById(R.id.edtText)).getText().toString()).find()) {
                    Toast.makeText(ScorpionTextActivity.this.getApplicationContext(), "Please Select another font style!!!", Toast.LENGTH_LONG).show();
                } else {
                    ScorpionUtils.typeface = "fontfile/" + ScorpionTextActivity.this.fontname[arg2];
                    ScorpionTextActivity.this.typeface = Typeface.createFromAsset(ScorpionTextActivity.this.getAssets(), "fontfile/" + ScorpionTextActivity.this.fontname[arg2]);
                    ((EditText) bottomSheetDialog.findViewById(R.id.edtText)).setTypeface(ScorpionTextActivity.this.typeface);
                }
                ScorpionTextActivity.this.cnt_pos = arg2;
            }
        });

        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private void openColorDialog(BottomSheetDialog bottomSheetDialog) {
        ColorPickerDialogBuilder.with(this).setTitle("Choose color").initialColor(-1).wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener(new OnColorSelectedListener() {
            public void onColorSelected(int selectedColor) {
            }
        }).setPositiveButton((CharSequence) "ok", new ColorPickerClickListener() {
            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                ((EditText) bottomSheetDialog.findViewById(R.id.edtText)).setTextColor(selectedColor);
                ScorpionUtils.clr = selectedColor;
                if (allColors != null) {
                    StringBuilder sb = null;
                    for (Integer color : allColors) {
                        if (color != null) {
                            if (sb == null) {
                                sb = new StringBuilder("Color List:");
                            }
                            sb.append("\r\n#" + Integer.toHexString(color.intValue()).toUpperCase());
                        }
                    }
                }
            }
        }).setNegativeButton((CharSequence) "cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        }).showColorEdit(true).setColorEditTextColor(getResources().getColor(17170459)).build().show();
    }

    private void showEmojiDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.emoji_bottom_sheet);

        LinearLayout btnLayout1 = bottomSheetDialog.findViewById(R.id.btnLayout1);
        LinearLayout btnLayout2 = bottomSheetDialog.findViewById(R.id.btnLayout2);
        LinearLayout btnLayout3 = bottomSheetDialog.findViewById(R.id.btnLayout3);
        LinearLayout btnLayout4 = bottomSheetDialog.findViewById(R.id.btnLayout4);
        LinearLayout btnLayout5 = bottomSheetDialog.findViewById(R.id.btnLayout5);
        LinearLayout btnLayout6 = bottomSheetDialog.findViewById(R.id.btnLayout6);
        LinearLayout btnLayout7 = bottomSheetDialog.findViewById(R.id.btnLayout7);
        ImageView imgSticker1 = bottomSheetDialog.findViewById(R.id.imgSticker1);
        ImageView imgSticker2 = bottomSheetDialog.findViewById(R.id.imgSticker2);
        ImageView imgSticker3 = bottomSheetDialog.findViewById(R.id.imgSticker3);
        ImageView imgSticker4 = bottomSheetDialog.findViewById(R.id.imgSticker4);
        ImageView imgSticker5 = bottomSheetDialog.findViewById(R.id.imgSticker5);
        ImageView imgSticker6 = bottomSheetDialog.findViewById(R.id.imgSticker6);
        ImageView imgSticker7 = bottomSheetDialog.findViewById(R.id.imgSticker7);
        ImageView imgSelected1 = bottomSheetDialog.findViewById(R.id.imgSelected1);
        ImageView imgSelected2 = bottomSheetDialog.findViewById(R.id.imgSelected2);
        ImageView imgSelected3 = bottomSheetDialog.findViewById(R.id.imgSelected3);
        ImageView imgSelected4 = bottomSheetDialog.findViewById(R.id.imgSelected4);
        ImageView imgSelected5 = bottomSheetDialog.findViewById(R.id.imgSelected5);
        ImageView imgSelected6 = bottomSheetDialog.findViewById(R.id.imgSelected6);
        ImageView imgSelected7 = bottomSheetDialog.findViewById(R.id.imgSelected7);

        imgSticker1.setColorFilter(getResources().getColor(R.color.appTheme));
        imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
        imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
        imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
        imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
        imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
        imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
        imgSelected1.setImageResource(R.drawable.ic_emoji_selected);
        imgSelected2.setImageResource(android.R.color.transparent);
        imgSelected3.setImageResource(android.R.color.transparent);
        imgSelected4.setImageResource(android.R.color.transparent);
        imgSelected5.setImageResource(android.R.color.transparent);
        imgSelected6.setImageResource(android.R.color.transparent);
        imgSelected7.setImageResource(android.R.color.transparent);
        folder = "symbol/cool";
        setStickers(folder, bottomSheetDialog);

        btnLayout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
                imgSelected1.setImageResource(R.drawable.ic_emoji_selected);
                imgSelected2.setImageResource(android.R.color.transparent);
                imgSelected3.setImageResource(android.R.color.transparent);
                imgSelected4.setImageResource(android.R.color.transparent);
                imgSelected5.setImageResource(android.R.color.transparent);
                imgSelected6.setImageResource(android.R.color.transparent);
                imgSelected7.setImageResource(android.R.color.transparent);

                folder = "symbol/cool";
                setStickers(folder, bottomSheetDialog);
            }
        });

        btnLayout2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker2.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
                imgSelected1.setImageResource(android.R.color.transparent);
                imgSelected2.setImageResource(R.drawable.ic_emoji_selected);
                imgSelected3.setImageResource(android.R.color.transparent);
                imgSelected4.setImageResource(android.R.color.transparent);
                imgSelected5.setImageResource(android.R.color.transparent);
                imgSelected6.setImageResource(android.R.color.transparent);
                imgSelected7.setImageResource(android.R.color.transparent);

                folder = "symbol/couple";
                setStickers(folder, bottomSheetDialog);
            }
        });
        btnLayout3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker3.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
                imgSelected1.setImageResource(android.R.color.transparent);
                imgSelected2.setImageResource(android.R.color.transparent);
                imgSelected3.setImageResource(R.drawable.ic_emoji_selected);
                imgSelected4.setImageResource(android.R.color.transparent);
                imgSelected5.setImageResource(android.R.color.transparent);
                imgSelected6.setImageResource(android.R.color.transparent);
                imgSelected7.setImageResource(android.R.color.transparent);

                folder = "symbol/dot";
                setStickers(folder, bottomSheetDialog);
            }
        });

        btnLayout4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker4.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
                imgSelected1.setImageResource(android.R.color.transparent);
                imgSelected2.setImageResource(android.R.color.transparent);
                imgSelected3.setImageResource(android.R.color.transparent);
                imgSelected4.setImageResource(R.drawable.ic_emoji_selected);
                imgSelected5.setImageResource(android.R.color.transparent);
                imgSelected6.setImageResource(android.R.color.transparent);
                imgSelected7.setImageResource(android.R.color.transparent);

                folder = "symbol/extra";
                setStickers(folder, bottomSheetDialog);
            }
        });

        btnLayout5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker5.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
                imgSelected1.setImageResource(android.R.color.transparent);
                imgSelected2.setImageResource(android.R.color.transparent);
                imgSelected3.setImageResource(android.R.color.transparent);
                imgSelected4.setImageResource(android.R.color.transparent);
                imgSelected5.setImageResource(R.drawable.ic_emoji_selected);
                imgSelected6.setImageResource(android.R.color.transparent);
                imgSelected7.setImageResource(android.R.color.transparent);

                folder = "symbol/feathers";
                setStickers(folder, bottomSheetDialog);
            }
        });

        btnLayout6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker6.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSticker7.setColorFilter(getResources().getColor(R.color.unselected));
                imgSelected1.setImageResource(android.R.color.transparent);
                imgSelected2.setImageResource(android.R.color.transparent);
                imgSelected3.setImageResource(android.R.color.transparent);
                imgSelected4.setImageResource(android.R.color.transparent);
                imgSelected5.setImageResource(android.R.color.transparent);
                imgSelected6.setImageResource(R.drawable.ic_emoji_selected);
                imgSelected7.setImageResource(android.R.color.transparent);

                folder = "symbol/heart";
                setStickers(folder, bottomSheetDialog);
            }
        });
        btnLayout7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSticker1.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker2.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker3.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker4.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker5.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker6.setColorFilter(getResources().getColor(R.color.unselected));
                imgSticker7.setColorFilter(getResources().getColor(R.color.appTheme));
                imgSelected1.setImageResource(android.R.color.transparent);
                imgSelected2.setImageResource(android.R.color.transparent);
                imgSelected3.setImageResource(android.R.color.transparent);
                imgSelected4.setImageResource(android.R.color.transparent);
                imgSelected5.setImageResource(android.R.color.transparent);
                imgSelected6.setImageResource(android.R.color.transparent);
                imgSelected7.setImageResource(R.drawable.ic_emoji_selected);

                folder = "symbol/strock";
                setStickers(folder, bottomSheetDialog);
            }
        });

        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    private void setStickers(final String folder, BottomSheetDialog bottomSheetDialog) {
        try {
            this.tattooName = getImage(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecyclerView emojiList = bottomSheetDialog.findViewById(R.id.emojiList);
        emojiList.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        emojiList.setItemAnimator(new DefaultItemAnimator());
        emojiList.setHasFixedSize(true);

        mEmojiAdapter = new EmojiAdapter(new ArrayList<>(Arrays.asList(this.tattooName)), folder, this, new EmojiAdapter.EmojiClickListener() {
            @Override
            public void onClick(int position) {
                ScorpionUtils.SelectedTattooName = folder + "/" + ScorpionTextActivity.this.tattooName[position];
                try {
                    drawable = new BitmapDrawable(BitmapFactory.decodeStream(getAssets().open(ScorpionUtils.SelectedTattooName)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sticker_view.addSticker(drawable);
                bottomSheetDialog.dismiss();
            }
        });
        emojiList.setAdapter(mEmojiAdapter);
        mEmojiAdapter.notifyDataSetChanged();
    }

    private void findById() {
        this.btn_back = findViewById(R.id.btn_back);
        this.btn_more = findViewById(R.id.btn_more);

        this.btn_text = findViewById(R.id.btn_text);
        this.btn_bg = findViewById(R.id.btn_bg);
        this.btn_bg_clr = findViewById(R.id.btn_bg_clr);
        this.btn_save = findViewById(R.id.btn_save);
        this.btn_gradiant = findViewById(R.id.btn_gradiant);
        this.btn_symbole = findViewById(R.id.btn_symbole);
        this.btn_gallary = findViewById(R.id.btn_gallary);
        this.btn_symbole_clr = findViewById(R.id.btn_symbole_clr);

        this.sticker_view = findViewById(R.id.sticker_view);
        this.frm = findViewById(R.id.frm);
        this.hlvSimpleList1 = findViewById(R.id.hlvSimpleList1);
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

            case R.id.btn_save /*2131427466*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionTextActivity.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        hlvSimpleList1.setVisibility(View.INVISIBLE);
                        if (isUpHoneycomb) {
                            new MySaveImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new MySaveImage().execute();
                        }
                    }
                });
            default:
        }
    }

    @SuppressLint("ResourceType")
    private void selectImage() {

        new ItemChoiceDialog(ScorpionTextActivity.this, (action -> {
            switch (action) {
                case "gallery":
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 102);
                    break;
                case "camera":
                    checkPermission(Manifest.permission.CAMERA, 101);
                    break;
            }
        })).show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == R.styleable.AutofitTextView_precision) { /*1*/
            if (grantResults.length > 0 && grantResults[0] == 0) {
                return;
            }
            return;
        }
    }

    public void openPopupMenu(Context context, ImageView btn) {
        this.pwindow = new PopupWindow(this);
        this.pwindow.setBackgroundDrawable(new BitmapDrawable());
        View contentView = getLayoutInflater().inflate(R.layout.pop_window, null);
        this.pwindow.setContentView(contentView);
        LinearLayout llsetas = contentView.findViewById(R.id.llsetas);
        LinearLayout llrateas = contentView.findViewById(R.id.llrateus);
        LinearLayout llsave = contentView.findViewById(R.id.llsave);
        LinearLayout llsharelink = contentView.findViewById(R.id.llsharelink);
        contentView.findViewById(R.id.llshareimg).setOnClickListener(new OnClickListener() {
            @SuppressLint({"NewApi"})
            public void onClick(View arg0) {

                if (ScorpionTextActivity.this.isUpHoneycomb) {
                    new shareTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new shareTask().execute();
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
                AppUtil.rateApp(ScorpionTextActivity.this);
//                try {
//                    try {
//                        ScorpionTextActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + ScorpionTextActivity.this.getApplicationContext().getPackageName())));
//                    } catch (ActivityNotFoundException e) {
//                        ScorpionTextActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + ScorpionTextActivity.this.getApplicationContext().getPackageName())));
//                    }
//                } catch (Exception e2) {
//                }


            }
        });
        llsave.setOnClickListener(new OnClickListener() {
            @SuppressLint({"NewApi"})
            public void onClick(View arg0) {

                if (ScorpionTextActivity.this.isUpHoneycomb) {
                    new MySaveImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new MySaveImage().execute();
                }
                ScorpionTextActivity.this.pwindow.dismiss();


            }
        });
        llsharelink.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                AppUtil.shareApp(ScorpionTextActivity.this);
//                try {
//                    Intent i = new Intent("android.intent.action.SEND");
//                    i.setType(StringBody.CONTENT_TYPE);
//                    i.putExtra("android.intent.extra.SUBJECT", ScorpionTextActivity.this.getResources().getString(R.string.app_name));
//                    i.putExtra("android.intent.extra.TEXT", ScorpionTextActivity.this.getResources().getString(R.string.sharemsg) + "https://play.google.com/store/apps/details?id=" + ScorpionTextActivity.this.getApplicationContext().getPackageName() + " \n\n");
//                    ScorpionTextActivity.this.startActivity(Intent.createChooser(i, "Choose one"));
//                } catch (Exception e) {
//                }


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

    private String[] getImagefont(String folderName) {
        try {
            return getAssets().list(folderName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}