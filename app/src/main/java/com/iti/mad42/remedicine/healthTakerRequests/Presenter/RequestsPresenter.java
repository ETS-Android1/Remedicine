package com.iti.mad42.remedicine.healthTakerRequests.Presenter;

import android.content.Context;
import android.util.Log;

import com.iti.mad42.remedicine.data.pojo.MedicationPojo;
import com.iti.mad42.remedicine.data.repositry.RepositoryInterface;
import com.iti.mad42.remedicine.data.pojo.RequestPojo;
import com.iti.mad42.remedicine.data.pojo.User;
import com.iti.mad42.remedicine.healthTakerRequests.View.RequestsViewActivityInterface;
import com.iti.mad42.remedicine.data.remoteDataSource.NetworkDelegate;

import java.util.List;

public class RequestsPresenter implements RequestsPresenterInterface, NetworkDelegate {
    private Context context;
    RequestsViewActivityInterface view;
    RepositoryInterface repository;

    public RequestsPresenter(Context context , RequestsViewActivityInterface view, RepositoryInterface repository) {
        this.context = context;
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getAllRequests(String receiverEmail) {
        repository.setNetworkDelegate(this);
        repository.getAllRequests(receiverEmail);
    }


    @Override
    public void saveString(String key, String value) {

    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void successReturnRequests(List<RequestPojo> requests) {

        view.getAllRequests(requests);
    }

    @Override
    public void insertMedFriend(User user) {
        repository.setNetworkDelegate(this);
        Log.e("sandra", "user from presenter calll is  "+user.getEmail());
        repository.insertMedfriendUser(user);
    }

    @Override
    public void successReturnMedications(List<MedicationPojo> meds) {

    }

    @Override
    public void rejectRequest(RequestPojo request) {
        repository.rejectRequest(request);
    }

    @Override
    public void updateStateWhenAcceptRequest(RequestPojo request) {
        repository.updateRequestStateWhenAccept(request);
    }
}
