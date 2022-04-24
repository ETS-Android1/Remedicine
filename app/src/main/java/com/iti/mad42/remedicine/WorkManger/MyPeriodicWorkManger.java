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
import com.iti.mad42.remedicine.WorkManger.RefillReminder.RefillReminderOneTimeWorkManager;
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
    List<MedicationPojo> refillMedsList;
    Single<List<MedicationPojo>> medicationSingleList;
    Single<List<MedicationPojo>> refillMedsSingleList;

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
        refillMedsSingleList = repository.getMedicationsToRefillReminder(Calendar.getInstance().getTimeInMillis());
        subscribeOnSingleForMedicationReminder();
        subscribeOnSingleListForRefillReminder();
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
        if(medList != null){
            Log.e("mando", "getCurrentAlarms med list size " +medList.size());
            for (MedicationPojo medication:medList) {
                if(medication.getMedDays().stream().anyMatch(s -> s.contains(Utility.getCurrentDay()))){
                   int indexOfDose =0;
                    for(MedState medState : medication.getMedState()){
                        if (checkPeriod(medState.getTime())){
                            Log.e("mando", "getCurrentAlarms after checking if it passed or not yet: " +medication.getName()+medState.getTime()+medication.getMedDays()+"|||"+indexOfDose);
                            setOnTimeWorkManger(delay,medication,indexOfDose);
                        }
                        indexOfDose++;
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

        timeNow = hour * 60;
        timeNow = (timeNow + minute) * 60 * 1000;
    }

    private void setOnTimeWorkManger(long time, MedicationPojo medicationPOJO,int indexOfDose) {
        Log.e("mando", "setOnTimeWorkManger: "+medicationPOJO.getName()+time );
        Data data = new Data.Builder()
                .putString(MyOneTimeWorkManger.MEDICINE_TAG, serializeToJason(medicationPOJO))
                .putInt(MyOneTimeWorkManger.MEDICINE_DOSE_INDEX,indexOfDose)
                .build();
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        String tag = indexOfDose+medicationPOJO.getName();
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

    private void subscribeOnSingleListForRefillReminder(){
        refillMedsSingleList.subscribe(new SingleObserver<List<MedicationPojo>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<MedicationPojo> medicationPojos) {
                refillMedsList = medicationPojos;
                checkIsNeededToRefill();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void checkIsNeededToRefill(){
        for (MedicationPojo med : refillMedsList){
            if(med.getMedQty() <= med.getReminderMedQtyLeft()){
                Log.e("sandra", "med to refill is: ----> "+med.getName());
                setOneTimeRequestForRefillReminder(med);
            }
        }
    }

    private void setOneTimeRequestForRefillReminder(MedicationPojo med) {
        Data data = new Data.Builder().putString(RefillReminderOneTimeWorkManager.REFILL_TAG, serializeToJason(med)).build();
        Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(true)
                .build();
        String tag = med.getName()+med.getMedQty()+med.getMedOwnerEmail();
        OneTimeWorkRequest refillReminderOneTime = new OneTimeWorkRequest.Builder(RefillReminderOneTimeWorkManager.class)
                .setInputData(data)
                .setConstraints(constraints)
                //.setInitialDelay(3, TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)

                .addTag(tag)
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE,refillReminderOneTime);

    }

}
