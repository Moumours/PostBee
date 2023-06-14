package com.example.mobile_app.model;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

public class GestionMedias {
    public static final int REQUEST_MEDIA_PICK = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    public static void selectMedia(AppCompatActivity activity) {
        if (checkPermission(activity)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("*/*");
            activity.startActivityForResult(Intent.createChooser(intent, "Select Media"), REQUEST_MEDIA_PICK);
        } else {
            requestPermission(activity);
        }
    }

    private static boolean checkPermission(AppCompatActivity activity) {
        int result = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public static void displayMedia(AppCompatActivity activity, Uri mediaUri, ImageView mediaView) {
        String mediaType = activity.getContentResolver().getType(mediaUri);
        if (mediaType != null) {
            if (mediaType.startsWith("image/")&& !mediaType.equals("image/gif")) {
                displayImage(activity, mediaUri, mediaView);
            } else if (mediaType.startsWith("video/")) {
                displayVideo(activity, mediaUri, mediaView);
            } else if (mediaType.equals("image/gif")) {
                displayGif(activity, mediaUri, mediaView);
            }
        } else {
            Toast.makeText(activity, "Unsupported media type", Toast.LENGTH_SHORT).show();
        }
    }

    private static void displayImage(AppCompatActivity activity, Uri imageUri, ImageView imageView) {
        try {
            Drawable drawable = Drawable.createFromStream(activity.getContentResolver().openInputStream(imageUri), imageUri.toString());
            imageView.setImageDrawable(drawable);
            Toast.makeText(activity, "Image chargée", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Échec du chargement de l'image", Toast.LENGTH_SHORT).show();
        }
    }

    private static void displayVideo(AppCompatActivity activity, Uri videoUri, ImageView videoView) {
        videoView.setVisibility(View.GONE);
        VideoView videoPlayer = new VideoView(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 16);
        videoPlayer.setLayoutParams(layoutParams);
        videoPlayer.setVideoURI(videoUri);
        videoPlayer.start();
        ((LinearLayout) videoView.getParent()).addView(videoPlayer);
    }

    private static void displayGif(AppCompatActivity activity, Uri gifUri, ImageView gifView) {
        try {
            Glide.with(activity)
                    .load(gifUri)
                    .apply(new RequestOptions().override(1200, 2000))
                    .into(gifView);
            Toast.makeText(activity, "GIF charge", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Failed to load GIF", Toast.LENGTH_SHORT).show();
        }
    }

    public static void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, @Nullable Intent data, LinearLayout mediaContainer, Uri[] mediaUris, int currentMediaIndex) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_MEDIA_PICK && data != null) {
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri != null) {
                    ImageView imageView = new ImageView(activity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 0, 0, 16);
                    imageView.setLayoutParams(layoutParams);
                    mediaContainer.addView(imageView);

                    displayMedia(activity, selectedMediaUri, imageView);

                    if (mediaUris == null) {
                        mediaUris = new Uri[10]; // 10 URIs max?
                    }
                    mediaUris[currentMediaIndex] = selectedMediaUri;
                    currentMediaIndex++;
                }
            }
        }
    }


    public static void handlePermissionResult(AppCompatActivity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectMedia(activity);
            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

