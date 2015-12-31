package com.example.gleb.telegraph.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gleb.telegraph.DatabaseHelper;

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

    public MailSettings(String namePostServer, String addressImap, String portImap, String addressPop3, String portPop3, String addressSmtp, String portSmtp) {
        this.namePostServer = namePostServer;
        this.addressImap = addressImap;
        this.portImap = portImap;
        this.addressPop3 = addressPop3;
        this.portPop3 = portPop3;
        this.addressSmtp = addressSmtp;
        this.portSmtp = portSmtp;
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
