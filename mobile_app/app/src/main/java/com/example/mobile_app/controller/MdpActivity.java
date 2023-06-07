package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mobile_app.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
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
                // Récupérer l'email saisi par l'utilisateur
                String email = editTextEmail.getText().toString();

                // Effectuer le traitement de réinitialisation du mot de passe ici

                // Afficher un message à l'utilisateur pour indiquer que le traitement a été effectué
                Toast.makeText(MdpActivity.this, "Le mot de passe a été réinitialisé.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}