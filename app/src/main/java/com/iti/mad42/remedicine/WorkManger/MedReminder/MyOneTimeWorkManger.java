package com.iti.mad42.remedicine.WorkManger.MedReminder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

public class MyOneTimeWorkManger extends Worker {

    Data data;

    public final static String MEDICINE_TAG = "MED";


    public MyOneTimeWorkManger(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        getData();
        return Result.success();
    }
    private void getData() {
        data = getInputData();
        Log.e("mando", "data recived: "+data.getString(MEDICINE_TAG));
    }

    public MedicationPojo fomStringPojo(String pojoString) {
        Gson gson = new Gson();
        return gson.fromJson(pojoString, MedicationPojo.class);
    }
}
