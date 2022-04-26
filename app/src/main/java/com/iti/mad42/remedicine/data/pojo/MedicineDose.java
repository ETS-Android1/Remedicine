package com.iti.mad42.remedicine.data.pojo;

import java.io.Serializable;

public class MedicineDose implements Serializable {
    String medForm;
    int medDose;
    long doseTimeInMilliSec;

    public MedicineDose() {
    }

    public MedicineDose(String medForm, int medDose, long doseTimeInMilliSec) {
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

    public long getDoseTimeInMilliSec() {
        return doseTimeInMilliSec;
    }

    public void setDoseTimeInMilliSec(Long doseTimeInMilliSec) {
        this.doseTimeInMilliSec = doseTimeInMilliSec;
    }



    @Override
    public String toString() {
        return "MedicineDose{" +
                "medForm='" + medForm + '\'' +
                ", medDose='" + medDose + '\'' +
                ", doseTimeInMilliSec=" + doseTimeInMilliSec +
                '}';
    }


}
