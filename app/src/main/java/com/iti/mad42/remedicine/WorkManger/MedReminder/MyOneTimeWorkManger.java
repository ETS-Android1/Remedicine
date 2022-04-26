package com.iti.mad42.remedicine.WorkManger.MedReminder;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
}
