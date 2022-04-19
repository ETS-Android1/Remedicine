package com.iti.mad42.remedicine.register.view.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mad42.remedicine.Model.User;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.homeRecyclerView.view.HomeRecyclerView;
import com.iti.mad42.remedicine.register.view.presenter.RegisterPresenter;
import com.iti.mad42.remedicine.register.view.presenter.RegisterPresenterInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements RegisterActivityInterface {

    private Button btnRegister;
    private TextView emailTV, usernameTV, passwordTV, emalErrorMessageTV;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private RegisterPresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        setBtnsLisetner();

        presenter = new RegisterPresenter(this,this);


    }

    private void initUI() {
        btnRegister = findViewById(R.id.btnRegister);
        emailTV = findViewById(R.id.txtViewEmailRegister);
        passwordTV = findViewById(R.id.txtViewPasswordRegister);
        usernameTV = findViewById(R.id.txtViewUserNameRegister);
        emalErrorMessageTV = findViewById(R.id.txtViewEmailErrorMessage);

    }

    private void setBtnsLisetner() {
        btnRegister.setOnClickListener(view -> {
            if (isValidUsernameAndPassword()) {
                String email = emailTV.getText().toString().trim();
                String username = usernameTV.getText().toString();
                String password = passwordTV.getText().toString();
                presenter.registerNewUser(new User(email,username,password));
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

    public void navigateToHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeRecyclerView.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateUIToShowError() {
        emailTV.requestFocus();
        emalErrorMessageTV.setText("This email is already registered.Try another one");
        emalErrorMessageTV.setTextColor(Color.RED);
    }
}