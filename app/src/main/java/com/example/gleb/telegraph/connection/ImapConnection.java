package com.example.gleb.telegraph.connection;

import com.example.gleb.telegraph.abstracts.AbstractConnection;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by gleb on 14.01.16.
 */
public class ImapConnection extends AbstractConnection {

    /**
     * Authentication on post server by using imap protochol
     * @param MailSettings        Settings of post server
     * @param MailBox             Mail of box with email and password email account
     * @return Store              Store of messages from post server
     * */
    @Override
    public Store authentication(MailSettings mailSettings, MailBox mailBox) {
        Properties props = new Properties();
        props.put("mail.imap.port", mailSettings.getPortImap());
        props.put("mail.imap.socketFactory.port", mailSettings.getPortImap());
        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getInstance(props, null);
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
