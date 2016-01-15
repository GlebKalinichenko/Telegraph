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
public class Pop3Connection extends AbstractConnection {

    /**
     * Authentication on post server by using pop3 protochol
     * @param MailSettings        Settings of post server
     * @param MailBox             Mail of box with email and password email account
     * @return Store              Store of messages from post server
     * */
    @Override
    public Store authentication(MailSettings mailSettings, MailBox mailBox) {
        Properties props = new Properties();
        props.put("mail.pop3.port", mailSettings.getPortPop3());
        props.put("mail.pop3.socketFactory.port", mailSettings.getPortPop3());
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "pop3");

        Session session = Session.getInstance(props, null);
        Store store = null;
        try {
            store = session.getStore();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
        try {
            store.connect(mailSettings.getAddressPop3(), mailBox.getEmail(), mailBox.getPassword());
            return store;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
