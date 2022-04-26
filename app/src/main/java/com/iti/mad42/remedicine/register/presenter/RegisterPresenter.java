package com.iti.mad42.remedicine.register.presenter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.iti.mad42.remedicine.data.pojo.CurrentUser;
import com.iti.mad42.remedicine.data.repositry.RepositoryInterface;
import com.iti.mad42.remedicine.data.pojo.User;
import com.iti.mad42.remedicine.utility.Utility;
import com.iti.mad42.remedicine.register.view.RegisterActivityInterface;

public class RegisterPresenter implements RegisterPresenterInterface {

    private Context context;
    private RegisterActivityInterface view;
    private DatabaseReference databaseReferenceUser;
    private RepositoryInterface repo;


    public RegisterPresenter(Context context, RegisterActivityInterface view, RepositoryInterface repo) {
        this.context = context;
        this.view = view;
        this.repo = repo;
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
    }


    @Override
    public void registerNewUser(User user) {
        String id = databaseReferenceUser.push().getKey();
        databaseReferenceUser.orderByChild("email")
                .equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    view.updateUIToShowError();
                }else {
                    databaseReferenceUser.child(id).setValue(user);
                    view.showToast("User added successfully");
                    saveString(Utility.myCredentials,user.getEmail());
                    CurrentUser.getInstance().setEmail(user.getEmail());
                    repo.insertMedfriendUser(user);
                    view.navigateToHome();
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
