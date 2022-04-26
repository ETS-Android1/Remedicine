package com.iti.mad42.remedicine.home.presenter;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;

import java.util.List;

public interface HomePresenterInterface {

    public void getAlMedicines();
    public void filterMedicationByDay(List<MedicationPojo> medicationList, String date);
    public void updateMedication(MedicationPojo medication);
    public void getOnlineData(String medFriendEmail);
    public String getSharedPref();
    public List<MedicationPojo> getMedList();
}
