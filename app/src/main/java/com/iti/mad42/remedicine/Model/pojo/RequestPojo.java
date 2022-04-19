package com.iti.mad42.remedicine.Model.pojo;

import java.io.Serializable;

public class RequestPojo implements Serializable {
    private String senderEmail;
    private String senderId;
    private String recieverEmail;
    private String recieverId;
    private String state;

    public RequestPojo() {
    }

    public RequestPojo(String senderEmail, String senderId, String recieverEmail, String recieverId, String state) {
        this.senderEmail = senderEmail;
        this.senderId = senderId;
        this.recieverEmail = recieverEmail;
        this.recieverId = recieverId;
        this.state = state;
    }


    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverEmail() {
        return recieverEmail;
    }

    public void setRecieverEmail(String recieverEmail) {
        this.recieverEmail = recieverEmail;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
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
                ", senderId='" + senderId + '\'' +
                ", recieverEmail='" + recieverEmail + '\'' +
                ", recieverId='" + recieverId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
