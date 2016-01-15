package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;

import java.io.Serializable;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailBox implements Serializable {
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
        email = email.substring(beginChar);
        return email;
    }

    /**
     * Add account information to database
     * @param SQLiteDatabase        Database
     * @return void
     * */
    public void addAccount(SQLiteDatabase sdb){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMAIL_ACCOUNT, this.email);
        values.put(DatabaseHelper.PASSWORD_ACCOUNT, this.password);
        sdb.insert(DatabaseHelper.TABLE_MAIL_BOXES, null, values);
    }

    /**
     * Get last inserted index in table mail box
     * @param SQLiteDatabase        Database
     * @return int                  Last inserted index
     * */
    public static int getLastAccount(SQLiteDatabase sdb){
        String query = "SELECT IdMailBox from MailBoxes order by IdMailBox DESC limit 1";
        Cursor cursor = sdb.rawQuery(query, null);
        cursor.moveToLast();
        int boxCode = cursor.getInt(0);
        return boxCode;
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
