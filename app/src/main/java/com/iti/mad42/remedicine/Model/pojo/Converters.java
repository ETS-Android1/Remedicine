package com.iti.mad42.remedicine.Model.pojo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {
    Gson gson = new Gson();

    //To convert the medDoseReminders List
    @TypeConverter
    public String fromMedDoseRemindersToString(List<MedicineDose> medDoseRemindersList) {
        return gson.toJson(medDoseRemindersList);
    }

    @TypeConverter
    public List<MedicineDose> fromStringToMedDoseReminders(String medDoseRemindersString) {
        if (medDoseRemindersString == null) {
            return Collections.emptyList();
        } else {
            Type list = new TypeToken<List<Boolean>>() {
            }.getType();
            return gson.fromJson(medDoseRemindersString, list);
        }
    }


    //To convert the medDays List
    @TypeConverter
    public String fromMedDaysToString(List<String> medDaysList) {
        return gson.toJson(medDaysList);
    }

    @TypeConverter
    public List<String> fromStringToMedDays(String medDaysString) {
        if (medDaysString == null) {
            return Collections.emptyList();
        } else {
            Type list = new TypeToken<List<Boolean>>() {
            }.getType();
            return gson.fromJson(medDaysString, list);
        }
    }


    //To convert the medState List
    @TypeConverter
    public String fromMedStateToString(List<MedState> medState) {
        return gson.toJson(medState);
    }

    @TypeConverter
    public List<MedState> fromStringToMedState(String medStateString) {
        if (medStateString == null) {
            return Collections.emptyList();
        } else {
            Type list = new TypeToken<List<Boolean>>() {
            }.getType();
            return gson.fromJson(medStateString, list);
        }
    }
}
