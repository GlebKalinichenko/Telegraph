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
public class Attach {
    private String nameAttachs;
    private int numPositions;

    public Attach(String nameAttachs, int numPositions) {
        this.nameAttachs = nameAttachs;
        this.numPositions = numPositions;
    }

    /**
     * Add attach to database
     * @param SQLiteDatabase        Database
     * @param int                   Id mail
     * @return void
     * */
    public void addAttach(SQLiteDatabase sdb, int mailCode){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NAME_ATTACH, this.nameAttachs);
        values.put(DatabaseHelper.NUM_POSITION, this.numPositions);
        values.put(DatabaseHelper.MAIL_CODE, mailCode);
        sdb.insert(DatabaseHelper.TABLE_ATTACHS, null, values);
    }

    /**
     * Select all attach
     * @param SQLiteDatabase        Database
     * @return List<Attach>         List of attachs
     * */
    public static List<Attach> selectAllAttach(SQLiteDatabase sdb){
        List<Attach> attachs = new ArrayList<>();
        String query = "Select Attachs.NameAttach, Attachs.NumPositionOfAttach from Attachs";
        Cursor cursor = sdb.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nameAttach = cursor.getString(0);
                int numPosition = cursor.getInt(1);
                Attach attach = new Attach(nameAttach, numPosition);
                attachs.add(attach);
            }
            while (cursor.moveToNext());
        }
        return attachs;
    }

    public String getNameAttachs() {
        return nameAttachs;
    }

    public void setNameAttachs(String nameAttachs) {
        this.nameAttachs = nameAttachs;
    }

    public int getNumPositions() {
        return numPositions;
    }

    public void setNumPositions(int numPositions) {
        this.numPositions = numPositions;
    }
}
