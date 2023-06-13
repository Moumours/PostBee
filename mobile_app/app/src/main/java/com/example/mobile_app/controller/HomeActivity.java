package com.example.mobile_app.controller;

import android.app.Activity;
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
import com.example.mobile_app.model.Author;
import com.example.mobile_app.model.Token;
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
import java.util.Arrays;
import java.util.Collection;
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
    private String mTokenAccess;
    private int amount = 5;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTokenAccess = getIntent().getStringExtra("TOKEN_ACCESS");

        mRecyclerView = findViewById(R.id.home_recyclerview_posts);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    receiveHomePage(amount);
                }
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.home_swiperefreshlayout_s2r);
        mAddPostButton = findViewById(R.id.home_menu_button_addpost);
        mProfileButton = findViewById(R.id.home_menu_button_profile);
        mModerationButton = findViewById(R.id.home_menu_button_moderation);
        mSettingsButton = findViewById(R.id.home_menu_button_settings);

        //TODO: change this generic condition
        // mModerationButton.setVisibility(thisUser.isModerator ? View.VISIBLE : View.GONE);

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
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                i.putExtra("TOKEN_ACCESS",mTokenAccess);
                startActivity(i);
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
        receiveHomePage(amount);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            posts.clear();
            mRecyclerView.getAdapter().notifyDataSetChanged();
            receiveHomePage(amount);
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
    public static List<ItemPost> convertObjectToList(Object obj) {
        if (obj != null) {
            List<ItemPost> list = new ArrayList<>();
            if (obj.getClass().isArray()) {
                list = Arrays.asList((ItemPost[]) obj);
            } else if (obj instanceof Collection) {
                list = new ArrayList<>((Collection<ItemPost>) obj);
            }
            return list;
        }
        else {
            return null;
        }
    }
    public void receiveHomePage(int amount) {
        isLoading = true;

        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("HomeActivity", "Début de la méthode receiveHomePage");
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    String endUrl = "posts/?amount=" + amount + "&start=" + posts.size();
                    final List<ItemPost> receivedPosts = convertObjectToList(Token.connectToServer(endUrl,"GET",mTokenAccess,null,null,null,type));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(receivedPosts.size() == 0){
                                Toast.makeText(getApplicationContext(),"Il reste plus de poste",Toast.LENGTH_SHORT).show();
                            } else {
                                posts.addAll(receivedPosts);
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    /*
                    Log.d("HomeActivity", "Nombre de posts reçus : " + posts.size());

                    django.disconnect();
                    */
                } catch (Exception e) {
                    Log.e("HomeActivity", "Erreur dans receiveHomePage", e);
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    isLoading = false;
                }

            }
        }).start();
    }
}