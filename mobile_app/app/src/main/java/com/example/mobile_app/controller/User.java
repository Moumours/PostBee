package com.example.mobile_app.controller;

public class User {

    public User(String firstName, String lastName, String email, String password1,String password2,int ensisaGroup) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.ensisaGroup = ensisaGroup;
    }

    private int ensisaGroup;
    private String first_name;
    private String last_name;
    private String email;
    private String password1;
    private String password2;

    public String getFirst_name() {
        return first_name;
    }

    public int getEnsisaGroup() {
        return ensisaGroup;
    }
}

