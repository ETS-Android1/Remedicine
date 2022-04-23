package com.iti.mad42.remedicine.WorkManger.RefillReminder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefillReminderOneTimeWorkManager extends Worker {

    public static final String REFILL_TAG = "medReminderList";
    public RefillReminderOneTimeWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data returnedData = getInputData();
        Log.e("sandra ", "data received from refill periodic is: ---> "+returnedData.getString(REFILL_TAG));
        return Result.success();
    }
}
