package com.iti.mad42.remedicine.MyAccount.Presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;

public interface MyAccountPresenterInterface {
    public void saveString (String key ,String value);
    public void sendRequest(RequestPojo request);
}
