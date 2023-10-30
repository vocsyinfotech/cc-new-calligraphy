package com.Example.calligrapy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.MyEmojiViewHolder> {
    private ArrayList<String> emojisPaths;
    private Activity activity;

    private String folder;
    private EmojiClickListener emojiClickListener;

    public EmojiAdapter(ArrayList<String> emojisPaths, String folder, Activity activity, EmojiClickListener emojiClickListener) {
        this.emojisPaths = emojisPaths;
        this.folder = folder;
        this.activity = activity;
        this.emojiClickListener = emojiClickListener;
    }

    @NonNull
    @Override
    public EmojiAdapter.MyEmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyEmojiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.MyEmojiViewHolder holder, int position) {
        Glide.with(activity).load("file:///android_asset/" + this.folder + "/" + ((String) this.emojisPaths.get(position))).into(holder.imgEmoji);

        holder.itemView.setOnClickListener(view -> emojiClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return emojisPaths.size();
    }

    public class MyEmojiViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgEmoji;

        public MyEmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEmoji = itemView.findViewById(R.id.imgEmoji);
        }
    }

    public interface EmojiClickListener {
        void onClick(int position);
    }
}
