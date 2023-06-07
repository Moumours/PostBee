package com.example.mobile_app.controller;
import java.util.List;

public class Poste {

    private String title;
    private String text;
    private String status;
    private List<String> images;
    private List<String> videos;

    public Poste (String title, String text, List<String> images, List<String> videos) {
        this.title = title;
        this.text = text;
        this.status = "en attente";
        this.images = images;
        this.videos = videos;
    }

    public Poste (String title, String text, List<String> images) {
        this.title = title;
        this.text = text;
        this.status = "en attente";
        this.images = images;
    }

    public Poste (String title, List<String> images) {
        this.title = title;
        this.status = "en attente";
        this.images = images;
    }

    public Poste (String title, String text) {
        this.title = title;
        this.text = text;
        this.status = "en attente";
    }

    public String getTitle() {
        return title;
    }
    public String getText() {
        return text;
    }
    public String getStatus() {
        return status;
    }
    public List<String> getImages() {
        return images;
    }
    public List<String> getVideos() {
        return videos;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
