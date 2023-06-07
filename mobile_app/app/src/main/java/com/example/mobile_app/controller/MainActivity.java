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
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_app.R;

public class MainActivity extends AppCompatActivity {
    private EditText nom, prenom, email, password, confirmPassword;
    private RadioGroup statusRadioGroup;
    private Button inscriptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.main_edittext_nom);
        prenom = findViewById(R.id.main_edittext_prenom);
        email = findViewById(R.id.main_edittext_email);
        password = findViewById(R.id.main_edittext_password);
        confirmPassword = findViewById(R.id.main_edittext_confirmPassword);
        statusRadioGroup = findViewById(R.id.main_edittext_statusRadioGroup);
        inscriptionButton = findViewById(R.id.main_edittext_inscriptionButton);

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomText = nom.getText().toString();
                String prenomText = prenom.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();

                int ensisaGroup = 0;
                int selectedId = statusRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String statut = selectedRadioButton.getText().toString();


                if (statut.equals("Etudiant")) {
                    ensisaGroup = 0;
                } else if (statut.equals("Professeur")) {
                    ensisaGroup = 1;
                } else {
                    ensisaGroup = 2;
                }

                String emailComplete = emailText + "@uha.fr";

                if (passwordText.equals(confirmPasswordText)) {
                    User user = new User(nomText, prenomText, emailComplete, passwordText, confirmPasswordText, ensisaGroup);
                    registerUser(user);
                    Toast.makeText(MainActivity.this, "Inscription réussie!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, String.valueOf(user.getEnsisaGroup()), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, user.getFirst_name(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Les mots de passe ne correspondent pas!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void registerUser(User user) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.117.21.10:8000/register");
                    HttpURLConnection django = (HttpURLConnection) url.openConnection();

                    django.setRequestMethod("POST");
                    django.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    django.setRequestProperty("Accept","application/json");
                    django.setDoOutput(true);
                    django.setDoInput(true);

                    Gson gson = new Gson();
                    String jsonUser = gson.toJson(user);

                    System.out.println(jsonUser);

                    Log.d("MainActivity", "Envoi de la requête d'inscription");

                    try (OutputStreamWriter os = new OutputStreamWriter(django.getOutputStream(), "UTF-8")) {
                        os.write(jsonUser);
                        os.flush();
                        Log.d("MainActivity", "Requête envoyée avec succès");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MainActivity", "Erreur lors de l'envoi de la requête : " + e.getMessage());
                    }

                    int responseCode = django.getResponseCode();
                    Log.d("MainActivity", "Code de réponse : " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(django.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d("MainActivity", "Réponse du serveur : " + response.toString());


                    System.out.println(response.toString());

                    django.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();}
            }
        }).start();
    }

}