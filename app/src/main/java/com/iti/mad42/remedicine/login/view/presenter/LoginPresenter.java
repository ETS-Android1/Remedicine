package com.iti.mad42.remedicine.login.view.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mad42.remedicine.Model.User;
import com.iti.mad42.remedicine.Model.Utility;
import com.iti.mad42.remedicine.data.FacebookAuthentication.AuthenticationHandler;
import com.iti.mad42.remedicine.data.FacebookAuthentication.AuthenticationHandlerInterface;
import com.iti.mad42.remedicine.login.view.view.LoginActivity;
import com.iti.mad42.remedicine.login.view.view.LoginActivityInterface;

public class LoginPresenter implements LoginPresenterInterface {

    private LoginActivityInterface view;
    private Context context;
    private AuthenticationHandlerInterface authenticationHandlerInterface;
    private CallbackManager callbackManager;
    private DatabaseReference databaseReferenceUser;


    public LoginPresenter(LoginActivityInterface view, Context context, CallbackManager callbackManager) {
        this.view = view;
        this.context = context;
        this.callbackManager = callbackManager;
        this.authenticationHandlerInterface = new AuthenticationHandler(context,callbackManager,this);
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");

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

    @Override
    public void tryToLogin(String email, String password) {
        databaseReferenceUser.orderByChild("email")
                .equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    view.requestFocusFor("email");
                    view.updateUIToShowError("Wrong email");
                } else {
                    for (DataSnapshot object : snapshot.getChildren()) {
                        User user = object.getValue(User.class);
                        if (user.getPassword().equals(password)) {
                            view.showToast("Logged in Successfully");
                            saveString(Utility.myCredentials,email);
                            view.navigateToHome();
                        }else {
                            view.requestFocusFor("pass");
                            view.showToast("Wrong password");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void saveString (String key ,String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("LoginTest",MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }

}
