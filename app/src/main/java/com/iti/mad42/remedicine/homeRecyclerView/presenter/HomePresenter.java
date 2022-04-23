package com.iti.mad42.remedicine.homeRecyclerView.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.Observer;

import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeFragmentInterface;

import java.util.List;

public class HomePresenter implements HomePresenterInterface {

    private Context context;
    private HomeFragmentInterface view;
    private Repository repo;

    public HomePresenter(Context context, HomeFragmentInterface view, Repository repo) {
        this.context = context;
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getAlMedicines() {
        view.showData(repo.getAllMedications());
    }

    public void setCurrentUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginTest",MODE_PRIVATE);
        CurrentUser.getInstance().setEmail(sharedPreferences.getString(Utility.myCredentials,null));
    }

}
