package com.iti.mad42.remedicine.Model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

import io.reactivex.Single;

public interface LocalDatabaseSourceInterface {
    LiveData<List<MedicationPojo>> getAllMedications();
    void insertMedication(MedicationPojo medication);
    Single<MedicationPojo> getSpecificMedication(String medName);
    void updateMedication(MedicationPojo med);
    void deleteMedication(MedicationPojo med);
    LiveData<List<MedicationPojo>> getActiveMedication(long currentDate);
    LiveData<List<MedicationPojo>> getInActiveMedications(long currentDate);
    void updateActiveStateForMedication(long currentDate);
    Single<List<MedicationPojo>> getMedicationsToRefillReminder(long refillTime);

    //User Database Methods
    void insertMedfriendUser(User user);
    LiveData<List<User>> getAllUsers();

    Single<List<MedicationPojo>> getAllMedicationsList();
    void deleteMedications();
    void deleteUsers();



}
