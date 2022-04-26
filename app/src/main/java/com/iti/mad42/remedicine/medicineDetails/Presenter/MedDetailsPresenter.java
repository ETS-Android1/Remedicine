package com.iti.mad42.remedicine.medicineDetails.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.iti.mad42.remedicine.networkChengerBrodcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.medicineDetails.View.MedDetailsInterface;
import com.iti.mad42.remedicine.data.pojo.CurrentUser;
import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.repositry.OnlineDataInterface;
import com.iti.mad42.remedicine.data.repositry.RepositoryInterface;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.WorkManger.MyPeriodicWorkManger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MedDetailsPresenter implements  MedDetailsPresenterInterface, OnlineDataInterface {

    MedicationPojo medicationPojo;
    int amount;
    int medQty;

    Context context;
    MedDetailsInterface view;
    private RepositoryInterface repository;

    public MedDetailsPresenter(Context context, MedDetailsInterface view, RepositoryInterface repository) {
        this.context = context;
        this.view = view;
        this.repository = repository;
        medicationPojo = view.getMedObject();
        medQty = medicationPojo.getMedQty();
    }

    public MedicationPojo getMedication(){
        return medicationPojo;
    }

    public void refillMed(){
        medicationPojo.setMedQty(amount);
        view.setPillsLeftLabel(amount+" "+ Utility.medForm[medicationPojo.getFormIndex()]+" left");
        if (NetworkChangeReceiver.isConnected){
            repository.updateMedication(medicationPojo);
            repository.updateMedicationToFirebase(medicationPojo);
            if(medQty <= medicationPojo.getReminderMedQtyLeft()){
                view.setWorkTimerForRefillReminder();
            }
        }else {
            repository.updateMedication(medicationPojo);
            if(medQty <= medicationPojo.getReminderMedQtyLeft()){
                view.setWorkTimerForRefillReminder();
            }
        }

    }
    public void setRefillAmount(int amount){
        this.amount=amount;
    }

    public void deleteMed(){
        repository.deleteMedication(medicationPojo);
        repository.deleteMedicationFromFirebase(medicationPojo);
        setWorkTimer();
    }
    public void suspendMed(){
        medicationPojo.setActive(!medicationPojo.isActive());
        if (medicationPojo.isActive()){
            view.setSuspendBtnText("SUSPEND");
        }
        else {
            view.setSuspendBtnText("ACTIVATE");
        }
        repository.updateMedication(medicationPojo);
        repository.updateMedicationToFirebase(medicationPojo);
    }

    private void setWorkTimer() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }
    public void setData(){
        view.setPillsLeftLabel(medicationPojo.getMedQty()+" "+ Utility.medForm[medicationPojo.getFormIndex()]+" left");
        view.setPrescriptionRefillLabel("Refill Reminder When I have "+medicationPojo.getReminderMedQtyLeft()+" Meds Left");
        view.setHowToUseLabel(medicationPojo.getInstructions());
        view.setMedDurationLabel(Utility.medReminderPerWeekList[medicationPojo.getRecurrencePerWeekIndex()]+" , for "+medicationPojo.getMedDays().size()+" days");
        view.setMedicationNameLabel(medicationPojo.getName());
        view.setMedicationStrengthLabel(medicationPojo.getStrength()+" "+Utility.medStrengthUnit[medicationPojo.getStrengthUnitIndex()]);
        view.setMedReasonLabel(medicationPojo.getReason());
        view.setMedTimeRecyclerview(medicationPojo.getMedDoseReminders());
    }

    @Override
    public void addDose() {
        if (getSharedPref().equals(CurrentUser.getInstance().getEmail())) {
            if (NetworkChangeReceiver.isConnected){
                medQty--;
                medicationPojo.setMedQty(medQty);
                view.setPillsLeftLabel(medicationPojo.getMedQty()+" "+ Utility.medForm[medicationPojo.getFormIndex()]+" left");
                repository.updateMedication(medicationPojo);
                repository.updateMedicationToFirebase(medicationPojo);
                if(medQty <= medicationPojo.getReminderMedQtyLeft()){
                    view.setWorkTimerForRefillReminder();
                }
            }else {
                medQty--;
                medicationPojo.setMedQty(medQty);
                view.setPillsLeftLabel(medicationPojo.getMedQty()+" "+ Utility.medForm[medicationPojo.getFormIndex()]+" left");
                repository.updateMedication(medicationPojo);
                if(medQty <= medicationPojo.getReminderMedQtyLeft()){
                    view.setWorkTimerForRefillReminder();
                }
            }
        }else {
            if (NetworkChangeReceiver.isConnected){
                medQty--;
                medicationPojo.setMedQty(medQty);
                Log.e("mando", "onViewCreated: " +CurrentUser.getInstance().getEmail().trim());
                view.setPillsLeftLabel(medicationPojo.getMedQty()+" "+ Utility.medForm[medicationPojo.getFormIndex()]+" left");
                repository.updateMedicationToFirebase(medicationPojo);
            }else {
                Toast.makeText(context,"Please check your network connection",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public String getSharedPref() {
        SharedPreferences prefs = context.getSharedPreferences("LoginTest", MODE_PRIVATE);
        return prefs.getString(Utility.myCredentials, "No user registered");
    }

    @Override
    public void getOnlineData(String medFriendEmail) {
        //repository.getAllMedicationFromFBForCurrentMedOwner(medFriendEmail,this);
    }


    @Override
    public void onlineDataResult(List<MedicationPojo> friendMedications) {

    }
}
