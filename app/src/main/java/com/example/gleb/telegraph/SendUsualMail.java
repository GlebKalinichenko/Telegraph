package com.example.gleb.telegraph;

/**
 * Created by gleb on 24.01.16.
 */
public class SendUsualMail implements SendMailInterface {
    @Override
    public boolean sendMail(String message, String[] receivers, boolean hasEncryption, boolean hasDigest) {
        return false;
    }
}
