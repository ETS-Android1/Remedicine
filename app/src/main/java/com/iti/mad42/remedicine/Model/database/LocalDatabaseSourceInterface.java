package com.iti.mad42.remedicine.Model.database;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

import java.util.List;

public interface LocalDatabaseSourceInterface {
    LiveData<List<MedicationPojo>> getAllMedications();
    void insertMedication(MedicationPojo medication);
    LiveData<MedicationPojo> getSpecificMedication(String medName);
    void updateMedication(MedicationPojo med);
    void deleteMedication(MedicationPojo med);
    LiveData<List<MedicationPojo>> getActiveMedication(long currentDate);
    LiveData<List<MedicationPojo>> getInActiveMedications(long currentDate);
    void updateActiveStateForMedication(long currentDate);

}
