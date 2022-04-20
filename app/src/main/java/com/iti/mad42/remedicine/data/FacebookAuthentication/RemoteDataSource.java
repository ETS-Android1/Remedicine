package com.iti.mad42.remedicine.data.FacebookAuthentication;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mad42.remedicine.Model.pojo.MedicationPojo;
import com.iti.mad42.remedicine.Model.pojo.User;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenterInterface;

public class RemoteDataSource implements RemoteDataSourceInterface {

    private Context context;
    private FirebaseAuth firebaseAuth;
    public static final String TAG = "Facebook Authentication";
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker tracker;
    private FirebaseUser user;
    private static RemoteDataSource instance = null;
    private DatabaseReference databaseReferenceUser;
    private DatabaseReference databaseReferenceMedication;


    private RemoteDataSource(Context context, CallbackManager callbackManager) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
        databaseReferenceMedication = FirebaseDatabase.getInstance().getReference("meds");
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = callbackManager;
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
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

    public static RemoteDataSource getInstance(Context context, CallbackManager callbackManager) {
        if (instance == null) {
            instance = new RemoteDataSource(context,callbackManager);
        }
        return  instance;
    }
    public void registerListeners() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void unregisterListeners() {
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }   
    }

    public void handleFacebookToken(AccessToken token,NetworkDelegate networkDelegate) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "sign in with credential: successful ");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    addUserToFirebase(networkDelegate , new User(user.getEmail(),user.getDisplayName(),""));
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

    private void addUserToFirebase(NetworkDelegate networkDelegate,User user) {
        String id = databaseReferenceUser.push().getKey();
        databaseReferenceUser.orderByChild("email")
                .equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context,"This email is already registered",Toast.LENGTH_SHORT).show();
                }else {
                    databaseReferenceUser.child(id).setValue(user);
                    networkDelegate.saveString(Utility.myCredentials,user.getEmail().toString().trim());
                    networkDelegate.navigateToHome();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    public void addMedicationToFirebase(MedicationPojo med){
        String id = databaseReferenceMedication.push().getKey();
        databaseReferenceMedication.orderByChild("name").equalTo(med.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(context,"This Medication is already added",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReferenceMedication.child(id).setValue(med);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void updateMedicationToFirebase(MedicationPojo med) {
        databaseReferenceMedication.orderByChild("medOwnerEmail").equalTo(getString(Utility.myCredentials)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        MedicationPojo medicationPojo = postSnapshot.getValue(MedicationPojo.class);
                        if (medicationPojo.getName().equals(med.getName())){
                            databaseReferenceMedication.child(postSnapshot.getKey()).setValue(med);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void deleteMedicationFromFirebase(MedicationPojo med){
        databaseReferenceMedication.orderByChild("medOwnerEmail").equalTo(getString(Utility.myCredentials)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        MedicationPojo medicationPojo = postSnapshot.getValue(MedicationPojo.class);
                        if (medicationPojo.getName().equals(med.getName())){
                            databaseReferenceMedication.child(postSnapshot.getKey()).removeValue();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String getString(String key){
        SharedPreferences sharedPreferences=
                context.getSharedPreferences("LoginTest",MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }


}
