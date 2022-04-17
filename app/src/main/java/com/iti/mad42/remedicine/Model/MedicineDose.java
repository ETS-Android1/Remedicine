package com.iti.mad42.remedicine.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineDose implements Parcelable {
    String medForm;
    int medDose;
    Long doseTimeInMilliSec;

    public MedicineDose() {
    }


    public MedicineDose(String medForm, int medDose, Long doseTimeInMilliSec) {
        this.medForm = medForm;
        this.medDose = medDose;
        this.doseTimeInMilliSec = doseTimeInMilliSec;
    }

    public String getMedForm() {
        return medForm;
    }

    public void setMedForm(String medForm) {
        this.medForm = medForm;
    }

    public int getMedDose() {
        return medDose;
    }

    public void setMedDose(int medDose) {
        this.medDose = medDose;
    }

    public Long getDoseTimeInMilliSec() {
        return doseTimeInMilliSec;
    }

    public void setDoseTimeInMilliSec(Long doseTimeInMilliSec) {
        this.doseTimeInMilliSec = doseTimeInMilliSec;
    }

    public static final Creator<MedicineDose> CREATOR = new Creator<MedicineDose>() {
        @Override
        public MedicineDose createFromParcel(Parcel in) {
            return new MedicineDose(in);
        }

        @Override
        public MedicineDose[] newArray(int size) {
            return new MedicineDose[size];
        }
    };

    @Override
    public String toString() {
        return "MedicineDose{" +
                "medForm='" + medForm + '\'' +
                ", medDose='" + medDose + '\'' +
                ", doseTimeInMilliSec=" + doseTimeInMilliSec +
                '}';
    }
    protected MedicineDose(Parcel in){
        medForm = in.readString();
        medDose = in.readInt();
        doseTimeInMilliSec=in.readLong();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(medForm);
        dest.writeInt(medDose);
        dest.writeLong(doseTimeInMilliSec);
    }
}
