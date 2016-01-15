package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.*;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailSettings implements Serializable {
    private String namePostServer;
    private String addressImap;
    private String portImap;
    private String addressPop3;
    private String portPop3;
    private String addressSmtp;
    private String portSmtp;

    public MailSettings(String namePostServer, String addressImap, String portImap, String addressPop3, String portPop3, String addressSmtp, String portSmtp) {
        this.namePostServer = namePostServer;
        this.addressImap = addressImap;
        this.portImap = portImap;
        this.addressPop3 = addressPop3;
        this.portPop3 = portPop3;
        this.addressSmtp = addressSmtp;
        this.portSmtp = portSmtp;
    }

    /**
     * Parse settings from api in format xml and create settings
     * @param String            Url address of server
     * @param Document          Settings gets from server in format xml
     * @return MailSettings     Created settings
     * */
    public static MailSettings newInstance(String urlServer, Document doc) {
        doc.getDocumentElement().normalize();
        NodeList hostList = doc.getElementsByTagName("hostname");
        NodeList portList = doc.getElementsByTagName("port");

        String imapHost = hostList.item(0).getFirstChild().getNodeValue();
        String pop3Host = hostList.item(1).getFirstChild().getNodeValue();
        String smtpHost = hostList.item(2).getFirstChild().getNodeValue();

        String imapPort = portList.item(0).getFirstChild().getNodeValue();
        String pop3Port = portList.item(1).getFirstChild().getNodeValue();
        String smtpPort = portList.item(2).getFirstChild().getNodeValue();

        MailSettings mailSettings = new MailSettings(urlServer, imapHost, imapPort, pop3Host,
                pop3Port, smtpHost, smtpPort);

        return mailSettings;
    }

//    /**
//     * Authentication on post server
//     * @param String        Email of account
//     * @param String        Password of account
//     * @return boolean      Is authentication complete
//     * */
//    public boolean authentication(String email, String password, boolean isImap){
//        Properties props = new Properties();
//        if (isImap) {
//            props.put("mail.imap.port", this.portImap);
//            props.put("mail.imap.socketFactory.port", this.portImap);
//            props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.imap.socketFactory.fallback", "false");
//            props.setProperty("mail.store.protocol", "imaps");
//        }
//        else{
//            props.put("mail.pop3.port", this.portPop3);
//            props.put("mail.pop3.socketFactory.port", this.portPop3);
//            props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.pop3.socketFactory.fallback", "false");
//            props.setProperty("mail.store.protocol", "pop3");
//        }
//
//        Session session = Session.getInstance(props, null);
//        Store store = null;
//        try {
//            store = session.getStore();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//            return false;
//        }
//        try {
//            if (isImap) {
//                store.connect(this.addressImap, email, password);
//            }
//            else
//                store.connect(this.addressPop3, email, password);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }

    /**
     * Add settings to database
     * @param SQLiteDatabase        Database
     * @return void
     * */
    public void addSettings(SQLiteDatabase sdb, int boxCode){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NAME_POST_SERVER, this.namePostServer);
        values.put(DatabaseHelper.ADDRESS_IMAP, this.addressImap);
        values.put(DatabaseHelper.PORT_IMAP, this.portImap);
        values.put(DatabaseHelper.ADDRESS_POP3, this.addressPop3);
        values.put(DatabaseHelper.PORT_POP3, this.portPop3);
        values.put(DatabaseHelper.ADDRESS_SMTP, this.addressSmtp);
        values.put(DatabaseHelper.PORT_SMTP, this.portSmtp);
        values.put(DatabaseHelper.BOX_CODE, boxCode);
        sdb.insert(DatabaseHelper.TABLE_MAIL_SETTINGS, null, values);
    }

    public String getNamePostServer() {
        return namePostServer;
    }

    public void setNamePostServer(String namePostServer) {
        this.namePostServer = namePostServer;
    }

    public String getAddressImap() {
        return addressImap;
    }

    public void setAddressImap(String addressImap) {
        this.addressImap = addressImap;
    }

    public String getPortImap() {
        return portImap;
    }

    public void setPortImap(String portImap) {
        this.portImap = portImap;
    }

    public String getAddressPop3() {
        return addressPop3;
    }

    public void setAddressPop3(String addressPop3) {
        this.addressPop3 = addressPop3;
    }

    public String getPortPop3() {
        return portPop3;
    }

    public void setPortPop3(String portPop3) {
        this.portPop3 = portPop3;
    }

    public String getAddressSmtp() {
        return addressSmtp;
    }

    public void setAddressSmtp(String addressSmtp) {
        this.addressSmtp = addressSmtp;
    }

    public String getPortSmtp() {
        return portSmtp;
    }

    public void setPortSmtp(String portSmtp) {
        this.portSmtp = portSmtp;
    }
}
