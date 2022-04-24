package com.iti.mad42.remedicine.WorkManger.MedReminder;

import static com.facebook.FacebookSdk.getApplicationContext;

import static android.content.Context.WINDOW_SERVICE;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.facebook.CallbackManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.WorkManger.MyPeriodicWorkManger;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;

import java.util.concurrent.TimeUnit;

public class ReminderDialog {
    MedicationPojo medication;
    int index;
    Context context;
    private WindowManager windowManager;
    private View customNotificationDialogView;
    Repository repository;
    private ImageView reminderDialogCloseBtn;
    private TextView reminderTime;
    private ImageView pillImg;
    private TextView medRemName;
    private TextView reminderMsg;
    private Button skipBtn;
    private Button takeBtn;
    private Button snoozeBtn;
    MediaPlayer mMediaPlayer;
    //Dialog reminderDg;

    public ReminderDialog(MedicationPojo medication, int index, Context context) {
        this.medication = medication;
        this.index = index;
        this.context = context;
        repository = Repository.getInstance(context, ConcreteLocalDataSource.getInstance(context), RemoteDataSource.getInstance(context, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        }));
    }

    public void setMyWindowManger() {
        setMediaPlayer(R.raw.hakim);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customNotificationDialogView = inflater.inflate(R.layout.medication_reminder_dialog, null);
        initView(customNotificationDialogView);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                android.R.attr.showWhenLocked | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
                PixelFormat.TRANSLUCENT);
        windowManager.addView(customNotificationDialogView, params);
    }

    void setMediaPlayer(int id){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(context, id);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();
    }
    private void initView(View view) {
        reminderDialogCloseBtn = (ImageView) view.findViewById(R.id.reminderDialogCloseBtn);
        reminderTime = (TextView) view.findViewById(R.id.reminderTime);
        pillImg = (ImageView) view.findViewById(R.id.pillImg);
        medRemName = (TextView) view.findViewById(R.id.medRemName);
        reminderMsg = (TextView) view.findViewById(R.id.reminderMsg);
        skipBtn = (Button) view.findViewById(R.id.skipBtn);
        takeBtn = (Button) view.findViewById(R.id.takeBtn);
        snoozeBtn = (Button) view.findViewById(R.id.snoozeBtn);
        handleButtons();
    }

    private void handleButtons() {
        reminderDialogCloseBtn.setOnClickListener(v -> {
            stopMyService();
            close();
        });
        takeBtn.setOnClickListener(v -> {
            takeBtn.setEnabled(false);
            reminderDialogCloseBtn.setEnabled(false);
            skipBtn.setEnabled(false);
            snoozeBtn.setEnabled(false);
            int doseLeft = medication.getMedQty() - medication.getMedDoseReminders().get(index).getMedDose();
            medication.setMedQty(doseLeft);
            medication.getMedState().get(index).setState("taken");
            updateMedication(medication);
            setMediaPlayer(R.raw.se7aa);
            setPeriodicWorkManger();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopMyService();
                            close();
                        }
                    },3000);
        });

        skipBtn.setOnClickListener(v -> {
            takeBtn.setEnabled(false);
            reminderDialogCloseBtn.setEnabled(false);
            skipBtn.setEnabled(false);
            snoozeBtn.setEnabled(false);
            setMediaPlayer(R.raw.skip);
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopMyService();
                            close();
                        }
                    },2000);
        });

        snoozeBtn.setOnClickListener(v -> {
            setMediaPlayer(R.raw.snooze);
            setOnTimeWorkManger(medication, index);
            Toast.makeText(context, "Snoozed for 5 minutes", Toast.LENGTH_SHORT).show();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopMyService();
                            close();
                        }
                    },3000);
        });
    }

    private void updateMedication(MedicationPojo medication) {
        repository.updateMedication(medication);
    }

    private void setOnTimeWorkManger(MedicationPojo medicationPOJO,int indexOfDose) {
        // pass medication POJO
        Data data = new Data.Builder()
                .putString(MyOneTimeWorkManger.MEDICINE_TAG, serializeToJason(medicationPOJO))
                .putInt(MyOneTimeWorkManger.MEDICINE_DOSE_INDEX, indexOfDose)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        String tag = indexOfDose+medicationPOJO.getName();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyOneTimeWorkManger.class).
                setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag(tag)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
    }

    private void setPeriodicWorkManger(){
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                3, TimeUnit.HOURS)
                .setInitialDelay(2,TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private String serializeToJason(MedicationPojo pojo) {
        Gson gson = new Gson();
        return gson.toJson(pojo);
    }

    private void close() {
        try {
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(customNotificationDialogView);
            customNotificationDialogView.invalidate();
            ((ViewGroup) customNotificationDialogView.getParent()).removeAllViews();
        } catch (Exception e) {
            Log.d("mando", e.toString());
        }
    }

    private void stopMyService() {
        mMediaPlayer.stop();
        context.stopService(new Intent(context, ReminderService.class));
    }
}
