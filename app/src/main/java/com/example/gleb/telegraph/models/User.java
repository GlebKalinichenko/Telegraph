package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.abstracts.AbstractUser;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;

/**
 * Created by Gleb on 30.12.2015.
 */
public class User extends AbstractUser {
    public static final String SELECT_USERS = "Select Email from Users";

    public User(String email) {
        super(email);
    }

    /**
     * Add user to database
     * @param SQLiteDatabase        Database
     * @return void
     * */
    public long addUser(DatabaseHelper databaseHelper){
        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
        String sql = "insert into Users (Email) values (?);";
        SQLiteStatement stmt = sdb.compileStatement(sql);
        sdb.beginTransaction();
        stmt.bindString(1, this.email);
        long entryID = stmt.executeInsert();
        stmt.clearBindings();
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        sdb.close();
        return entryID;
    }

    /**
     * Check has user in table Users
     * @param SQLiteDatabase        Database
     * @param String                Email is found in table
     * @return int                  Id of email sender or 0 if email is empty
     * */
    public static long checkUserEmail(SQLiteDatabase sdb, String emailSender){
        String query = "Select Users.IdUser from Users where Email='" + emailSender + "'";
        Cursor cursor = sdb.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            sdb.close();
            return cursor.getLong(0);
        }
        else {
            sdb.close();
            return 0;
        }
    }

    /**
     * Select users from table Users
     * @param SQLiteDatabase        Database
     * @return List<User>           Array of selected users
     * */
    public static List<User> selectUsers(SQLiteDatabase sdb){
        List<User> users = new ArrayList<>();
        Cursor cursor = sdb.rawQuery(SELECT_USERS, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                users.add(new User(cursor.getString(0)));
            }
            while (cursor.moveToNext());
        }
        return users;
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
