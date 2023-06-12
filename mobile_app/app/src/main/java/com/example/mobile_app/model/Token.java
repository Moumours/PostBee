package com.example.mobile_app.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.mobile_app.controller.HomeActivity;
import com.example.mobile_app.controller.LoginActivity;
import com.example.mobile_app.controller.TokenActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Token implements Serializable {
    private String access;
    private String refresh;

    public Token(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    static MasterKey getMasterKey(Context c) {
        try {
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    "_androidx_security_master_key_",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            return new MasterKey.Builder(c)
                    .setKeyGenParameterSpec(spec)
                    .build();
        } catch (Exception e) {
            Log.e(c.getClass().getSimpleName(), "Error on getting master key", e);
        }
        return null;
    }

    public static SharedPreferences getEncryptedSharedPreferences(Context c) {
        try {
            return EncryptedSharedPreferences.create(
                    c,
                    "Your preference file name",
                    getMasterKey(c), // calling the method above for creating MasterKey
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e(c.getClass().getSimpleName(), "Error on getting encrypted shared preferences", e);
        }
        return null;
    }

    public void someFunction(Context c){
        SharedPreferences sharedPreferences = getEncryptedSharedPreferences(c);
        //Used to add new entries and save changes
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //To add entry to your shared preferences file
        editor.putString("Name", "Value");
        editor.putBoolean("Name", false);
        //Apply changes and commit
        editor.apply();
        editor.commit();

        //To clear all keys from your shared preferences file
        editor.clear().apply();

        //To get a value from your shared preferences file
        String returnedValue  = sharedPreferences.getString("Name", "Default value if null is returned or the key doesn't exist");
    }

    public static void clearKeys(Context c){
        SharedPreferences sharedPreferences = getEncryptedSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    public static void storeToken(Context c,Token t){
        SharedPreferences sharedPreferences = getEncryptedSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token_access", t.getAccess());
        editor.putString("token_refresh", t.getRefresh());
        editor.apply();
        editor.commit();
    }
}


