package com.iti.mad42.remedicine.healthTakerRequests.Presenter;

import com.iti.mad42.remedicine.data.pojo.RequestPojo;
import com.iti.mad42.remedicine.data.pojo.User;

public interface RequestsPresenterInterface {
    public void getAllRequests(String receiverEmail);
    public void rejectRequest(RequestPojo request);
    public void updateStateWhenAcceptRequest(RequestPojo request);
    public void insertMedFriend(User user);
}
