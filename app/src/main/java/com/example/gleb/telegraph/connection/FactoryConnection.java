package com.example.gleb.telegraph.connection;

import com.example.gleb.telegraph.abstracts.AbstractConnection;
import com.example.gleb.telegraph.connection.ImapConnection;
import com.example.gleb.telegraph.connection.Pop3Connection;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import javax.mail.Store;

/**
 * Created by gleb on 14.01.16.
 */
public class FactoryConnection {
    private Store store;

    public Store getStore(boolean isImap, MailSettings mailSettings, MailBox mailBox) {
        AbstractConnection connection = null;
        if (isImap) {
            connection = new ImapConnection();
            store = connection.authentication(mailSettings, mailBox);
        } else if (!isImap) {
            connection = new Pop3Connection();
            store = connection.authentication(mailSettings, mailBox);
        }
        return store;
    }
}
