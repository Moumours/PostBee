package com.example.postbee;

import java.util.Date;
import java.util.Calendar;

public class Comment {
    private Integer commentId;
    private String username;
    private String commentContent;
    private Date date;

    public Comment(Integer commentId, String username, String commentContent, Date date) {
        this.commentId = commentId;
        this.username = username;
        this.commentContent = commentContent;
        this.date = date;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCommentContentEmpty() {
        return commentContent == null || commentContent.length() == 0;
    }

    public String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return String.format("%02d/%02d/%04d %02d:%02d:%02d", day, month, year, hour, minute, second);
    }
}
