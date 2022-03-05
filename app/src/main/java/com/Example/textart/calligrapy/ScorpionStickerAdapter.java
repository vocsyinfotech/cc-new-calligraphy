package com.Example.textart.calligrapy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders.Any.B;

import java.util.ArrayList;

public class ScorpionStickerAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> f;
    String folder;
    LayoutInflater inflater;

    class ViewHolder {
        ImageView imageview;

        ViewHolder() {
        }
    }

    public ScorpionStickerAdapter(Context context, ArrayList<String> f, String folder) {
        this.context = context;
        this.f = f;
        this.folder = folder;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
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
            tmpView = this.inflater.inflate(R.layout.pixel_item, null);
            holder.imageview = (ImageView) tmpView.findViewById(R.id.img_pixel);
            tmpView.setTag(holder);
        }
        ((B) Ion.with(this.context).load("file:///android_asset/" + this.folder + "/" + ((String) this.f.get(position)))).intoImageView(((ViewHolder) tmpView.getTag()).imageview);
        return tmpView;
    }
}
