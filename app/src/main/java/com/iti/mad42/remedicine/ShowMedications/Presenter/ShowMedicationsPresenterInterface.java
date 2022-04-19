package com.iti.mad42.remedicine.ShowMedications.Presenter;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

import java.util.List;

public interface ShowMedicationsPresenterInterface {
    public LiveData<List<MedicationPojo>> getActiveMedications(long currentDate);
    public LiveData<List<MedicationPojo>> getInActiveMedications(long currentDate);
}
