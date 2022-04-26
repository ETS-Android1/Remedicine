package com.iti.mad42.remedicine.register.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.iti.mad42.remedicine.networkChengerBrodcast.NetworkChangeReceiver;
import com.iti.mad42.remedicine.data.localDataSource.ConcreteLocalDataSource;
import com.iti.mad42.remedicine.data.repositry.Repository;
import com.iti.mad42.remedicine.data.pojo.User;
import com.iti.mad42.remedicine.R;
import com.iti.mad42.remedicine.data.remoteDataSource.RemoteDataSource;
import com.iti.mad42.remedicine.home.view.BottomNavigationBar;
import com.iti.mad42.remedicine.register.presenter.RegisterPresenter;
import com.iti.mad42.remedicine.register.presenter.RegisterPresenterInterface;

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

        presenter = new RegisterPresenter(this,this, Repository.getInstance(this, ConcreteLocalDataSource.getInstance(this), RemoteDataSource.getInstance(this, new CallbackManager() {
            @Override
            public boolean onActivityResult(int i, int i1, @Nullable Intent intent) {
                return false;
            }
        })));

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
            if(NetworkChangeReceiver.isConnected){
                if (isValidUsernameAndPassword()) {
                    String email = emailTV.getText().toString().trim();
                    String username = usernameTV.getText().toString();
                    String password = passwordTV.getText().toString();
                    presenter.registerNewUser(new User(email,username,password));
                }else {
                    Toast.makeText(this,"Invalid inputs", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this,"Please check your network connection",Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(RegisterActivity.this, BottomNavigationBar.class);
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