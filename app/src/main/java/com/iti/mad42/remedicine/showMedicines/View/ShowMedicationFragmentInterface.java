package com.iti.mad42.remedicine.showMedicines.View;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;

import java.util.List;

public interface ShowMedicationFragmentInterface {
    public void showActiveMedications(List<MedicationPojo> meds);
    public void showInActiveMedications(List<MedicationPojo> meds);
    public void getOnlineData(List<MedicationPojo> friendMedications);
}
