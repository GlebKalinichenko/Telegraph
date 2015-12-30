package com.example.gleb.telegraph.models;

/**
 * Created by Gleb on 30.12.2015.
 */
public class MailSettings {
    private String namePostServer;
    private String addressImap;
    private String portImap;
    private String addressPop3;
    private String portPop3;

    public MailSettings(String namePostServer, String addressImap, String portImap, String addressPop3, String portPop3) {
        this.namePostServer = namePostServer;
        this.addressImap = addressImap;
        this.portImap = portImap;
        this.addressPop3 = addressPop3;
        this.portPop3 = portPop3;
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
}
