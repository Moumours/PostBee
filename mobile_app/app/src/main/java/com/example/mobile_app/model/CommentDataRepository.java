package com.example.mobile_app.model;

import java.util.ArrayList;
import java.util.List;

public class CommentDataRepository {
    private static CommentDataRepository instance;
    private List<com.example.postbee.Comment> commentList;

    CommentDataRepository() {
        commentList = new ArrayList<>();
    }

    public static CommentDataRepository getInstance() {
        if (instance == null) {
            instance = new CommentDataRepository();
        }
        return instance;
    }

    public List<com.example.postbee.Comment> getComments() {
        return commentList;
    }

    public void setComments(List<com.example.postbee.Comment> comments) {
        commentList.clear();
        if (comments != null) {
            commentList.addAll(comments);
        }
    }

    public void addComment(com.example.postbee.Comment comment) {
        commentList.add(comment);
    }

    public void deleteComment(com.example.postbee.Comment comment) {
        commentList.remove(comment);
    }
}
