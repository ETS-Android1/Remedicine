package com.iti.mad42.remedicine.MyAccount.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;

import java.util.List;

public interface MyAccountPresenterInterface {
    public void saveString (String key ,String value);
    public void sendRequest(RequestPojo request);
    public LiveData<List<User>> getAllUsers();
}
