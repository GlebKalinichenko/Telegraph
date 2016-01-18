package com.example.gleb.telegraph;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gleb.telegraph.abstracts.AbstractActivity;
import com.example.gleb.telegraph.connection.FactoryConnection;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * Created by Gleb on 03.01.2016.
 */
public class TelegraphActivity extends AbstractActivity {
    public static final String MAIL_BOX = "MailBox";
    public static final String MAIL_SETTINGS = "MailSettings";
    private MailBox mailBox;
    private MailSettings mailSettings;
    private CircularProgressView progressView;
    final long startTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegraph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeWidgets();

        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new Loader(mailBox, mailSettings).execute();
    }

    @Override
    protected void initializeWidgets() {
        mailBox = (MailBox) getIntent().getSerializableExtra(TelegraphActivity.MAIL_BOX);
        mailSettings = (MailSettings) getIntent().getSerializableExtra(TelegraphActivity.MAIL_SETTINGS);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        databaseHelper = new DatabaseHelper(TelegraphActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerListView = (ListView) findViewById(R.id.list_slidermenu);
    }

    public class Loader extends AsyncTask<Void, Void, Void> {
        private MailBox mailBox;
        private MailSettings mailSettings;
        private Store store;

        public Loader(MailBox mailBox, MailSettings mailSettings) {
            this.mailSettings = mailSettings;
            this.mailBox = mailBox;
        }

        @Override
        protected Void doInBackground(Void... params) {
            final FactoryConnection factoryConnection = new FactoryConnection();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    store = factoryConnection.getStore(mailSettings, mailBox);
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
                    Folder[] folders = store.getDefaultFolder().list();
                    ParserMail parserMail = new ParserMail(mailBox.getEmail(), databaseHelper);
                    parserMail.parseFolder(folders);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final long endtime = System.currentTimeMillis();
            Toast.makeText(TelegraphActivity.this, "Add to database "
                    + String.valueOf(endtime - startTime), Toast.LENGTH_LONG).show();
            progressView.setVisibility(View.INVISIBLE);
        }
    }

//    /**
//     * Get mails from folders
//     * @param Folder[]        Array of folder from post server
//     * @return void
//     * */
//    private void parseFolder(Folder[] folders) throws MessagingException, IOException {
//        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//        for (Folder f : folders){
//            MailFolder folder = new MailFolder(f.getName());
//            folder.addFolder(sdb, MailBox.getLastAccount(db));
//
//            f.open(Folder.READ_ONLY);
//            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.USER), false);
//            Message[] messages = f.search(ft);
//            if (messages.length != 0)
//                parsePostMessage(messages);
//        }
//        sdb.close();
//        db.close();
//    }
//
//    /**
//     * Parse mail from message
//     * @param Message[]        Array of message from post server
//     * @return void
//     * */
//    private void parsePostMessage(Message[] messages) throws MessagingException, IOException {
//        List<String> emails = new ArrayList<>();
//        List<String> names = new ArrayList<>();
//        List<String> dates = new ArrayList<>();
//        List<String> subjects = new ArrayList<>();
//        List<String> bodies = new ArrayList<>();
//        if (messages.length > 5)
//            for (int i = messages.length - 1; i > messages.length - 5; i--){
//                emails.add(parseEmailAddress(messages[i]));
//                names.add(parseNameEmail(messages[i]));
//                dates.add(parseDate(messages[i]));
//                subjects.add(parseSubject(messages[i]));
//                Object content = new MimeMessage((MimeMessage) messages[i]).getContent();
//                if (content instanceof String)
//                    bodies.add(parseContentString(content));
//                else if (content instanceof  Multipart)
//                    bodies.add(parseContentMultipart(content));
//            }
//        else
//            for (int i = messages.length - 1; i >= 0; i--){
//                emails.add(parseEmailAddress(messages[i]));
//                names.add(parseNameEmail(messages[i]));
//                dates.add(parseDate(messages[i]));
//                subjects.add(parseSubject(messages[i]));
//            }
//
//    }
//
//    /**
//     * Get email of sender
//     * @param Message        Message with email of sender
//     * @return String        Email of sender
//     * */
//    private String parseEmailAddress(Message message) throws MessagingException, UnsupportedEncodingException {
//        String email = "";
//        Address[] in = message.getFrom();
//        for (Address address : in) {
//            String decodeAddress = MimeUtility.decodeText(address.toString());
//            if (decodeAddress.indexOf("<") == -1 && decodeAddress.indexOf(">") == -1)
//                email = decodeAddress;
//            else
//                //add email of sender of mail
//                email = decodeAddress.substring(decodeAddress.indexOf("<"), decodeAddress.indexOf(">") + 1);
//        }
//        return email;
//    }
//
//    /**
//     * Get name of sender
//     * @param Message        Message with name of sender
//     * @return String        Name of sender
//     * */
//    private String parseNameEmail(Message message) throws MessagingException, UnsupportedEncodingException {
//        String name = "";
//        Address[] in = message.getFrom();
//        for (Address address : in) {
//            String decodeAddress = MimeUtility.decodeText(address.toString());
//            if (decodeAddress.indexOf("<") == -1 && decodeAddress.indexOf(">") == -1)
//                name = decodeAddress;
//            else
//                //add name of sender of mail
//                name = decodeAddress.substring(0, decodeAddress.indexOf("<"));
//        }
//        return name;
//    }
//
//    /**
//     * Get send date from mail
//     * @param Message        Message with mail from post server
//     * @return String        Send date of mail
//     * */
//    private String parseDate(Message message) throws MessagingException, UnsupportedEncodingException {
//        String date = "";
//        String[] parts = message.getSentDate().toString().split(" ");
//        date = String.valueOf(parts[3] + message.getSentDate().getDate()) + "."
//                + (message.getSentDate().getMonth() + 1) + "."
//                + (message.getSentDate().getYear() % 100);
//        return date;
//    }
//
//    /**
//     * Get subject from mail
//     * @param Message        Subject of mail from post server
//     * @return String        Subject of mail
//     * */
//    private String parseSubject(Message message) throws MessagingException, UnsupportedEncodingException {
//        String subject = "";
//        subject = message.getSubject();
//        return subject;
//    }
//
//    /**
//     * Get string body of mail
//     * @param Object         Content object
//     * @return String        Content body of mail
//     * */
//    private String parseContentString(Object content) throws MessagingException, UnsupportedEncodingException {
//        String body = "";
//        body = (String) content;
//        return body;
//    }
//
//    /**
//     * Get multipart content of mail
//     * @param Object         Content object
//     * @return String        Content body of mail
//     * */
//    private String parseContentMultipart(Object content) throws MessagingException, IOException {
//        String body = "";
//        Multipart mp = (Multipart) content;
//        BodyPart bp = mp.getBodyPart(0);
//        body = bp.getContent().toString();
//        parseMultipartAttach(mp);
//        return body;
//    }
//
//    /**
//     * Get attach files from mail
//     * @param Multipart        Body of mail
//     * @return void
//     * */
//    private void parseMultipartAttach(Multipart content) throws MessagingException, IOException {
//        for (int j = 0; j < content.getCount(); j++) {
//            BodyPart bodyPart = content.getBodyPart(j);
//            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
//                InputStream is = bodyPart.getInputStream();
//            }
//        }
//    }
}
