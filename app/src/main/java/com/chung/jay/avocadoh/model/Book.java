package com.chung.jay.avocadoh.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by jaychung on 5/20/16.
 */
public class Book {
    @Getter
    private String uid;
    @Getter
    private String author;
    @Getter
    private String name;
    @Getter
    private String summary;
    @Getter
    private String ownerName;
    @Getter
    private int pageNum;
    @Getter
    private String photoUrl;

    //owners
    @Getter
    private List<String> borrowers = new ArrayList<>();

    @Getter
    @Setter
    private int starCount = 0;
    @Getter
    private Map<String, Boolean> stars = new HashMap<>();

    public Book() {
    }

    public Book(String uid, String ownerName, String author, String name, int pageNum, String summary, String photoUrl) {
        this.uid = uid;
        this.author = author;
        this.name = name;
        this.summary = summary;
        this.ownerName = ownerName;
        this.pageNum = pageNum;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("name", name);
        result.put("summary", summary);
        result.put("owner", ownerName);
        result.put("pageNum", pageNum);
        result.put("photoUrl", photoUrl);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}
