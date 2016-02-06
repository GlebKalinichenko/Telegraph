package com.example.gleb.telegraph.sendmail;

import android.os.AsyncTask;

import com.example.gleb.telegraph.DatabaseHelper;
import com.example.gleb.telegraph.ParserMail;
import com.example.gleb.telegraph.events.SendMailEvent;
import com.example.gleb.telegraph.models.Mail;
import com.example.gleb.telegraph.models.MailFolder;
import com.example.gleb.telegraph.models.StraightIndex;
import com.example.gleb.telegraph.models.User;
import com.example.gleb.telegraph.properties.FactoryProperties;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.greenrobot.event.EventBus;

/**
 * Created by gleb on 24.01.16.
 */
public class SendUsualMail extends javax.mail.Authenticator implements SendMailInterface {
    private MailBox mailBox;
    private List<String> attachFiles;
    private DatabaseHelper databaseHelper;

    /**
     * Send usual mail
     * @param String        Subject of mail
     * @param String        Message of mail
     * @param String[]      Array of receivers
     * @param boolean       Is mail has encryption
     * @param boolean       Is mail has digest
     * @param MailSettings  Settings of mail
     * @param MailBox       Email account
     * @return boolean      Is mail send successfully
     * */
    @Override
    public boolean sendMail(String subject, String message, String[] receivers, boolean hasEncryption,
        boolean hasDigest, MailSettings mailSettings, MailBox mailBox, List<String> attachFiles,
        DatabaseHelper databaseHelper) {
        this.mailBox = mailBox;
        this.attachFiles = attachFiles;
        this.databaseHelper = databaseHelper;
        new SendUsualMailAsyncTask(mailBox, mailSettings, message, receivers, subject, attachFiles).execute();
        return true;
    }

    public class SendUsualMailAsyncTask extends AsyncTask<MailSettings, MailBox, String> {
        private MailSettings mailSettings;
        private MailBox mailBox;
        private Session session;
        private Properties props;
        private String subject;
        private String message;
        private String[] receivers;
        private Multipart multipart;
        private MimeMessage msg;
        private List<String> attachFiles;

        public SendUsualMailAsyncTask(MailBox mailBox, MailSettings mailSettings, String message,
            String[] receivers, String subject, List<String> attachFiles) {
            this.mailBox = mailBox;
            this.mailSettings = mailSettings;
            this.message = message;
            this.receivers = receivers;
            this.subject = subject;
            this.attachFiles = attachFiles;
        }

        @Override
        protected String doInBackground(MailSettings... params) {
            multipart = new MimeMultipart();
            final FactoryProperties factoryProperties = new FactoryProperties();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    props = factoryProperties.getProperties(mailSettings, mailBox.getSendProtocol());
                    session = Session.getInstance(props, SendUsualMail.this);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                msg = initializeMessage(multipart, session, mailBox, message, receivers, subject,
                        attachFiles);
                Transport.send(msg);
                //add send mail to database
                try {
                    addSendMail(mailBox, message, (InternetAddress[]) msg.getAllRecipients(),
                            subject, ParserMail.parseDate(msg), new ArrayList<String>(), StraightIndex.REVERSE,
                            databaseHelper);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(new SendMailEvent(true));
            new SyncSendMailAsyncTask(mailBox, mailSettings, msg).execute();
        }
    }

    public class SyncSendMailAsyncTask extends AsyncTask<MailSettings, MailBox, String> {
        private MailSettings mailSettings;
        private MailBox mailBox;
        private Session syncSession;
        private Properties syncProps;
        private Store syncStore;
        private MimeMessage msg;

        public SyncSendMailAsyncTask(MailBox mailBox, MailSettings mailSettings, MimeMessage msg) {
            this.mailBox = mailBox;
            this.mailSettings = mailSettings;
            this.msg = msg;
        }

        @Override
        protected String doInBackground(MailSettings... params) {
            final FactoryProperties factoryProperties = new FactoryProperties();
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    syncProps = factoryProperties.getProperties(mailSettings, mailBox.getReceiveProtocol());
                    syncSession = Session.getInstance(syncProps, SendUsualMail.this);
                    syncStore = factoryProperties.authentication(syncSession, mailSettings,
                            mailBox);
                    try {
                        Folder[] folders = syncStore.getDefaultFolder().list();
                        for (Folder folder : folders){
                            if (MailFolder.isSendFolder(folder)){
                                folder.open(Folder.READ_WRITE);
                                MimeMessage message = msg;
                                folder.appendMessages(new Message[]{message});
                                folder.close(true);
                                syncStore.close();
                            }
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(new SendMailEvent(true));
        }
    }

    /**
     * Initialize message
     * @param Multipart     Multipart of mail
     * @param Session       Session of  send mail
     * @param MailBox       Email account
     * @param String        Message of mail
     * @param String[]      Array of email receivers
     * @param String        Subject of mail
     * @return MimeMessage  Initialize mime message
     * */
    private MimeMessage initializeMessage(Multipart multipart, Session session, final MailBox mailBox,
        final String message, String[] receivers, final String subject, List<String> attachFiles) throws Exception {
        final MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailBox.getEmail()));
        InternetAddress[] addressTo = new InternetAddress[receivers.length];
        for (int i = 0; i < receivers.length; i++)
            addressTo[i] = new InternetAddress(receivers[i]);
        msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        final Date sendDate = new Date();
        msg.setSentDate(sendDate);
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(message);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        if (attachFiles.size() != 0)
           initializeAttachment(multipart, attachFiles);
        return msg;
    }

    /**
     * Add attach file to send mail
     * @param Multipart     Multipart of mail
     * @param List<String>  Array with files to attach
     * @return void
     * */
    private void initializeAttachment(Multipart multipart, List<String> filename) throws Exception {
        for (String name : filename) {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(name);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(name.substring(name.lastIndexOf("/") + 1));
            multipart.addBodyPart(messageBodyPart);
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mailBox.getEmail(), mailBox.getPassword());
    }

    /**
     * Add send mail to database
     * @param MailBox                Email account of mail box
     * @param String                 Message of mail
     * @param InternetAddress[]      Array of receivers emails
     * @param String                 Subject of mail
     * @param String                 Date of send mail
     * @param List<String>           Array of attached files
     * @param SQLiteDatabase         Database
     * @return void
     * */
    private static void addSendMail(final MailBox mailBox, final String message, InternetAddress[] receivers,
        final String subject, final String date, final List<String> attachFiles, int straightIndex,
        final DatabaseHelper databaseHelper){
        final List<Mail> mails = new ArrayList<>();
        final List<Long> usersCode = new ArrayList<>();
        final List<Integer> foldersCode = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (final InternetAddress receiver : receivers) {
            final Thread thread = new Thread(new Runnable() {
                public void run() {
                    if (attachFiles.size() != 0) {
                        Mail mail = new Mail(receiver.getAddress(), receiver.getPersonal(), mailBox.getEmail(),
                                subject, message, date, 1);
                        mails.add(mail);
                    } else {
                        Mail mail = new Mail(receiver.getAddress(), receiver.getAddress(), mailBox.getEmail(),
                                subject, message, date, 0);
                        mails.add(mail);
                    }
                    //if user is no in table Users add it and return his index
                    long userCode = User.checkUserEmail(databaseHelper.getReadableDatabase(),
                            receiver.getAddress());
                    if (userCode == 0) {
                        User user = new User(receiver.getAddress());
                        //add user
                        usersCode.add(user.addUser(databaseHelper));
                    } else
                        //add mail at first return index user and get last folder
                        usersCode.add(userCode);
                    int folderCode = MailFolder.selectFolderWithContainsSend(
                            databaseHelper.getReadableDatabase());
                    foldersCode.add(folderCode);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Mail.addMails(databaseHelper, mails, usersCode, foldersCode, straightIndex, null);
    }
}
