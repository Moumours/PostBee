package com.example.mobile_app.controller;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mobile_app.model.ResponseData;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.User;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_app.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText nom, prenom, email, password, confirmPassword;
    private RadioGroup statusRadioGroup;
    private Button inscriptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nom = findViewById(R.id.main_edittext_lastname);
        prenom = findViewById(R.id.main_edittext_firstname);
        email = findViewById(R.id.main_edittext_email);
        password = findViewById(R.id.main_edittext_password);
        confirmPassword = findViewById(R.id.main_edittext_confirmPassword);
        statusRadioGroup = findViewById(R.id.main_edittext_statusRadioGroup);
        inscriptionButton = findViewById(R.id.main_button_register);

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomText = nom.getText().toString();
                String prenomText = prenom.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();

                char ensisaGroup = '0';
                int selectedId = statusRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String statut = selectedRadioButton.getText().toString();


                if (statut.equals(getText(R.string.status_student))) {
                    ensisaGroup = '0';
                } else if (statut.equals(getText(R.string.status_teacher))) {
                    ensisaGroup = '1';
                } else {
                    ensisaGroup = '2';
                }

                Log.d("RegisterActivity", "ensisaGroup : " + ensisaGroup);
                String emailComplete = emailText + "@uha.fr";

                if (passwordText.equals(confirmPasswordText)) {
                    User user = new User(nomText, prenomText, emailComplete, passwordText, confirmPasswordText, ensisaGroup);
                    registerUser(user);
                } else {
                    Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(User user) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    ResponseData response;
                    Log.d("RegisterActivity","Attempt to register...");
                    //connectToServer(String endURL, String requestMethod, String token, Object objToSend, Class classToSend, Class classToReceive)
                    response = (ResponseData) Token.connectToServer("register", "POST", null, user, user.getClass(), ResponseData.class);
                    if(response != null) {
                        Log.d("RegisterActivity","Response : Success :"+ response.getSuccess() + " | " + "Message :" + response.getMessage());
                        if (response.getSuccess().equals("True")) {
                            Toast.makeText(RegisterActivity.this, "Inscription réussie, activez votre compte en cliquant sur le lien reçu par mail", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        else {
                            Log.d("RegisterActivity","Error : no server response");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();}
            }
        }).start();
    }

}