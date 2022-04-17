package com.iti.mad42.remedicine.data.FacebookAuthentication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenterInterface;

public class AuthenticationHandler implements AuthenticationHandlerInterface {

    Context context;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    public static final String TAG = "Facebook Authentication";
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker tracker;
    private LoginPresenterInterface loginPresenterInterface;

    public AuthenticationHandler(Context context, CallbackManager callbackManager, LoginPresenterInterface loginPresenterInterface) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.loginPresenterInterface = loginPresenterInterface;
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = callbackManager;
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                getUserInfo(user);
            }else {
                getUserInfo(null);
            }
        };
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
                if (accessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };
    }

    public void registerListeners() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void unregisterListeners() {
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }   
    }

    public void handleFacebookToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "sign in with credential: successful ");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    loginPresenterInterface.navigateToHome();
                }else {
                    Log.i(TAG, "sign in with credential: failed ",task.getException());

                }
            }
        });
    }

    private void getUserInfo(FirebaseUser user) {
        if (user != null) {
            Log.i(TAG, "updateUI with: " + user.getEmail() );
            Log.i(TAG, "updateUI with: " + user.getDisplayName() );
            Log.i(TAG, "updateUI with: " + user.getUid() );
            Log.i(TAG, "updateUI with: " + user.getProviderId() );
            Log.i(TAG, "updateUI with: " + user.getPhoneNumber() );
        }
    }

}
