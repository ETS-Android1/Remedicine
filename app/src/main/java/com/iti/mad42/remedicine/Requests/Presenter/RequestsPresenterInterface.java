package com.iti.mad42.remedicine.Requests.Presenter;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;

import java.util.List;

public interface RequestsPresenterInterface {
    public void getAllRequests(String receiverEmail);
    public void rejectRequest(RequestPojo request);
}
