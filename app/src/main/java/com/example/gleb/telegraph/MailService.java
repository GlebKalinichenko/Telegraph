package com.example.gleb.telegraph;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.example.gleb.telegraph.events.ReceiveMailEvent;
import com.example.gleb.telegraph.models.Mail;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;
import com.example.gleb.telegraph.models.MailSettings;
import com.example.gleb.telegraph.properties.FactoryProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import de.greenrobot.event.EventBus;

public class MailService extends Service {
    public static final String MAIL_BOX = "MailBox";
    public static final String MAIL_SETTINGS = "MailSettings";
    public static final String CUR_OFFSET_MAILS = "CurOffsetMails";
    public static final String PREV_OFFSET_MAILS = "PrevOffsetMails";
    public static final long NOTIFY_INTERVAL = 10 * 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private MailBox mailBox;
    private MailSettings mailSettings;
    private int curOffsetMails;
    private int prevOffsetMails;
    private Session session;
    private Properties props;
    private Store store;
    private Folder[] folders;

    public MailService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mailBox = (MailBox) intent.getSerializableExtra(MAIL_BOX);
        mailSettings = (MailSettings) intent.getSerializableExtra(MAIL_SETTINGS);
        curOffsetMails = intent.getIntExtra(CUR_OFFSET_MAILS, 1);
        prevOffsetMails = intent.getIntExtra(PREV_OFFSET_MAILS, 1);
        return START_STICKY;
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
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    new Loader(mailBox, mailSettings).execute();
                }
            });
        }
    }

    public class Loader extends AsyncTask<Void, Void, Boolean> {
        private MailBox mailBox;
        private MailSettings mailSettings;
        private Session session;
        private Properties props;
        private Store store;
        private Folder[] folders;
        private boolean isReceiveMails = false;
        private Message[] messages;
        private Folder folder;
        private DatabaseHelper databaseHelper;

        public Loader(MailBox mailBox, MailSettings mailSettings) {
            this.mailSettings = mailSettings;
            this.mailBox = mailBox;
            databaseHelper = new DatabaseHelper(getApplicationContext());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final FactoryProperties factoryProperties = new FactoryProperties();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    props = factoryProperties.getProperties(mailSettings, mailBox.getReceiveProtocol());
                    session = Session.getInstance(props, null);
                    store = factoryProperties.authentication(session, mailSettings, mailBox);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (store != null) {
                    folders = store.getDefaultFolder().list();
                    for (int i = 0; i < folders.length; i++) {
                        folders[i].open(Folder.READ_ONLY);
                        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.USER), false);
                        messages = folders[i].search(ft);
                        //check is inbox folder for save current num of mails to application
                        if (MailFolder.isInboxMessages(folders[i].getName())) {
                            folder = folders[i];
                            int oldMails = MessageApplication.getNumMessages();
                            if (oldMails < messages.length){
                                isReceiveMails = true;
                                //delta receive mails
                                int receiveMails = messages.length - oldMails;
                                MessageApplication.setNumMessage(oldMails + receiveMails);
                                Message[] receiveMessages = getReceiveMessages(receiveMails,
                                        messages);
                                ParserMail parserMail = new ParserMail(mailBox.getEmail(),
                                        databaseHelper, curOffsetMails,
                                        prevOffsetMails);
                                try {
                                    parserMail.parsePostMessage(receiveMessages,
                                            MailFolder.selectFolderByNameAndMailBoxCode(
                                            databaseHelper.getReadableDatabase(), folder.getName(),
                                            MailBox.getAccountByName(databaseHelper.getReadableDatabase(),
                                            mailBox.getEmail())));
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                isReceiveMails = false;
                            }
                        }
                    }
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return isReceiveMails;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Toast.makeText(getApplicationContext(), "Receive mails",
                        Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new ReceiveMailEvent(true));
            }
        }
    }

    /**
     * Gets mails from post server num of receive mails
     * @param int            Num mails for receive
     * @param Message[]      Array of general mails
     * @return Message[]     Array received mails
     * */
    private Message[] getReceiveMessages(int receiveMails, Message[] messages){
        List<Message> receiveMessagesList = new ArrayList<>();
        for (int i = messages.length - receiveMails; i < messages.length; i++){
            receiveMessagesList.add(messages[i]);
        }
        Message[] receiveMessages = new Message[receiveMessagesList.size()];
        for (int i = 0; i < receiveMessagesList.size(); i++){
            receiveMessages[i] = receiveMessagesList.get(i);
        }
        return receiveMessages;
    }
}
