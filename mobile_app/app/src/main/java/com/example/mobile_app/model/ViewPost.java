package com.example.mobile_app.model;

import com.example.mobile_app.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ViewPost {
    private String content;
    private List<Document> listdocument;
    private List<com.example.postbee.Comment> listcomment;

    public ViewPost(int id, int status) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.39.251.162:8000/test");
                    HttpURLConnection django = (HttpURLConnection) url.openConnection();

                    django.setRequestMethod("GET");
                    django.setRequestProperty("Accept","application/json");

                    BufferedReader in = new BufferedReader(new InputStreamReader(django.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    String rawPostData = response.toString();

                    Gson gson = new Gson();
                    Type type = new TypeToken<ViewPost>(){}.getType();
                    ViewPost viewpost = gson.fromJson(rawPostData, type);

                    django.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getContent() {
        return content;
    }

    public List<Document> getListdocument() {
        return listdocument;
    }

    public List<com.example.postbee.Comment> getListcomment() {
        return listcomment;
    }
}
