package com.iti.mad42.remedicine.homeRecyclerView.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;

import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.HomeParentItem;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.MedicineDose;
import com.iti.mad42.remedicine.Model.pojo.OnlineDataInterface;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeParentItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface HomePresenterInterface {

    public void getAlMedicines();
    public void filterMedicationByDay(List<MedicationPojo> medicationList, String date);
    public void updateMedication(MedicationPojo medication);
    public void getOnlineData(String medFriendEmail);
    public String getSharedPref();
}
