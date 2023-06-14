package com.example.mobile_app.model.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.item_user.ItemUser;
import com.example.mobile_app.model.item_user.ItemUserAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ModerationUsersFragment extends Fragment implements RecyclerViewInterface {
    private List<ItemUser> users = new ArrayList<ItemUser>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mTokenAccess = UserStatic.access;
    private int amount = 7;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moderation_users, container, false);
        mRecyclerView = rootView.findViewById(R.id.modusers_recyclerview_users);
        mSwipeRefreshLayout = rootView.findViewById(R.id.modusers_swiperefreshlayout_s2r);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ItemUserAdapter(users, getActivity(), this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    receiveUserList(amount);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                users.clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                receiveUserList(amount);
            }
        });

        receiveUserList(amount);
        return rootView;
    }

    @Override
    public void onItemClick(int position) {
    }

    public static List<ItemUser> convertObjectToList(Object obj) {
        if (obj != null) {
            List<ItemUser> list = new ArrayList<>();
            if (obj.getClass().isArray())
                list = Arrays.asList((ItemUser[]) obj);
            else if (obj instanceof Collection)
                list = new ArrayList<>((Collection<ItemUser>) obj);
            return list;
        }else
            return null;
    }

    public void receiveUserList(int amount) {
        isLoading = true;
        new Thread(new Runnable() {
            public void run() {
                try {
                    Type type = new TypeToken<List<ItemUser>>(){}.getType();
                    String endUrl = "users/?type=moderate&amount=" + amount + "&start=" + users.size();
                    final List<ItemUser> receivedUsers = convertObjectToList(Token.connectToServer(endUrl,"GET", UserStatic.getAccess(),null,null,null,type));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            users.addAll(receivedUsers);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } catch (Exception e) {
                    Log.e("ModerationUsersFragment", "Erreur dans ModerationUsersFragment", e);
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