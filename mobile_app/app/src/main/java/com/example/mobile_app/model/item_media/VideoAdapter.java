package com.example.mobile_app.model.item_media;

import android.app.Dialog;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Uri> mUris;

    //Données de l'adapter
    public VideoAdapter(List<Uri> uris) {
        mUris = uris;
    }

    // Référence le type de view que l'on va utiliser
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final VideoView videoView;
        private final ImageView playButton;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View
            videoView = itemView.findViewById(R.id.itemMediaVideo_videoView);
            playButton = itemView.findViewById(R.id.itemMediaVideo_imageView);
        }

        public VideoView getVideoView() {
            return videoView;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_media_video,
                parent,
                false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.getVideoView().setVideoURI(mUris.get(position));

        holder.getVideoView().setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
            if (!holder.getVideoView().isPlaying()) {
                holder.playButton.setVisibility(View.INVISIBLE);
                holder.getVideoView().start();
            } else {
                holder.playButton.setVisibility(View.VISIBLE);
                holder.getVideoView().pause();
            }
            return true;
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUris.size();
    }
}
