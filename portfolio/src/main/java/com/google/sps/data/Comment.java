package com.google.sps.data;

// Commment Class Encapsulation to store comment properties in single object
public final class Comment {

    private final String user_name;
    private final String comment_text;
    private final String date;

    public Comment(String user_name, String comment_text, String date) {
        this.user_name = user_name;
        this.comment_text = comment_text;
        this.date = date;

    }

    public String getUserName() {
        return this.user_name;
    }

    public String getCommentText() {
        return this.comment_text;
        
    }

    public String getDate() {
        return this.date;
    }
}
