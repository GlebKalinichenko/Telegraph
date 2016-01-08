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
        String query = "Select Users.Email, Mails.NameSender, Mails.Receiver, Mails.Subject," +
                " Mails.Content, Mails.Date, Mails.HasAttach, Mails.StraightIndex " +
                "from Mails inner join Users on Mails.SenderUserCode = Users.IdUser";
        Cursor cursor = sdb.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(0);
                String namesender = cursor.getString(1);
                String receiver = cursor.getString(2);
                String subject = cursor.getString(3);
                String content = cursor.getString(4);
                String date = cursor.getString(5);
                int hasAttach = cursor.getInt(6);
                int straightIndex = cursor.getInt(7);
                Mail mail = new Mail(sender, namesender, receiver, subject, content, date,
                        hasAttach, straightIndex);
                mails.add(mail);
            }
            while (cursor.moveToNext());
        }
        return mails;
    }

    /**
     * Get last index of mail in database
     * @param SQLiteDatabase        Database
     * @return int                  Id of last mail in database
     * */
    public static int getLastMail(SQLiteDatabase sdb){
        String query = "SELECT IdMail from Mails order by IdMail DESC limit 1";
        Cursor cursor = sdb.rawQuery(query, null);
        cursor.moveToLast();
        int folderCode = cursor.getInt(0);
        return folderCode;
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
