package com.iti.mad42.remedicine.splashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.iti.mad42.remedicine.Broadcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.Model.pojo.CurrentUser;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;
import com.iti.mad42.remedicine.login.view.view.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initConnectionListener();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginTest",MODE_PRIVATE);
                String email = sharedPreferences.getString(Utility.myCredentials,null);
                if (email != null) {
                    mainIntent = new Intent(SplashScreen.this, HomeRecyclerView.class);
                    CurrentUser.getInstance().setEmail(email);
                }else {
                    mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, 2000);
    }
    private void initConnectionListener(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);
    }

}