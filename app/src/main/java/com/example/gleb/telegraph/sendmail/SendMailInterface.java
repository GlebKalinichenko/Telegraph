package com.example.gleb.telegraph.sendmail;

import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.List;

/**
 * Created by gleb on 24.01.16.
 */
public interface SendMailInterface {
    public boolean sendMail(String subject, String message, String[] receivers, boolean hasEncryption,
        boolean hasDigest, MailSettings mailSettings, MailBox mailBox, List<String> attachFiles,
        DatabaseHelper databaseHelper);
}
