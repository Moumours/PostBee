package com.example.mobile_app.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UploadPost {
    private final String title;
    private final String text;
    private final List<String> attachments;


    public UploadPost(String title, String text, List<String> attachments) {
        this.title = title;
        this.text = text;
        this.attachments = attachments;
    }
}

//Attribut
//private List<Document> listedocument;

//Constructeur
//List<Document> listedocument
//this.listedocument = listedocument;