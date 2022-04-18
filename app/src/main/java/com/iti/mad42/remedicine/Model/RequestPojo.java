package com.iti.mad42.remedicine.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestPojo implements Parcelable {
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

    public static final Creator<RequestPojo> CREATOR = new Creator<RequestPojo>() {
        @Override
        public RequestPojo createFromParcel(Parcel in) {
            return new RequestPojo(in);
        }

        @Override
        public RequestPojo[] newArray(int size) {
            return new RequestPojo[size];
        }
    };

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

    protected RequestPojo(Parcel in){
        senderEmail = in.readString();
        senderId = in.readString();
        recieverEmail = in.readString();
        recieverId = in.readString();
        state = in.readString();
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

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(senderEmail);
        dest.writeString(senderId);
        dest.writeString(recieverEmail);
        dest.writeString(recieverId);
        dest.writeString(state);
    }
}
