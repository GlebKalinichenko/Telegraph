package com.example.gleb.telegraph;

import android.app.Application;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gleb on 05.01.2016.
 */
public class AttachApplication extends Application {
    public static List<InputStream> attachInputStreams = new ArrayList<>();

    public static int add(InputStream inputStream){
        attachInputStreams.add(inputStream);
        return attachInputStreams.size() - 1;
    }
}
