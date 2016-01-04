package com.example.gleb.telegraph;

import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailFolder;

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
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

/**
 * Created by Gleb on 04.01.2016.
 */
public class ParserMail {
    /**
     * Get mails from folders
     * @param Folder[]        Array of folder from post server
     * @return void
     * */
    public void parseFolder(Folder[] folders, DatabaseHelper databaseHelper) throws MessagingException, IOException {
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
        List<String> emails = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> subjects = new ArrayList<>();
        List<String> bodies = new ArrayList<>();
        if (messages.length > 5)
            for (int i = messages.length - 1; i > messages.length - 5; i--){
                emails.add(parseEmailAddress(messages[i]));
                names.add(parseNameEmail(messages[i]));
                dates.add(parseDate(messages[i]));
                subjects.add(parseSubject(messages[i]));
                Object content = new MimeMessage((MimeMessage) messages[i]).getContent();
                if (content instanceof String)
                    bodies.add(parseContentString(content));
                else if (content instanceof Multipart)
                    bodies.add(parseContentMultipart(content));
            }
        else
            for (int i = messages.length - 1; i >= 0; i--){
                emails.add(parseEmailAddress(messages[i]));
                names.add(parseNameEmail(messages[i]));
                dates.add(parseDate(messages[i]));
                subjects.add(parseSubject(messages[i]));
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
                email = decodeAddress.substring(decodeAddress.indexOf("<"), decodeAddress.indexOf(">") + 1);
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
        parseMultipartAttach(mp);
        return body;
    }

    /**
     * Get attach files from mail
     * @param Multipart        Body of mail
     * @return void
     * */
    private void parseMultipartAttach(Multipart content) throws MessagingException, IOException {
        for (int j = 0; j < content.getCount(); j++) {
            BodyPart bodyPart = content.getBodyPart(j);
            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                InputStream is = bodyPart.getInputStream();
            }
        }
    }
}
