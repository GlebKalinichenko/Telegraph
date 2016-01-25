package com.example.gleb.telegraph;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

/**
 * Created by gleb on 25.01.16.
 */
public class SendMailContext {
    SendMailInterface sendMailInterface;

    public SendMailContext(SendMailInterface sendMailInterface) {
        this.sendMailInterface = sendMailInterface;
    }

    public boolean executeSendMail(String subject, String message, String[] receivers,
        boolean hasEncryption, boolean hasDigest, MailSettings mailSettings, MailBox mailBox){
        return sendMailInterface.sendMail(subject, message, receivers, hasEncryption, hasDigest,
                mailSettings, mailBox);
    }
}
