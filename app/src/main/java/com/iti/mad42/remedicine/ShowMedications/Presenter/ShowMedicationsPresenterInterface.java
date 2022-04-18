package com.iti.mad42.remedicine.ShowMedications.Presenter;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

import java.util.List;

public interface ShowMedicationsPresenterInterface {
    public void getActiveMedications(long currentDate);
    public void getInActiveMedications(long currentDate);
}
