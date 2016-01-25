package com.example.gleb.telegraph;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

/**
 * Created by gleb on 24.01.16.
 */
public interface SendMailInterface {
    public boolean sendMail(String subject, String message, String[] receivers, boolean hasEncryption,
        boolean hasDigest, MailSettings mailSettings, MailBox mailBox);
}