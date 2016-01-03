package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailFolder {
    private String folder;

    public MailFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * Add folder to database
     * @param SQLiteDatabase        Database
     * @param int                   Code of email account
     * @return void
     * */
    public void addFolder(SQLiteDatabase sdb, int boxCode){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NAME_FOLDER, this.folder);
        values.put(DatabaseHelper.MAIL_BOX_CODE, boxCode);
        sdb.insert(DatabaseHelper.TABLE_FOLDERS, null, values);
    }

    /**
     * Get last index of folder in database
     * @param SQLiteDatabase        Database
     * @return int                  Code of folder in database
     * */
    public static int getLastFolder(SQLiteDatabase sdb){
        String query = "SELECT IdFolder from Folders order by IdFolder DESC limit 1";
        Cursor cursor = sdb.rawQuery(query, null);
        cursor.moveToLast();
        int folderCode = cursor.getInt(0);
        return folderCode;
    }
}
