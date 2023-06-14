package com.example.mobile_app.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.User;
import com.example.mobile_app.model.UserStatic;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class TokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = Token.getEncryptedSharedPreferences(TokenActivity.this);
        String token_refresh = (sharedPreferences.getString("token_refresh", "null or does not exist"));
        if (!(token_refresh.equals("null or does not exist"))){
            Log.d("TokenActivity","Token found, updating token...");
            new Thread(new Runnable() {
                public void run() {
                    User user = null;
                    try {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("refresh", token_refresh);
                        user = (User.class).cast(Token.connectToServer("refresh_token", "POST", null, params, params.getClass(), User.class, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (user != null) {
                            Log.d("LoginActivity", "Sauvegarde des tokens et mise à jour des variables statiques");
                            Token.storeToken(TokenActivity.this, new Token(user.getAccess(), user.getRefresh()));
                            UserStatic.setAccess(user.getAccess());
                            UserStatic.setRefresh(user.getRefresh());
                            UserStatic.setFirst_name(user.getFirst_name());
                            UserStatic.setLast_name(user.getLast_name());
                            UserStatic.setEmail(user.getEmail());
                            UserStatic.setEnsisaGroup(user.getEnsisaGroup());
                            UserStatic.setProfile_picture(user.getProfile_picture());
                            UserStatic.setIs_staff(user.getIs_staff());
                            Log.d("TokenActivity", "Token update successful");

                            // Token réactualisé --> on peut passer directement à homeactivity
                            startActivity(new Intent(TokenActivity.this, HomeActivity.class));
                        } else {
                            Log.d("TokenActivity", "Token update failed ");
                            startActivity(new Intent(TokenActivity.this, LoginActivity.class));
                        }
                    }
                }
            }).start();
        }
        else{
            Log.d("TokenActivity","Token not found, going to login/register page...");
            startActivity(new Intent(TokenActivity.this, LoginActivity.class));
        }
        super.onCreate(savedInstanceState);
    }
}
