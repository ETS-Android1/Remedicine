package com.iti.mad42.remedicine.Model.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

import io.reactivex.Single;

public class ConcreteLocalDataSource implements LocalDatabaseSourceInterface{

    private MedicineDAO medicineDAO;
    private UserDAO userDAO;
    private static ConcreteLocalDataSource localDataSource = null;
    private LiveData<List<MedicationPojo>> allMeds;
    private Single<List<MedicationPojo>> medicationPojoList;

    private ConcreteLocalDataSource(Context context){
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        medicineDAO = db.medicineDAO();
        userDAO = db.userDAO();
        allMeds = medicineDAO.getAllMedications();
        medicationPojoList = medicineDAO.getAllMedicationsList();
    }

    public static ConcreteLocalDataSource getInstance(Context context){
        if (localDataSource == null){
            localDataSource = new ConcreteLocalDataSource(context);
        }
        return localDataSource;
    }


    @Override
    public LiveData<List<MedicationPojo>> getAllMedications() {
        return allMeds;
    }


    @Override
    public void insertMedication(MedicationPojo medication) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicineDAO.insertMedication(medication);
            }
        }).start();

    }

    @Override
    public LiveData<MedicationPojo> getSpecificMedication(String medName) {
        return medicineDAO.getSpecificMedication(medName);
    }

    @Override
    public void updateMedication(MedicationPojo med) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicineDAO.updateMedication(med);
            }
        }).start();
    }

    @Override
    public void deleteMedication(MedicationPojo med) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicineDAO.deleteMedication(med);
            }
        }).start();
    }

    @Override
    public LiveData<List<MedicationPojo>> getActiveMedication(long currentDate) {
        return medicineDAO.getActiveMedications(currentDate);
    }

    @Override
    public LiveData<List<MedicationPojo>> getInActiveMedications(long currentDate) {
        return medicineDAO.getInActiveMedication(currentDate);
    }

    @Override
    public void updateActiveStateForMedication(long currentDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicineDAO.updateActiveStateForMedication(currentDate);
            }
        }).start();
    }

    @Override
    public void insertMedfriendUser(User user) {
        Log.e("sanra", "USer from local source "+ user.getEmail());
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDAO.insertMedfriendUser(user);
            }
        }).start();
    }

    @Override
    public Single<List<MedicationPojo>>  getAllMedicationsList() {
        return medicationPojoList;
    }
}
