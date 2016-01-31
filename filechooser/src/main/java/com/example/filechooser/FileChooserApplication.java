package com.example.filechooser;

import android.app.Application;
import android.content.Context;

public class FileChooserApplication extends Application{
    public static volatile Context applicationContext = null;

	@Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
