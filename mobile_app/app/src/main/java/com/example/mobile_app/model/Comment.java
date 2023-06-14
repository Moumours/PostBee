package com.example.mobile_app.model;

import android.widget.ImageButton;

public class Comment {
    private String text;
    private Author author; // full_name et profile_picture
    private String date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
