package com.example.gleb.telegraph.syncmails;

import com.example.gleb.telegraph.abstracts.AbstractSyncMails;

import javax.mail.Store;
import javax.mail.internet.MimeMessage;

/**
 * Created by gleb on 06.02.16.
 */
public class FactorySyncMails {
    public void getSyncMail(Store store, MimeMessage msg, String protocol) {
        AbstractSyncMails syncMails = null;
        if (protocol.equals(SyncTypeMails.SYNC_SEND_MAIL)) {
            syncMails = new SyncSendMail();
            syncMails.syncMail(store, msg);
        }
    }
}
