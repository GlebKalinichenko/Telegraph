package com.example.gleb.telegraph.connection;

import com.example.gleb.telegraph.abstracts.AbstractConnection;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

import javax.mail.Session;

/**
 * Created by gleb on 14.01.16.
 */
public class Pop3Connection extends AbstractConnection {

    /**
     * Authentication on post server by using pop3 protocol
     * @param MailSettings        Settings of post server
     * @return Store              Store of messages from post server
     * */
    @Override
    public Session settingProperties(MailSettings mailSettings) {
        Properties props = new Properties();
        props.put("mail.pop3.port", mailSettings.getPortPop3());
        props.put("mail.pop3.socketFactory.port", mailSettings.getPortPop3());
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "pop3");
        Session session = Session.getInstance(props, null);
        return session;
    }
}
