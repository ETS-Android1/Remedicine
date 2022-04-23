package com.iti.mad42.remedicine.MedDetails.Presenter;

import android.content.Context;
import android.util.Log;

import com.iti.mad42.remedicine.MedDetails.View.MedDetailsInterface;
import com.iti.mad42.remedicine.Model.pojo.MedState;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.Model.pojo.Utility;

import java.util.ArrayList;
import java.util.List;

public class MedDetailsPresenter implements  MedDetailsPresenterInterface{

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
        repository.updateMedication(medicationPojo);
        repository.updateMedicationToFirebase(medicationPojo);
    }
    public void setRefillAmount(int amount){
        this.amount=amount;
    }

    public void deleteMed(){
        repository.deleteMedication(medicationPojo);
        repository.deleteMedicationFromFirebase(medicationPojo);
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
        medQty--;
        Log.e("sandra", "from med Qty: ---> "+medQty+" and the med qty from med obj is-----> "+medicationPojo.getMedQty()+" and the med dose left to remind is: ---> "+medicationPojo.getReminderMedQtyLeft());
        medicationPojo.setMedQty(medQty);
        repository.updateMedication(medicationPojo);
        if(medQty <= medicationPojo.getReminderMedQtyLeft()){
            view.setWorkTimerForRefillReminder();
        }
    }


}
