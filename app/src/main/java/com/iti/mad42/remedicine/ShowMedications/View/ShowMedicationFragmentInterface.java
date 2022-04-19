package com.iti.mad42.remedicine.ShowMedications.View;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

import java.util.List;

public interface ShowMedicationFragmentInterface {
    public void showActiveMedications(List<MedicationPojo> meds);
    public void showInActiveMedications(List<MedicationPojo> meds);
}
