package com.iti.mad42.remedicine.Model.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static final String [] medForm ={"pill","capsule","Solution","Injection","Powder","Drops","Inhaler"};
    public static final String [] medStrengthUnit ={"g","IU","mcg","mEq","mL","%","mg/g","mg/cm2","mg/ml","mcg/hr"};
    public static final String [] medReminderPerDayList ={"one time per day","two times per day","three times per day"};
    public static final String [] medReminderPerWeekList ={"Every day","Every two days","Every three days","Every Week","Every Month"};
    public static final String myCredentials = "MyCredentials";
    public static final String currentMedFriend = "CurrentMedFriend";

    public static long timeToMillis(int hour , int min){
        return ((hour*60)+min) * 60 * 1000;
    }

    public static String millisToTimeAsString(long timeInMillis){
        int minutes = (int) ((timeInMillis / (1000*60)) % 60);
        int hours   = (int) ((timeInMillis / (1000*60*60)) % 24);
        return (hours+":"+minutes);
    }

    public static String longToDateAsString(long dateInMillis){

        Date d = new Date(dateInMillis);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(d);
    }

    public static long dateToLong(String date){
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        long milliseconds = 0;
        try {
            Date d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public static String getCurrentDay(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
