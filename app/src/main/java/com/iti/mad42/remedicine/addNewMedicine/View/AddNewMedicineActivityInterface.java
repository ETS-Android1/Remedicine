package com.iti.mad42.remedicine.addNewMedicine.View;

import com.iti.mad42.remedicine.data.pojo.MedicineDose;

import java.util.List;

public interface AddNewMedicineActivityInterface {
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

}
