package com.Example.textart.calligrapy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Example.textart.calligrapy.GoogleAds.GoogleAds;
import com.Example.textart.calligrapy.GoogleAds.RandomAdListener;
import com.Example.textart.calligrapy.GoogleAds.RandomBackAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScorpionStickerGridActivity extends Activity implements OnClickListener {
    ImageButton back;
    ImageButton btn_sticker1;
    ImageButton btn_sticker2;
    ImageButton btn_sticker3;
    ImageButton btn_sticker4;
    ImageButton btn_sticker5;
    ImageButton btn_sticker6;
    ImageButton btn_sticker7;
    String folder;
    HorizontalScrollView hlv_stickers;
    GridView tattooGrid;
    String[] tattooName;
    TextView txt_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_tattoo_grid);
        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.nativeLay));
        FindViews();
        this.folder = "symbol/cool";
        this.txt_title.setText("Symbol");
        setStickers(this.folder);
        this.btn_sticker1.setOnClickListener(this);
        this.btn_sticker2.setOnClickListener(this);
        this.btn_sticker3.setOnClickListener(this);
        this.btn_sticker4.setOnClickListener(this);
        this.btn_sticker5.setOnClickListener(this);
        this.btn_sticker6.setOnClickListener(this);
        this.btn_sticker7.setOnClickListener(this);
        this.back.setOnClickListener(this);
    }

    private void FindViews() {
        this.tattooGrid = (GridView) findViewById(R.id.tattooGrid);
        this.back = (ImageButton) findViewById(R.id.btnBack);
        this.btn_sticker1 = (ImageButton) findViewById(R.id.btn_sticker1);
        this.btn_sticker2 = (ImageButton) findViewById(R.id.btn_sticker2);
        this.btn_sticker3 = (ImageButton) findViewById(R.id.btn_sticker3);
        this.btn_sticker4 = (ImageButton) findViewById(R.id.btn_sticker4);
        this.btn_sticker5 = (ImageButton) findViewById(R.id.btn_sticker5);
        this.btn_sticker6 = (ImageButton) findViewById(R.id.btn_sticker6);
        this.btn_sticker7 = (ImageButton) findViewById(R.id.btn_sticker7);
        this.txt_title = (TextView) findViewById(R.id.txt_title);
        this.hlv_stickers = (HorizontalScrollView) findViewById(R.id.hlv_stickers);
    }

    public void onBackPressed() {
        GoogleAds.getInstance().showBackCounterInterstitialAd(this, new RandomBackAdListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private String[] getImage(String folderName) throws IOException {
        return getAssets().list(folderName);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btnBack /*2131427443*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        onBackPressed();
                    }
                });


                return;
            case R.id.btn_sticker1 /*2131427449*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {

                        folder = "symbol/cool";
                        setStickers(folder);
                    }
                });

                return;
            case R.id.btn_sticker2 /*2131427450*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        folder = "symbol/couple";
                        setStickers(folder);
                    }
                });


                return;
            case R.id.btn_sticker3 /*2131427451*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {

                        folder = "symbol/dot";
                        setStickers(folder);
                    }
                });

                return;
            case R.id.btn_sticker4 /*2131427452*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        folder = "symbol/extra";
                        setStickers(folder);
                    }
                });


                return;
            case R.id.btn_sticker5 /*2131427453*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        folder = "symbol/feathers";
                        setStickers(folder);
                    }
                });


                return;
            case R.id.btn_sticker6 /*2131427454*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {

                        folder = "symbol/heart";
                        setStickers(folder);
                    }
                });

                return;
            case R.id.btn_sticker7 /*2131427455*/:
                GoogleAds.getInstance().showCounterInterstitialAd(ScorpionStickerGridActivity.this, new RandomAdListener() {
                    @Override
                    public void onClick() {
                        folder = "symbol/strock";
                        setStickers(folder);
                    }
                });

                return;
            default:
                return;
        }
    }

    private void setStickers(final String folder) {
        try {
            this.tattooName = getImage(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tattooGrid.setAdapter(new ScorpionStickerAdapter(getApplicationContext(), new ArrayList(Arrays.asList(this.tattooName)), folder));
        this.tattooGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                ScorpionUtils.SelectedTattooName = folder + "/" + ScorpionStickerGridActivity.this.tattooName[arg2];
                ScorpionStickerGridActivity.this.setResult(-1);
                ScorpionStickerGridActivity.this.finish();
            }
        });
    }

}
