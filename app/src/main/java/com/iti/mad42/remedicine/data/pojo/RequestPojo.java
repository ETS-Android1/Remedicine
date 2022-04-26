package com.iti.mad42.remedicine.data.pojo;

import java.io.Serializable;

public class RequestPojo implements Serializable {
    private String senderEmail;
    private String recieverEmail;
    private String state;

    public RequestPojo() {
    }

    public RequestPojo(String senderEmail, String recieverEmail, String state) {
        this.senderEmail = senderEmail;
        this.recieverEmail = recieverEmail;
        this.state = state;
    }


    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }



    public String getRecieverEmail() {
        return recieverEmail;
    }

    public void setRecieverEmail(String recieverEmail) {
        this.recieverEmail = recieverEmail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RequestPojo{" +
                "senderEmail='" + senderEmail + '\'' +
                ", recieverEmail='" + recieverEmail + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
