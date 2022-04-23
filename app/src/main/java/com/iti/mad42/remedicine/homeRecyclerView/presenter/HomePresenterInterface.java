package com.iti.mad42.remedicine.homeRecyclerView.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Utility;

import java.util.List;

public interface HomePresenterInterface {

    public void getAlMedicines();
    public void setCurrentUser();
}
