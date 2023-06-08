package com.example.mobile_app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.ItemPost;
import com.example.mobile_app.model.ItemPostAdapter;
import com.example.mobile_app.model.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();

    private Button mAddPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = findViewById(R.id.home_recyclerview_posts);
        mAddPostButton = findViewById(R.id.home_menu_button_addpost);

        posts.add(new ItemPost(0,"Titre d'article 1", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(1,"Titre d'article 2", "Jean Neymar", "02/02/2002"));
        posts.add(new ItemPost(2,"Titre d'article 3", "Martine", "03/03/2003"));
        posts.add(new ItemPost(3,"Titre d'article 4", ":D", "12/10/2015"));
        posts.add(new ItemPost(4,"Titre d'article 5", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(5,"Titre d'article 6", "Jean Neymar", "02/02/2002"));
        posts.add(new ItemPost(6,"Titre d'article 7", "Martine", "03/03/2003"));
        posts.add(new ItemPost(7,"Titre d'article 8", ":D", "12/10/2015"));
        posts.add(new ItemPost(8,"Titre d'article 9", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(9,"Titre d'article 10", "Jean Neymar", "02/02/2002"));
        posts.add(new ItemPost(10,"Titre d'article 11", "Martine", "03/03/2003"));
        posts.add(new ItemPost(11,"Titre d'article 12", ":D", "12/10/2015"));
        posts.add(new ItemPost(12,"Titre d'article 13", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(13,"Titre d'article 14", "Jean Neymar", "02/02/2002"));
        posts.add(new ItemPost(14,"Titre d'article 15", "Martine", "03/03/2003"));
        posts.add(new ItemPost(15,"Titre d'article 16", ":D", "12/10/2015"));
        posts.add(new ItemPost(16,"ABCDEF", "def", "abc"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));

        mAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditPostActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent homeActivityIntent = new Intent(HomeActivity.this, ViewPostActivity.class);
        homeActivityIntent.putExtra("ID", posts.get(position).getId());
        homeActivityIntent.putExtra("TITLE", posts.get(position).getTitle());
        homeActivityIntent.putExtra("AUTHOR", posts.get(position).getAuthor());
        homeActivityIntent.putExtra("DATE", posts.get(position).getDate());
        startActivity(homeActivityIntent);
    }
}