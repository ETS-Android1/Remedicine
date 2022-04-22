package com.iti.mad42.remedicine.MyAccount.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;
import com.iti.mad42.remedicine.MyAccount.View.MyAccountFragmentInterface;
import com.iti.mad42.remedicine.data.FacebookAuthentication.NetworkDelegate;

import java.util.List;

public class MyAccountPresenter implements MyAccountPresenterInterface, NetworkDelegate {

    Context context;
    MyAccountFragmentInterface view;
    RepositoryInterface repository;

    public MyAccountPresenter(Context context, MyAccountFragmentInterface view, RepositoryInterface repository) {
        this.context = context;
        this.view = view;
        this.repository = repository;
    }

    public void saveString (String key , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("LoginTest",MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }

    @Override
    public void sendRequest(RequestPojo request) {
        repository.sendRequest(request);
    }

    @Override
    public LiveData<List<User>> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public void getAllMedsForMedfriend(String medOwnerEmail) {
        repository.setNetworkDelegate(this);
        repository.getAllMedicationFromFBForCurrentMedOwner(medOwnerEmail);
    }


    @Override
    public void navigateToHome() {

    }

    @Override
    public void successReturnRequests(List<RequestPojo> requests) {

    }

    @Override
    public void insertMedFriend(User user) {

    }

    @Override
    public void successReturnMedications(List<MedicationPojo> meds) {
        view.sendAllMedsToHomeScreen(meds);
    }

}
