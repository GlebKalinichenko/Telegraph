package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;

/**
 * Created by Gleb on 30.12.2015.
 */
public class Mail {
    private String sender;
    private String nameSender;
    private String receiver;
    private String subject;
    private String content;
    private String date;
    private boolean hasAttach;

    public Mail(String sender, String nameSender, String receiver, String subject, String content, String date, boolean hasAttach) {
        this.sender = sender;
        this.nameSender = nameSender;
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.hasAttach = hasAttach;
    }

    /**
     * Add mail to database
     * @param SQLiteDatabase        Database
     * @param int                   Id user from table Users for email sender
     * @param int                   Id folder
     * @param int                   Type mail 0 is sent mail 1 is received mail
     * @return void
     * */
    public void addMail(SQLiteDatabase sdb, int userCode, int folderCode, int straightIndex){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SENDER_USER_CODE, userCode);
        values.put(DatabaseHelper.NAME_SENDER, this.nameSender);
        values.put(DatabaseHelper.RECEIVER, this.receiver);
        values.put(DatabaseHelper.SUBJECT, this.subject);
        values.put(DatabaseHelper.CONTENT, this.content);
        values.put(DatabaseHelper.DATE, this.date);
        values.put(DatabaseHelper.FOLDER_CODE, folderCode);
        values.put(DatabaseHelper.HAS_ATTACH, this.hasAttach);
        values.put(DatabaseHelper.STRAIGHT_INDEX, straightIndex);
        sdb.insert(DatabaseHelper.TABLE_MAILS, null, values);
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHasAttach() {
        return hasAttach;
    }

    public void setHasAttach(boolean hasAttach) {
        this.hasAttach = hasAttach;
    }
}
