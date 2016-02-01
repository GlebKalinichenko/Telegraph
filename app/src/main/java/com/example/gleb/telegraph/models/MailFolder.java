package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.gleb.telegraph.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
    public static List<Integer> addFolders(DatabaseHelper databaseHelper, Folder[] folders){
        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Integer> ids = new ArrayList<>();
        String sql = "insert into Folders (NameFolder, MailBoxCode) values (?, ?);";
        SQLiteStatement stmt = sdb.compileStatement(sql);
        sdb.beginTransaction();
        for (Folder f : folders){
            MailFolder folder = new MailFolder(f.getName());
            stmt.bindString(1, folder.getFolder());
            //get code of folder from database
            stmt.bindLong(2, MailBox.getLastAccount(db));
            long entryID = stmt.executeInsert();
            ids.add((int) entryID);
            stmt.clearBindings();
        }
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        sdb.close();
        db.close();
        return ids;
    }

    /**
     * Get last index of folder in database
     * @param SQLiteDatabase        Database
     * @return int                  Code of folder in database
     * */
    public static int getFolderByName(DatabaseHelper databaseHelper, String nameFolder){
        SQLiteDatabase sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.query(DatabaseHelper.TABLE_FOLDERS,
                new String[]{DatabaseHelper.ID_FOLDER}, DatabaseHelper.NAME_FOLDER + "=?",
                new String[]{nameFolder}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return 0;
    }

    /**
     * Get last index of folder in database
     * @param SQLiteDatabase        Database
     * @return int                  Code of folder in database
     * */
    public static List<String> selectFolders(SQLiteDatabase sdb){
        List<String> folders = new ArrayList<>();
        Cursor cursor = sdb.query(DatabaseHelper.TABLE_FOLDERS,
                new String[]{DatabaseHelper.NAME_FOLDER}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                folders.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }
        return folders;
    }

    /**
     * Remove folders from database by mail box code
     * @param SQLiteDatabase        Database
     * @param int                   Code of mail box account
     * */
    public static void removeFoldersByMailCode(SQLiteDatabase sdb, int mailBoxCode){
        String query = "Delete from Folders where MailBoxCode='" + mailBoxCode + "'";
        sdb.rawQuery(query, null);
    }

    /**
     * Get name of folders for show on view pager
     * @param Folder[]              Array of folders
     * @return List<String>         Array names of folders
     * */
    public static List<String> folderToString(Folder[] arrayFolders){
        List<String> folders = new ArrayList<>();
        for (Folder folder : arrayFolders) {
            folders.add(folder.getName());
        }
        return folders;
    }
}
