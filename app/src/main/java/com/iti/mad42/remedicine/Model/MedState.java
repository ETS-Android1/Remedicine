package com.iti.mad42.remedicine.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MedState implements Parcelable {
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

    public static final Creator<MedState> CREATOR = new Creator<MedState>() {
        @Override
        public MedState createFromParcel(Parcel in) {
            return new MedState(in);
        }

        @Override
        public MedState[] newArray(int size) {
            return new MedState[size];
        }
    };

    protected MedState(Parcel in){
        time = in.readLong();
        state = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(time);
        dest.writeString(state);
    }
}
