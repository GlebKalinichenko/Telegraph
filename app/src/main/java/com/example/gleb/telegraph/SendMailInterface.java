package com.example.gleb.telegraph;

/**
 * Created by gleb on 24.01.16.
 */
public interface SendMailInterface {
    public boolean sendMail(String message, String[] receivers, boolean hasEncryption,
        boolean hasDigest);
}
