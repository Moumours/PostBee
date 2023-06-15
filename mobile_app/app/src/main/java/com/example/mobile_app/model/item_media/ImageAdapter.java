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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Drawable> mDrawables;

    //Données de l'adapter
    public ImageAdapter(List<Drawable> drawables) {
        mDrawables = drawables;
    }

    // Référence le type de view que l'on va utiliser
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View
            imageView = itemView.findViewById(R.id.itemMediaImage_imageView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_media_image,
                parent,
                false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.getImageView().setImageDrawable(mDrawables.get(position));

        holder.imageView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.item_media_image);
            ImageView dialogImageView = dialog.findViewById(R.id.itemMediaImage_imageView);
            dialogImageView.setImageDrawable(mDrawables.get(position));
            dialog.show();
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDrawables.size();
    }
}
