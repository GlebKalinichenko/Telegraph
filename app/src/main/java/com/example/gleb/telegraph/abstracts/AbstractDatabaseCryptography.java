package com.example.gleb.telegraph.abstracts;

/**
 * Created by Gleb on 30.12.2015.
 */
public abstract class AbstractDatabaseCryptography {
    protected String linkToFolder;

    public AbstractDatabaseCryptography(String linkToFolder) {
        this.linkToFolder = linkToFolder;
    }

    public String getLinkToFolder() {
        return linkToFolder;
    }

    public void setLinkToFolder(String linkToFolder) {
        this.linkToFolder = linkToFolder;
    }
}
