package com.example.mobile_app.model.item_pfp;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;

public class ProfilePictureViewHolder extends RecyclerView.ViewHolder {
    ImageView mImageView;

    public ProfilePictureViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.itempfp_imageView);
        itemView.setOnClickListener(v -> {});
    }
}