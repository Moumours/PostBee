package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_post.ItemPostAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ImageView mImageView;
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageView = findViewById(R.id.profile_imageview_pfp);
        mRecyclerView = findViewById(R.id.profile_recyclerview_posts);

        mImageView.setImageDrawable(getDrawable(R.drawable.logo));

        posts.add(new ItemPost(0,"Mon 1er article", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(4,"Titre d'article 2", "Jean Sériens", "02/02/2002"));
        posts.add(new ItemPost(7,"Titre d'article 3", "Jean Sériens", "03/03/2003"));
        posts.add(new ItemPost(9,"Titre d'article 4", "Jean Sériens", "12/10/2015"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));
    }

    @Override
    public void onItemClick(int position) {
        Intent homeActivityIntent = new Intent(ProfileActivity.this, ViewPostActivity.class);
        homeActivityIntent.putExtra("ID", posts.get(position).getId());
        homeActivityIntent.putExtra("TITLE", posts.get(position).getTitle());
        homeActivityIntent.putExtra("AUTHOR", posts.get(position).getAuthor());
        homeActivityIntent.putExtra("DATE", posts.get(position).getDate());
        startActivity(homeActivityIntent);
    }
}