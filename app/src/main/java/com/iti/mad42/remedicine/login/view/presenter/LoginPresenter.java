package com.iti.mad42.remedicine.login.view.presenter;

import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.iti.mad42.remedicine.data.FacebookAuthentication.AuthenticationHandler;
import com.iti.mad42.remedicine.data.FacebookAuthentication.AuthenticationHandlerInterface;
import com.iti.mad42.remedicine.login.view.view.LoginActivityInterface;

public class LoginPresenter implements LoginPresenterInterface {

    private LoginActivityInterface view;
    private Context context;
    private AuthenticationHandlerInterface authenticationHandlerInterface;
    private CallbackManager callbackManager;

    public LoginPresenter(LoginActivityInterface view, Context context, CallbackManager callbackManager) {
        this.view = view;
        this.context = context;
        this.callbackManager = callbackManager;
        this.authenticationHandlerInterface = new AuthenticationHandler(context,callbackManager,this);
    }

    public void registerListeners() {
        authenticationHandlerInterface.registerListeners();
    }
    public void unregisterListeners() {
        authenticationHandlerInterface.unregisterListeners();
    }

    public void handleFacebookToken(AccessToken token) {
        authenticationHandlerInterface.handleFacebookToken(token);
    }

    @Override
    public void navigateToHome() {
        view.navigateToHome();
    }

}
