package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.ViewPost;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ViewPostActivity extends AppCompatActivity {

    TextView mTextTitle;
    TextView mTextContent;
    TextView mTextAuthor;
    TextView mTextDate;
    ViewPost mViewPost;

    String mTokenAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        mTextTitle = findViewById(R.id.viewpost_textview_title);
        mTextAuthor = findViewById(R.id.viewpost_textview_author);
        mTextDate = findViewById(R.id.viewpost_textview_date);
        mTextContent = findViewById(R.id.viewpost_textview_content);

        Intent i = getIntent();
        int postId = i.getIntExtra("ID",0);
        downloadViewPost(postId);

        mTextTitle.setText(i.getStringExtra("TITLE"));
        mTextAuthor.setText(i.getStringExtra("AUTHOR"));
        mTextDate.setText(i.getStringExtra("DATE") + " " + getIntent().getIntExtra("ID", 0));

        mTokenAccess = i.getStringExtra("TOKEN_ACCESS");

        Log.d("ViewPostActivity","Voici l'id : " + postId);

        // ViewPost a aussi les documents et les commentaires
        // ViewPost.getListdocument()
        // ViewPost.getListcomment()
    }

    public void downloadViewPost(int postId) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://postbee.alwaysdata.net/post/?id=" + postId);
                    mViewPost = (ViewPost.class).cast(Token.connectToServer("post/?id=" + postId,"GET",mTokenAccess,null,null, ViewPost.class,null));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mViewPost != null) {
                                Log.d("ViewPostActivity","Post content received successfully");
                                mTextContent.setText(mViewPost.getText());
                            }
                            else{
                                Log.d("ViewPostActivity","Post content not received");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

