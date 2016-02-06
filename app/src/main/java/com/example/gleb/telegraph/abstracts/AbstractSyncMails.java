package com.example.gleb.telegraph.abstracts;

import javax.mail.Store;
import javax.mail.internet.MimeMessage;

/**
 * Created by gleb on 06.02.16.
 */
abstract public class AbstractSyncMails {
    public abstract void syncMail(Store syncStore, MimeMessage msg);
}
