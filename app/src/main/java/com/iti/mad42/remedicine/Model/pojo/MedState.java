package com.iti.mad42.remedicine.Model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MedState implements Serializable {
        long time;
        String state;

    public MedState() {
    }

    public MedState(long time, String state) {
        this.time = time;
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MedState{" +
                "time=" + time +
                ", state='" + state + '\'' +
                '}';
    }

}
