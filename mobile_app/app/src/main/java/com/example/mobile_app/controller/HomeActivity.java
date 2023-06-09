package com.example.mobile_app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();
    private int mPostStatus = 0;

    private Button mAddPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = findViewById(R.id.home_recyclerview_posts);
        mAddPostButton = findViewById(R.id.home_menu_button_addpost);



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));

        mAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditPostActivity.class));
            }
        });
        receiveHomePage();
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

                    URL url = new URL("http://10.117.21.10:8000/posts");
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
                            mRecyclerView.getAdapter().notifyDataSetChanged(); // Notify the adapter about the new data
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