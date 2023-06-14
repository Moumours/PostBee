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
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("first_name",prenomText);
                    params.put("last_name",nomText);
                    params.put("email",emailComplete);
                    params.put("ensisaGroup", String.valueOf(ensisaGroup));
                    params.put("password1",passwordText);
                    params.put("password2",confirmPasswordText);
                    registerUser(params);
                } else {
                    Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(HashMap<String, String> params) {
        new Thread(new Runnable() {
            public void run() {
                ResponseData response = null;
                try {
                    response = (ResponseData) Token.connectToServer("register", "POST", null, params, params.getClass(), ResponseData.class,null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ResponseData finalResponse = response;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(finalResponse != null) {
                                Log.d("RegisterActivity","Response : Success :"+ finalResponse.getSuccess() + " | " + "Message :" + finalResponse.getMessage());
                                if (finalResponse.getSuccess().equals("True")) {
                                    Toast.makeText(RegisterActivity.this, "Inscription réussie, activez votre compte en cliquant sur le lien reçu par mail", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Erreur : "+ finalResponse.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Log.d("RegisterActivity","Error : no server response");
                            }
                        }
                    });
                }
            }
        }).start();
    }

}