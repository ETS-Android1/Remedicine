package com.iti.mad42.remedicine.data.repositry;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;

import java.util.List;

public interface OnlineDataInterface {
    public void onlineDataResult(List<MedicationPojo> friendMedications);
//    public void medDataResult(MedicationPojo medicationPojo);

}
