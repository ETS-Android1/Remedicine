package com.iti.mad42.remedicine.WorkManger.RefillReminder;

import static android.content.Context.WINDOW_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.WorkManger.MedReminder.ReminderService;
import com.iti.mad42.remedicine.WorkManger.MyPeriodicWorkManger;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;

import java.util.concurrent.TimeUnit;

public class RefillWindow {
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    private ImageView refillAlertCloseBtn;
    private TextView medName;
    private Button skipBtn;
    private Button refillBtn;
    private Button snoozeBtn;
    EditText refillEdt;
    private RepositoryInterface repository;
    private MedicationPojo medication;
    Dialog refillDialog;
    int amount;

    public RefillWindow(Context context, MedicationPojo medication) {
        this.context = context;
        this.medication = medication;
        refillDialog = new Dialog(context);

        repository = Repository.getInstance(context, ConcreteLocalDataSource.getInstance(context), RemoteDataSource.getInstance(context, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        }));
    }

    public void setRefillWindowManager(){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.medication_refill_alert, null);
        initView(mView);
        int layoutFlag;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutFlag,
                android.R.attr.showWhenLocked | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mView, mParams);
    }

    public void initView(View view){
        refillAlertCloseBtn = view.findViewById(R.id.refillDialogCloseBtn);
        medName = view.findViewById(R.id.refillMedName);
        skipBtn =view.findViewById(R.id.refillSkipBtn);
        refillBtn = view.findViewById(R.id.refillTakeBtn);
        snoozeBtn = view.findViewById(R.id.refillSnoozeBtn);
        refillEdt = view.findViewById(R.id.refillEdit);
        handleRefillDialogBtns();
    }
    private void handleRefillDialogBtns(){

        refillEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                refillEdt.setCursorVisible(true);
                WindowManager.LayoutParams floatWindowLayoutParamUpdateFlag = mParams;

                floatWindowLayoutParamUpdateFlag.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

                // WindowManager is updated with the Updated Parameters
                mWindowManager.updateViewLayout(mView, floatWindowLayoutParamUpdateFlag);
                return false;
            }
        });

        refillAlertCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMyService();
                close();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.stopService(new Intent(context, RefillReminderService.class));
                stopMyService();
                close();
            }
        });

        refillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(refillEdt.getText().toString());
                setRefillAmount(amount);
                refillMed();
                setPeriodicWorkManger();
                stopMyService();
                close();

            }
        });

        snoozeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRefillOneTimeWorkManager(medication);
                Toast.makeText(context, "Snoozed for 5 minutes", Toast.LENGTH_SHORT).show();
                stopMyService();
                close();
            }
        });


    }

    public void refillMed(){
        medication.setMedQty(amount);
        updateMed(medication);
        repository.updateMedicationToFirebase(medication);
    }
    public void setRefillAmount(int amount){
        this.amount=amount;
    }


    private String serializeToJason(MedicationPojo pojo) {
        Gson gson = new Gson();
        return gson.toJson(pojo);
    }

    private void updateMed(MedicationPojo medication){
        repository.updateMedication(medication);
    }

    private void setRefillOneTimeWorkManager(MedicationPojo medication){

        Data data = new Data.Builder().putString(RefillReminderOneTimeWorkManager.REFILL_TAG, serializeToJason(medication))
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        String tag = medication.getName()+medication.getMedQty()+medication.getMedOwnerEmail();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefillReminderOneTimeWorkManager.class)
                .setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(5 , TimeUnit.MINUTES)
                .addTag(tag)
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE,oneTimeWorkRequest);

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

    private void close() {
        try {
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView.invalidate();
            ((ViewGroup) mView.getParent()).removeAllViews();
        } catch (Exception e) {
            Log.d("sandra", e.toString());
        }
    }

    private void stopMyService() {
        context.stopService(new Intent(context, ReminderService.class));
    }
}
