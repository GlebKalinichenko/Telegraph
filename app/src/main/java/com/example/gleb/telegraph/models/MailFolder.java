package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.gleb.telegraph.DatabaseHelper;

import javax.mail.Folder;

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
     * Add array of folders to database
     * @param DatabaseHelper        Helper for work with database
     * @param Folder[]              Array of folders
     * @return void
     * */
    public static void addFolders(DatabaseHelper databaseHelper, Folder[] folders){
        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String sql = "insert into Folders (NameFolder, MailBoxCode) values (?, ?);";
        SQLiteStatement stmt = sdb.compileStatement(sql);
        sdb.beginTransaction();
        for (Folder f : folders){
            MailFolder folder = new MailFolder(f.getName());
//            folder.addFolder(sdb, MailBox.getLastAccount(db));
            stmt.bindString(1, folder.getFolder());
            //get code of folder from database
            stmt.bindLong(2, MailBox.getLastAccount(db));
            long entryID = stmt.executeInsert();
            stmt.clearBindings();
        }
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        sdb.close();
        db.close();
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
