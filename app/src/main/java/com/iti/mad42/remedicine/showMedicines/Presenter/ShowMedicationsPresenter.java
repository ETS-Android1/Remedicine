package com.iti.mad42.remedicine.showMedicines.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.repositry.OnlineDataInterface;
import com.iti.mad42.remedicine.data.repositry.RepositoryInterface;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.showMedicines.View.ShowMedicationFragmentInterface;

import java.util.List;

public class ShowMedicationsPresenter implements ShowMedicationsPresenterInterface , OnlineDataInterface {
    Context context;
    private ShowMedicationFragmentInterface view;
    private RepositoryInterface repository;

    public ShowMedicationsPresenter(Context context,ShowMedicationFragmentInterface view, RepositoryInterface repository) {
        this.context = context;
        this.view = view;
        this.repository = repository;
    }

    @Override
    public LiveData<List<MedicationPojo>> getActiveMedications(long currentDate) {
         return repository.getActiveMedications(currentDate);
    }

    @Override
    public LiveData<List<MedicationPojo>> getInActiveMedications(long currentDate) {
        return repository.getInActiveMedication(currentDate);
    }

    @Override
    public void getOnlineData(String medFriendEmail) {
        repository.getAllMedicationFromFBForCurrentMedOwner(medFriendEmail,this);
    }

    @Override
    public String getSharedPref() {
        SharedPreferences prefs = context.getSharedPreferences("LoginTest", MODE_PRIVATE);
        return prefs.getString(Utility.myCredentials, "No user registered");
    }

    @Override
    public void onlineDataResult(List<MedicationPojo> friendMedications) {
        view.getOnlineData(friendMedications);
    }
//
//    @Override
//    public void medDataResult(MedicationPojo medicationPojo) {
//
//    }
}
