package com.example.mobile_app.model.item_post;

import com.example.mobile_app.model.Author;

public class ItemPost {
    private int id;
    private String title;
    private Author author;
    private String date;
    private char status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


    public ItemPost(int id, String title, Author author, String date, char status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.status = status;
    }
}
