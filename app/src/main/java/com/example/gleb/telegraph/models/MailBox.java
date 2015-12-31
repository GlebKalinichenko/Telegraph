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

    /**
     * Parse email for get name of post server
     * @param String        Email of post account
     * @return String       Name of post server
     * */
    public static String parseEmail(String email){
        int beginChar = email.indexOf("@") + 1;
        int endChar = email.indexOf(".");
        email = email.substring(beginChar, endChar);
        return email;
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
