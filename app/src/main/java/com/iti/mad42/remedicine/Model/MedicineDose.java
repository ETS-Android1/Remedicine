package com.iti.mad42.remedicine.Model;

public class MedicineDose {
    private String type;
    private int dose;
    private int hour;
    private int minute;

    public MedicineDose(){}

    public MedicineDose(String name, int dose, int hour, int minute) {
        this.type = name;
        this.dose = dose;
        this.hour = hour;
        this.minute = minute;
    }

    public String getName() {
        return type;
    }

    public void setName(String name) {
        this.type = name;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
