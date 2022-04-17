package com.iti.mad42.remedicine.data.FacebookAuthentication;

import com.facebook.AccessToken;

public interface AuthenticationHandlerInterface {

    public void registerListeners();

    public void unregisterListeners();

    public void handleFacebookToken(AccessToken token);

}
