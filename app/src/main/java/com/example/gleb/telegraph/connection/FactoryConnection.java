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

    /**
     * Get store with messages from post server
     * @param MailSettings        Settings of post server
     * @param MailBox             Mail of box with email and password email account
     * @return Store              Store of messages from post server
     * */
    public Store getStore(MailSettings mailSettings, MailBox mailBox) {
        AbstractConnection connection = null;
        if (mailBox.getProtocol().equals("imap")) {
            connection = new ImapConnection();
            store = connection.authentication(mailSettings, mailBox);
        } else if (!mailBox.getProtocol().equals("imap")) {
            connection = new Pop3Connection();
            store = connection.authentication(mailSettings, mailBox);
        }
        return store;
    }
}
