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
    private String receiveProtocol;
    private String sendProtocol;

    public MailBox(String email, String password, String receiveProtocol, String sendProtocol) {
        this.email = email;
        this.password = password;
        this.receiveProtocol = receiveProtocol;
        this.sendProtocol = sendProtocol;
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
    public long addAccount(SQLiteDatabase sdb){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMAIL_ACCOUNT, this.email);
        values.put(DatabaseHelper.PASSWORD_ACCOUNT, this.password);
        values.put(DatabaseHelper.RECEIVE_PROTOCOL, this.receiveProtocol);
        return sdb.insert(DatabaseHelper.TABLE_MAIL_BOXES, null, values);
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

    /**
     * Get index of inserted account in table mail box by email of account
     * @param SQLiteDatabase        Database
     * @param String                Email of account
     * @return int                  Index of inserted account
     * */
    public static int getAccountByName(SQLiteDatabase sdb, String email){
        String query = "SELECT IdMailBox from MailBoxes where Email='" + email + "'";
        Cursor cursor = sdb.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return 0;
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

    public String getReceiveProtocol() {
        return receiveProtocol;
    }

    public void setReceiveProtocol(String receiveProtocol) {
        this.receiveProtocol = receiveProtocol;
    }

    public String getSendProtocol() {
        return sendProtocol;
    }

    public void setSendProtocol(String sendProtocol) {
        this.sendProtocol = sendProtocol;
    }
}
