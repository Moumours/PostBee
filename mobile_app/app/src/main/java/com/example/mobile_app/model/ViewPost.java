package com.example.mobile_app.model;

import android.util.Log;
import android.view.View;

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
    private String text;
    private List<Document> listdocument;
    private List<com.example.mobile_app.model.Comment> listcomment;

    public ViewPost(String text, List<Document> listdocument, List<Comment> listcomment) {
        this.text = text;
        this.listdocument = listdocument;
        this.listcomment = listcomment;
    }

    public String getText() {
        return text;
    }

    public List<Document> getListdocument() {
        return listdocument;
    }

    public List<Comment> getListcomment() {
        return listcomment;
    }
}
