package com.iti.mad42.remedicine.register.presenter;

import com.iti.mad42.remedicine.data.pojo.User;


public interface RegisterPresenterInterface {
    public void registerNewUser(User user);
    public void saveString (String key ,String value);
}
