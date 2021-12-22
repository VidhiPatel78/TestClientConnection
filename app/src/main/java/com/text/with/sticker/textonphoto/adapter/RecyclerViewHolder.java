package com.text.with.sticker.textonphoto.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.text.with.sticker.textonphoto.R;


public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}
