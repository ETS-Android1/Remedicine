package com.iti.mad42.remedicine.medicineDetails.View;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.pojo.MedicineDose;

import java.util.List;

public interface MedDetailsInterface {
    public MedicationPojo getMedObject();
    public void setMedTimeRecyclerview(List<MedicineDose> medDose);
    public void setMedicationNameLabel(String medName);

    public void setPrescriptionRefillLabel(String prescriptionRefill);

    public void setHowToUseLabel(String howToUse);

    public void setPillsLeftLabel(String pillsLeft);

    public void setMedReasonLabel(String medReason);

    public void setMedDurationLabel(String medDuration);

    public void setMedicationStrengthLabel(String medicationStrength);
    public void setSuspendBtnText(String state);
    public void setWorkTimerForRefillReminder();

}
