package com.example.gleb.telegraph.properties;

import com.example.gleb.telegraph.abstracts.AbstractProperties;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

/**
 * Created by gleb on 24.01.16.
 */
public class SmtpProperties extends AbstractProperties {

    /**
     * Set properties for smtp protocol
     * @param MailSettings        Settings of post server
     * @return Properties         Properties for smtp properties
     * */
    @Override
    public Properties settingProperties(MailSettings mailSettings) {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailSettings.getAddressSmtp());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", mailSettings.getPortSmtp());
        props.put("mail.smtp.socketFactory.port", mailSettings.getPortSmtp());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    }
}
