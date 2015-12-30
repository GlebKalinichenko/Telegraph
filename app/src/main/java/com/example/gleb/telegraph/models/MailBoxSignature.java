package com.example.gleb.telegraph.models;

import com.example.gleb.telegraph.abstracts.AbstractDatabaseCryptography;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailBoxSignature extends AbstractDatabaseCryptography {
    public MailBoxSignature(String linkToFolder) {
        super(linkToFolder);
    }
}
