package com.chung.jay.avocadoh.model;

import lombok.Getter;

/**
 * Created by jaychung on 5/29/16.
 */
public class User {
    @Getter
    private String username;
    @Getter
    private String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
