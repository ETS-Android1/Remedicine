package com.iti.mad42.remedicine.Requests.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;
import com.iti.mad42.remedicine.Requests.View.RequestsViewActivityInterface;
import com.iti.mad42.remedicine.data.FacebookAuthentication.NetworkDelegate;

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
        Log.e("sandra", "user from presenter is  "+user.getEmail());
        repository.insertMedfriendUser(user);

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
