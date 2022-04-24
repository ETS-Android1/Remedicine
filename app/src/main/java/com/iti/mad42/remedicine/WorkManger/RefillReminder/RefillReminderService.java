package com.iti.mad42.remedicine.WorkManger.RefillReminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;

public class RefillReminderService extends Service {

    private MedicationPojo medication;
    final static int CHANNEL_ID = 30;
    final static int FOREGROUND_ID = 40;
    NotificationManager notificationManager;
    RefillWindow refillWindow;
    String description;

    public RefillReminderService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        medication = fomStringPojo(intent.getStringExtra(RefillReminderOneTimeWorkManager.REFILL_TAG));
        notificationChannel();
        startForeground(FOREGROUND_ID, makeNotification());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)){
                refillWindow = new RefillWindow(getApplicationContext(), medication);
                refillWindow.setRefillWindowManager();
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MedicationPojo fomStringPojo(String pojoString) {
        Gson gson = new Gson();
        return gson.fromJson(pojoString, MedicationPojo.class);
    }
    private Notification makeNotification() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), myMedicine.getImageID());

        description = "It's time to refill your Meds, you need to refill "+medication.getName()+" today";
        return new NotificationCompat.Builder(getApplicationContext(),
                String.valueOf(CHANNEL_ID))
                .setSmallIcon(R.drawable.logo)
                .setContentText("Schedule for " + medication.getName() + ", today")
                .setContentTitle("Refill Reminder")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .build();

    }

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "RefillReminderChannel";
            String description = "RefillReminderChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(String.valueOf(CHANNEL_ID),
                    name, importance);
            channel.setDescription(description);
            notificationManager =
                    this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
