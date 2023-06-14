package com.example.mobile_app.model.item_media;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<Drawable> mDrawables;

    public MediaAdapter(List<Drawable> drawables) {
        mDrawables = drawables;
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemMediaImage_imageView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_media_image,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        holder.getImageView().setImageDrawable(mDrawables.get(position));
        holder.imageView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.item_media_image);
            ImageView dialogImageView = dialog.findViewById(R.id.itemMediaImage_imageView);
            dialogImageView.setImageDrawable(mDrawables.get(position));
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mDrawables.size();
    }
}
