package com.Example.textart.calligrapy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ScorpionCustomeColorAdapter extends BaseAdapter
{
    Context context;
    ArrayList<String> f;
    LayoutInflater inflater;

    class ViewHolder {
        ImageView item_name;

        ViewHolder() {
        }
    }

    public ScorpionCustomeColorAdapter(Context context, ArrayList<String> f) {
        this.context = context;
        this.f = f;
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
            tmpView = this.inflater.inflate(R.layout.items, null);
            holder.item_name = (ImageView) tmpView.findViewById(R.id.item_name);
            tmpView.setTag(holder);
        }
        ((ViewHolder) tmpView.getTag()).item_name.setBackgroundColor(Color.parseColor((String) this.f.get(position)));
        return tmpView;
    }
}
