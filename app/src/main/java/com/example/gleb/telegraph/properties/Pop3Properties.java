package com.example.gleb.telegraph.properties;

import com.example.gleb.telegraph.abstracts.AbstractProperties;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

/**
 * Created by gleb on 14.01.16.
 */
public class Pop3Properties extends AbstractProperties {

    /**
     * Set properties for pop3 protocol
     * @param MailSettings        Settings of post server
     * @return Properties         Properties for pop3 protocol
     * */
    @Override
    public Properties settingProperties(MailSettings mailSettings) {
        Properties props = new Properties();
        props.put("mail.pop3.port", mailSettings.getPortPop3());
        props.put("mail.pop3.socketFactory.port", mailSettings.getPortPop3());
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "pop3");
        return props;
    }
}
