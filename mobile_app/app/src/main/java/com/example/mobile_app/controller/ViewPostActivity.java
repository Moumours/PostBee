package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.ViewPost;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ViewPostActivity extends AppCompatActivity {

    TextView mTextTitle;
    TextView mTextContent;
    TextView mTextAuthor;
    TextView mTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        mTextTitle = findViewById(R.id.viewpost_textview_title);
        mTextAuthor = findViewById(R.id.viewpost_textview_author);
        mTextDate = findViewById(R.id.viewpost_textview_date);
        mTextContent = findViewById(R.id.viewpost_textview_content);

        int postId = getIntent().getIntExtra("ID",0);

        getPostDetails(postId);
        Log.d("ViewPostActivity","Voici l'id : " + postId);

        // ViewPost a aussi les documents et les commentaires
        // ViewPost.getListdocument()
        // ViewPost.getListcomment()
    }

    public void getPostDetails(int postId) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.117.21.10:8000/post/?id=" + postId);
                    HttpURLConnection django = (HttpURLConnection) url.openConnection();

                    django.setRequestMethod("GET");
                    django.setRequestProperty("Accept","application/json");

                    BufferedReader in = new BufferedReader(new InputStreamReader(django.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    String rawPostData = response.toString();

                    Gson gson = new Gson();
                    ViewPost viewpost = gson.fromJson(rawPostData, ViewPost.class);

                    Log.d("ViewPostActivity","Voici le contenu : " + viewpost.getText());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextTitle.setText(getIntent().getStringExtra("TITLE"));
                            mTextAuthor.setText(getIntent().getStringExtra("AUTHOR"));
                            mTextDate.setText(getIntent().getStringExtra("DATE") + " " + getIntent().getStringExtra("ID"));
                            mTextContent.setText(viewpost.getText());
                        }
                    });

                    django.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

