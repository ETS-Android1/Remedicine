package com.iti.mad42.remedicine.home.view;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;

public interface OnNodeListener {
    public void getChosenMedicine(MedicationPojo medicine, Long time);
}
