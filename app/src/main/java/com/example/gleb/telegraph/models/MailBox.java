package com.example.gleb.telegraph.models;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailBox {
    private String email;
    private String password;

    public MailBox(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
