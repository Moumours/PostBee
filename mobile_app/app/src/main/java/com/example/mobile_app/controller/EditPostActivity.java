package com.example.mobile_app.controller;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobile_app.R;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Button mButtonSubmit;

    private Button mButtonImage;

    private Uri[] mImageUris;
    private String mTokenAccess;

    private byte mCurrentImageIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        mEditTextTitle = findViewById(R.id.editpost_edittext_title);
        mEditTextContent = findViewById(R.id.editpost_edittext_content);
        mButtonImage = findViewById(R.id.edit_post_button_photo);
        mButtonImage.setOnClickListener(v -> GestionPhoto.selectPhoto(this));
        mButtonSubmit = findViewById(R.id.editpost_button_submit);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEditTextTitle.getText().toString();
                String text = mEditTextContent.getText().toString();
                //Récupérer la liste de documents
                //ici null représente la liste de documents
                UploadPost uploadPost = new UploadPost(title,text);

                new Thread(new Runnable() {
                    public void run() {
                        ResponseData response = null;
                        try {
                            Log.d("EditPostActivity","Attempt to post...");
                            response = publishPostExample(mEditTextTitle.getText().toString(), mEditTextContent.getText().toString(), null);


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

    public ResponseData publishPostExample(String rawtitle, String rawtext, String imagePath) {
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

            if (imagePath != null) {
                File imageFile = new File(imagePath);

                // Write the image field
                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"images\"; filename=\"" + imageFile.getName() +
                        "\"\r\n");
                request.writeBytes("Content-Type: image/jpeg\r\n\r\n");


                // Write the image file data
                FileInputStream imageStream = new FileInputStream(imageFile);
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

/*

    mButtonSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = mEditTextTitle.getText().toString();
            String text = mEditTextContent.getText().toString();
            //Récupérer la liste de documents
            //ici null représente la liste de documents
            UploadPost uploadPost = new UploadPost(title,text);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(ContextCompat.checkSelfPermission(EditPostActivity.this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                            // Permission is granted
                            Log.d("EditPostActivity","Permission is granted");
                            PublishPostExample();
                        }
                        else{
                            Log.d("EditPostActivity","Permission is not granted");
                            ActivityCompat.requestPermissions(EditPostActivity.this, new String[] { READ_EXTERNAL_STORAGE }, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    });
}


    // A modifier
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GestionPhoto.handleActivityResult(this, requestCode, resultCode, data, mImageContainer, mImageUris, mCurrentImageIndex);

        // Afficher l'URI de l'image sélectionnée
        if (resultCode == RESULT_OK && requestCode == GestionPhoto.REQUEST_CODE_SELECT_PHOTO) {
            if (data != null) {

                // Gerer l'URI
            }
        }
    }

    public void PublishPostExample() {
        //String url = "http://10.39.251.162:8000/publish";
        String url = "http://postbee.alwaysdata.net/publish";
        String imagePath = "/storage/emulated/0/Pictures/IMG_20230613_180421.jpg";
        String title = "Post Alex";
        String textdeg = "Si tu vois ça je suis content";
        String text = null;
        try {
            text = textdeg.getBytes("UTF-8").toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            File imageFile = new File(imagePath);

            // Create the connection
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // Set the content type
            String boundary = "*****" + System.currentTimeMillis() + "*****";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Start writing the request body
            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            // Write the title field
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n");
            request.writeBytes(title + "\r\n");

            // Write the text field
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"text\"\r\n\r\n");
            request.writeBytes(text + "\r\n");

            // Write the image field
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"images\"; filename=\"" + imageFile.getName() +
                    "\"\r\n");
            request.writeBytes("Content-Type: image/jpeg\r\n\r\n");

            // Write the image file data
            FileInputStream imageStream = new FileInputStream(imageFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                request.write(buffer, 0, bytesRead);
            }
            imageStream.close();
            request.writeBytes("\r\n");

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

            // Print the response
            Log.d("EditPostActivity","Response: " + response.toString());

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */


    /*
    ResponseData rep = null;
                            //rep = (ResponseData) Token.connectToServer("publish", "POST", mTokenAccess,uploadPost, UploadPost.class, ResponseData.class,null);
                            String endURL = "publish";
                            String requestMethod = "POST";
                            String token = mTokenAccess;

                            URL url = new URL("http://10.39.251.162:8000/"+endURL);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod(requestMethod);
                            conn.setRequestProperty("Content-Type", "multipart/form-data");
                            conn.setRequestProperty("Accept","application/json");
                            if(token != null){
                                Log.d("connectToServer", "Token used : "+ token);
                                conn.setRequestProperty("Authorization", "Bearer " + token);
                            }
                            if(requestMethod.equals("POST")) {
                                conn.setDoOutput(true);
                                conn.setDoInput(true);
                            }
                            Log.d("connectToServer", "Connecting to "+"http://postbee.alwaysdata.net/"+endURL+" with method \"" + requestMethod + "\"");

                            //Send data to the server
                            if (uploadPost != null) {
                                Gson gson = new Gson();
                                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                                os.writeBytes(gson.toJson(uploadPost));

                                Log.d("connectToServer", "JSON sent to the server: " + gson.toJson(uploadPost));

                                os.flush();
                                os.close();
                            }

                            int respCode = conn.getResponseCode();
                            Log.d("connectToServer", "Response code from the server : " + respCode);
                            Log.d("connectToServer", "Response message from the server : " + conn.getResponseMessage());
                            if(respCode == HttpURLConnection.HTTP_OK){
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String inputLine;
                                StringBuffer response = new StringBuffer();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                                String rawPostData = response.toString();

                                Gson gsonreceiving = new Gson();
                                rep = gsonreceiving.fromJson(rawPostData, ResponseData.class);
                                Log.d("connectToServer", "Object received");
                            }
                            conn.disconnect();

                            if(rep != null) {
                                Log.d("EditPostActivity", "Response : Success :" + rep.getSuccess() + " | " + "Message :" + rep.getMessage());
                                if (rep.getSuccess().equals("True")) {
                                    Toast.makeText(EditPostActivity.this, "Publication envoyée", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(EditPostActivity.this, HomeActivity.class);
                                    startActivity(i);
                                } else {
                                    Log.d("EditPostActivity", "Error : no server response");
                                }
                            }
     */
}

//rep = (ResponseData) Token.connectToServer("publish", "POST", mTokenAccess,uploadPost, UploadPost.class, ResponseData.class,null);

