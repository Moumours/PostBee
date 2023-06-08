package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.ViewPost;

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

        //Récupère ViewPost
        ViewPost viewpost = new ViewPost(getIntent().getIntExtra("ID",0),getIntent().getIntExtra("STATUS",0));

        mTextTitle.setText(getIntent().getStringExtra("TITLE"));
        mTextAuthor.setText(getIntent().getStringExtra("AUTHOR"));
        mTextDate.setText(getIntent().getStringExtra("DATE") + " " + getIntent().getStringExtra("ID"));
        mTextContent.setText(viewpost.getContent());

        // ViewPost a aussi les documents et les commentaires
        // ViewPost.getListdocument()
        // ViewPost.getListcomment()
    }
}