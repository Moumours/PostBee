package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobile_app.R;
import com.google.gson.Gson;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonForgotPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

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
                Toast.makeText(LoginActivity.this, "Mot de passe oublié", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MdpActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Inscription", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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

                    Log.d("LoginData", "HTTP response code: " + conn.getResponseCode()); // Add this line


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}