package com.iti.mad42.remedicine.WorkManger.RefillReminder;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.iti.mad42.remedicine.WorkManger.MedReminder.ReminderService;

public class RefillReminderOneTimeWorkManager extends Worker {

    Data returnedData;

    public static final String REFILL_TAG = "medReminderList";
    public RefillReminderOneTimeWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        returnedData = getInputData();
        Log.e("sandra ", "data received from refill periodic is: ---> "+returnedData.getString(REFILL_TAG));
        startMyService();
        return Result.success();
    }

    private void startMyService() {
        Intent intent = new Intent(getApplicationContext(), RefillReminderService.class);
        intent.putExtra(REFILL_TAG, returnedData.getString(REFILL_TAG));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(getApplicationContext(), intent);
        } else {
            getApplicationContext().startService(intent);
        }
    }
}
