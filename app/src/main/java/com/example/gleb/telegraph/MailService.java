package com.example.gleb.telegraph;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Timer;
import java.util.TimerTask;

public class MailService extends Service {
    public static final long NOTIFY_INTERVAL = 10 * 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private MailBox mailBox;
    private MailSettings mailSettings;
    private int curOffsetMails;
    private int prevOffsetMails;

    public MailService(MailBox mailBox, MailSettings mailSettings, int curOffsetMails,
        int prevOffsetMails) {
        this.mailBox = mailBox;
        this.mailSettings = mailSettings;
        this.curOffsetMails = curOffsetMails;
        this.prevOffsetMails = prevOffsetMails;
    }

    public MailService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }
}
