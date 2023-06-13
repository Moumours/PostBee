package com.example.mobile_app.model;
public class User {
    private String success;
    private String message;
    private String access;
    private String refresh;
    private String first_name;
    private String last_name;
    private String email;
    private int ensisaGroup;
    private int profile_picture;
    private String is_staff;

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public int getEnsisaGroup() {
        return ensisaGroup;
    }

    public int getProfile_picture() {
        return profile_picture;
    }

    public String getIs_staff() {
        return is_staff;
    }
}

