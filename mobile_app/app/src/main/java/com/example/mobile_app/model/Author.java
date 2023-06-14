package com.example.mobile_app.model;

public class Author {
    private String first_name;
    private String last_name;
    private String full_name;

    public String getFullname() {
        if (this.full_name == null || this.full_name.isEmpty()) {
            return this.first_name + " " + this.last_name;
        } else {
            return this.full_name;
        }
    }

    public void setFullname(String fullname) {
        this.full_name = fullname;
    }

    public String getFirstname() {
        return first_name;
    }

    public void setFirstname(String firstname) {
        this.first_name = firstname;
    }

    public String getLastname() {
        return last_name;
    }

    public void setLastname(String lastname) {
        this.last_name = lastname;
    }

    public Author(String firstname, String lastname) {
        this.first_name = firstname;
        this.last_name = lastname;
        this.full_name = firstname + " " + lastname;
    }

    public Author(String fullname) {
        this.full_name = fullname;
    }
}
