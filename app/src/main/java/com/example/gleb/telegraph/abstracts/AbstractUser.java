package com.example.gleb.telegraph.abstracts;

/**
 * Created by Gleb on 30.12.2015.
 */
public abstract class AbstractUser {
    protected String email;

    public AbstractUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
