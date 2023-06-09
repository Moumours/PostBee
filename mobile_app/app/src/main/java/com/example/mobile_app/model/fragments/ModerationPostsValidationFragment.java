package com.example.mobile_app.model.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_app.R;
import com.example.mobile_app.controller.HomeActivity;
import com.example.mobile_app.controller.ModerationActivity;
import com.example.mobile_app.controller.ViewPostActivity;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_post.ItemPostAdapter;
import com.example.mobile_app.model.item_post.ItemPostValidationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModerationPostsValidationFragment extends Fragment implements RecyclerViewInterface {
    private List<ItemPost> posts = new ArrayList<ItemPost>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moderation_posts_validation, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.modposts_recyclerview_posts);

        posts.add(new ItemPost(0,"Titre d'article 1", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(1,"Titre d'article 2", "Jean Neymar", "02/02/2002"));
        posts.add(new ItemPost(2,"Titre d'article 3", "Martine", "03/03/2003"));
        posts.add(new ItemPost(3,"Titre d'article 4", ":D", "12/10/2015"));
        posts.add(new ItemPost(4,"Titre d'article 5", "Jean Sériens", "01/01/2000"));
        posts.add(new ItemPost(5,"Titre d'article 6", "Jean Neymar", "02/02/2002"));
        posts.add(new ItemPost(6,"Titre d'article 7", "Martine", "03/03/2003"));
        posts.add(new ItemPost(7,"Titre d'article 8", ":D", "12/10/2015"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ItemPostValidationAdapter(posts, getActivity().getApplicationContext(), this));

        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Intent homeActivityIntent = new Intent(getActivity(), ViewPostActivity.class);
        homeActivityIntent.putExtra("ID", posts.get(position).getId());
        homeActivityIntent.putExtra("TITLE", posts.get(position).getTitle());
        homeActivityIntent.putExtra("AUTHOR", posts.get(position).getAuthor());
        homeActivityIntent.putExtra("DATE", posts.get(position).getDate());
        startActivity(homeActivityIntent);
    }
}