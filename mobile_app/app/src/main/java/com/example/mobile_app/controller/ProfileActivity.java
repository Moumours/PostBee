package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.item_pfp.ProfilePictureAdapter;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements RecyclerViewInterface {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView mImageView;
    private RecyclerView mRecyclerView;
    private List<ItemPost> posts = new ArrayList<ItemPost>();

    private String mTokenAccess = UserStatic.access;
    private int amount = 5;
    private boolean isLoading = false;

    private TextView mLastName;
    private TextView mFirstName;
    private TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mLastName = findViewById(R.id.profile_textview_name);
        mFirstName = findViewById(R.id.profile_textview_firstname);
        mEmail = findViewById(R.id.profile_textview_email);
        mLastName.setText(UserStatic.getLast_name());
        mFirstName.setText(UserStatic.getFirst_name());
        mEmail.setText(UserStatic.getEmail());


        mRecyclerView = findViewById(R.id.profile_recyclerview_posts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
        ItemPostAdapter adapter = new ItemPostAdapter(posts, ProfileActivity.this, this);
        mRecyclerView.setAdapter(adapter);


        final EditText oldPasswordEditText = findViewById(R.id.profile_edittext_oldPassword);
        final EditText newPasswordEditText = findViewById(R.id.profile_edittext_newPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.profile_edittext_passwordConfirm);
        final Button changePasswordButton = findViewById(R.id.profile_button_changePassword);

        mImageView = findViewById(R.id.profile_imageview_pfp);
        mRecyclerView = findViewById(R.id.profile_recyclerview_posts);

        ProfilePictureManager.setProfilePicture(ProfileActivity.this, mImageView, UserStatic.getProfile_picture());
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

        /*mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.profile_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                receiveprofilePage(amount);
            }
        });*/

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
        mRecyclerView.setAdapter(new ItemPostAdapter(posts, getApplicationContext(), this));
        receiveprofilePage(amount);
    }

    @Override
    public void onItemClick(int position) {
        Intent homeActivityIntent = new Intent(ProfileActivity.this, ViewPostActivity.class);
        homeActivityIntent.putExtra("ID", posts.get(position).getId());
        homeActivityIntent.putExtra("TITLE", posts.get(position).getTitle());
        homeActivityIntent.putExtra("AUTHOR", posts.get(position).getAuthor().getFull_name());
        homeActivityIntent.putExtra("DATE", posts.get(position).getDate());
        startActivity(homeActivityIntent);
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
    public void receiveprofilePage(int amount) {
        isLoading = true;
        new Thread(new Runnable() {
            public void run() {
                try {
                    Type type = new TypeToken<List<ItemPost>>(){}.getType();
                    String endUrl = "posts/?type=own&amount=" + amount + "&start=" + posts.size();
                    final List<ItemPost> receivedPosts = convertObjectToList(Token.connectToServer(endUrl,"GET",mTokenAccess,null,null,null,type));
                    for (ItemPost itemPost : receivedPosts){
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            String rawStringDate = itemPost.getDate();
                            Log.d("HomeActivity","rawStringDate : "+rawStringDate);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
                            LocalDateTime date = LocalDateTime.parse(rawStringDate, formatter);
                            Log.d("HomeActivity","Conversion de la date : "+date.toString());
                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.FRENCH);
                            Log.d("HomeActivity","Conversion de la date : "+date.format(formatter2).toString());
                            itemPost.setDate(date.format(formatter2).toString());
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            posts.addAll(receivedPosts);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                            //mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } catch (Exception e) {
                    Log.e("ProfileActivity", "Erreur dans ProfileActivity", e);
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });isLoading = false;
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
                    ResponseData response = (ResponseData) Token.connectToServer("change_password", "POST", UserStatic.getAccess(),params, params.getClass(), ResponseData.class,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void clickImage(View view) {
        Log.d("ProfileActivity","Image cliquée");
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_pfp);
        RecyclerView recyclerView = dialog.findViewById(R.id.pfpdialog_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new ProfilePictureAdapter(this, this, dialog));
        dialog.show();
    }
}