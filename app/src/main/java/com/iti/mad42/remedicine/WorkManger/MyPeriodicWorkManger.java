package com.iti.mad42.remedicine.WorkManger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.WorkManger.MedReminder.MyOneTimeWorkManger;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MyPeriodicWorkManger extends Worker {

    long delay;
    long timeNow;

    Repository repository;
    Context context;
    List<MedicationPojo> medList;
    Single<List<MedicationPojo>> medicationSingleList;


    public MyPeriodicWorkManger(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context= context;
        repository = Repository.getInstance(context, ConcreteLocalDataSource.getInstance(context), RemoteDataSource.getInstance(context, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        }));
    }
    @NonNull
    @Override
    public Result doWork() {

        medicationSingleList = repository.getAllMedicationsList();
        subscribeOnSingleForMedicationReminder();
        getTimePeriod();
        getCurrentAlarms();

        return Result.success();

    }
    private void subscribeOnSingleForMedicationReminder() {

        medicationSingleList.subscribe(new SingleObserver<List<MedicationPojo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<MedicationPojo> medicationPOJOS) {

                medList = medicationPOJOS;
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @SuppressLint("NewApi")
    private void getCurrentAlarms() {
        Log.e("mando", "getCurrentAlarms med list size " +medList.size());

        if(medList != null){
            Log.e("mando", "getCurrentAlarms med list size " +medList.size());
            for (MedicationPojo medication:medList) {
                Log.e("mando", "getCurrentAlarms before matching days: " +medication.getName()+medication.getMedState()+medication.getMedDays());
                Log.e("mando", "getCurrentAlarms before matching days: " +Utility.getCurrentDay()+"||||"+medication.getMedDays());

                if(medication.getMedDays().stream().anyMatch(s -> s.contains(Utility.getCurrentDay()))){
                    Log.e("mando", "getCurrentAlarms after matching days: " +medication.getName()+medication.getMedState()+medication.getMedDays());
                    for(MedState medState : medication.getMedState()){
                        Log.e("mando", "getCurrentAlarms before checking if it passed or not yet: " +medication.getName()+medState.getTime()+medication.getMedDays());

                        if (checkPeriod(medState.getTime())){
                            Log.e("mando", "getCurrentAlarms after checking if it passed or not yet: " +medication.getName()+medState.getTime()+medication.getMedDays());
                            setOnTimeWorkManger(delay,medication);
                        }
                    }
                }
            }
        }

    }

    private boolean checkPeriod(long medTime) {
        delay = medTime - timeNow;
        if (delay>0) {
            return true;
        }
        return false;
    }
    private void getTimePeriod() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour == 0) {
            hour += 12;
        }
        timeNow = hour * 60;
        timeNow = (timeNow + minute) * 60 * 1000;
    }

    private void setOnTimeWorkManger(long time, MedicationPojo medicationPOJO) {
        Log.e("mando", "setOnTimeWorkManger: "+medicationPOJO.getName()+time );
        Data data = new Data.Builder()
                .putString(MyOneTimeWorkManger.MEDICINE_TAG, serializeToJason(medicationPOJO))
                .build();
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        String tag = time+medicationPOJO.getName();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyOneTimeWorkManger.class).
                setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE,oneTimeWorkRequest);
    }
    private String serializeToJason(MedicationPojo pojo) {
        Gson gson = new Gson();
        return gson.toJson(pojo);
    }

}
