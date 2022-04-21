package com.iti.mad42.remedicine.Model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

@Dao
public interface MedicineDAO {

    @Query("SELECT * FROM medication")
    LiveData<List<MedicationPojo>> getAllMedications();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMedication(MedicationPojo medication);

    @Query("SELECT * FROM medication WHERE name =:medName")
    LiveData<MedicationPojo> getSpecificMedication(String medName);

    @Update
    void updateMedication(MedicationPojo med);

    @Delete
    void deleteMedication(MedicationPojo med);

    @Query("SELECT * FROM medication WHERE (:currentDate BETWEEN startDate AND endDate) AND isActive =1")
    LiveData<List<MedicationPojo>> getActiveMedications(long currentDate);

    @Query("SELECT * FROM medication WHERE (:currentDate > endDate) AND isActive =0")
    LiveData<List<MedicationPojo>> getInActiveMedication(long currentDate);

    @Query("UPDATE medication SET isActive =0 WHERE (:currentDate > endDate)")
    void updateActiveStateForMedication(long currentDate);

    // User Database Methods


}
