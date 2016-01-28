package com.example.gleb.telegraph.sendmail;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.List;

/**
 * Created by gleb on 25.01.16.
 */
public class SendMailContext {
    SendMailInterface sendMailInterface;

    public SendMailContext(SendMailInterface sendMailInterface) {
        this.sendMailInterface = sendMailInterface;
    }

    public boolean executeSendMail(String subject, String message, String[] receivers,
        boolean hasEncryption, boolean hasDigest, MailSettings mailSettings, MailBox mailBox,
        List<String> attachFiles){
        return sendMailInterface.sendMail(subject, message, receivers, hasEncryption, hasDigest,
                mailSettings, mailBox, attachFiles);
    }
}
