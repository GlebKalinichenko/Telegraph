package com.example.gleb.telegraph.abstracts;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Properties;

import javax.mail.Store;

/**
 * Created by gleb on 14.01.16.
 */
abstract public class AbstractConnection {
    public Properties props;
    public abstract Store authentication(MailSettings mailSettings, MailBox mailBox);
}
