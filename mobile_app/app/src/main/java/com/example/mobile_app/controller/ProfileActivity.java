package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Author;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_post.ItemPost;
import com.example.mobile_app.model.item_post.ItemPostAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ImageView mImageView;
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();

    private String type = "self";
    String mTokenAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final EditText oldPasswordEditText = findViewById(R.id.profile_edittext_oldPassword);
        final EditText newPasswordEditText = findViewById(R.id.profile_edittext_newPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.profile_edittext_passwordConfirm);
        final Button changePasswordButton = findViewById(R.id.profile_button_changePassword);

        mImageView = findViewById(R.id.profile_imageview_pfp);
        mRecyclerView = findViewById(R.id.profile_recyclerview_posts);

        mImageView.setImageDrawable(getDrawable(R.drawable.round_person_24));

        Intent i = getIntent();
        mTokenAccess = i.getStringExtra("TOKEN_ACCESS");

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                if (newPassword.equals(confirmPassword)) {
                    askToResetPassword(oldPassword, newPassword);
                } else {
                    Toast.makeText(ProfileActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        Author me = new Author("Jean", "Sériens");
        posts.add(new ItemPost(0,"Mon 1er article", me, "01/01/2000"));
        posts.add(new ItemPost(4,"Titre d'article 2", me, "02/02/2002"));
        posts.add(new ItemPost(7,"Titre d'article 3", me, "03/03/2003"));
        posts.add(new ItemPost(9,"Titre d'article 4", me, "12/10/2015"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));
         */
        //receiveprofilePage();
    }

    @Override
    public void onItemClick(int position) {
        Intent homeActivityIntent = new Intent(ProfileActivity.this, ViewPostActivity.class);
        homeActivityIntent.putExtra("ID", posts.get(position).getId());
        homeActivityIntent.putExtra("TITLE", posts.get(position).getTitle());
        homeActivityIntent.putExtra("AUTHOR", posts.get(position).getAuthor().getFullname());
        homeActivityIntent.putExtra("DATE", posts.get(position).getDate());
        startActivity(homeActivityIntent);
    }

    public void receiveprofilePage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("ProfileActivity", "Début de la méthode receiveprofilePage");

                    URL url = new URL("http://postbee.alwaysdata.net/posts/?type=" + type);
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
                    Log.d("ProfileActivity", "Données brutes reçues : " + rawPostData);

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    final List<ItemPost> receivedPosts = gson.fromJson(rawPostData, type);

                    runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            posts.clear();
                            posts.addAll(receivedPosts);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                    Log.d("ProfileActivity", "Nombre de posts reçus : " + posts.size());

                    django.disconnect();
                } catch (Exception e) {
                    Log.e("ProfileActivity", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }

    public void askToResetPassword(String oldPassword, String newPassword) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://postbee.alwaysdata.net/change_password");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setRequestProperty("Authorization", "Bearer " + mTokenAccess);

                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Gson gson = new Gson();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("old_password", oldPassword);
                    params.put("new_password", newPassword);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(gson.toJson(params));

                    Log.d("ProfileActivity", "Request JSON sent to the server: " + gson.toJson(params));

                    Log.d("ProfileActivity", "Token récupéré: " + mTokenAccess);

                    int responseCode = conn.getResponseCode();
                    Log.d("ProfileActivity", "HTTP response code: " + responseCode);

                    /*
                    if (responseCode == 200) {
                        Sa c'est bien passé
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this, "L'ancien mot de passe est incorrect", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                     */

                    os.flush();
                    os.close();

                    Log.d("ProfileActivity", "HTTP response code: " + conn.getResponseCode()); // Add this line

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}