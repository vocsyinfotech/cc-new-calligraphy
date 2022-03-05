package com.Example.textart.calligrapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;

public class ScorpionSave_Images_ShowActivity extends Activity {
    public static GridView grid;
    ImageButton btn_back;
    ArrayList<String> f = new ArrayList();
    File[] listFile;
    DisplayImageOptions options;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        initImageLoader(getApplicationContext());
        this.options = new Builder().showImageOnLoading(0).showImageForEmptyUri(-16711936).showImageOnFail(-16777216).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Config.RGB_565).build();
        setContentView(R.layout.activity_save__images__show);



        if (ScorpionNetwork.isDataConnectionAvailable(getBaseContext())) {


            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.banner);

            Banner(relativeLayout, ScorpionSave_Images_ShowActivity.this);
        }



        try {

        } catch (Exception e) {
        }
        this.btn_back = (ImageButton) findViewById(R.id.btn_back_image);
        this.btn_back.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                ScorpionSave_Images_ShowActivity.this.onBackPressed();
            }
        });
        GridView imagegrid = (GridView) findViewById(R.id.gridView1);
        imagegrid.setAdapter(new ScorpionMyImageAdpter(this, getFromSdcard()));
        imagegrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                Intent i = new Intent(ScorpionSave_Images_ShowActivity.this.getApplicationContext(), ScorpionImage_Activity.class);
                i.putExtra("imageID", new ScorpionMyImageAdpter(ScorpionSave_Images_ShowActivity.this.getApplicationContext(), ScorpionSave_Images_ShowActivity.this.getFromSdcard()).getItem(position));
                ScorpionSave_Images_ShowActivity.this.startActivity(i);
                ScorpionSave_Images_ShowActivity.this.finish();
            }
        });
    }

    public static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).threadPriority(3).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build());
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public ArrayList<String> getFromSdcard() {
        File file = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.isDirectory()) {
            this.listFile = file.listFiles();
            for (File absolutePath : this.listFile) {
                this.f.add(absolutePath.getAbsolutePath());
            }
        }
        return this.f;
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




}
