package com.example.gleb.telegraph.connection;

import com.example.gleb.telegraph.abstracts.AbstractConnection;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

import javax.mail.Session;

/**
 * Created by gleb on 24.01.16.
 */
public class SmtpConnection extends AbstractConnection {

    /**
     * Authentication on post server by using smtp protocol
     * @param MailSettings        Settings of post server
     * @return Store              Store of messages from post server
     * */
    @Override
    public Session settingProperties(MailSettings mailSettings) {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailSettings.getAddressSmtp());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", mailSettings.getPortSmtp());
        props.put("mail.smtp.socketFactory.port", mailSettings.getPortPop3());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(props, null);
        return session;
    }
}
