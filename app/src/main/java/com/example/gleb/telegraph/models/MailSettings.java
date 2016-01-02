package com.example.gleb.telegraph.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.gleb.telegraph.DatabaseHelper;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailSettings {
    private String namePostServer;
    private String addressImap;
    private String portImap;
    private String addressPop3;
    private String portPop3;
    private String addressSmtp;
    private String portSmtp;

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

        MailSettings mailSettings = new MailSettings();
        mailSettings.setNamePostServer(urlServer);
        mailSettings.setAddressImap(imapHost);
        mailSettings.setPortImap(imapPort);
        mailSettings.setAddressPop3(pop3Host);
        mailSettings.setPortPop3(pop3Port);
        mailSettings.setAddressSmtp(smtpHost);
        mailSettings.setPortSmtp(smtpPort);

        return mailSettings;
    }

    /**
     * Add settings to database
     * @param SQLiteDatabase        Database
     * @return void
     * */
    public void addSettings(SQLiteDatabase sdb){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NAME_POST_SERVER, this.namePostServer);
        values.put(DatabaseHelper.ADDRESS_IMAP, this.addressImap);
        values.put(DatabaseHelper.PORT_IMAP, this.portImap);
        values.put(DatabaseHelper.ADDRESS_POP3, this.addressPop3);
        values.put(DatabaseHelper.PORT_POP3, this.portPop3);
        values.put(DatabaseHelper.ADDRESS_SMTP, this.addressSmtp);
        values.put(DatabaseHelper.PORT_SMTP, this.portSmtp);

        sdb.insert(DatabaseHelper.TABLE_MAIL_SETTINGS, null, values);
        sdb.close();
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
