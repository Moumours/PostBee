package com.example.mobile_app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mobile_app.R;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_post.ItemPostAdapter;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecyclerViewInterface {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();
    private int mPostStatus = 0;

    private Button mAddPostButton;
    private Button mProfileButton;
    private Button mModerationButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSwipeRefreshLayout = findViewById(R.id.home_swiperefreshlayout_s2r);
        mRecyclerView = findViewById(R.id.home_recyclerview_posts);
        mAddPostButton = findViewById(R.id.home_menu_button_addpost);
        mProfileButton = findViewById(R.id.home_menu_button_profile);
        mModerationButton = findViewById(R.id.home_menu_button_moderation);
        mSettingsButton = findViewById(R.id.home_menu_button_settings);



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));

        mAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditPostActivity.class));
            }
        });

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        mModerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ModerationActivity.class));
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });
        receiveHomePage();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                receiveHomePage();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent homeActivityIntent = new Intent(HomeActivity.this, ViewPostActivity.class);
        homeActivityIntent.putExtra("ID", posts.get(position).getId());
        homeActivityIntent.putExtra("TITLE", posts.get(position).getTitle());
        homeActivityIntent.putExtra("AUTHOR", posts.get(position).getAuthor().getFirstname());
        homeActivityIntent.putExtra("DATE", posts.get(position).getDate());
        homeActivityIntent.putExtra("STATUS", mPostStatus);

        startActivity(homeActivityIntent);
    }

    public void receiveHomePage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("HomeActivity", "Début de la méthode receiveHomePage");

                    URL url = new URL("http://postbee.alwaysdata.net/posts");
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
                    Log.d("HomeActivity", "Données brutes reçues : " + rawPostData);

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    final List<ItemPost> receivedPosts = gson.fromJson(rawPostData, type);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            posts.clear();
                            posts.addAll(receivedPosts);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                    Log.d("HomeActivity", "Nombre de posts reçus : " + posts.size());

                    django.disconnect();
                } catch (Exception e) {
                    Log.e("HomeActivity", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }
}