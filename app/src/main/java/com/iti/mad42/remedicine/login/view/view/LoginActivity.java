package com.iti.mad42.remedicine.login.view.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.iti.mad42.remedicine.Broadcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.Model.database.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.Model.pojo.Repository;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.data.FacebookAuthentication.RemoteDataSource;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenter;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenterInterface;
import com.iti.mad42.remedicine.register.view.view.RegisterActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements LoginActivityInterface {

    private Button btnLogin, btnSignup;
    private LoginButton btnLoginWithFacebook;
    private Intent intent;
    private CallbackManager callbackManager;
    private LoginPresenterInterface presenter;
    private TextView emailTV, passwordTV, emailErrorMessage;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final int REQUEST_PERMISSION = 14;
    public final static int REQUEST_CODE = -1010101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        checkDrawOverlayPermission();
        setBtnsListener();
        btnLoginWithFacebook.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();
        setRegisterCallback();
        presenter = new LoginPresenter(this,this, Repository.getInstance(this, ConcreteLocalDataSource.getInstance(this), RemoteDataSource.getInstance(this,callbackManager)));
    }

    @SuppressLint("NewApi")
    public void checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(this)) {
            // if not construct intent to request permission
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            alertDialogBuilder.setTitle("Permission Required")
                    .setMessage("Enable Overlay Permission")
                    .setPositiveButton("Enable", (dialog, i) -> {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getApplicationContext().getPackageName()));
                        // request permission via start activity for result
                        startActivityForResult(intent, REQUEST_CODE); //It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                        dialog.dismiss();
                    }).setNegativeButton("Cancel", (dialog, i) -> {
                dialog.dismiss();
            }).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int permissionRequestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(permissionRequestCode, permissions, grantResults);
        if (permissionRequestCode == REQUEST_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "please we need your permission to have all our features", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.registerListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        emailErrorMessage = findViewById(R.id.txtViewEmailErrorMessageLogin);
    }


    private void setBtnsListener() {

        btnLogin.setOnClickListener(view -> {
            if (NetworkChangeReceiver.isConnected) {
                if (isValidUsernameAndPassword()) {
                    String email = emailTV.getText().toString().trim();
                    String password = passwordTV.getText().toString().trim();
                    presenter.tryToLogin(email,password);
                }
            }
            else
                Toast.makeText(this,"Please check your network connection",Toast.LENGTH_SHORT).show();
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
                Log.i(RemoteDataSource.TAG, "onSuccess: "+loginResult);
                presenter.handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i(RemoteDataSource.TAG, "onCancel: ");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i(RemoteDataSource.TAG, "onError: "+e);
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

    public void updateUIToShowError(String message) {
        emailTV.requestFocus();
        emailErrorMessage.setText(message);
        emailErrorMessage.setTextColor(Color.RED);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void requestFocusFor(String viewName) {
        if (viewName.equals("email")){
            emailTV.requestFocus();
        }else if (viewName.equals("pass")) {
            passwordTV.requestFocus();
        }
    }

}