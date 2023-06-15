package com.example.mobile_app.controller;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app.R;
import com.example.mobile_app.model.GestionMedias;
import com.example.mobile_app.model.ResponseData;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UploadPost;
import com.example.mobile_app.model.User;
import com.example.mobile_app.model.UserStatic;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EditPostActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Button mButtonSubmit;

    private Button mButtonMedia;

    private byte mCurrentImageIndex = 0;
    private LinearLayout mMediaContainer;
    private Uri[] mMediaUris;
    private int mCurrentMediaIndex = 0;

    public List<String> listpath = new LinkedList<>();

    private TextView mAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        mEditTextTitle = findViewById(R.id.editpost_edittext_title);
        mEditTextContent = findViewById(R.id.editpost_edittext_content);
        mButtonMedia = findViewById(R.id.editpost_button_addMedia);
        mButtonMedia.setOnClickListener(v -> GestionMedias.selectMedia(this));
        mMediaContainer = findViewById(R.id.media_container);
        mButtonSubmit = findViewById(R.id.editpost_button_submit);
        mAuthor = findViewById(R.id.editpost_textview_author);
        String author = UserStatic.getFirst_name()+" "+UserStatic.getLast_name();
        mAuthor.setText(author);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEditTextTitle.getText().toString();
                String text = mEditTextContent.getText().toString();
                //Récupérer la liste de documents
                //ici null représente la liste de documents

                new Thread(new Runnable() {
                    public void run() {
                        ResponseData response = null;
                        try {
                            Log.d("EditPostActivity","Attempt to post...");
                            response = publishPostExample(mEditTextTitle.getText().toString(), mEditTextContent.getText().toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ResponseData finalResponse = response;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if(finalResponse != null) {
                                        Log.d("EditPostActivity","Response : Success :"+ finalResponse.getSuccess() + " | " + "Message :" + finalResponse.getMessage());
                                        if (finalResponse.getSuccess().equals("True")) {
                                            Toast.makeText(EditPostActivity.this, finalResponse.getMessage(), Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(EditPostActivity.this, HomeActivity.class);
                                            startActivity(i);
                                        }
                                        else {
                                            Toast.makeText(EditPostActivity.this, "Erreur : "+ finalResponse.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else{
                                        Log.d("RegisterActivity","Error : no server response");
                                    }
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    public ResponseData publishPostExample(String rawtitle, String rawtext) {
        String url = "http://postbee.alwaysdata.net/publish";
        String text = rawtext;
        String title = rawtitle;
        //text = rawtext.getBytes(StandardCharsets.UTF_8).toString();
        //title = rawtitle.getBytes(StandardCharsets.UTF_8).toString();

        ResponseData responseData = null;
        try {
            // Create the connection
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + UserStatic.getAccess());

            // Set the content type
            String boundary = "*****" + System.currentTimeMillis() + "*****";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Start writing the request body
            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            // Write the title field
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n");
            request.write( title.getBytes("UTF-8"));
            request.writeBytes("\r\n");

            // Write the text field
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"text\"\r\n\r\n");
            request.write( text.getBytes("UTF-8"));
            request.writeBytes("\r\n");

            for (String path : listpath) {
                Log.d("EditPostActivity","path : "+path);
                File convertedImageFile = new File(path);

                //Convert to jpeg

                /*
                String convertedImagePath = GestionMedias.getConvertedImagePath(imageFile);
                File convertedImageFile = new File(convertedImagePath);
                */


                // Write the image field
                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"attachments\"; filename=\"" + convertedImageFile.getName() +
                        "\"\r\n");
                request.writeBytes("Content-Type: image/jpeg\r\n\r\n");


                // Write the image file data
                FileInputStream imageStream = new FileInputStream(convertedImageFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = imageStream.read(buffer)) != -1) {
                    request.write(buffer, 0, bytesRead);
                }
                imageStream.close();
                request.writeBytes("\r\n");
            }

            // Write the closing boundary
            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();
            request.close();

            // Read the response
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = responseReader.readLine()) != null) {
                response.append(line);
            }
            responseReader.close();

            String rawPostData = response.toString();
            Log.d("EditPostActivity", "JSON received from the server: " + rawPostData);
            Gson gsonreceiving = new Gson();
            responseData = gsonreceiving.fromJson(rawPostData, ResponseData.class);

            // Print the response
            Log.d("EditPostActivity", "Response: " + response.toString());

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (responseData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GestionMedias.REQUEST_MEDIA_PICK) {
            String path = GestionMedias.handleActivityResult(this, requestCode, resultCode, data, mMediaContainer, mMediaUris, mCurrentMediaIndex,EditPostActivity.this);
            Log.d("ALERTE2","path : "+path);
            if (path != null) {
                listpath.add(path);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GestionMedias.handlePermissionResult(this, requestCode, permissions, grantResults);
    }
}

//rep = (ResponseData) Token.connectToServer("publish", "POST", mTokenAccess,uploadPost, UploadPost.class, ResponseData.class,null);

