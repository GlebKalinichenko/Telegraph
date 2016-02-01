package com.example.gleb.telegraph;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MailService extends Service {
    public MailService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
