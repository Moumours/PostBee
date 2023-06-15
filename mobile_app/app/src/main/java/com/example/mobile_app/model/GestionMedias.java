package com.example.mobile_app.model;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.exifinterface.media.ExifInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobile_app.controller.EditPostActivity;
 */

import java.io.File;
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
                //displayGif(activity, mediaUri, mediaView);
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

    public static void jpegConverter(String inputImagePath, String outputImagePath) {
        try {
            // Charger l'image d'entrée
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputImagePath);

            // Créer une nouvelle image en utilisant le type de format Bitmap ARGB_8888
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), Bitmap.Config.ARGB_8888);

            // Copier l'image d'entrée vers l'image de sortie en utilisant un Canvas
            Canvas canvas = new Canvas(outputBitmap);
            canvas.drawBitmap(inputBitmap, 0, 0, null);

            // Enregistrer l'image de sortie en format JPEG
            File outputFile = new File(outputImagePath);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            System.out.println("Erreur lors de la conversion de l'image : " + ex.getMessage());
        }
    }

    public static String getConvertedImagePath(File imageFile) {
        String outputImagePath = imageFile.getAbsolutePath() + "_converted.jpeg";
        jpegConverter(imageFile.getAbsolutePath(), outputImagePath);
        return outputImagePath;
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

    /*
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
    */

    public static String handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, @Nullable Intent data, LinearLayout mediaContainer, Uri[] mediaUris, int currentMediaIndex, Context c) {
        String path = null;
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_MEDIA_PICK && data != null) {
                Uri selectedMediaUri = data.getData();
                path = getPathFromUri(c, selectedMediaUri);
                Log.d("ALERTE", "Path : " + getPathFromUri(c, selectedMediaUri));
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
        return path;
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

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}

