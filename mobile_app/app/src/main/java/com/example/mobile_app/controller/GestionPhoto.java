package com.example.mobile_app.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class GestionPhoto {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;
    public static final int REQUEST_CODE_SELECT_PHOTO = 4;


    public static void selectPhoto(AppCompatActivity activity) {
        if (checkPermission(activity)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICK);
        } else {
            requestPermission(activity);
        }
    }

    private static boolean checkPermission(AppCompatActivity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public static void takePhoto(AppCompatActivity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, @Nullable Intent data, LinearLayout imageContainer, Uri[] imageUris, int currentImageIndex) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);
                        ImageView imageView = new ImageView(activity);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(0, 0, 0, 16);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(bitmap);
                        imageContainer.addView(imageView);
                        if (imageUris == null) {
                            imageUris = new Uri[10]; // 10 URIS max ?
                        }

                        currentImageIndex++;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null && extras.containsKey(MediaStore.EXTRA_OUTPUT)) {
                    Uri capturedImageUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
                    if (capturedImageUri != null) {
                        ImageView imageView = new ImageView(activity);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(0, 0, 0, 16);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageURI(capturedImageUri);
                        imageContainer.addView(imageView);
                        // Save the captured image to a file or database
                        // You can use imageUris[currentImageIndex] to get the URI of the image if needed
                        currentImageIndex++;
                    }
                } else {
                    Toast.makeText(activity, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static void handlePermissionResult(AppCompatActivity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhoto(activity);
            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
