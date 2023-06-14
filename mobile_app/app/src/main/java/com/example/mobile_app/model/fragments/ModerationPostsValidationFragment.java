package com.example.mobile_app.model.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mobile_app.R;
import com.example.mobile_app.controller.ViewPostActivity;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_post.ItemPostValidationAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ModerationPostsValidationFragment extends Fragment implements RecyclerViewInterface {
    private List<ItemPost> posts = new ArrayList<ItemPost>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mTokenAccess = UserStatic.access;
    private int amount = 7;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_moderation_posts_validation, container, false);
        mRecyclerView = rootView.findViewById(R.id.modposts_recyclerview_posts);
        mSwipeRefreshLayout = rootView.findViewById(R.id.modposts_swiperefreshlayout_s2r);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ItemPostValidationAdapter(posts, getActivity().getApplicationContext(), this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading)
                    receiveModaratePage(amount);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                receiveModaratePage(amount);
            }
        });
        receiveModaratePage(amount);
        return rootView;
    }

    public static List<ItemPost> convertObjectToList(Object obj) {
        if (obj != null) {
            List<ItemPost> list = new ArrayList<>();
            if (obj.getClass().isArray())
                list = Arrays.asList((ItemPost[]) obj);
            else if (obj instanceof Collection)
                list = new ArrayList<>((Collection<ItemPost>) obj);
            return list;
        }else
            return null;
    }

    @Override
    public void onItemClick(int position) {
        Intent ModerationPostValidationFragment = new Intent(getActivity(), ViewPostActivity.class);
        ModerationPostValidationFragment.putExtra("ID", posts.get(position).getId());
        ModerationPostValidationFragment.putExtra("TITLE", posts.get(position).getTitle());
        ModerationPostValidationFragment.putExtra("AUTHOR", posts.get(position).getAuthor().getFull_name());
        ModerationPostValidationFragment.putExtra("DATE", posts.get(position).getDate());

        startActivity(ModerationPostValidationFragment);
    }

    public void receiveModaratePage(int amount) {
        isLoading = true;
        new Thread(new Runnable() {
            public void run() {
                try {
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    String endUrl = "posts/?type=moderate&amount=" + amount + "&start=" + posts.size();
                    final List<ItemPost> receivedPosts = convertObjectToList(Token.connectToServer(endUrl,"GET", UserStatic.getAccess(),null,null,null,type));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            posts.addAll(receivedPosts);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } catch (Exception e) {
                        Log.e("ModeratinonPostsValidationFragment", "Erreur dans ModeratinonPostsValidationFragment", e);
                } finally {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });isLoading = false;
                }
            }
        }).start();
    }
}