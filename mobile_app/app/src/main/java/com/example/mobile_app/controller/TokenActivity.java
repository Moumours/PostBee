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
import com.example.mobile_app.model.UserStatic;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class TokenActivity extends AppCompatActivity {
    private Token mToken = new Token(null,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = Token.getEncryptedSharedPreferences(TokenActivity.this);
        mToken.setAccess(sharedPreferences.getString("token_access", "null or does not exist"));
        mToken.setRefresh(sharedPreferences.getString("token_refresh", "null or does not exist"));
        Log.d("TokenActivity","Token access démarrage appli : "+mToken.getAccess());
        Log.d("TokenActivity","Token refresh démarrage appli : "+mToken.getRefresh());
        if (!(mToken.getAccess().equals("null or does not exist"))){
            Log.d("TokenActivity","Token found, updating token...");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        UserStatic.setAccess(mToken.getAccess());
                        UserStatic.setRefresh(mToken.getRefresh());
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("refresh", mToken.getRefresh());
                        mToken = (Token.class).cast(Token.connectToServer("refresh_token","POST",UserStatic.getRefresh(),params,params.getClass(), Token.class,null));

                        if (mToken != null){
                            Log.d("TokenActivity","Token update successful");
                            //Stocke les infos du token de manière sécurisée
                            Token.storeToken(TokenActivity.this,mToken);

                            // Token réactualisé --> on peut passer directement à homeactivity
                            Intent i = new Intent(TokenActivity.this, HomeActivity.class);
                            i.putExtra("TOKEN_ACCESS",mToken.getAccess());
                            startActivity(i);
                        }
                        else {
                            Log.d("TokenActivity","Token update failed ");
                            startActivity(new Intent(TokenActivity.this, LoginActivity.class));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
