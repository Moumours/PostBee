package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobile_app.R;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;
    private Button mLogoutButton, mAccountDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mContext = getApplicationContext();
        mLogoutButton = findViewById(R.id.settings_button_logout);
        mAccountDeleteButton = findViewById(R.id.settings_button_accountDelete);

        mLogoutButton.setOnClickListener(v -> logout());

        mAccountDeleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(getString(R.string.ui_warning));
            builder.setMessage(getString(R.string.ui_deleteAccountWarning));
            builder.setPositiveButton(getString(R.string.ui_delete), (dialog, id) -> {
                //TODO: Insérer suppression du compte ici
                logout();
                Toast.makeText(mContext, "Account Deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton(getString(R.string.ui_cancel), (dialog, id) -> { });
            builder.create();
            builder.show();
        });
    }

    private void logout () {
        //TODO: Insérer logique de déconnexion au serveur ici
        Intent intents = new Intent(SettingsActivity.this, LoginActivity.class);
        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intents);
        finish();
    }
}