package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.mobile_app.R;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.item_user.DelatedUser;
import com.example.mobile_app.model.item_user.ItemUserAdapter;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;
    private Button mLogoutButton, mAccountDeleteButton;
    private String mTokenAccess = UserStatic.access;

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
                String currentUserEmail = UserStatic.getEmail();
                DelatedUser currentUser = new DelatedUser(currentUserEmail);
                ItemUserAdapter.deleteUser(currentUser);
                logout();
                Toast.makeText(mContext, "Account Deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton(getString(R.string.ui_cancel), (dialog, id) -> { });
            builder.create();
            builder.show();
        });
    }

    private void logout () {
        Token.clearKeys(SettingsActivity.this);
        Intent intents = new Intent(SettingsActivity.this, LoginActivity.class);
        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intents);
        finish();
    }
}