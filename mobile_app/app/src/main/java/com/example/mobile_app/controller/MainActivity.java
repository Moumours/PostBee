package com.example.mobile_app.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

                int selectedId = statusRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String statut = selectedRadioButton.getText().toString();


                Toast.makeText(MainActivity.this, "Inscription réussie!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}