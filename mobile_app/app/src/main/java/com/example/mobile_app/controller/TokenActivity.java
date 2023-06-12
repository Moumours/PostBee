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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TokenActivity extends AppCompatActivity {
    private Token mToken = new Token(null,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = Token.getEncryptedSharedPreferences(TokenActivity.this);
        mToken.setAccess(sharedPreferences.getString("token_access", "null or does not exist"));
        Log.d("Token démarrage appli",mToken.getAccess());
        if (!(mToken.getAccess().equals("null or does not exist"))){
            // Le token existe --> on le refresh voir s'il n'est pas périmé
            new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL("http://postbee.alwaysdata.net/refresh_token");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Authorization", "Bearer " + mToken.getAccess());

                        int responseCode = conn.getResponseCode();
                        Log.d("TokenActivity","Code de réponse : "+responseCode);


                        if (conn.getResponseCode()==200) { // Token ok
                            Log.d("TokenActivity","Token approuvé");
                            StringBuffer rep = new StringBuffer();
                            System.out.println(rep.toString());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();

                            String rawPostData = response.toString();
                            Log.d("TokenActivity","rawPostData : "+rawPostData);

                            Gson gsonreceiving = new Gson();
                            mToken = gsonreceiving.fromJson(rawPostData, Token.class);

                            //Stocke les infos du token de manière sécurisée
                            Token.storeToken(TokenActivity.this,mToken);

                            // Token réactualisé --> on peut passer directement à homeactivity
                            Intent i = new Intent(TokenActivity.this, HomeActivity.class);
                            i.putExtra("TOKEN",mToken.getAccess());
                            startActivity(i);
                        }
                        else {
                            Log.d("TokenActivity","Token expiré");
                            startActivity(new Intent(TokenActivity.this, LoginActivity.class));
                        }
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else{
            startActivity(new Intent(TokenActivity.this, LoginActivity.class));
        }
        super.onCreate(savedInstanceState);
    }
}
