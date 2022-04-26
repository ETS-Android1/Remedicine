package com.iti.mad42.remedicine.data.localDataSource;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MedicineDAO {

    @Query("SELECT * FROM medication")
    LiveData<List<MedicationPojo>> getAllMedications();

    @Query("SELECT * FROM medication")
    Single<List<MedicationPojo>> getAllMedicationsList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedication(MedicationPojo medication);

    @Query("SELECT * FROM medication WHERE name =:medName")
    Single<MedicationPojo> getSpecificMedication(String medName);

    @Update
    void updateMedication(MedicationPojo med);

    @Query("DELETE FROM medication")
    void deleteMedications();

    @Delete
    void deleteMedication(MedicationPojo med);

    @Query("SELECT * FROM medication WHERE (:currentDate BETWEEN startDate AND endDate) AND isActive =1")
    LiveData<List<MedicationPojo>> getActiveMedications(long currentDate);

    @Query("SELECT * FROM medication WHERE (:currentDate > endDate) AND isActive =0")
    LiveData<List<MedicationPojo>> getInActiveMedication(long currentDate);

    @Query("UPDATE medication SET isActive =0 WHERE (:currentDate > endDate)")
    void updateActiveStateForMedication(long currentDate);

    @Query("SELECT * FROM medication WHERE (:refillTime BETWEEN startDate AND endDate) AND medQty <= reminderMedQtyLeft AND isActive =1")
    Single<List<MedicationPojo>> getMedicationsToRefillReminder(long refillTime);

    // User Database Methods


}
