package com.iti.mad42.remedicine.login.view.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.data.FacebookAuthentication.AuthenticationHandler;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenter;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenterInterface;
import com.iti.mad42.remedicine.register.view.RegisterActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements LoginActivityInterface {

    private Button btnLogin, btnSignup;
    private LoginButton btnLoginWithFacebook;
    private Intent intent;
    private CallbackManager callbackManager;
    private LoginPresenterInterface presenter;
    private TextView emailTV, passwordTV;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setBtnsListener();
        btnLoginWithFacebook.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();
        setRegisterCallback();
        presenter = new LoginPresenter(this,this,callbackManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.registerListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unregisterListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUI() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginWithFacebook = findViewById(R.id.btnLoginWithFacebook);
        btnSignup = findViewById(R.id.btnSignUp);
        emailTV = findViewById(R.id.txtViewEmailLogin);
        passwordTV = findViewById(R.id.txtViewPasswordLogin);
    }


    private void setBtnsListener() {

        btnLogin.setOnClickListener(view -> {
            if (isValidUsernameAndPassword()) {
                navigateToHome();
            }
        });


        btnSignup.setOnClickListener(view -> {
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


    private void setRegisterCallback() {
        btnLoginWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(AuthenticationHandler.TAG, "onSuccess: "+loginResult);
                presenter.handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i(AuthenticationHandler.TAG, "onCancel: ");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i(AuthenticationHandler.TAG, "onError: "+e);
            }
        });
    }

    public void navigateToHome() {
        intent = new Intent(LoginActivity.this, HomeRecyclerView.class);
        startActivity(intent);
        finish();
    }

    private boolean isValidUsernameAndPassword() {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailTV.getText().toString());
        if (matcher.find() && !passwordTV.getText().toString().isEmpty()) {
            return true;
        }else {
            Toast.makeText(this,"Invalid email or password",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}