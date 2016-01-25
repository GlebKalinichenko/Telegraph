package com.example.gleb.telegraph;

import android.os.AsyncTask;

import com.example.gleb.telegraph.connection.FactoryConnection;
import com.example.gleb.telegraph.models.MailBox;
import com.example.gleb.telegraph.models.MailSettings;

import java.util.Date;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
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

    @Override
    public boolean sendMail(String subject, String message, String[] receivers, boolean hasEncryption,
        boolean hasDigest, MailSettings mailSettings, MailBox mailBox) {
        this.mailBox = mailBox;
        new SendUsualMailAsyncTask(mailBox, mailSettings, message, receivers, subject).execute();
        return true;
    }

    public class SendUsualMailAsyncTask extends AsyncTask<MailSettings, MailBox, String> {
        private MailSettings mailSettings;
        private MailBox mailBox;
        private Session session;
        private String subject;
        private String message;
        private String[] receivers;
        private Multipart multipart;

        public SendUsualMailAsyncTask(MailBox mailBox, MailSettings mailSettings, String message, String[] receivers, String subject) {
            this.mailBox = mailBox;
            this.mailSettings = mailSettings;
            this.message = message;
            this.receivers = receivers;
            this.subject = subject;
        }

        @Override
        protected String doInBackground(MailSettings... params) {
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);

            multipart = new MimeMultipart();
            final FactoryConnection factoryConnection = new FactoryConnection();
            session = factoryConnection.getSession(mailSettings, mailBox.getSendProtocol());
            MimeMessage msg = new MimeMessage(session);
            try {
                msg.setFrom(new InternetAddress(mailBox.getEmail()));
                InternetAddress[] addressTo = new InternetAddress[receivers.length];
                for (int i = 0; i < receivers.length; i++) {
                    addressTo[i] = new InternetAddress(receivers[i]);
                }
                msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
                msg.setSubject(subject);
                msg.setSentDate(new Date());
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(message);
                multipart.addBodyPart(messageBodyPart);
                msg.setContent(multipart);
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

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mailBox.getEmail(), mailBox.getPassword());
    }
}
