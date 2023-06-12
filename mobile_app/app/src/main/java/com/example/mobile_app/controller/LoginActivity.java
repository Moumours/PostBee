package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.ViewPost;
import com.google.gson.Gson;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonForgotPassword;
    private Button buttonRegister;
    private Token mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        // Gestion du token
        SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
        mToken.setAccess(sharedPreferences.getString("token_access", "null or does not exist"));
        if (!(mToken.getAccess().equals("null or does not exist"))){
            // Le token existe --> on le refresh voir s'il n'est pas périmé
            new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL(""); // Attention adresse à compléter : http://postbee.alwaysdata.net/refreshtoken
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        Gson gson = new Gson();
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("access", mToken.getAccess());

                        Log.d("TokenAccessData", "Token Access data JSON sent to the server: " + gson.toJson(params)); // Add this line

                        try (DataOutputStream os = new DataOutputStream(conn.getOutputStream())) {
                            os.writeBytes(gson.toJson(params));
                            os.flush();
                            Log.d("MainActivity", "Requête envoyée avec succès");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("MainActivity", "Erreur lors de l'envoi de la requête : " + e.getMessage());
                        }

                        int responseCode = conn.getResponseCode();
                        Log.d("MainActivity", "Code de réponse : " + responseCode);

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        Log.d("LoginData", "HTTP response code: " + conn.getResponseCode()); // Add this line

                        String rawPostData = response.toString();

                        Gson gsonreceiving = new Gson();
                        mToken = gsonreceiving.fromJson(rawPostData, Token.class);
                        if (!(mToken.getAccess().equals(""))) { // Attention : que renvoie le serveur si le token est expiré ?
                            // Le token est réactualisé
                            Log.d("LoginData", "Token reçu, token access : " + mToken.getAccess());
                            Log.d("LoginData", "Token reçu, token refresh : " + mToken.getRefresh());

                            //Stocke les infos du token de manière sécurisée
                            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token_access", mToken.getAccess());
                            editor.putString("token_refresh", mToken.getRefresh());
                            editor.apply();
                            editor.commit();

                            Log.d("LoginData", "Token enregistré, token access : " + sharedPreferences.getString("token_access", "null or does not exist"));
                            Log.d("LoginData", "Token enregistré, token refresh : " + sharedPreferences.getString("token_refresh", "null or does not exist"));

                            // Token réactualisé --> on peut passer directement à homeactivity
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        };
        */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.login_edittext_email);
        editTextPassword = findViewById(R.id.login_edittext_password);
        buttonLogin = findViewById(R.id.login_button_login);
        buttonForgotPassword = findViewById(R.id.login_button_forgotPassword);
        buttonRegister = findViewById(R.id.login_button_register);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                sendLoginDataToServer (email,password);

                Toast.makeText(LoginActivity.this, "Connexion : " + email + ", " + password, Toast.LENGTH_SHORT).show();
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "Mot de passe oublié", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MdpActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "Inscription", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }

        });

    }

    public void sendLoginDataToServer(String email, String password) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://postbee.alwaysdata.net/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Gson gson = new Gson();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(gson.toJson(params));

                    Log.d("LoginData", "Login JSON sent to the server: " + gson.toJson(params)); // Add this line

                    os.flush();
                    os.close();

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String rawPostData = response.toString();

                        Gson gsonreceiving = new Gson();
                        mToken = gsonreceiving.fromJson(rawPostData, Token.class);
                        Log.d("LoginData", "Token reçu, token access : " + mToken.getAccess());
                        Log.d("LoginData", "Token reçu, token refresh : " + mToken.getRefresh());

                        //Stocke les infos du token de manière sécurisée
                        SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token_access", mToken.getAccess());
                        editor.putString("token_refresh", mToken.getRefresh());
                        editor.apply();
                        editor.commit();

                        Log.d("LoginData", "Token enregistré, token access : " + sharedPreferences.getString("token_access", "null or does not exist"));
                        Log.d("LoginData", "Token enregistré, token refresh : " + sharedPreferences.getString("token_refresh", "null or does not exist"));

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                    Log.d("LoginData", "HTTP response code: " + conn.getResponseCode()); // Add this line


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    MasterKey getMasterKey() {
        try {
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    "_androidx_security_master_key_",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            return new MasterKey.Builder(LoginActivity.this)
                    .setKeyGenParameterSpec(spec)
                    .build();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error on getting master key", e);
        }
        return null;
    }

    private SharedPreferences getEncryptedSharedPreferences() {
        try {
            return EncryptedSharedPreferences.create(
                    LoginActivity.this,
                    "Your preference file name",
                    getMasterKey(), // calling the method above for creating MasterKey
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error on getting encrypted shared preferences", e);
        }
        return null;
    }

    public void someFunction(){
        SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
        //Used to add new entries and save changes
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //To add entry to your shared preferences file
        editor.putString("Name", "Value");
        editor.putBoolean("Name", false);
        //Apply changes and commit
        editor.apply();
        editor.commit();

        //To clear all keys from your shared preferences file
        editor.clear().apply();

        //To get a value from your shared preferences file
        String returnedValue  = sharedPreferences.getString("Name", "Default value if null is returned or the key doesn't exist");
    }

}