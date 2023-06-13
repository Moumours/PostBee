package com.example.mobile_app.model;

public class Comment {
    private String commentText;
    private String author;
    private String fullName;
    private String profilePicture;
    private String date;

    public Comment(String commentText, String author, String fullName, String profilePicture, String date) {
        this.commentText = commentText;
        this.author = author;
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.date = date;
    }

    public String getText() {
        return commentText;
    }

    public String getAuthor() {
        return author;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getDate() {
        return date;
    }

}
