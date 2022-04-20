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
import com.iti.mad42.remedicine.Model.pojo.RequestPojo;
import com.iti.mad42.remedicine.Model.pojo.User;
import com.iti.mad42.remedicine.Model.pojo.Utility;
import com.iti.mad42.remedicine.login.view.presenter.LoginPresenterInterface;

import java.util.ArrayList;
import java.util.List;

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
    private DatabaseReference databaseReferenceRequests;
    private NetworkDelegate networkDelegate;


    private RemoteDataSource(Context context, CallbackManager callbackManager) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
        databaseReferenceMedication = FirebaseDatabase.getInstance().getReference("meds");
        databaseReferenceRequests = FirebaseDatabase.getInstance().getReference("requests");
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

    @Override
    public void setNetworkDelegate(NetworkDelegate networkDelegate) {
        this.networkDelegate = networkDelegate;
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
    public String getString(String key){
        SharedPreferences sharedPreferences=
                context.getSharedPreferences("LoginTest",MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }



    @Override
    public void sendRequest(RequestPojo request) {
        String id = databaseReferenceRequests.push().getKey();
        databaseReferenceUser.orderByChild("email")
                .equalTo(request.getRecieverEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReferenceRequests.orderByChild("recieverEmail").equalTo(request.getRecieverEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                boolean isFound = false;
                                Log.i("TAG", "Error Here before for" + snapshot.toString());
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                    RequestPojo requestPojo = snapshot1.getValue(RequestPojo.class);
                                    if(request.getSenderEmail().equals(requestPojo.getSenderEmail())){
                                        isFound = true;
                                        Log.i("TAG", "request exists");
                                        Toast.makeText(context,"Request is already exists" ,Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if(!isFound){
                                    Log.i("TAG", "in small IF");
                                    databaseReferenceRequests.child(id).setValue(request);
                                }
                            }else {
                                Log.i("TAG", "in big ELSE");
                                databaseReferenceRequests.child(id).setValue(request);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context,"Something went wrong. Error is: "+error ,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(context,"This Email Doesn't Exist." ,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    @Override
    public void getAllRequestsForReceiver(String receiverEmail) {
        List<RequestPojo> requests = new ArrayList<>();
        databaseReferenceRequests.orderByChild("recieverEmail").equalTo(receiverEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    requests.clear();
                    for (DataSnapshot requestsSnapshot: snapshot.getChildren()){
                        RequestPojo request = requestsSnapshot.getValue(RequestPojo.class);
                        requests.add(request);
                    }
                }else {
                    Toast.makeText(context,"There is no requests to show.",Toast.LENGTH_SHORT).show();
                }

                //call method that will set the adapter with the list requests.
                networkDelegate.successReturnRequests(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




//    databaseReference
//            .orderByChild("recieverEmail")
//                .equalTo(getString(Utility.myCredentials)).addValueEventListener

//    if (snapshot.exists()){
//        Toast.makeText(context,"This Medication is already added",Toast.LENGTH_SHORT).show();

    //                     requests.clear();
    //                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
    //                        Pill pill = postSnapshot.getValue(Pill.class);
    //                        pills.add(pill);
    //                    }
    //                    adapter = new PillsFragmentHomePageAdapter(getContext(), pills);
//    }
//                else {
//        databaseReferenceMedication.child(id).setValue(med);
//    }
}
