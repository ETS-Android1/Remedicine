package com.iti.mad42.remedicine.myAccount.Presenter;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.data.pojo.RequestPojo;
import com.iti.mad42.remedicine.data.pojo.User;

import java.util.List;

public interface MyAccountPresenterInterface {
    public void saveString (String key ,String value);
    public void sendRequest(RequestPojo request);
    public LiveData<List<User>> getAllUsers();
    public void emptyLocalDB();
    public void deleteFromFirebase();

}
