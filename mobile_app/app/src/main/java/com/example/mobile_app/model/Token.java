package com.example.mobile_app.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
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

    public static Object connectToServer(String endURL, String requestMethod, String token, Object objToSend, Class classToSend, Class classToReceive, Type typeToReceive){;
        Object objToReceive = null;
        try {
            //URL url = new URL("http://10.39.251.162:8000/"+endURL);
            URL url = new URL("http://postbee.alwaysdata.net/"+endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            Log.d("ViewPostActivity","TOKENNN in con : " + token);
            if(token != null){

                Log.d("connectToServer", "Token used : "+ token);
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            if(requestMethod.equals("POST")) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
            }
            Log.d("connectToServer", "Connecting to "+url+" with method \"" + requestMethod + "\"");

            //Send data to the server
            if (objToSend != null) {
                Gson gson = new Gson();
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(gson.toJson((classToSend).cast(objToSend)));

                Log.d("connectToServer", "JSON sent to the server: " + gson.toJson((classToSend).cast(objToSend)));

                os.flush();
                os.close();
            }

            int respCode = conn.getResponseCode();
            Log.d("connectToServer", "Response code from the server : " + respCode);
            Log.d("connectToServer", "Response message from the server : " + conn.getResponseMessage());
            if(respCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String rawPostData = response.toString();
                Log.d("connectToServer", "JSON received from the server: " + rawPostData);

                Gson gsonreceiving = new Gson();
                if(classToReceive != null) {
                    objToReceive = gsonreceiving.fromJson(rawPostData, classToReceive);
                }
                else if (typeToReceive != null){
                    objToReceive = gsonreceiving.fromJson(rawPostData, typeToReceive);
                }
                Log.d("connectToServer", "Object received");
            }
            else if(respCode == HttpURLConnection.HTTP_ACCEPTED){
                Log.d("connectToServer","Connected to server but data sent is invalid");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String rawPostData = response.toString();

                Gson gsonerrormsg = new Gson();
                HashMap<String, String> params = new HashMap<String, String>();
                params = gsonerrormsg.fromJson(rawPostData, params.getClass());

                Log.d("connectToServer","Error message : "+params.get("error"));
                UserStatic.setMessage(params.get("error"));
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objToReceive;
    }
}

//conn.setRequestProperty("Content-Type", "multipart/form-data");


