package com.example.a4322_term_project;

import android.app.Application;

public class Key extends Application {

    private String key;

    public String getKey() {
        return key;
    }
    public void setToken(String token) {
        this.key = token;
    }

}
