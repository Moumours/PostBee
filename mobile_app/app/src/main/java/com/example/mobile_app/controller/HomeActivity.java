package com.example.mobile_app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mobile_app.R;
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HomeActivity extends AppCompatActivity implements RecyclerViewInterface {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();

    private Button mAddPostButton;
    private Button mProfileButton;
    private Button mModerationButton;
    private Button mSettingsButton;

    private String mTokenAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        mTokenAccess = getIntent().getStringExtra("TOKEN_ACCESS");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //posts.add(new ItemPost(0, "Premier test", new Author("Jean-Michel", "II"), "03/03/2003"));
        //posts.add(new ItemPost(1, "Deuxième test (mon préféré :) )", new Author("Moi", ""), "01/01/2000"));
        //posts.add(new ItemPost(2, "abc", new Author("Bernard", "Rouge"), "12/06/2023"));

        mSwipeRefreshLayout = findViewById(R.id.home_swiperefreshlayout_s2r);
        mRecyclerView = findViewById(R.id.home_recyclerview_posts);
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
                Intent i = new Intent(HomeActivity.this, EditPostActivity.class);
                i.putExtra("TOKEN_ACCESS",mTokenAccess);
                startActivity(i);
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
                Intent i = new Intent(HomeActivity.this, ModerationActivity.class);
                i.putExtra("TOKEN_ACCESS",mTokenAccess);
                startActivity(i);
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SettingsActivity.class);
                i.putExtra("TOKEN_ACCESS",mTokenAccess);
                startActivity(i);
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
        homeActivityIntent.putExtra("TOKEN_ACCESS", mTokenAccess);

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
    public void receiveHomePage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("HomeActivity", "Début de la méthode receiveHomePage");
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    String endUrl = "posts/";
                    final List<ItemPost> receivedPosts = convertObjectToList(Token.connectToServer(endUrl,"GET",mTokenAccess,null,null,null,type));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            posts.clear();
                            if(receivedPosts != null) {
                                posts.addAll(receivedPosts);
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                            else {
                                Log.d("HomeActivity","Error while receiving the list of posts");
                            }
                        }
                    });
                    /*
                    Log.d("HomeActivity", "Nombre de posts reçus : " + posts.size());

                    django.disconnect();
                    */
                } catch (Exception e) {
                    Log.e("HomeActivity", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }
}