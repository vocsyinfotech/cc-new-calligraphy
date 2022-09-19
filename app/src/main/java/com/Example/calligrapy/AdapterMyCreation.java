package com.Example.calligrapy;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterMyCreation extends RecyclerView.Adapter<AdapterMyCreation.ViewHolder> {

    private Activity activity;
    private CreationListener listener;
    private ArrayList<String> fileList = new ArrayList<>();

    public AdapterMyCreation(Activity activity, CreationListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void updateList(ArrayList<String> file_list) {
        this.fileList = file_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_my_creation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.logoView.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile((String) fileList.get(position)), 256, 256));

        holder.itemView.setOnClickListener((view) -> {
            if (listener != null) {
                listener.onClick(position, fileList.get(position));
            }
        });

        Glide.with(activity).load(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile((String) fileList.get(position)), 256, 256)).into(holder.logoView);

    }

    @Override
    public int getItemCount() {
        return fileList != null ? fileList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LogoView logoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoView = itemView.findViewById(R.id.logoView);
        }
    }
}
