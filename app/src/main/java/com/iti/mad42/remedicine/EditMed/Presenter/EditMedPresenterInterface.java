package com.iti.mad42.remedicine.EditMed.Presenter;

import com.iti.mad42.remedicine.Model.MedicineDose;

import java.util.List;

public interface EditMedPresenterInterface {
    public void setMedicineForm(int pos);
    public void setInterval(int pos);
    public void setMedStrength(int pos);
    public List<MedicineDose> getMedDose(int pos);
    public void openDatePicker(String state);
    public void getData();
    public void openTimePicker();
}
