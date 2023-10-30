package com.Example.calligrapy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import vocsy.ads.GoogleAds;

public class MyCreation extends Activity {
    private ImageButton btn_back;
    private ArrayList<String> fileArraylist = new ArrayList();

    private AdapterMyCreation adapter;
    private RecyclerView recyclerView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemConfiguration.setStatusBarColor(this, R.color.appTheme, true);
        setContentView(R.layout.activity_my_creation);

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.nativeLay));

        recyclerView = findViewById(R.id.recyclerView);
        this.btn_back = (ImageButton) findViewById(R.id.btn_back_image);
        this.btn_back.setOnClickListener(arg0 -> onBackPressed());

//        imagegrid.setAdapter(new ScorpionMyImageAdpter(this, getFromSdcard()));
//        imagegrid.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
//
//                Intent i = new Intent(MyCreation.this.getApplicationContext(), ScorpionImage_Activity.class);
//                i.putExtra("imageID", new ScorpionMyImageAdpter(MyCreation.this.getApplicationContext(), MyCreation.this.getFromSdcard()).getItem(position));
//                MyCreation.this.startActivity(i);
//                MyCreation.this.finish();
//            }
//        });

        setupRecycler();

        adapter.updateList(getFromSdcard());
    }


    public ArrayList<String> getFromSdcard() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (File absolutePath : listFile) {
                this.fileArraylist.add(absolutePath.getAbsolutePath());
            }
        }
        return this.fileArraylist;
    }

    public void onBackPressed() {
        finish();
    }

    private void setupRecycler() {
        recyclerView.setLayoutManager(new GridLayoutManager(MyCreation.this, 3, RecyclerView.VERTICAL, false));
        adapter = new AdapterMyCreation(MyCreation.this, (position, path) -> {
            Intent i = new Intent(getApplicationContext(), ScorpionImage_Activity.class);
            i.putExtra("imageID", path);
            startActivity(i);
        });
        recyclerView.setAdapter(adapter);
    }

}
