package com.example.mobile_app.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UploadPost {
    private String title;
    private String author;
    private String contenu;
    private List<Document> listedocument;

    public UploadPost(String title, String author, String contenu, List<Document> listedocument) {
        this.title = title;
        this.author = author;
        this.contenu = contenu;
        this.listedocument = listedocument;
    }

    public void sendtoserver(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.39.251.162:8000/test");
                    HttpURLConnection django = (HttpURLConnection) url.openConnection();

                    django.setRequestMethod("POST");
                    django.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    django.setRequestProperty("Accept","application/json");
                    django.setDoOutput(true);
                    django.setDoInput(true);

                    Gson gson = new Gson();
                    String jsonUploadPost = gson.toJson(this);

                    System.out.println(jsonUploadPost);

                    try(OutputStreamWriter os = new OutputStreamWriter(django.getOutputStream(), "UTF-8")) {
                        os.write(jsonUploadPost);
                        os.flush();
                    }

                    int responseCode = django.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(django.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println(response.toString());

                    django.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();}
            }
        }).start();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public List<Document> getListedocument() {
        return listedocument;
    }

    public void setListedocument(List<Document> listedocument) {
        this.listedocument = listedocument;
    }
}
