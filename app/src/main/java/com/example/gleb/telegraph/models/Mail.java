package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.example.gleb.telegraph.DatabaseHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gleb on 30.12.2015.
 */
public class Mail {
    public static final String INSERT_MAIL = "insert into Mails (SenderUserCode, NameSender, " +
            "Receiver, Subject, Content, FolderCode, Date, HasAttach, StraightIndex) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private String sender;
    private String nameSender;
    private String receiver;
    private String subject;
    private String content;
    private String date;
    private int hasAttach;

    public Mail(String sender, String nameSender, String receiver, String subject, String content, String date, int hasAttach) {
        this.sender = sender;
        this.nameSender = nameSender;
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.hasAttach = hasAttach;
    }

    /**
     * Add array of mails to database
     * @param SQLiteDatabase        Database
     * @param int                   Id user from table Users for email sender
     * @param int                   Id folder
     * @param int                   Type mail 0 is sent mail 1 is received mail
     * @param List<List<Attach>>    Array of search_attach for each mail
     * @return void
     * */
    public static void addMails(DatabaseHelper databaseHelper, List<Mail> mails,
            List<Long> usersCode, List<Integer> foldersCode, int straightIndex, List<List<Attach>> attachs){
        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
        SQLiteStatement stmt = sdb.compileStatement(Mail.INSERT_MAIL);
        sdb.beginTransaction();
        for (int  i = 0; i < mails.size(); i++) {
            stmt.bindLong(1, usersCode.get(i));
            stmt.bindString(2, mails.get(i).getNameSender());
            stmt.bindString(3, mails.get(i).getReceiver());
            stmt.bindString(4, mails.get(i).getSubject());
            stmt.bindString(5, mails.get(i).getContent());
            stmt.bindString(6, mails.get(i).getDate());
            stmt.bindLong(7, foldersCode.get(i));
            stmt.bindLong(8, mails.get(i).isHasAttach());
            stmt.bindLong(9, straightIndex);
            long entryID = stmt.executeInsert();
            //is has mail search_attach
            if (mails.get(i).isHasAttach() == 1){
                //array of attachs of mail
                List<Attach> listAttachs = attachs.get(0);
                //delete array of search_attach for mail from common store of array attachs
                attachs.remove(0);
                for (Attach attach: listAttachs) {
                    //add list search_attach of mail to database
                    attach.addAttachs(databaseHelper.getWritableDatabase(), (int) entryID, listAttachs);
                }
            }
            stmt.clearBindings();
        }
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        sdb.close();
    }

    /**
     * Select all mails
     * @param SQLiteDatabase        Database
     * @return List<Mail>           List of mails
     * */
    public static List<Mail> selectAllMails(SQLiteDatabase sdb){
        List<Mail> mails = new ArrayList<>();
        String query = "Select Users.Email, Mails.NameSender, Mails.Receiver, Mails.Subject," +
                " Mails.Content, Mails.Date, Mails.HasAttach " +
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
                Mail mail = new Mail(sender, namesender, receiver, subject, content, date,
                        hasAttach);
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
