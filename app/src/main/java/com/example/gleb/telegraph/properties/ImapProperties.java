package com.example.gleb.telegraph.properties;

import com.example.gleb.telegraph.abstracts.AbstractProperties;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

/**
 * Created by gleb on 14.01.16.
 */
public class ImapProperties extends AbstractProperties {

    /**
     * Set properties for imap protocol
     * @param MailSettings        Settings of post server
     * @return Properties         Properties for imap protocol
     * */
    @Override
    public Properties settingProperties(MailSettings mailSettings) {
        Properties props = new Properties();
        props.put("mail.imap.port", mailSettings.getPortImap());
        props.put("mail.imap.socketFactory.port", mailSettings.getPortImap());
        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "imaps");
        return props;
    }
}
