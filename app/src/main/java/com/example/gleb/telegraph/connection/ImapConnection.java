package com.example.gleb.telegraph.connection;

import com.example.gleb.telegraph.abstracts.AbstractConnection;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

import javax.mail.Session;

/**
 * Created by gleb on 14.01.16.
 */
public class ImapConnection extends AbstractConnection {

    /**
     * Authentication on post server by using imap protocol
     * @param MailSettings        Settings of post server
     * @return Store              Store of messages from post server
     * */
    @Override
    public Session settingProperties(MailSettings mailSettings) {
        Properties props = new Properties();
        props.put("mail.imap.port", mailSettings.getPortImap());
        props.put("mail.imap.socketFactory.port", mailSettings.getPortImap());
        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props, null);
        return session;
    }
}
