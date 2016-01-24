package com.example.gleb.telegraph;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.gleb.telegraph.models.Attach;
import com.example.gleb.telegraph.models.Mail;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;
import com.example.gleb.telegraph.models.StraightIndex;
import com.example.gleb.telegraph.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

/**
 * Created by Gleb on 04.01.2016.
 */
public class ParserMail {
    private String emailReceiver;
    private DatabaseHelper databaseHelper;

    public ParserMail(String emailReceiver, DatabaseHelper databaseHelper) {
        this.emailReceiver = emailReceiver;
        this.databaseHelper = databaseHelper;
    }

    /**
     * Get mails from folders
     * @param Folder[]        Array of folder from post server
     * @return void
     * */
    public void parseFolder(Folder[] folders) throws MessagingException, IOException {
        final List<Integer> ids = MailFolder.addFolders(databaseHelper, folders);
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < folders.length; i++) {
            final Folder f = folders[i];
            final int finalI = i;
            final Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        f.open(Folder.READ_ONLY);
                        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.USER), false);
                        Message[] messages = f.search(ft);
                        if (messages.length != 0)
                            parsePostMessage(messages, ids.get(finalI));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    /**
     * Parse mail from message
     * @param Message[]        Array of message from post server
     * @return void
     * */
    private synchronized void parsePostMessage(Message[] messages, int folderCode) throws MessagingException, IOException {
        List<Mail> mails = new ArrayList<>();
        List<Long> usersCode = new ArrayList<>();
        List<Integer> foldersCode = new ArrayList<>();
        List<List<Attach>> attachs = new ArrayList<>();
        String emailSender = "";
        String nameSender = "";
        String date = "";
        String subject = "";
        String body = "";
        int hasAttach = 0;
        Mail mail;

        if (messages.length > 5) {
            for (int i = messages.length - 1; i > messages.length - 5; i--) {
                emailSender = parseEmailAddress(messages[i]);
                nameSender = parseNameEmail(messages[i]);
                date = parseDate(messages[i]);
                subject = parseSubject(messages[i]);
                Object content = new MimeMessage((MimeMessage) messages[i]).getContent();
                if (content instanceof String)
                    body = parseContentString(content);
                else if (content instanceof Multipart) {
                    body = parseContentMultipart(content);
                    hasAttach = parseMultipartAttach((Multipart) content);
                    if (hasAttach == 1)
                        attachs.add(saveAttach(content));
                }
                mail = new Mail(emailSender, nameSender, emailReceiver, subject, body, date, hasAttach);
                mails.add(mail);

                //if user is no in table Users add it and return his index
                if (User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender) == 0) {
                    User user = new User(emailSender);
                    //add user
                    usersCode.add(user.addUser(databaseHelper));
                } else
                    //add mail at first return index user and get last folder
                    usersCode.add(User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender));
                    foldersCode.add(folderCode);
            }
            Mail.addMails(databaseHelper, mails, usersCode, foldersCode, StraightIndex.STRAIGHT, attachs);
        }
        else {
            for (int i = messages.length - 1; i >= 0; i--) {
                emailSender = parseEmailAddress(messages[i]);
                nameSender = parseNameEmail(messages[i]);
                date = parseDate(messages[i]);
                subject = parseSubject(messages[i]);
                Object content = new MimeMessage((MimeMessage) messages[i]).getContent();
                if (content instanceof String)
                    body = parseContentString(content);
                else if (content instanceof Multipart) {
                    body = parseContentMultipart(content);
                    hasAttach = parseMultipartAttach((Multipart) content);
                    if (hasAttach == 1)
                        attachs.add(saveAttach(content));
                }
                mail = new Mail(emailSender, nameSender, emailReceiver, subject, body, date, hasAttach);
                mails.add(mail);

                //if user is no in table Users add it and return his index
                if (User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender) == 0) {
                    User user = new User(emailSender);
                    //add user
                    usersCode.add(user.addUser(databaseHelper));
                } else
                    //add mail at first return index user and get last folder
                    usersCode.add(User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender));
                foldersCode.add(folderCode);
            }
            Mail.addMails(databaseHelper, mails, usersCode, foldersCode, StraightIndex.STRAIGHT, attachs);
        }
    }

    /**
     * Get email of sender
     * @param Message        Message with email of sender
     * @return String        Email of sender
     * */
    private String parseEmailAddress(Message message) throws MessagingException, UnsupportedEncodingException {
        String email = "";
        Address[] in = message.getFrom();
        for (Address address : in) {
            email = ((InternetAddress) address).getAddress();
        }
        return email;
    }

    /**
     * Get name of sender
     * @param Message        Message with name of sender
     * @return String        Name of sender
     * */
    private String parseNameEmail(Message message) throws MessagingException, UnsupportedEncodingException {
        String name = "";
        Address[] in = message.getFrom();
        for (Address address : in) {
            name = ((InternetAddress) address).getPersonal();
        }
        return name;
    }

    /**
     * Get send date from mail
     * @param Message        Message with mail from post server
     * @return String        Send date of mail
     * */
    private String parseDate(Message message) throws MessagingException, UnsupportedEncodingException {
        String date = "";
        String[] parts = message.getSentDate().toString().split(" ");
        date = String.valueOf(parts[3] + message.getSentDate().getDate()) + "."
                + (message.getSentDate().getMonth() + 1) + "."
                + (message.getSentDate().getYear() % 100);
        return date;
    }

    /**
     * Get subject from mail
     * @param Message        Subject of mail from post server
     * @return String        Subject of mail
     * */
    private String parseSubject(Message message) throws MessagingException, UnsupportedEncodingException {
        String subject = "";
        subject = message.getSubject();
        return subject;
    }

    /**
     * Get string body of mail
     * @param Object         Content object
     * @return String        Content body of mail
     * */
    private String parseContentString(Object content) throws MessagingException, UnsupportedEncodingException {
        String body = "";
        body = (String) content;
        return body;
    }

    /**
     * Get multipart content of mail
     * @param Object         Content object
     * @return String        Content body of mail
     * */
    private String parseContentMultipart(Object content) throws MessagingException, IOException {
        String body = "";
        Multipart mp = (Multipart) content;
        BodyPart bp = mp.getBodyPart(0);
        body = bp.getContent().toString();
        return body;
    }

    /**
     * Get search_attach files from mail
     * @param Multipart        Body of mail
     * @return void
     * */
    private int parseMultipartAttach(Multipart content) throws MessagingException, IOException {
        int hasAttach = 0;
        for (int j = 0; j < content.getCount(); j++) {
            BodyPart bodyPart = content.getBodyPart(j);
            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                InputStream is = bodyPart.getInputStream();
                hasAttach = 1;
            }
        }
        return hasAttach;
    }

    /**
     * Save search_attach files to database
     * @param Object        Body of mail
     * @return void
     * */
    private List<Attach> saveAttach(Object content) throws MessagingException, IOException {
        List<Attach> attachs = new ArrayList<>();
        Multipart context = (Multipart) content;
        for (int j = 0; j < context.getCount(); j++) {
            BodyPart bodyPart = context.getBodyPart(j);
            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                InputStream is = bodyPart.getInputStream();
                String nameAttach = MimeUtility.decodeText(bodyPart.getFileName());
                int posAttach = AttachApplication.add(is);
                Attach attach = new Attach(nameAttach, posAttach);
                attachs.add(attach);
            }
        }
        return attachs;
    }

    /**
     * Get name of sender
     * @param Message        Message with name of sender
     * @return String        Name of sender
     * */
    public static String parseName(String email) {
        String name = email.substring(0, email.indexOf("@"));
        return name;
    }
}
