package com.iti.mad42.remedicine.EditMed.View;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;

import java.util.List;

public interface EditMedActivityInterface {

    public void setStartDateTextView(String startDate);
    public void setEndDateTextView(String endDate);
    public List<MedicineDose> getDoseFromAdapter();
    public String getMedNameTextView();

    public String getMedStrengthTextView();

    public String getMedReasonTextView();

    public String getMedInstructionTextView();

    public int getMedQtyTextView();

    public int getMedReminderQtyTextView();
    public void setRefillTime(String time);
    public void setDataToTextViews(MedicationPojo medicationPojo);
    public MedicationPojo getMedicationObject();

}
