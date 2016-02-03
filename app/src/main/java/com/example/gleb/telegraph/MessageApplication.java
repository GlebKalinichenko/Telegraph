package com.example.gleb.telegraph;

import android.app.Application;

/**
 * Created by gleb on 01.02.16.
 */
public class MessageApplication extends Application {
    public static int numMessages;

    public static void setNumMessage(int messages){
        numMessages = messages;
    }

    public static int getNumMessages(){
        return numMessages;
    }
}
