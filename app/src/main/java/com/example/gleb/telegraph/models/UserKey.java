package com.example.gleb.telegraph.models;

import com.example.gleb.telegraph.abstracts.AbstractDatabaseCryptography;

/**
 * Created by Gleb on 30.12.2015.
 */
public class UserKey extends AbstractDatabaseCryptography {

    public UserKey(String linkToFolder) {
        super(linkToFolder);
    }
}
