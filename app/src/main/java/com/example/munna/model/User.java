package com.example.munna.model;

public class User {
    private String UserId;
    private String Email;
    private String Bvn;

    public User(String userId, String email) {
        UserId = userId;
        Email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBvn() {
        return Bvn;
    }

    public void setBvn(String bvn) {
        Bvn = bvn;
    }
}
