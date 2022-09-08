package com.Example.calligrapy;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
public class ScorpionCustomeFontAdapter extends BaseAdapter
{
    Typeface TEXT_BITMAP_FONT;
    Context context;
    ArrayList<String> f;
    LayoutInflater inflater;

    class ViewHolder
    {
        TextView text_font;

        ViewHolder()
        {

        }
    }

    public ScorpionCustomeFontAdapter(Context context, ArrayList<String> f)
    {
        this.context = context;
        this.f = f;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.f.size();
    }

    public String getItem(int position)
    {
        return (String) this.f.get(position);
    }

    public long getItemId(int position)
    {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View tmpView = convertView;
        if (tmpView == null) {
            ViewHolder holder = new ViewHolder();
            tmpView = this.inflater.inflate(R.layout.font_item, null);
            holder.text_font = (TextView) tmpView.findViewById(R.id.text_font);
            tmpView.setTag(holder);
        }
        this.TEXT_BITMAP_FONT = Typeface.createFromAsset(this.context.getAssets(), "fontfile/" + ((String) this.f.get(position)));
        ViewHolder v1 = (ViewHolder) tmpView.getTag();
        v1.text_font.setText("TEXT");
        v1.text_font.setTypeface(this.TEXT_BITMAP_FONT);
        return tmpView;
    }
}
