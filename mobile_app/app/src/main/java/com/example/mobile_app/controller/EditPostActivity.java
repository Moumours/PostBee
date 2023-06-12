package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile_app.R;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Button mButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        mEditTextTitle = findViewById(R.id.editpost_edittext_title);
        mEditTextContent = findViewById(R.id.editpost_edittext_content);
        mButtonSubmit = findViewById(R.id.editpost_button_submit);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEditTextTitle.getText().toString();
                String text = mEditTextContent.getText().toString();

                HashMap<String, String> postData = new HashMap<>();
                postData.put("title", title);
                postData.put("text", text);

                Gson gson = new Gson();
                String json = gson.toJson(postData);

                Log.d("EditPostActivity","Le JSON : " + json);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://postbee.alwaysdata.net/publish");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                            connection.setDoOutput(true);
                            connection.setDoInput(true);

                            OutputStream os = connection.getOutputStream();
                            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                            writer.write(json);
                            writer.flush();
                            writer.close();


                            int responseCode = connection.getResponseCode();
                            Log.d("EditPostActivity", "Response Code: " + responseCode);

                            connection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
