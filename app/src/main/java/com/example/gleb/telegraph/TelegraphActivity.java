package com.example.gleb.telegraph;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;
import com.example.gleb.telegraph.models.MailSettings;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

/**
 * Created by Gleb on 03.01.2016.
 */
public class TelegraphActivity extends AbstractActivity {
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String MAIL_SETTINGS = "MailSettings";
    private String email, password;
    private MailSettings mailSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegraph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeWidgets();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new Loader(email, password, mailSettings, true).execute();
    }

    @Override
    protected void initializeWidgets() {
        email = getIntent().getStringExtra(TelegraphActivity.EMAIL);
        password = getIntent().getStringExtra(TelegraphActivity.PASSWORD);
        mailSettings = (MailSettings) getIntent().getSerializableExtra(TelegraphActivity.MAIL_SETTINGS);
        databaseHelper = new DatabaseHelper(TelegraphActivity.this);
    }

    public class Loader extends AsyncTask<Void, Void, Void> {
        private String email, password;
        private MailSettings mailSettings;
        private boolean isImap;

        public Loader(String email, String password, MailSettings mailSettings, boolean isImap) {
            this.email = email;
            this.password = password;
            this.mailSettings = mailSettings;
            this.isImap = isImap;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Properties props = new Properties();
            if (isImap) {
                props.put("mail.imap.port", mailSettings.getPortImap());
                props.put("mail.imap.socketFactory.port", mailSettings.getPortImap());
                props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.imap.socketFactory.fallback", "false");
                props.setProperty("mail.store.protocol", "imaps");
            }
            else{
                props.put("mail.pop3.port", mailSettings.getPortPop3());
                props.put("mail.pop3.socketFactory.port", mailSettings.getPortPop3());
                props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.pop3.socketFactory.fallback", "false");
                props.setProperty("mail.store.protocol", "pop3");
            }

            Session session = Session.getInstance(props, null);
            Store store = null;
            try {
                store = session.getStore();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            try {
                if (isImap)
                    store.connect(mailSettings.getAddressImap(), email, password);
                else
                    store.connect(mailSettings.getAddressPop3(), email, password);
                Folder[] folders = store.getDefaultFolder().list();
                parseFolder(folders, store);

            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void parseFolder(Folder[] folders, Store store) throws MessagingException, UnsupportedEncodingException {
        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        for (Folder f : folders){
            MailFolder folder = new MailFolder(f.getName());
            folder.addFolder(sdb, MailBox.getLastAccount(db));

            f.open(Folder.READ_ONLY);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.USER), false);
            Message[] messages = f.search(ft);
            if (messages.length != 0)
                parsePostMessage(messages);
        }
        sdb.close();
        db.close();
    }

    private void parsePostMessage(Message[] messages) throws MessagingException, UnsupportedEncodingException {
        List<String> emails = new ArrayList<>();
        if (messages.length > 5)
            for (int i = messages.length - 1; i > messages.length - 5; i--){
                emails.add(parseEmailAddress(messages[i]));
            }
        else
            for (int i = messages.length - 1; i >= 0; i--){
                emails.add(parseEmailAddress(messages[i]));
            }

    }

    private String parseEmailAddress(Message message) throws MessagingException, UnsupportedEncodingException {
        String email = "";
        Address[] in = message.getFrom();
        for (Address address : in) {
            String decodeAddress = MimeUtility.decodeText(address.toString());
            if (decodeAddress.indexOf("<") == -1 && decodeAddress.indexOf(">") == -1)
                email = decodeAddress;
            else
                //add email of sender of mail
                email = decodeAddress.substring(decodeAddress.indexOf("<"), decodeAddress.indexOf(">") + 1);
        }
        return email;
    }
}
