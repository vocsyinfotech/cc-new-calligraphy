package com.Example.textart.calligrapy;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ScorpionMyImageAdpter extends BaseAdapter {
    final int THUMBSIZE = 128;
    String applicationname;
    Context context;
    ArrayList<String> f = new ArrayList();
    LayoutInflater inflater;
    File[] listFile;

    class ViewHolder {
        ImageView imageview;
        TextView t;

        ViewHolder() {
        }
    }

    public ScorpionMyImageAdpter(Context context, ArrayList<String> f) {
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.context = context;
        this.f = f;
    }

    public int getCount() {
        return this.f.size();
    }

    public String getItem(int position) {
        return (String) this.f.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View tmpView = convertView;
        if (tmpView == null) {
            ViewHolder holder = new ViewHolder();
            tmpView = this.inflater.inflate(R.layout.custom_gallery_grid, null);
            holder.t = (TextView) tmpView.findViewById(R.id.title);
            holder.imageview = (ImageView) tmpView.findViewById(R.id.grid_item);
            holder.imageview.setScaleType(ScaleType.FIT_XY);
            tmpView.setTag(holder);
        }
        ViewHolder v1 = (ViewHolder) tmpView.getTag();
        try {
            v1.imageview.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile((String) this.f.get(position)), 128, 128));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpView;
    }
}
