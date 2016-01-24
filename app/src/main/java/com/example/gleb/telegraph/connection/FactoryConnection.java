package com.example.gleb.telegraph.connection;

import com.example.gleb.telegraph.abstracts.AbstractConnection;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by gleb on 14.01.16.
 */
public class FactoryConnection {
    private Session session;

    /**
     * Get session with messages from post server
     * @param MailSettings        Settings of post server
     * @param String              Use protocol
     * @return Store              Store of messages from post server
     * */
    public Session getSession(MailSettings mailSettings, String protocol) {
        AbstractConnection connection = null;
        if (protocol.equals(Protocols.IMAP)) {
            connection = new ImapConnection();
            session = connection.settingProperties(mailSettings);
        } else if (protocol.equals(Protocols.POP3)) {
            connection = new Pop3Connection();
            session = connection.settingProperties(mailSettings);
        } else if (protocol.equals(Protocols.SMTP)){
            connection = new SmtpConnection();
            session = connection.settingProperties(mailSettings);
        }
        return session;
    }

    /**
     * Authentication on post server
     * @param Session             Settings of post server
     * @param MailSettings        Settings of mail
     * @return Store              Store of messages from post server
     * */
    public Store authentication(Session session, MailSettings mailSettings,
        MailBox mailBox) {
        Store store = null;
        try {
            store = session.getStore();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
        try {
            store.connect(mailSettings.getAddressImap(), mailBox.getEmail(), mailBox.getPassword());
            return store;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
