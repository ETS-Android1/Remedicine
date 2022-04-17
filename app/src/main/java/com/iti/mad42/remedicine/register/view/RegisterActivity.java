package com.iti.mad42.remedicine.register.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextView emailTV, usernameTV, passwordTV;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        setBtnsLisetner();
    }

    private void initUI() {
        btnRegister = findViewById(R.id.btnRegister);
        emailTV = findViewById(R.id.txtViewEmailRegister);
        passwordTV = findViewById(R.id.txtViewPasswordRegister);
        usernameTV = findViewById(R.id.txtViewUserNameRegister);

    }

    private void setBtnsLisetner() {
        btnRegister.setOnClickListener(view -> {
            if (isValidUsernameAndPassword()) {
                navigateToHome();
            }else {
                Toast.makeText(this,"Invalid inputs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidUsernameAndPassword() {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailTV.getText().toString());
        if (matcher.find() && !passwordTV.getText().toString().isEmpty() && !usernameTV.getText().toString().isEmpty()) {
            return true;
        }else {
            Toast.makeText(this,"Invalid Username or Password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeRecyclerView.class);
        startActivity(intent);
        finish();
    }
}