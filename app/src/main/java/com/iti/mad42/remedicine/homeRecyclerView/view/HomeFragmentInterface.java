package com.iti.mad42.remedicine.homeRecyclerView.view;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;

import java.util.List;

public interface HomeFragmentInterface {
    public void showData(LiveData<List<MedicationPojo>> medicines);
}
