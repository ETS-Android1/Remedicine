package com.iti.mad42.remedicine.Model.pojo;

public class CurrentUser {

    private static CurrentUser instance = null;
    private String email;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
