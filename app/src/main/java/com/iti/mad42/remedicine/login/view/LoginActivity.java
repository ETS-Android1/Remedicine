package com.iti.mad42.remedicine.login.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;
import com.iti.mad42.remedicine.register.view.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnSignup, getBtnLoginWithFacebook;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LoginActivity.this, HomeRecyclerView.class);
                startActivity(intent);
            }
        });

        getBtnLoginWithFacebook = findViewById(R.id.btnLoginWithFacebook);
        getBtnLoginWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LoginActivity.this, HomeRecyclerView.class);
                startActivity(intent);
            }
        });

        btnSignup = findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}