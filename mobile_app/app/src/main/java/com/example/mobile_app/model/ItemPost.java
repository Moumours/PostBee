package com.example.mobile_app.model;

public class ItemPost {
    private int id;
    private String title;
    private String author;
    private String date;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


    public ItemPost(int id, String title, String author, String date) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
    }
}
