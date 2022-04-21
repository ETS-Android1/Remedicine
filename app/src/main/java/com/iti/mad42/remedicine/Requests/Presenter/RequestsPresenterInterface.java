package com.iti.mad42.remedicine.Requests.Presenter;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

public interface RequestsPresenterInterface {
    public void getAllRequests(String receiverEmail);
    public void rejectRequest(RequestPojo request);
    public void updateStateWhenAcceptRequest(RequestPojo request);
    public void insertMedFriend(User user);
}
