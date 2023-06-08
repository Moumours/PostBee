package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mobile_app.R;
import com.google.gson.Gson;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MdpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdp);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                askToRestPassword(email);

                Toast.makeText(MdpActivity.this, "Le mot de passe a été réinitialisé.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void askToRestPassword(String email) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http:// /reset_password");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    Gson gson = new Gson();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(gson.toJson(params));

                    os.flush();
                    os.close();

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}