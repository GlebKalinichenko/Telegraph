package com.example.gleb.telegraph.models;

import com.example.gleb.telegraph.abstracts.AbstractDatabaseCryptography;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailBoxKey extends AbstractDatabaseCryptography {
    private String typeKey;

    public MailBoxKey(String linkToFolder, String typeKey) {
        super(linkToFolder);
        this.typeKey = typeKey;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }
}
