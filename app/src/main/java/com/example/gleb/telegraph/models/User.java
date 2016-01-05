package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.abstracts.AbstractUser;

/**
 * Created by Gleb on 30.12.2015.
 */
public class User extends AbstractUser {
    public User(String email) {
        super(email);
    }

    /**
     * Add user to database
     * @param SQLiteDatabase        Database
     * @return void
     * */
    public void addUser(SQLiteDatabase sdb){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMAIL_USER, this.email);
        sdb.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    /**
     * Check has user in table Users
     * @param SQLiteDatabase        Database
     * @param String                Email is found in table
     * @return int                  Id of email sender or 0 if email is empty
     * */
    public static int checkUserEmail(SQLiteDatabase sdb, String emailSender){
        Cursor cursor = sdb.query(DatabaseHelper.TABLE_USERS, new String[]{DatabaseHelper.ID_USER, DatabaseHelper.EMAIL_USER},
                DatabaseHelper.EMAIL_USER + "=?", new String[]{emailSender}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return 0;
    }

    /**
     * Get last index of user in table Users
     * @param SQLiteDatabase        Database
     * @return int                  Id of last user
     * */
    public static int getLastUser(SQLiteDatabase sdb){
        String query = "SELECT IdUser from Users order by IdUser DESC limit 1";
        Cursor cursor = sdb.rawQuery(query, null);
        cursor.moveToLast();
        int boxCode = cursor.getInt(0);
        return boxCode;
    }
}
