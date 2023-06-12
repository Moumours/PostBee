package com.example.mobile_app.model.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_user.ItemUser;
import com.example.mobile_app.model.item_user.ItemUserAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModerationUsersFragment extends Fragment implements RecyclerViewInterface {
    private List<ItemUser> users = new ArrayList<ItemUser>();
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moderation_users, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.modusers_recyclerview_users);

        //users.add(new ItemUser(7,"Delta", "Alexandre", "alexandre.delta@uha.fr", 2));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ItemUserAdapter(users, getActivity(), this));

        receiveModarateUserPage();
        return rootView;
    }

    @Override
    public void onItemClick(int position) {

    }

    public void receiveModarateUserPage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("ModerationUsersValidationFragment", "Début de la méthode receiveModarateUserPage");

                    URL url = new URL("http://postbee.alwaysdata.net/users/?moderate=True&amount=10");
                    //TODO: Mettre la bonne URL pour receuvoir la liste des utilisateur
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
                    Log.d("ModerationUsersValidationFragment", "Données brutes reçues : " + rawPostData);

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ItemUser>>(){}.getType();
                    final List<ItemUser> receivedUser = gson.fromJson(rawPostData, type);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            users.clear();
                            users.addAll(receivedUser);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                    Log.d("ModerationPostsValidationFragment", "Nombre d'utilisateurs reçus : " + users.size());
                    django.disconnect();
                } catch (Exception e) {
                    Log.e("ModerationPostsValidationFragment", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }
}