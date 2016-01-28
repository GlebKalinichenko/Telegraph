package com.example.gleb.telegraph.sendmail;

import android.os.AsyncTask;

import com.example.gleb.telegraph.properties.FactoryProperties;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by gleb on 24.01.16.
 */
public class SendUsualMail extends javax.mail.Authenticator implements SendMailInterface {
    private MailBox mailBox;
    private List<String> attachFiles;

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
        boolean hasDigest, MailSettings mailSettings, MailBox mailBox, List<String> attachFiles) {
        this.mailBox = mailBox;
        this.attachFiles = attachFiles;
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
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
    private MimeMessage initializeMessage(Multipart multipart, Session session, MailBox mailBox,
        String message, String[] receivers, String subject, List<String> attachFiles) throws Exception {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailBox.getEmail()));
        InternetAddress[] addressTo = new InternetAddress[receivers.length];
        for (int i = 0; i < receivers.length; i++)
            addressTo[i] = new InternetAddress(receivers[i]);
        msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
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
}
