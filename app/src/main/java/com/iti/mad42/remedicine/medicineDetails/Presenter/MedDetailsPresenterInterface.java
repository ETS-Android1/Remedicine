package com.iti.mad42.remedicine.medicineDetails.Presenter;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;

public interface MedDetailsPresenterInterface {
    public MedicationPojo getMedication();
    public void refillMed();
    public void setRefillAmount(int amount);
    public void deleteMed();
    public void setData();
    public void suspendMed();
    public void addDose();
    public String getSharedPref();
    public void getOnlineData(String medFriendEmail);
}
