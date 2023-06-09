package com.example.mobile_app.model;

public class Author {
    private String firstname;
    private String lastname;

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getFullname () { return getFirstname() + " " + getLastname(); }
    public String getFullnameInverted () { return getLastname() + " " + getFirstname(); }

    public Author (String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
