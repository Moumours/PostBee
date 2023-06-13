package com.example.mobile_app.model;

public class Document {
    private String name;
    private String fileType;
    private String filePath;

    public Document(String name, String fileType, String filePath) {
        this.name = name;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
