package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
    private int hasAttach;
    private int straightIndex;

    public Mail(String sender, String nameSender, String receiver, String subject, String content, String date, int hasAttach, int straightIndex) {
        this.sender = sender;
        this.nameSender = nameSender;
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.hasAttach = hasAttach;
        this.straightIndex = straightIndex;
    }

    /**
     * Add mail to database
     * @param SQLiteDatabase        Database
     * @param int                   Id user from table Users for email sender
     * @param int                   Id folder
     * @param int                   Type mail 0 is sent mail 1 is received mail
     * @return void
     * */
    public void addMail(SQLiteDatabase sdb, int userCode, int folderCode){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SENDER_USER_CODE, userCode);
        values.put(DatabaseHelper.NAME_SENDER, this.nameSender);
        values.put(DatabaseHelper.RECEIVER, this.receiver);
        values.put(DatabaseHelper.SUBJECT, this.subject);
        values.put(DatabaseHelper.CONTENT, this.content);
        values.put(DatabaseHelper.DATE, this.date);
        values.put(DatabaseHelper.FOLDER_CODE, folderCode);
        values.put(DatabaseHelper.HAS_ATTACH, this.hasAttach);
        values.put(DatabaseHelper.STRAIGHT_INDEX, this.straightIndex);
        sdb.insert(DatabaseHelper.TABLE_MAILS, null, values);
    }

    /**
     * Select all mails
     * @param SQLiteDatabase        Database
     * @return List<Mail>           List of mails
     * */
    public static List<Mail> selectAllMail(SQLiteDatabase sdb){
        List<Mail> mails = new ArrayList<>();
        Cursor cursor = sdb.query(DatabaseHelper.TABLE_MAILS,
                new String[]{DatabaseHelper.ID_MAIL, DatabaseHelper.SENDER_USER_CODE,
                DatabaseHelper.NAME_SENDER, DatabaseHelper.RECEIVER, DatabaseHelper.SUBJECT,
                DatabaseHelper.CONTENT, DatabaseHelper.DATE, DatabaseHelper.FOLDER_CODE,
                DatabaseHelper.HAS_ATTACH, DatabaseHelper.STRAIGHT_INDEX},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int senderCode = cursor.getInt(1);
                Cursor cursorUser = sdb.query(DatabaseHelper.TABLE_USERS, new String[]{
                        DatabaseHelper.EMAIL_USER}, DatabaseHelper.ID_USER + "=?",
                        new String[]{String.valueOf(senderCode)}, null, null, null, null);
                if (cursorUser != null && cursorUser.moveToFirst()) {
                    String sender = cursorUser.getString(0);
                    String namesender = cursor.getString(2);
                    String receiver = cursor.getString(3);
                    String subject = cursor.getString(4);
                    String content = cursor.getString(5);
                    String date = cursor.getString(6);
                    int hasAttach = cursor.getInt(7);
                    int straightIndex = cursor.getInt(8);
                    Mail mail = new Mail(sender, namesender, receiver, subject, content, date,
                            hasAttach, straightIndex);
                    mails.add(mail);
                }
            }
            while (cursor.moveToNext());
        }
        return mails;
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

    public int isHasAttach() {
        return hasAttach;
    }

    public void setHasAttach(int hasAttach) {
        this.hasAttach = hasAttach;
    }
}
