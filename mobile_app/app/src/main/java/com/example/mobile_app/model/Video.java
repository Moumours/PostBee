package com.example.mobile_app.model;

public class Video extends Document {
    private String video;
    private int duration;

    public Video(String name, String fileType, String filePath, int duration) {
        //super(name, fileType, filePath);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

}
