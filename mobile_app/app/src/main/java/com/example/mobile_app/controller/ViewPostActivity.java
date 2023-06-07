package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mobile_app.R;

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

        mTextTitle.setText(getIntent().getStringExtra("TITLE"));
        mTextAuthor.setText(getIntent().getStringExtra("AUTHOR"));
        mTextDate.setText(getIntent().getStringExtra("DATE"));
        mTextContent.setText("L'ID du post est : " + getIntent().getIntExtra("ID", -1));
    }
}