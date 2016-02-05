package com.example.gleb.telegraph.events;

/**
 * Created by gleb on 05.02.16.
 */
public class SendMailEvent {
    private boolean isSendMail;

    public SendMailEvent(boolean isSendMail) {
        this.isSendMail = isSendMail;
    }

    public boolean isSendMail() {
        return isSendMail;
    }
}
