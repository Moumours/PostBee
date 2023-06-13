package com.example.mobile_app.model;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class User {

    public User(String firstName, String lastName, String email, String password1,String password2,char ensisaGroup) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.ensisaGroup = ensisaGroup;
    }

    private char ensisaGroup;
    private String first_name;
    private String last_name;
    private String email;
    private String password1;
    private String password2;

    public String getFirst_name() {
        return first_name;
    }

    public char getEnsisaGroup() {
        return ensisaGroup;
    }
}

