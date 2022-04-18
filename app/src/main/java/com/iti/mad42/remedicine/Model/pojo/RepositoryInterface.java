package com.iti.mad42.remedicine.Model.pojo;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface RepositoryInterface {

    //functions for Room Database
    public LiveData<List<MedicationPojo>> getAllMedications();
    public void insertMedication(MedicationPojo medication);
    public LiveData<MedicationPojo> getSpecificMedication(String medName);
    public void updateMedication(MedicationPojo med);
    public void deleteMedication(MedicationPojo med);
    public LiveData<List<MedicationPojo>> getActiveMedications(long currentDate);
    public LiveData<List<MedicationPojo>> getInActiveMedication(long currentDate);
    public void updateActiveStateForMedication(long currentDate);


    //functions for Firebase-RealTime
}
