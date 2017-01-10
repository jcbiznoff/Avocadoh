package com.chung.jay.avocadoh.model;

/**
 * Created by jaychung on 5/29/16.
 */
public class Comment {
    public String uid;
    public String author;
    public String text;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }
}
