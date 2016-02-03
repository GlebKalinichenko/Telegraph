package com.example.gleb.telegraph.events;

import javax.mail.Folder;

/**
 * Created by gleb on 03.02.16.
 */
public class ReceiveMailEvent {
    private boolean isReceiveMail;

    public ReceiveMailEvent(boolean isReceiveMail) {
        this.isReceiveMail = isReceiveMail;
    }

    public boolean isReceiveMail() {
        return isReceiveMail;
    }
}
