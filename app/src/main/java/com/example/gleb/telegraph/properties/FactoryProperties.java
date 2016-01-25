package com.example.gleb.telegraph.properties;

import com.example.gleb.telegraph.abstracts.AbstractProperties;
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
public class FactoryProperties {
    private Properties props;

    /**
     * Get session with messages from post server
     * @param MailSettings        Settings of post server
     * @param String              Use protocol
     * @return Session            Session of messages from post server
     * */
    public Properties getProperties(MailSettings mailSettings, String protocol) {
        AbstractProperties connection = null;
        if (protocol.equals(Protocols.IMAP)) {
            connection = new ImapProperties();
            props = connection.settingProperties(mailSettings);
        } else if (protocol.equals(Protocols.POP3)) {
            connection = new Pop3Properties();
            props = connection.settingProperties(mailSettings);
        } else if (protocol.equals(Protocols.SMTP)){
            connection = new SmtpProperties();
            props = connection.settingProperties(mailSettings);
        }
        return props;
    }

    /**
     * Authentication on post server
     * @param Session             Settings of post server
     * @param MailSettings        Settings of mail
     * @param MailBox             Email account
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
