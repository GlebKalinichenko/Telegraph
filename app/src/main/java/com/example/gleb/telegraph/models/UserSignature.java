package com.example.gleb.telegraph.models;

import com.example.gleb.telegraph.abstracts.AbstractDatabaseCryptography;

/**
 * Created by Gleb on 30.12.2015.
 */
public class UserSignature extends AbstractDatabaseCryptography {
    public UserSignature(String linkToFolder) {
        super(linkToFolder);
    }
}
