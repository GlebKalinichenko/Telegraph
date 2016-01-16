package com.example.gleb.telegraph.networkconnection;

/**
 * Created by gleb on 16.01.16.
 */
public class NetworkStateChanged {
    private boolean mIsInternetConnected;

    public NetworkStateChanged(boolean isInternetConnected) {
        this.mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }
}
