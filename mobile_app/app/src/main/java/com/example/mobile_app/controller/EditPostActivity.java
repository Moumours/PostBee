package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Button mButtonSubmit;
    private String mTokenAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        mTokenAccess = getIntent().getStringExtra("TOKEN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        mEditTextTitle = findViewById(R.id.editpost_edittext_title);
        mEditTextContent = findViewById(R.id.editpost_edittext_content);
        mButtonSubmit = findViewById(R.id.editpost_button_submit);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEditTextTitle.getText().toString();
                String text = mEditTextContent.getText().toString();
                //Récupérer la liste de documents
                //ici null représente la liste de documents
                UploadPost uploadPost = new UploadPost(title,text,null);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ResponseData response;
                            response = (ResponseData) Token.connectToServer("publish", "POST", mTokenAccess,uploadPost, UploadPost.class, ResponseData.class,null);
                            if(response != null) {
                                Log.d("EditPostActivity","Response : Success :"+ response.getSuccess() + " | " + "Message :" + response.getMessage());
                                if (response.getSuccess().equals("True")) {
                                    Toast.makeText(EditPostActivity.this, "Publication envoyée", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(EditPostActivity.this, HomeActivity.class);
                                    startActivity(i);
                                }
                                else {
                                    Log.d("EditPostActivity","Error : no server response");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
