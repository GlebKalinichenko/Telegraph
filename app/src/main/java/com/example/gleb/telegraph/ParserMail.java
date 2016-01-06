package com.example.gleb.telegraph;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.models.Attach;
import com.example.gleb.telegraph.models.Mail;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;
import com.example.gleb.telegraph.models.StraightIndex;
import com.example.gleb.telegraph.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
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

    /**
     * Parse mail from message
     * @param Message[]        Array of message from post server
     * @return void
     * */
    private void parsePostMessage(Message[] messages) throws MessagingException, IOException {
        String emailSender = "";
        String nameSender = "";
        String date = "";
        String subject = "";
        String body = "";
        int hasAttach = 0;
        Mail mail;
        if (messages.length > 5)
            for (int i = messages.length - 1; i > messages.length - 5; i--){
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
                }
                mail = new Mail(emailSender, nameSender, emailReceiver, subject, body, date,
                        hasAttach, StraightIndex.STRAIGHT);
                //if user is no in table Users add it and return his index
                if (User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender) == 0) {
                    User user = new User(emailSender);
                    //add user
                    user.addUser(databaseHelper.getReadableDatabase());
                    //add mail at first return index last inserted user and get last folder
                    mail.addMail(databaseHelper.getWritableDatabase(),
                            User.getLastUser(databaseHelper.getReadableDatabase()),
                            MailFolder.getLastFolder(databaseHelper.getReadableDatabase()));
                }else
                    //add mail at first return index user and get last folder
                    mail.addMail(databaseHelper.getWritableDatabase(),
                            User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender),
                            MailFolder.getLastFolder(databaseHelper.getReadableDatabase()));

                //get attach files from post server
                if (hasAttach == 1){
                    Multipart context = (Multipart) content;
                    for (int j = 0; j < context.getCount(); j++) {
                        BodyPart bodyPart = context.getBodyPart(j);
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                            InputStream is = bodyPart.getInputStream();
                            String nameAttach = bodyPart.getFileName();
                            int posAttach = AttachApplication.add(is);
                            Attach attach = new Attach(nameAttach, posAttach);
                            //add attach to database
                            attach.addAttach(databaseHelper.getWritableDatabase(),
                                    Mail.getLastMail(databaseHelper.getReadableDatabase()));
                        }
                    }
                }
            }
        else
            for (int i = messages.length - 1; i >= 0; i--){
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
                }
                mail = new Mail(emailSender, nameSender, emailReceiver, subject, body, date,
                        hasAttach, StraightIndex.STRAIGHT);
                //if user is no in table Users add it and return his index
                if (User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender) == 0) {
                    User user = new User(emailSender);
                    //add user
                    user.addUser(databaseHelper.getReadableDatabase());
                    //add mail at first return index last inserted user and get last folder
                    mail.addMail(databaseHelper.getWritableDatabase(),
                            User.getLastUser(databaseHelper.getReadableDatabase()),
                            MailFolder.getLastFolder(databaseHelper.getReadableDatabase()));
                }else
                    //add mail at first return index user and get last folder
                    mail.addMail(databaseHelper.getWritableDatabase(),
                            User.checkUserEmail(databaseHelper.getReadableDatabase(), emailSender),
                            MailFolder.getLastFolder(databaseHelper.getReadableDatabase()));

                //get attach files from post server
                if (hasAttach == 1){
                    Multipart context = (Multipart) content;
                    for (int j = 0; j < context.getCount(); j++) {
                        BodyPart bodyPart = context.getBodyPart(j);
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                            InputStream is = bodyPart.getInputStream();
                            String nameAttach = bodyPart.getFileName();
                            int posAttach = AttachApplication.add(is);
                            Attach attach = new Attach(nameAttach, posAttach);
                            //add attach to database
                            attach.addAttach(databaseHelper.getWritableDatabase(),
                                    Mail.getLastMail(databaseHelper.getReadableDatabase()));
                        }
                    }
                }
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
            String decodeAddress = MimeUtility.decodeText(address.toString());
            if (decodeAddress.indexOf("<") == -1 && decodeAddress.indexOf(">") == -1)
                email = decodeAddress;
            else
                //add email of sender of mail
                email = decodeAddress.substring(decodeAddress.indexOf("<") + 1, decodeAddress.indexOf(">"));
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
            String decodeAddress = MimeUtility.decodeText(address.toString());
            if (decodeAddress.indexOf("<") == -1 && decodeAddress.indexOf(">") == -1)
                name = decodeAddress;
            else
                //add name of sender of mail
                name = decodeAddress.substring(0, decodeAddress.indexOf("<"));
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
     * Get attach files from mail
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
}
