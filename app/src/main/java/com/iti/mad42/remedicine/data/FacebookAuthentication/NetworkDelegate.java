package com.iti.mad42.remedicine.data.FacebookAuthentication;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

public interface NetworkDelegate {
    public void saveString (String key ,String value);
    public void navigateToHome();
    public void successReturnRequests(List<RequestPojo> requests);
    public void insertMedFriend(User user);
}
