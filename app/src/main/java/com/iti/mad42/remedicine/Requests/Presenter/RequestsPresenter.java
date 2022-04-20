package com.iti.mad42.remedicine.Requests.Presenter;

import com.iti.mad42.remedicine.Model.pojo.RepositoryInterface;
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Requests.View.RequestsViewActivityInterface;
import com.iti.mad42.remedicine.data.FacebookAuthentication.NetworkDelegate;

import java.util.List;

public class RequestsPresenter implements RequestsPresenterInterface, NetworkDelegate {
    RequestsViewActivityInterface view;
    RepositoryInterface repository;

    public RequestsPresenter(RequestsViewActivityInterface view, RepositoryInterface repository) {
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
    public void rejectRequest(RequestPojo request) {
        repository.rejectRequest(request);
    }
}
