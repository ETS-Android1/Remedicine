package com.iti.mad42.remedicine.login.presenter;

import com.facebook.AccessToken;

public interface LoginPresenterInterface {

    public void registerListeners();
    public void unregisterListeners();
    public void handleFacebookToken(AccessToken token);
    public void tryToLogin(String email, String password);


}
