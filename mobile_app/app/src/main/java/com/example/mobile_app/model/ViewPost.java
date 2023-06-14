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
    private List<com.example.mobile_app.model.Comment> comments;
    private List<Image> images;
    private List<Video> videos;

    public String getText() {
        return text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
