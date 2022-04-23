package com.iti.mad42.remedicine.WorkManger.MedReminder;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
    public final static String MEDICINE_DOSE_INDEX = "MED_DOSE_INDEX";


    public MyOneTimeWorkManger(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        getData();
        startMyService();
        return Result.success();
    }
    private void getData() {
        data = getInputData();
        Log.e("mando", "data recived: "+data.getString(MEDICINE_TAG)+" "+data.getInt(MEDICINE_DOSE_INDEX,0));
    }
    private void startMyService() {
        Intent intent = new Intent(getApplicationContext(), ReminderService.class);
        intent.putExtra(MEDICINE_TAG, data.getString(MEDICINE_TAG));
        intent.putExtra(MEDICINE_DOSE_INDEX, data.getInt(MEDICINE_DOSE_INDEX,0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(getApplicationContext(), intent);
        } else {
            getApplicationContext().startService(intent);
        }
    }
    public MedicationPojo fomStringPojo(String pojoString) {
        Gson gson = new Gson();
        return gson.fromJson(pojoString, MedicationPojo.class);
    }
}
