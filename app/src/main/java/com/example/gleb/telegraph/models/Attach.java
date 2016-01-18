package com.example.gleb.telegraph.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gleb on 30.12.2015.
 */
public class Attach {
    public static final String INSERT_ATTACH = "insert into Attachs (NameAttach, NumPositionOfAttach, MailCode) " +
            "values (?, ?, ?);";
    private String nameAttachs;
    private int numPositions;

    public Attach(String nameAttachs, int numPositions) {
        this.nameAttachs = nameAttachs;
        this.numPositions = numPositions;
    }

    /**
     * Add array attach to database
     * @param SQLiteDatabase        Database
     * @param int                   Id mail
     * @param List<Attach>          Array of attach for mail
     * @return void
     * */
    public static void addAttachs(SQLiteDatabase sdb, int mailCode, List<Attach> attachs){
        SQLiteStatement stmt = sdb.compileStatement(Attach.INSERT_ATTACH);
        for (Attach attach : attachs) {
            stmt.bindString(1, attach.getNameAttachs());
            stmt.bindLong(2, attach.getNumPositions());
            stmt.bindLong(3, mailCode);
            stmt.clearBindings();
        }
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
