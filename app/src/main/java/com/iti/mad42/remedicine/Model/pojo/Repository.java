package com.iti.mad42.remedicine.Model.pojo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.database.AppDataBase;
import com.iti.mad42.remedicine.Model.database.LocalDatabaseSourceInterface;
import com.iti.mad42.remedicine.Model.database.MedicineDAO;

import java.util.List;

public class Repository implements RepositoryInterface{
    private Context context;
    private LocalDatabaseSourceInterface localDatabaseSource;
    private static  Repository repository = null;

    private Repository(Context context, LocalDatabaseSourceInterface localDatabaseSource){
        this.context = context;
        this.localDatabaseSource = localDatabaseSource;
    }

    public static Repository getInstance(Context context, LocalDatabaseSourceInterface localDatabaseSource){
        if(repository == null){
            repository = new Repository(context, localDatabaseSource);
        }
        return repository;
    }

    /// Room Database Methods
    @Override
    public LiveData<List<MedicationPojo>> getAllMedications() {
        return localDatabaseSource.getAllMedications();
    }

    @Override
    public void insertMedication(MedicationPojo medication) {
        localDatabaseSource.insertMedication(medication);
    }

    @Override
    public LiveData<MedicationPojo> getSpecificMedication(String medName) {
        return localDatabaseSource.getSpecificMedication(medName);
    }

    @Override
    public void updateMedication(MedicationPojo med) {
        localDatabaseSource.updateMedication(med);
    }

    @Override
    public void deleteMedication(MedicationPojo med) {
        localDatabaseSource.deleteMedication(med);
    }

    @Override
    public LiveData<List<MedicationPojo>> getActiveMedications(long currentDate) {
        return localDatabaseSource.getActiveMedication(currentDate);
    }

    @Override
    public LiveData<List<MedicationPojo>> getInActiveMedication(long currentDate) {
        return localDatabaseSource.getInActiveMedications(currentDate);
    }

    @Override
    public void updateActiveStateForMedication(long currentDate) {
        localDatabaseSource.updateActiveStateForMedication(currentDate);
    }
}
