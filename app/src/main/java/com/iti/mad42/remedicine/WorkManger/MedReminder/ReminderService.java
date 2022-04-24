package com.iti.mad42.remedicine.WorkManger.MedReminder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;

public class ReminderService extends Service {

    private MedicationPojo myMedicine;

    int index;
    final static int CHANNEL_ID = 74;
    final static int FOREGROUND_ID = 27;
    NotificationManager notificationManager;
    String description;
    ReminderDialog reminderDialog;
    private NotificationManagerCompat notificationManagerCompat;



    public ReminderService() {
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        myMedicine = fomStringPojo(intent.getStringExtra(MyOneTimeWorkManger.MEDICINE_TAG));
        index = intent.getIntExtra(MyOneTimeWorkManger.MEDICINE_DOSE_INDEX,0);
        notificationChannel();
        startForeground(FOREGROUND_ID, makeNotification());
        if (Settings.canDrawOverlays(this)) {
            reminderDialog = new ReminderDialog(myMedicine,index,getApplicationContext());
            reminderDialog.setMyWindowManger();
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public MedicationPojo fomStringPojo(String pojoString) {
        Gson gson = new Gson();
        return gson.fromJson(pojoString, MedicationPojo.class);
    }
    private Notification makeNotification() {
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        description = "take " + myMedicine.getMedDoseReminders().get(index).getMedDose() + " " + Utility.medForm[myMedicine.getFormIndex()] + "(s), " +
                myMedicine.getStrength() + Utility.medStrengthUnit[myMedicine.getStrengthUnitIndex()] +" , "+ myMedicine.getInstructions();
        Notification notification = new NotificationCompat.Builder(getApplicationContext(),
                String.valueOf(CHANNEL_ID))
                .setSmallIcon(R.drawable.logo)
                .setContentText("Schedule for " + myMedicine.getName() + ", today")
                .setContentTitle("Medication Reminder")
                //.setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .build();
        notificationManagerCompat.notify(1,notification);
        return notification;

    }

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MedReminderChannel";
            String description = "MedReminderChannelDescription";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(String.valueOf(CHANNEL_ID),
                    name, importance);
            channel.setDescription(description);
            notificationManager =
                    this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}