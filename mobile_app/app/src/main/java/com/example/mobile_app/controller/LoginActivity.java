package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.User;
import com.example.mobile_app.model.UserStatic;
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
import java.io.IOException;
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
    private Token mToken = new Token(null,null);
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                sendLoginDataToServer(email,password);
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
                User user = null;
                try {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    user = (User.class).cast(Token.connectToServer("login", "POST", null, params, params.getClass(), User.class, null));
                    if (user != null) {Log.d("LogInActivity","user : "+ user.getFirst_name() + user.getEmail() + user.getAccess() + user.getIs_staff() + user.getLast_name());}

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    User finalUser = user;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(finalUser != null) {
                                    Log.d("LoginActivity","finalUser : "+finalUser.getFirst_name());
                                    Log.d("LoginActivity","Sauvegarde des tokens et mise à jour des variables statiques");
                                    Token.storeToken(LoginActivity.this,new Token(finalUser.getAccess(),finalUser.getRefresh()));
                                    UserStatic.setAccess(finalUser.getAccess());
                                    UserStatic.setRefresh(finalUser.getRefresh());
                                    UserStatic.setFirst_name(finalUser.getFirst_name());
                                    UserStatic.setLast_name(finalUser.getLast_name());
                                    UserStatic.setEmail(finalUser.getEmail());
                                    UserStatic.setEnsisaGroup(finalUser.getEnsisaGroup());
                                    UserStatic.setProfile_picture(finalUser.getProfile_picture());
                                    UserStatic.setIs_staff(finalUser.getIs_staff());
                                    Log.d("LoginActivity","UserStatic : "+UserStatic.getFirst_name());
                                    Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }
                            else {
                                Toast.makeText(LoginActivity.this, "Erreur : "+ UserStatic.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
}

