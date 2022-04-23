package com.iti.mad42.remedicine.register.view.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.User;


public interface RegisterPresenterInterface {
    public void registerNewUser(User user);
    public void saveString (String key ,String value);
}
