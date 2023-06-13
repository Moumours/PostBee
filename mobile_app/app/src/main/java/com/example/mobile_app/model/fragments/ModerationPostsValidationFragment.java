package com.example.mobile_app.model.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_app.R;
import com.example.mobile_app.controller.HomeActivity;
import com.example.mobile_app.controller.ModerationActivity;
import com.example.mobile_app.controller.ViewPostActivity;
import com.example.mobile_app.model.Author;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_post.ItemPostAdapter;
import com.example.mobile_app.model.item_post.ItemPostValidationAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModerationPostsValidationFragment extends Fragment implements RecyclerViewInterface {
    private List<ItemPost> posts = new ArrayList<ItemPost>();
    private RecyclerView recyclerView;
    private String type = "moderate";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moderation_posts_validation, container, false);
        recyclerView = rootView.findViewById(R.id.modposts_recyclerview_posts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ItemPostValidationAdapter(posts, getActivity().getApplicationContext(), this));

        receiveModaratePage();
        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Intent ModerationPostValidationFragment = new Intent(getActivity(), ViewPostActivity.class);
        ModerationPostValidationFragment.putExtra("ID", posts.get(position).getId());
        ModerationPostValidationFragment.putExtra("TITLE", posts.get(position).getTitle());
        ModerationPostValidationFragment.putExtra("AUTHOR", posts.get(position).getAuthor().getFullname());
        ModerationPostValidationFragment.putExtra("DATE", posts.get(position).getDate());

        startActivity(ModerationPostValidationFragment);
    }

    public void receiveModaratePage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("HomeActivity", "Début de la méthode receiveModaratePage");

                    URL url = new URL("http://postbee.alwaysdata.net/posts/?type=" + type + "&amount=10");
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
                    Log.d("ModerationPostsValidationFragment", "Données brutes reçues : " + rawPostData);

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    final List<ItemPost> receivedPosts = gson.fromJson(rawPostData, type);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            posts.clear();
                            posts.addAll(receivedPosts);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                    Log.d("ModerationPostsValidationFragment", "Nombre de posts reçus : " + posts.size());

                    django.disconnect();
                } catch (Exception e) {
                    Log.e("ModerationPostsValidationFragment", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }
}