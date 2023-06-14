package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Author;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.ResponseData;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.item_pfp.ProfilePictureAdapter;
import com.example.mobile_app.model.UserStatic;
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

        final TextView lastnameText = findViewById(R.id.profile_textview_name);
        final TextView firstnameText = findViewById(R.id.profile_textview_firstname);
        final TextView emailText = findViewById(R.id.profile_textview_email);

        final EditText oldPasswordEditText = findViewById(R.id.profile_edittext_oldPassword);
        final EditText newPasswordEditText = findViewById(R.id.profile_edittext_newPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.profile_edittext_passwordConfirm);
        final Button changePasswordButton = findViewById(R.id.profile_button_changePassword);

        mImageView = findViewById(R.id.profile_imageview_pfp);
        mRecyclerView = findViewById(R.id.profile_recyclerview_posts);

        Intent i = getIntent();
        mTokenAccess = i.getStringExtra("TOKEN_ACCESS");
        mImageView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_pfp);
            RecyclerView recyclerView = dialog.findViewById(R.id.pfpdialog_recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(new ProfilePictureAdapter(this, this, dialog));
            dialog.show();
        });

        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            if (newPassword.equals(confirmPassword)) {
                askToResetPassword(oldPassword, newPassword);
            } else {
                Toast.makeText(ProfileActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            }
        });


        Author me = new Author("Jean", "Sériens");
        posts.add(new ItemPost(0,"Mon 1er article", me, "01/01/2000"));
        posts.add(new ItemPost(4,"Titre d'article 2", me, "02/02/2002"));
        posts.add(new ItemPost(7,"Titre d'article 3", me, "03/03/2003"));
        posts.add(new ItemPost(9,"Titre d'article 4", me, "12/10/2015"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));
        //receiveprofilePage();

        //Affectation des valeurs
        lastnameText.setText(UserStatic.getLast_name());
        firstnameText.setText(UserStatic.getFirst_name());
        emailText.setText(UserStatic.getEmail());
        ProfilePictureManager.setProfilePicture(this, mImageView, UserStatic.getProfile_picture());
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
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("old_password", oldPassword);
                    params.put("new_password", newPassword);
                    ResponseData response;
                    response = (ResponseData) Token.connectToServer("change_password", "POST", UserStatic.getAccess(),params, params.getClass(), ResponseData.class,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}