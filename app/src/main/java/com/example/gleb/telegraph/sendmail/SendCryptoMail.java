package com.example.gleb.telegraph.sendmail;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.List;

/**
 * Created by gleb on 24.01.16.
 */
public class SendCryptoMail extends javax.mail.Authenticator implements SendMailInterface {

    @Override
    public boolean sendMail(String subject, String message, String[] receivers,
        boolean hasEncryption, boolean hasDigest, MailSettings mailSettings, MailBox mailBox,
        List<String> attachFiles) {

        return false;
    }
}
