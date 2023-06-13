package com.example.mobile_app.model;

import android.widget.ImageButton;

public class Comment {
    private String username, content, date;
    private int role, profilePicture;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return this.date; }
    public void setDate(String date) { this.date = date; }

    public int getRole() { return this.role; }
    public void setRole(int username) { this.role = role; }

    public int getProfilePicture() { return profilePicture; }
    public void setProfilePicture(int profilePicture) { this.profilePicture = profilePicture; }

    public Comment(String username, String content, int role, int profilePicture, String date) {
        setUsername(username);
        setContent(content);
        setRole(role);
        setProfilePicture(profilePicture);
        setDate(date);
    }
}
