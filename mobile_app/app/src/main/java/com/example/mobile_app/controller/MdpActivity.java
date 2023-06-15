package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mobile_app.R;
import com.example.mobile_app.model.ResponseData;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UserStatic;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

public class MdpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdp);
        editTextEmail = findViewById(R.id.resetpassword_edittext_email);
        buttonResetPassword = findViewById(R.id.resetpassword_button_reset);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                askToRestPassword(email);
                Toast.makeText(MdpActivity.this, "Un email de réinitialisation de mot de passe vous a été envoyé", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void askToRestPassword(String email) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    ResponseData response = (ResponseData) Token.connectToServer("reset_password", "POST", UserStatic.getAccess(),params, params.getClass(), null,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}